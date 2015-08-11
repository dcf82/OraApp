package com.imagination.technologies.ora.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.adapters.VisaViewPagerAdapter;
import com.visa.checkout.VisaLibrary;
import com.visa.checkout.VisaMcomLibrary;
import com.visa.checkout.VisaPaymentSummary;
import com.visa.checkout.utils.VisaEnvironmentConfig;

import static com.imagination.technologies.ora.app.visa.ConfigureVisaPaymentInfo.VISA_CHECKOUT_API_KEY;
import static com.imagination.technologies.ora.app.visa.ConfigureVisaPaymentInfo.VISA_CHECKOUT_ENVIRONMENT_CONFIG;

public class MainActivityVisa extends BaseActivity {

    private final String TAG = MainActivityVisa.class.getSimpleName();

    private static final int VISA_CHECKOUT_REQUEST_CODE = 10102;
    private VisaPaymentSummary paymentSummary = null;

    private static VisaMcomLibrary visaMcomLibrary = null;

    public static VisaMcomLibrary getVisaMcomLibrary() {
        return visaMcomLibrary;
    }

    private void initializeVisaCheckoutSdk() {
        VisaEnvironmentConfig visaEnvironmentConfig = VISA_CHECKOUT_ENVIRONMENT_CONFIG;
        /** Optional: a non-empty profile name obtained during onboarding can be included here.
         *  visaEnvironmentConfig.setMerchantProfileName(VISA_CHECKOUT_PROFILE_NAME);
         */
        visaEnvironmentConfig.setMerchantApiKey(VISA_CHECKOUT_API_KEY);
        /** Required for EXOButton integration only. Optional for VisaPaymentButton and CustomView integration.
         *  RequestCode to start SDK activity with, may be used in onActivityResult() as indicator that result came from the SDK. */
        visaEnvironmentConfig.setVisaCheckoutRequestCode(VISA_CHECKOUT_REQUEST_CODE);
        /** getLibrary should be invoked when the activity/application is created. getLibrary initializes the SDK
         in the background and makes the launch faster when user clicks the checkout button/view.  */
        visaMcomLibrary = VisaMcomLibrary.getLibrary(this, visaEnvironmentConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_visa);

        initializeVisaCheckoutSdk();

        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new VisaViewPagerAdapter(getSupportFragmentManager()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // reinitialize the SDK to prepare for next launch
        Toast.makeText(this, "Reinitializing Visa Checkout SDK", Toast.LENGTH_LONG).show();
        initializeVisaCheckoutSdk();

        Log.d(TAG, "Result got back from Visa Checkout SDK");
        String result = "Failed";
        if (data != null) {
            if (data.getParcelableExtra(VisaLibrary.PAYMENT_SUMMARY) != null) {
                paymentSummary =
                        data.getParcelableExtra(VisaLibrary.PAYMENT_SUMMARY);
                if (paymentSummary != null) {
                    Log.d(TAG, "PURCHASE SUCCESS : \n : countryCode : "
                                    + paymentSummary.getCountryCode()
                                    + "\n postalCode : " + paymentSummary.getPostalCode()
                                    + "\n lastFourDigits : " + paymentSummary.getLastFourDigits()
                                    + "\n cardBrand : " + paymentSummary.getCardBrand()
                                    + "\n cardType : " + paymentSummary.getCardType()
                                    + "\n encPaymentData : " + paymentSummary.getEncPaymentData()
                                    + "\n encKey : " + paymentSummary.getEncKey()
                    );
                    result = "Success";
                }
            }
            else if (data.getStringExtra(VisaLibrary.VALIDATION_RESULTS) != null) {
                result = data.getStringExtra(VisaLibrary.VALIDATION_RESULTS);
            }
        }
        if (requestCode == VISA_CHECKOUT_REQUEST_CODE) {
            String msg = "purchase result " + result;
            Log.d(TAG, msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
        if (resultCode == 505) {
            Toast.makeText(this, "SDK Version is not Supported, Result Code : " +
                    resultCode, Toast.LENGTH_LONG).show();
        }
        if (resultCode == 506) {
            Toast.makeText(this,
                    "SDK Version Check couldn't be completed, Result Code : "
                            + resultCode, Toast.LENGTH_LONG).show();
        }
        if (resultCode == 510) {
            Toast.makeText(this, "Device OS version is not supported : " +
                    resultCode, Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public VisaPaymentSummary getPaymentSummary() {
        return paymentSummary;
    }
}
