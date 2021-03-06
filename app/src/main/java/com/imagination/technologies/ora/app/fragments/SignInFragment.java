package com.imagination.technologies.ora.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.beans.RegistrationErrorWrapper;
import com.imagination.technologies.ora.app.beans.RegistrationResponse;
import com.imagination.technologies.ora.app.controller.Config;
import com.imagination.technologies.ora.app.network.LoadServices;
import com.imagination.technologies.ora.app.network.Service;
import com.imagination.technologies.ora.app.utilities.Utility;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInFragment extends FragmentBase {
    private static final String LOG = SignInFragment.class.getName();
    @Bind(R.id.emailAccount)
    EditText emailAccount;
    @Bind(R.id.mailPassword)
    EditText mailPassword;

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    public SignInFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        View view = getView();
        ButterKnife.bind(this, view);
    }

    @OnClick({ R.id.sign })
    public void onClickView(View view) {
        switch(view.getId()) {
            case R.id.sign:
                if (isProcessing) return;
                Map map = new HashMap();
                map.put("email", emailAccount.getText().toString());
                map.put("password", mailPassword.getText().toString());
                login(map);
                break;
        }
    }

    protected void login(Map map) {
        Service mService = new Service();
        mService.setServiceCode(Config.POST_USER_LOGIN_CODE);
        mService.setServiceName(Config.POST_USER_LOGIN);
        mService.setServiceType(Config.POST);
        mService.setHeaders(Utility.getJsonAccess());
        mService.setServiceInput(map);
        mService.setNotificationTask(this);
        new LoadServices().loadOnExecutor(mService);
        openProgressBar(true);
    }

    @Override
    public void completed(Service response) {
        openProgressBar(false);

        switch(response.getServiceCode()) {

            case Config.POST_USER_LOGIN_CODE: {
                RegistrationResponse rr;
                RegistrationErrorWrapper errorWrapper;

                try {
                    rr = Utility.parseJSON(response.getOutput(),
                            RegistrationResponse.class);
                    if (rr == null) {
                        errorWrapper = Utility.parseJSON(response.getOutput(),
                                RegistrationErrorWrapper.class);

                        // Error Query
                        if (errorWrapper != null) {
                            Utility.showToast(errorWrapper.getError().getMessage());
                        } else {
                            Utility.showToast("server not responded");
                        }
                    } else {
                        // Successful query
                        Utility.storeUserProfile(rr);

                        // Move to User Session
                        Utility.sendEvent(Config.SESSION_APP, null);
                    }
                } catch (Exception e) {
                    Utility.showToast("server not responded");
                }
            }
            break;
            default:
                break;
        }
    }
}
