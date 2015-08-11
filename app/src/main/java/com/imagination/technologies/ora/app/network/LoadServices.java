package com.imagination.technologies.ora.app.network;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.imagination.technologies.ora.app.controller.Config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LoadServices extends AsyncTask<Service, Void, Result> {
    private static final String LOG = LoadServices.class.getName();
    private static final int corePoolSize = 5;
    private static final int maximumPoolSize = 10;
    private static final int keepAliveTime = 10;

    private Service[] services;

    private static BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);

    private static Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            TimeUnit.SECONDS,
            workQueue);

    public static BlockingQueue<Runnable> getWorkQueue() {
        return workQueue;
    }

    public static Executor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    @Override
    protected void onPostExecute(Result result) {

        for (Service service : this.services) {
            if (service != null && service.getNotificationTask() != null) {
                service.getNotificationTask().completed(service);
            }
        }

        this.services = null;
    }

    @Override
    protected Result doInBackground(Service... _services) {
        RestfulRequest httpRequest = RestfulRequest.getSingleton();
        this.services = _services;
        String out = "";
        int i = 0;

        while (i < this.services.length) {

            if (this.services[i] == null) {
                ++i;
                continue;
            }

            Log.d(LOG, services[i].getServiceName()
                    + " with request:  " + services[i].getServiceInput());

            switch (services[i].getServiceType()) {
                case Config.GET:
                    out = httpRequest.sendGetRequest(
                            this.services[i].getBaseURL() + this.services[i].getServiceName(),
                            this.services[i].getHeaders());
                    break;
                case Config.POST:
                    out = httpRequest.sendPostRequest(
                            this.services[i].getBaseURL() + this.services[i].getServiceName(),
                            this.services[i].getServiceInput(),
                            this.services[i].getHeaders());
                    break;
                case Config.PUT:
                    out = httpRequest.sendPutRequest(
                            this.services[i].getBaseURL() + this.services[i].getServiceName(),
                            this.services[i].getServiceInput(),
                            this.services[i].getHeaders());
                    break;
            }

            Log.i(LOG, this.services[i].getBaseURL() + this.services[i].getServiceName()
                    + ", Response: " + out);

            this.services[i].setResponseCode(0);
            this.services[i++].setOutput(out);
        }
        return null;
    }

    public void loadOnExecutor(Service... services) {

        if (services.length == 0)
            return;

        if (services.length == 1
                || Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    this.executeOnExecutor(
                            LoadServices.getThreadPoolExecutor(), services);
                } else {
                    this.execute(services);
                }
            } catch (Exception e) {
                Log.i(LOG, "Thread executor error (" + e + ")");
            }

        } else {
            LoadServices[] lServices = new LoadServices[services.length];
            Service[][] tmpServices = new Service[services.length][];
            Service[] service;

            for (int i = 0; i < services.length; i++) {
                service = new Service[1];
                service[0] = services[i];
                tmpServices[i] = service;

                if (i > 0) {
                    lServices[i] = new LoadServices();
                } else {
                    lServices[0] = this;
                }
            }

            for (int i = 0; i < lServices.length; i++) {
                lServices[i].loadOnExecutor(tmpServices[i]);
            }
        }
    }
}
