package com.imagination.technologies.ora.app.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.imagination.technologies.ora.app.fragments.CustomViewVisaCheckoutFragment;
import com.imagination.technologies.ora.app.fragments.VisaExpressCheckoutFragment;
import com.imagination.technologies.ora.app.fragments.VisaPaymentButtonFragment;

/**
 *  Adapter for tab fragments
 */
public class VisaViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;

    // Tab Titles
    private String tabtitles[] = new String[] { "Express Checkout" , "Visa Payment Button", "Custom View" };

    public VisaViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 2:
                return new CustomViewVisaCheckoutFragment();
            case 1:
                return new VisaPaymentButtonFragment();
            case 0:
                return new VisaExpressCheckoutFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
