package com.supermandelivery.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.supermandelivery.MainActivity;
import com.supermandelivery.R;
import com.supermandelivery.otpactivities.OTPActivity;
import com.supermandelivery.otpactivities.OTPConfirmActivity;
import com.supermandelivery.otpactivities.UserProfileActivity;
import com.supermandelivery.utils.AppPreferences;

/**
 * Created by Ganga on 3/12/2016.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        doInit();
    }

    /** */
    protected void doInit() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startNewActivity();
            }
        }, 2000);
    }

    private void startNewActivity() {
        Intent in;
        if (!AppPreferences.getInstance().getOtpState()) {
            in = new Intent(this, OTPActivity.class);
        } else if (!AppPreferences.getInstance().getOtpConfirmState()) {
            in = new Intent(this, OTPConfirmActivity.class);
        } else if (!AppPreferences.getInstance().getProfileState()) {
            in = new Intent(this, UserProfileActivity.class);
            in.putExtra("fromSplash", true);
        } else {
            in = new Intent(this, MainActivity.class);
        }
        startActivity(in);
        finish();
    }
}
