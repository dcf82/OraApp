package com.imagination.technologies.ora.app.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.adapters.NavigationAdapter;
import com.imagination.technologies.ora.app.beans.Option;
import com.imagination.technologies.ora.app.controller.Config;
import com.imagination.technologies.ora.app.controller.OraInteractiveApp;
import com.imagination.technologies.ora.app.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NavigationFragment extends Fragment implements AdapterView.OnItemClickListener{
    static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String LOG = NavigationFragment.class.getName();

    NavigationDrawerCallbacks mCallbacks;
    ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;

    @Bind(R.id.options)
    ListView mDrawerListView;
    @Bind(R.id.myPhoto)
    ImageView myPhoto;
    @Bind(R.id.myName)
    TextView myName;

    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    ArrayList<Option> options = new ArrayList<>();
    NavigationAdapter mAdapter;

    NavigationReceiver mReceiver;
    IntentFilter mFilter;

    public NavigationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserLearnedDrawer = Utility.readValueFromProfile(boolean.class, PREF_USER_LEARNED_DRAWER, false);
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        ButterKnife.bind(this, view);

        mDrawerListView = (ListView) view.findViewById(R.id.options);
        mDrawerListView.setOnItemClickListener(this);

        loadOptions();
        mAdapter = new NavigationAdapter(getActivity(), R.layout.navigation_option,
                options);
        mDrawerListView.setAdapter(mAdapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        updateOptions();

        mReceiver = new NavigationReceiver();
        mFilter = new IntentFilter();
        mFilter.addAction(Config.NO_SESSION_APP);
        mFilter.addAction(Config.SESSION_APP);
        mFilter.addAction(Config.OPEN_USER_PROFILE);

        updateName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.navigation_menu, container, false);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     * @param toolbar Toolbar to be used for this navigator
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);

        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        setUpActionBar();

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),
                mDrawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                if (!isAdded()) return;

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    Utility.writeValueToProfile(boolean.class, PREF_USER_LEARNED_DRAWER, true);
                }

                // calls onPrepareOptionsMenu()
                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!isAdded()) return;

                // calls onPrepareOptionsMenu()
                getActivity().supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    protected void setUpActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
        }
    }

    private void selectItem(int position) {

        mCurrentSelectedPosition = position;

        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }

        setOption(position);
    }

    public void setOption(int position) {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }

        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mReceiver, mFilter);
        Log.i(LOG, "onResume()");
    }

    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
        Log.i(LOG, "onPause()");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected List<Option> loadOptions() {
        ArrayList<Option> options = new ArrayList<>();
        Resources resources = getResources();

        if (Utility.readValueFromProfile(boolean.class, Config.IS_USER_LOGGED, false)) {
            options.add(new Option(Config.HOME,
                    resources.getString(R.string.home)));
            options.add(new Option(Config.MENU,
                    resources.getString(R.string.menu)));
            options.add(new Option(Config.MY_LOCATIONS,
                    resources.getString(R.string.oMyLocations)));
            options.add(new Option(Config.LOCATIONS_PROVIDER,
                    resources.getString(R.string.locatorProvider)));
            options.add(new Option(Config.VISA_CHECKOUT,
                    resources.getString(R.string.visaCheckout)));
        } else {
            options.add(new Option(Config.SIGN_IN,
                    resources.getString(R.string.oSignIn)));
            options.add(new Option(Config.SIGN_UP,
                    resources.getString(R.string.oSignUp)));
            options.add(new Option(Config.MY_LOCATIONS,
                    resources.getString(R.string.oMyLocations)));
            options.add(new Option(Config.LOCATIONS_PROVIDER,
                    resources.getString(R.string.locatorProvider)));
            options.add(new Option(Config.VISA_CHECKOUT,
                    resources.getString(R.string.visaCheckout)));
        }

        return options;
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public final ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setOption(options.get(position).getId());
    }

    public void updateOptions() {
        mAdapter.clear();
        mAdapter.addAll(loadOptions());
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    private class NavigationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateOptions();
            if (intent.getAction().equals(Config.NO_SESSION_APP)) {
                setOption(Config.SIGN);
            } else if (intent.getAction().equals(Config.SESSION_APP)) {
                setOption(Config.HOME_NEW);
                OraInteractiveApp.getApp().loadServices();
                updateName();
            } else if (intent.getAction().equals(Config.OPEN_USER_PROFILE)) {
                setOption(Config.PROFILE);
            }
        }
    }

    protected void updateName() {
        myName.setText(Utility.readValueFromProfile(String.class, Config.FIRST_NAME, "Me"));
    }
}
