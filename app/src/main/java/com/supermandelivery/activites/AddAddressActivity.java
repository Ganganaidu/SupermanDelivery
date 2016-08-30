package com.supermandelivery.activites;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.supermandelivery.MyApplication;
import com.supermandelivery.R;
import com.supermandelivery.fragments.SavedAddressFragment;
import com.supermandelivery.listeners.RequestListener;
import com.supermandelivery.models.DropAddress;
import com.supermandelivery.models.FavAddress;
import com.supermandelivery.models.Geopoint;
import com.supermandelivery.models.SavedAddress;
import com.supermandelivery.network.RestAPIRequest;
import com.supermandelivery.network.RestRequestInterface;
import com.supermandelivery.permissions.AndroidPermissions;
import com.supermandelivery.utils.AppPreferences;
import com.supermandelivery.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by ADMIN on 3/20/2016.
 */
public class AddAddressActivity extends BaseActivity {

    private static final String TAG = "AddAddressActivity";
    static final int PICK_CONTACT_REQUEST = 102;  // The request code

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.addPhno_editText)
    EditText addPhnoEditText;
    @Bind(R.id.addName_editText)
    EditText addNameEditText;
    @Bind(R.id.addFlatNo_editText)
    EditText addFlatNoEditText;
    @Bind(R.id.addAdres_editText)
    EditText addAdresEditText;
    @Bind(R.id.addInstruct_editText)
    EditText addInstructEditText;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    SavedAddress mSavedAddress;
    boolean isAddressFromSearch;

    String favAddress = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adresses);
        ButterKnife.bind(this);

        mSavedAddress = new SavedAddress();

        setUpToolBar();

        //remove default geo point to save new one
        MyApplication.getInstance().setGeoPoint(null);

        favAddress = AppPreferences.getInstance().getFavAddress();
        mSavedAddress.setAddress(favAddress);

        addNameEditText.setText(AppPreferences.getInstance().getFavUser());
        addPhnoEditText.setText(AppPreferences.getInstance().getFavPhn());
        addFlatNoEditText.setText(AppPreferences.getInstance().getFavFlatNo());
        addAdresEditText.setText(favAddress);

        AppPreferences.getInstance().saveFavAddress("");
        AppPreferences.getInstance().saveFavUser("");
        AppPreferences.getInstance().saveFavPhn("");
        AppPreferences.getInstance().saveFavFlatNo("");
    }

    private void setUpToolBar() {
        //set up tool bar
        setSupportActionBar(toolbar);

        //set title for this activity
        toolbarTitle.setText(getResources().getString(R.string.add_address));

        //navigation close
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
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
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                onClick();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.addAdres_editText, R.id.addContact_imageBtn, R.id.load_username_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addAdres_editText:
                AppPreferences.getInstance().saveFavUser(addNameEditText.getText().toString());
                AppPreferences.getInstance().saveFavPhn(addPhnoEditText.getText().toString());
                AppPreferences.getInstance().saveFavFlatNo(addFlatNoEditText.getText().toString());
                Intent in = new Intent(this, FavMapActivity.class);
                startActivity(in);
                break;
            case R.id.load_username_icon:
                addNameEditText.setText(AppPreferences.getInstance().getUserName());
                addPhnoEditText.setText(AppPreferences.getInstance().getPhoneNumber());
                break;
            case R.id.addContact_imageBtn:
                if (AndroidPermissions.getInstance().checkContactPermission(this)) {
                    pickContact();
                } else {
                    AndroidPermissions.getInstance().displayContactPermission(this);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.getInstance().getGeoPoint() != null) {
            isAddressFromSearch = true;
            MyApplication.getInstance().getLatLngArrayList().add(0, MyApplication.getInstance().getGeoPoint());

            mSavedAddress.setAddress(MyApplication.getInstance().getAddress().getAddress());
            mSavedAddress.setGeopoint(MyApplication.getInstance().getAddress().getGeopoint());

//            addAdresEditText.setText(MyApplication.getInstance().getAddress().getAddress());

        } else {
            isAddressFromSearch = false;
        }
    }

    // validating fields and sending details to server
    public void onClick() {
        mSavedAddress.setName(addNameEditText.getText().toString().trim());
        mSavedAddress.setAddress(addAdresEditText.getText().toString().trim());
        mSavedAddress.setFlatNo(addFlatNoEditText.getText().toString().trim());
        mSavedAddress.setContactNo(addPhnoEditText.getText().toString().trim());
        mSavedAddress.setRemarks(addInstructEditText.getText().toString().trim());

        if (mSavedAddress.getName().length() == 0) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        } else if (mSavedAddress.getContactNo().length() == 0) {
            Toast.makeText(this, "please enter phone number", Toast.LENGTH_SHORT).show();
        } else if (mSavedAddress.getFlatNo().length() == 0) {
            Toast.makeText(this, "please enter flat number", Toast.LENGTH_SHORT).show();
        } else if (mSavedAddress.getAddress().length() == 0) {
            Toast.makeText(this, "please enter Address", Toast.LENGTH_SHORT).show();
        }

        showProgress(this, "Please wait saving address");
        if (isAddressFromSearch) {
            sendAddressToServer();
        } else {
            getLocationFromAddress(mSavedAddress.getAddress());
        }
    }

    private void sendAddressToServer() {
        RestRequestInterface request = RestAPIRequest.getRetrofit().create(RestRequestInterface.class);//this is how retrofit create your api
        Call<SavedAddress> call = request.addAddress(AppPreferences.getInstance().getAccessToken(),
                mSavedAddress, AppPreferences.getInstance().getUserId());
        RestAPIRequest.getInstance().doRequest(call, new RequestListener<SavedAddress>() {
            @Override
            public void onResponse(SavedAddress response) {
                hideProgress();
                finish();
            }

            @Override
            public void onDisplayError(String errorMsg) {
                hideProgress();
            }
        });
    }

    List<Address> address = new ArrayList<>();
    Geopoint geopoint = null;

    //get location lat and lang based in address string
    public void getLocationFromAddress(final String strAddress) {
        final Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        new AsyncTask<String, Integer, Geopoint>() {
            @Override
            protected Geopoint doInBackground(String... params) {
                try {
                    address = geocoder.getFromLocationName(strAddress, 5);
                    if (address == null) {
                        return null;
                    }
                    Address location = address.get(0);
                    geopoint = new Geopoint(location.getLatitude(), location.getLongitude());

                } catch (Exception e) {
                    e.printStackTrace();
                    hideProgress();
                }
                return geopoint;
            }

            @Override
            protected void onPostExecute(Geopoint geopoint) {
                Log.e(TAG, "geopoint:" + geopoint.toString());

                mSavedAddress.setGeopoint(geopoint);
                sendAddressToServer();
            }
        }.execute();
    }

    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AndroidPermissions.REQUEST_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickContact();
                } else {
                    AndroidPermissions.getInstance().displayAlert(this, AndroidPermissions.REQUEST_CONTACTS);
                }
                break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT_REQUEST):
                if (resultCode == Activity.RESULT_OK) {
                    contactPicked(data);
                }
        }
    }

    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     *
     * @param data
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            // Set the value to the textviews
            addNameEditText.setText(Utils.getOnlyStrings(name.trim()));
            addPhnoEditText.setText(Utils.getOnlyDigits(phoneNo.trim()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

