package com.imagination.technologies.ora.app.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.imagination.technologies.ora.app.fragments.SignInFragment;
import com.imagination.technologies.ora.app.fragments.SignUpFragment;

public class SignAdapter extends FragmentStatePagerAdapter {
    private static final int NUMBER = 2;

    public SignAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f;

        if (position == 0) {
            f = SignInFragment.newInstance();
        } else {
            f = SignUpFragment.newInstance();
        }

        return f;
    }

    @Override
    public int getCount() {
        return NUMBER;
    }
}
