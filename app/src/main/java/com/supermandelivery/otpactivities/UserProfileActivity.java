package com.supermandelivery.otpactivities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.supermandelivery.MainActivity;
import com.supermandelivery.R;
import com.supermandelivery.activites.BaseActivity;
import com.supermandelivery.listeners.RequestListener;
import com.supermandelivery.models.UserProfile;
import com.supermandelivery.network.RestAPIRequest;
import com.supermandelivery.network.RestRequestInterface;
import com.supermandelivery.utils.AppPreferences;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Ganga on 3/14/2016.
 */
public class UserProfileActivity extends BaseActivity {

    private static final String TAG = "UserProfileActivity";

    @Bind(R.id.profName_editText)
    EditText profNameEditText;

    @Bind(R.id.profPhno_editText)
    EditText profPhnoEditText;

    @Bind(R.id.profMail_editText)
    EditText profMailEditText;

    @Bind(R.id.profUpdateInfo_btn)
    Button profUpdateInfoBtn;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;

    UserProfile mUserProfile;
    private boolean isFromSplash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_activity);
        ButterKnife.bind(this);

        mUserProfile = new UserProfile();

        //set up tool bar
        setSupportActionBar(toolbar);

        //based on this flag we will close or open activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isFromSplash = extras.getBoolean("fromSplash");
        }

        //updating previous stored details
        profNameEditText.setText(AppPreferences.getInstance().getUserName());
        profMailEditText.setText(AppPreferences.getInstance().getUserEmailId());
        profPhnoEditText.setText(AppPreferences.getInstance().getPhoneNumber());

        //updating tool bar with name and button
        setUpToolBar();
    }

    private void setUpToolBar() {
        //set title for this activity
        toolbarTitle.setText(getResources().getString(R.string.my_profile));

        //navigation close
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextActivity();
                    }
                });
        //updating status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.profUpdateInfo_btn)
    public void onClick() {

        //saving user details
        mUserProfile.setFullName(profNameEditText.getText().toString().trim());
        mUserProfile.setEmail(profMailEditText.getText().toString().trim());
        mUserProfile.setUsername(profPhnoEditText.getText().toString().trim());
        mUserProfile.setId(AppPreferences.getInstance().getUserId());

        //validating user details
        if (!isValidEmail(mUserProfile.getEmail())) {
            Toast.makeText(this, "Wrong email id", Toast.LENGTH_SHORT).show();
        } else if (mUserProfile.getFullName().length() == 0) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        } else if (mUserProfile.getUsername().length() == 0) {
            Toast.makeText(this, "please enter phone number", Toast.LENGTH_SHORT).show();
        } else {

            //sending user details to server
            showProgress(this, "Please wait... updating profile");

            RestRequestInterface request = RestAPIRequest.getRetrofit().create(RestRequestInterface.class);//this is how retrofit create your api
            Call<UserProfile> call = request.updateProfile(AppPreferences.getInstance().getAccessToken(),
                    mUserProfile, AppPreferences.getInstance().getUserId());
            RestAPIRequest.getInstance().doRequest(call, new RequestListener<UserProfile>() {
                @Override
                public void onResponse(UserProfile response) {
                    hideProgress();
                    AppPreferences.getInstance().saveProfileState(true);

                    //save user details to use in the application
                    AppPreferences.getInstance().saveUserEmailId(mUserProfile.getEmail());
                    AppPreferences.getInstance().savePhoneNUmber(mUserProfile.getUsername());
                    AppPreferences.getInstance().saveUserName(mUserProfile.getFullName());

                    nextActivity();
                }

                @Override
                public void onDisplayError(String errorMsg) {
                    hideProgress();
                }
            });
        }
    }

    /*validate email address*/
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void nextActivity() {
        if (isFromSplash) { //For the first time no need to close
            Intent in = new Intent(this, MainActivity.class);
            startActivity(in);
        } else {
            finish(); //close activity
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        nextActivity();
    }
}
