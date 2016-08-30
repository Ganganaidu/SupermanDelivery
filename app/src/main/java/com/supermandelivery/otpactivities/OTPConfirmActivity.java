package com.supermandelivery.otpactivities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.supermandelivery.QuickstartPreferences;
import com.supermandelivery.R;
import com.supermandelivery.RegistrationIntentService;
import com.supermandelivery.activites.BaseActivity;
import com.supermandelivery.listeners.RequestListener;
import com.supermandelivery.models.LoginResponse;
import com.supermandelivery.models.OtpResponse;
import com.supermandelivery.models.PushToken;
import com.supermandelivery.network.RestAPIRequest;
import com.supermandelivery.network.RestRequestInterface;
import com.supermandelivery.permissions.AndroidPermissions;
import com.supermandelivery.recivers.MySMSReceiver;
import com.supermandelivery.utils.AppPreferences;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Ganga on 3/13/2016.
 */
public class OTPConfirmActivity extends BaseActivity implements MySMSReceiver.SMSReceiveCallBack {

    private static final String TAG = "OTPConfirmActivity";

    @Bind(R.id.vcode_editText)
    EditText vcodeEditText;

    @Bind(R.id.resend_button)
    Button resendButton;

    @Bind(R.id.otpConfirm_button)
    Button otpConfirmButton;

    private boolean isOtpGenerated;
    private OtpResponse otpResponse;
    private MySMSReceiver smsReceiver;
    private PushToken pushToken;
    private Timer buttonTimer;

    private static boolean isPermissionRequested = false;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_confirm_activity);
        ButterKnife.bind(this);

        pushToken = new PushToken();

        //updating status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        //sending otp password to server
        generateOtp();
        pushNotificationDetails();

        //updating status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }


    private void pushNotificationDetails() {
        // receiver for receiving push notification token after successful registration
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    //display token
                    Log.e(TAG, "Push Toke:" + sentToken);
                } else {
                    //no token
                }
            }
        };

        // Registering BroadcastReceiver for push
        registerPushReceiver();

        //check google play services available or not and then call GCM push registration service
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private void registerPushReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @OnClick({R.id.resend_button, R.id.otpConfirm_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resend_button:
                if (!isOtpGenerated) {
                    isOtpGenerated = true;
                    resendButton.setEnabled(false);
                    resendButton.setTextColor(getResources().getColor(R.color.gray_lighter));
                    generateOtp();
                }
                break;
            case R.id.otpConfirm_button:
                if (vcodeEditText.getText().toString().trim().length() != 0) {
                    getAccessToken();
                }
                break;
        }
    }

    @Override
    public void onSmsReceived(String msg) {
        vcodeEditText.setText(msg);
    }

    //send phone number to server..and wait for OTP number ...
    private void generateOtp() {
        showProgress(this, "Please wait.... Contacting Server for OTP number");

        RestRequestInterface myLogin = RestAPIRequest.getRetrofit().create(RestRequestInterface.class);//this is how retrofit create your api
        Call<OtpResponse> call = myLogin.createUserOtpCall(AppPreferences.getInstance().getPhoneNumber());
        RestAPIRequest.getInstance().doRequest(call, new RequestListener<OtpResponse>() {
            @Override
            public void onResponse(OtpResponse response) {
                hideProgress();
                startResendTimer();

                if (response != null) {
                    otpResponse = response;
                    //remove this in final build
                    vcodeEditText.setText(response.getOtp());
                }
            }

            @Override
            public void onDisplayError(String errorMsg) {
                hideProgress();
            }
        });
    }

    //We need to send OTP and phone number to get Access token from server.
    private void getAccessToken() {

        String username = AppPreferences.getInstance().getPhoneNumber();
        String password = vcodeEditText.getText().toString().trim();

        showProgress(this, "Please wait.... Contacting Server for OPT number");

        RestRequestInterface myLogin = RestAPIRequest.getRetrofit().create(RestRequestInterface.class);//this is how retrofit create your api
        Call<LoginResponse> call = myLogin.getAccessToken(username, password);
        RestAPIRequest.getInstance().doRequest(call, new RequestListener<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse response) {
                if (response != null) {

                    //save access token and user id for later use
                    AppPreferences.getInstance().saveAccessToken(response.getId());
                    AppPreferences.getInstance().saveUserId(response.getUserId());

                    pushToken.setDeviceToken(AppPreferences.getInstance().getPushToken());
                    pushToken.setAppId(getResources().getString(R.string.gcm_senderId));
                    pushToken.setDeviceType("Android");
                    pushToken.setUserId(response.getUserId());

                    //after success log in response we need to send push token to server
                    sendPushNotificationToServer();
                }
            }

            @Override
            public void onDisplayError(String errorMsg) {
                hideProgress();
            }
        });
    }

    //send push notification token to server
    private void sendPushNotificationToServer() {

        RestRequestInterface myLogin = RestAPIRequest.getRetrofit().create(RestRequestInterface.class);//this is how retrofit create your api
        Call<PushToken> call = myLogin.sendGCMPushToken(AppPreferences.getInstance().getAccessToken(), pushToken);
        RestAPIRequest.getInstance().doRequest(call, new RequestListener<PushToken>() {
            @Override
            public void onResponse(PushToken response) {
                hideProgress();
                if (response != null) {
                    nextActivity();
                }
            }

            @Override
            public void onDisplayError(String errorMsg) {
                hideProgress();
            }
        });
    }

    private void nextActivity() {
        //set this true... so that we will not call this cass again
        AppPreferences.getInstance().saveOtpConfirmState(true);

        Intent in = new Intent(this, UserProfileActivity.class);
        in.putExtra("fromSplash", true);
        startActivity(in);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerPushReceiver();

        if (!AndroidPermissions.getInstance().checkSmsPermission(this) && !isPermissionRequested) {
            AndroidPermissions.getInstance().displaySmsPermission(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopResendTimer();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopResendTimer();
        ButterKnife.unbind(this);
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }

    // we need to wait for 5 sec for enable this button
    private void startResendTimer() {
        try {
            buttonTimer = new Timer();
            buttonTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isOtpGenerated = false;
                            //disable and enable button
                            resendButton.setEnabled(true);
                            resendButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                        }
                    });
                }
            }, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopResendTimer() {
        if (buttonTimer != null) {
            buttonTimer.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent in = new Intent(this, OTPActivity.class);
        startActivity(in);
        finish();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AndroidPermissions.REQUEST_SMS:
                isPermissionRequested = true;
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // we need to get sms when user get sms
                    // we will use this receiver for reading SMS from phone Inbox
                    smsReceiver = new MySMSReceiver(this); // Create the receiver
                    registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED")); // Register receiver\\\
                } else {
                    AndroidPermissions.getInstance().displayAlert(this, AndroidPermissions.REQUEST_SMS);
                }
                break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}
