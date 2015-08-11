package com.imagination.technologies.ora.app.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.adapters.LocationsAdapter;
import com.imagination.technologies.ora.app.controller.Config;
import com.imagination.technologies.ora.app.events.LocationsDownloaded;
import com.imagination.technologies.ora.app.events.LocationsProcessed;
import com.imagination.technologies.ora.app.greendao.Locations;
import com.imagination.technologies.ora.app.network.Service;
import com.imagination.technologies.ora.app.utilities.Utility;
import com.imagination.technologies.ora.app.views.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class MyLocationFragment extends FragmentBase {
    @Bind(R.id.recyclerView)
    MyRecyclerView vRecyclerView;
    @Bind(R.id.empty_textview)
    TextView vEmptyTV;
    LocationsAdapter mAdapter;

    public static MyLocationFragment newInstance() {
        MyLocationFragment fragment = new MyLocationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_location_fragment, container, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        View view = getView();
        ButterKnife.bind(this, view);

        mAdapter = new LocationsAdapter(new ArrayList<Locations>());
        vRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        vRecyclerView.setAdapter(mAdapter);
        vRecyclerView.setEmptyView(vEmptyTV);

        fillBuffer();
    }

    protected void fillBuffer() {
        List<Locations> l = Utility.getMyLocations();
        if (l == null  || l.size() == 0) return;
        mAdapter.addItems(l);
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onResume() {
        super.onResume();
        if (mAdapter.getItemCount() == 0) {
            Utility.loadLocations(this);
        }
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEventBackgroundThread(LocationsDownloaded message) {
        // Process Input
        Utility.storeLocations(message.getInput());
    }

    public void onEventMainThread(LocationsProcessed message) {
        if (message.getResponse() == LocationsProcessed.RESPONSE.SUCCESSFUL) {
            fillBuffer();
        }
    }

    @Override
    public void completed(Service response) {
        switch(response.getServiceCode()) {
            case Config.GET_LOCATIONS:
                // Send the message
                EventBus.getDefault().post(new LocationsDownloaded(response.getOutput()));
                break;
        }
    }
}
