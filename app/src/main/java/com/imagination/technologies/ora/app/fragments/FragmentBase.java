package com.imagination.technologies.ora.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.controller.Config;
import com.imagination.technologies.ora.app.controller.OraInteractiveApp;
import com.imagination.technologies.ora.app.interfaces.NotificationTask;
import com.imagination.technologies.ora.app.interfaces.OnFragmentInteractionListener;
import com.imagination.technologies.ora.app.network.Service;

public class FragmentBase extends Fragment implements NotificationTask {
    protected OnFragmentInteractionListener mListener;
    protected boolean isProcessing;

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.global, menu);
        showGlobalContextActionBar(OraInteractiveApp.getApp().getResources()
                .getString(R.string.app_name));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_exit) {
            getActivity().finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onButtonPressed(Bundle bundle) {
            mListener.onFragmentInteraction(bundle);
    }

    protected void openProgressBar(boolean open) {
        isProcessing = open;
        Bundle bundle = new Bundle();
        bundle.putInt(Config.ACTION, Config.INDETERMINATE_PROGRESS_BAR);
        bundle.putBoolean(Config.OPEN_INDETERMINATE_PROGRESS_BAR,
                isProcessing);
        onButtonPressed(bundle);
    }

    protected void showGlobalContextActionBar(String title) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }

    protected ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void completed(Service response) {
    }
}
