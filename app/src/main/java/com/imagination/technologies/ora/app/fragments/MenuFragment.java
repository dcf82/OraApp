package com.imagination.technologies.ora.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.controller.Config;
import com.imagination.technologies.ora.app.utilities.Utility;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuFragment extends FragmentBase {

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ButterKnife.bind(this, getView());
    }

    @OnClick({ R.id.editProfile, R.id.logOut })
    public void onClickView(View view) {
        switch(view.getId()) {
            case R.id.editProfile:
                Utility.sendEvent(Config.OPEN_USER_PROFILE, null);
                break;
            case R.id.logOut:
                Utility.deleteUserProfile();
                Utility.sendEvent(Config.NO_SESSION_APP, null);
                break;
        }
    }
}
