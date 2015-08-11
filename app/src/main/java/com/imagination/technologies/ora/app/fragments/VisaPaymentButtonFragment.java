package com.imagination.technologies.ora.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.activities.MainActivityVisa;
import com.imagination.technologies.ora.app.visa.ConfigureVisaPaymentInfo;
import com.visa.checkout.VisaPaymentInfo;
import com.visa.checkout.widget.VisaPaymentButton;

import static com.imagination.technologies.ora.app.visa.ConfigureVisaPaymentInfo.VISA_CHECKOUT_REQUEST_CODE;

/**
 *  Fragment demonstrating integration of Visa Checkout with VisaPaymentButton widget
 */
public class VisaPaymentButtonFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_visa_payment_button, viewGroup, false);

        VisaPaymentButton visaPaymentButton = (VisaPaymentButton) view.findViewById(R.id.visaCheckoutButton);

        // set the onclick listener for the custom view to call Visa checkout
        visaPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkoutWithVisa(view);
            }
        });

        return view;
    }

    public void checkoutWithVisa(View view) {
        VisaPaymentInfo visaPaymentInfo = ConfigureVisaPaymentInfo.getSampleVisaPaymentInfo();
        MainActivityVisa.getVisaMcomLibrary().checkoutWithPayment(visaPaymentInfo, VISA_CHECKOUT_REQUEST_CODE);
    }
}
