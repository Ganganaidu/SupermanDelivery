package com.supermandelivery.otpactivities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.supermandelivery.activites.BaseActivity;
import com.supermandelivery.R;
import com.supermandelivery.utils.AppPreferences;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ganga on 3/13/2016.
 */
public class OTPActivity extends BaseActivity {

    @Bind(R.id.phno_editText)
    EditText phoneEditText;

    @Bind(R.id.otpcont_button)
    Button otpcontButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_register_activity);
        ButterKnife.bind(this);

        //updating status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @OnClick(R.id.otpcont_button)
    public void onClick() {

        String phoneNumber = phoneEditText.getText().toString().trim();

        if (phoneNumber.length() > 0 && PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            AppPreferences.getInstance().saveOtpState(true);
            AppPreferences.getInstance().savePhoneNUmber(phoneNumber);

            Intent in = new Intent(this, OTPConfirmActivity.class);
            startActivity(in);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
