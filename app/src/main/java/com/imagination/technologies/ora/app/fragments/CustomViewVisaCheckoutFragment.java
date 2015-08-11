package com.imagination.technologies.ora.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.visa.ConfigureVisaPaymentInfo;
import com.visa.checkout.VisaMcomLibrary;
import com.visa.checkout.VisaPaymentInfo;
import com.visa.checkout.utils.VisaEnvironmentConfig;

import static com.imagination.technologies.ora.app.visa.ConfigureVisaPaymentInfo.VISA_CHECKOUT_API_KEY;
import static com.imagination.technologies.ora.app.visa.ConfigureVisaPaymentInfo.VISA_CHECKOUT_ENVIRONMENT_CONFIG;
import static com.imagination.technologies.ora.app.visa.ConfigureVisaPaymentInfo.VISA_CHECKOUT_REQUEST_CODE;

/**
 *  Fragment demonstrating Visa Checkout integration with a custom view
 */
public class CustomViewVisaCheckoutFragment extends Fragment {

    private VisaMcomLibrary mVisaMcomLibrary;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_custom_view_checkout, viewGroup, false);

        ImageView customView = (ImageView) view.findViewById(R.id.visaCheckoutCustomView);

        // set the onclick listener for the custom view to call Visa checkout
        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customViewCheckoutWithVisa(view);
            }
        });

        VisaEnvironmentConfig visaEnvironmentConfig = VISA_CHECKOUT_ENVIRONMENT_CONFIG;
        /** Optional: a non-empty profile name obtained during onboarding can be included here.
         * visaEnvironmentConfig.setMerchantProfileName("<YOUR_PROFILE_NAME>");
         */
        visaEnvironmentConfig.setMerchantApiKey(VISA_CHECKOUT_API_KEY);

        /** If you are using a custom view, you should pass the view to getLibrary. getLibrary initializes the SDK
         in the background and makes the launch faster when user clicks the checkout button/view.  */
        mVisaMcomLibrary = VisaMcomLibrary.getLibrary(getActivity(), visaEnvironmentConfig, customView);

        return view;
    }

    public void customViewCheckoutWithVisa(View view) {
        VisaPaymentInfo visaPaymentInfo = ConfigureVisaPaymentInfo.getSampleVisaPaymentInfo();
        mVisaMcomLibrary.checkoutWithPayment(visaPaymentInfo, VISA_CHECKOUT_REQUEST_CODE);
    }
}
