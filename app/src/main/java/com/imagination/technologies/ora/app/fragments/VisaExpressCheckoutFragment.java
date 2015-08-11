package com.imagination.technologies.ora.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.visa.ConfigureVisaPaymentInfo;
import com.visa.checkout.VisaPaymentInfo;
import com.visa.checkout.widget.VisaExpressCheckoutButton;

/**
 *  Fragment demonstrating Visa Checkout integration with the Express Checkout Animated Button
 */
public class VisaExpressCheckoutFragment extends Fragment implements VisaExpressCheckoutButton.CheckoutWithVisaListener {

    private VisaPaymentInfo mVisaPaymentInfo = ConfigureVisaPaymentInfo.getSampleVisaPaymentInfo();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_express_checkout, viewGroup, false);

        VisaExpressCheckoutButton visaPaymentButton = (VisaExpressCheckoutButton) view.findViewById(R.id.visaEXOButton);
        visaPaymentButton.setCheckoutListener(this);

        return view;
    }

    @Override
    public VisaPaymentInfo getPaymentInfo() {
        return mVisaPaymentInfo;
    }
}
