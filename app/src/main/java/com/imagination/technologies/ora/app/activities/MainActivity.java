package com.imagination.technologies.ora.app.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.controller.Config;
import com.imagination.technologies.ora.app.fragments.HomeFragment;
import com.imagination.technologies.ora.app.fragments.MenuFragment;
import com.imagination.technologies.ora.app.fragments.MyLocationFragment;
import com.imagination.technologies.ora.app.fragments.MyProfileFragment;
import com.imagination.technologies.ora.app.fragments.NavigationFragment;
import com.imagination.technologies.ora.app.fragments.SignFragment;
import com.imagination.technologies.ora.app.interfaces.OnFragmentInteractionListener;
import com.imagination.technologies.ora.app.utilities.Utility;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements
        NavigationFragment.NavigationDrawerCallbacks,
        OnFragmentInteractionListener {

    static final String LOG = MainActivity.class.getSimpleName();
    NavigationFragment mNavigationFragment;
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mNavigationFragment = (NavigationFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Setup drawer toggle
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Setup toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Set up the drawer.
        mNavigationFragment.setUp(R.id.navigation_drawer, mDrawerLayout, mToolbar);
        mDrawerToggle = mNavigationFragment.getDrawerToggle();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction;

        if (savedInstanceState == null) {
            transaction = fragmentManager.beginTransaction();
            if (Utility.readValueFromProfile(boolean.class,Config.IS_USER_LOGGED, false)) {
                transaction.replace(R.id.container, HomeFragment.newInstance(), HomeFragment.class.getName())
                        .addToBackStack(HomeFragment.class.getName());
            } else {
                transaction.replace(R.id.container, SignFragment.newInstance(), SignFragment.class.getName())
                        .addToBackStack(SignFragment.class.getName());
            }
            transaction.commit();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int id) {
        Fragment fragment;
        String tag = null;
        Bundle value = null;

        switch(id) {
            case Config.SIGN_IN:
                fragment = getSupportFragmentManager().findFragmentByTag(SignFragment
                        .class.getName());
                if (fragment != null && fragment.isAdded()) {
                    SignFragment sg = (SignFragment)fragment;
                    sg.moveToPage(0);
                    return;
                }
                tag = SignFragment.class.getName();
                value = new Bundle();
                value.putInt("index" , 0);
                break;
            case Config.SIGN_UP:
                fragment = getSupportFragmentManager().findFragmentByTag(SignFragment
                        .class.getName());
                if (fragment != null && fragment.isAdded()) {
                    SignFragment sg = (SignFragment) fragment;
                    sg.moveToPage(1);
                    return;
                }
                tag = SignFragment.class.getName();
                value = new Bundle();
                value.putInt("index" , 1);
                break;
            case Config.SIGN:
                tag = SignFragment.class.getName();
                value = new Bundle();
                value.putBoolean("emptyFragmentStack", true);
                break;
            case Config.HOME:
            case Config.HOME_NEW:
                tag = HomeFragment.class.getName();
                if (id == Config.HOME_NEW) {
                    value = new Bundle();
                    value.putBoolean("emptyFragmentStack", true);
                }
                break;
            case Config.MENU:
                tag = MenuFragment.class.getName();
                break;
            case Config.PROFILE:
                tag = MyProfileFragment.class.getName();
                break;
            case Config.MY_LOCATIONS:
                tag = MyLocationFragment.class.getName();
                break;
            case Config.LOCATIONS_PROVIDER:
                startActivity(new Intent(this, LocationActivity.class));
                return;
            case Config.VISA_CHECKOUT:
                startActivity(new Intent(this, MainActivityVisa.class));
                return;
        }

        if (tag != null) {
            Utility.replaceFragment(getSupportFragmentManager(), tag,
                    R.id.container, value);
        }
    }

    @Override
    public void onFragmentInteraction(Bundle data) {
        switch (data.getInt(Config.ACTION, 0)) {
            case Config.INDETERMINATE_PROGRESS_BAR:
                openIndeterminateBar(data.getBoolean(Config.OPEN_INDETERMINATE_PROGRESS_BAR, false));
                break;
            default:
                break;
        }
    }

    public void openIndeterminateBar(boolean open) {
        if (open) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
