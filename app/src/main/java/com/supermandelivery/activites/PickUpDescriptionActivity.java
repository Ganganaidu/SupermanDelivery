package com.supermandelivery.activites;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.supermandelivery.MyApplication;
import com.supermandelivery.R;
import com.supermandelivery.adapters.ProductAdapter;
import com.supermandelivery.listeners.RequestListener;
import com.supermandelivery.models.Geopoint;
import com.supermandelivery.models.PickupAddress;
import com.supermandelivery.models.Prodcut;
import com.supermandelivery.models.PurchaseAddress;
import com.supermandelivery.models.SavedAddress;
import com.supermandelivery.network.RestAPIRequest;
import com.supermandelivery.network.RestRequestInterface;
import com.supermandelivery.permissions.AndroidPermissions;
import com.supermandelivery.utils.AppPreferences;
import com.supermandelivery.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class PickUpDescriptionActivity extends BaseActivity {

    private static final String TAG = "PickUpActivity";
    static final int PICK_CONTACT_REQUEST = 102;  // The request code

    @Bind(R.id.location_description_done)
    ImageView mLocationDescriptionDone;
    @Bind(R.id.input_layout_password)
    TextInputLayout mInputLayoutPassword;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.location_description_next)
    TextView mLocationNext;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.pickup_description)
    Button mPickupDescription;
    @Bind(R.id.pckAmtEst_textView)
    TextView mPckAmtEstTextView;
    @Bind(R.id.price_range_button)
    Button mPriceRangeButton;
    @Bind(R.id.pckPhno_editText)
    EditText mPckPhnoEditText;
    @Bind(R.id.pckName_editText)
    EditText mPckNameEditText;
    @Bind(R.id.pckFlatNo_editText)
    EditText mPckFlatNoEditText;
    @Bind(R.id.adresPckup_editText)
    EditText mAdresPckupEditText;
    @Bind(R.id.pckInstruct_editText)
    EditText mPckInstructEditText;
    @Bind(R.id.add_to_favourite_btn)
    Button addTOFavBtn;
    @Bind(R.id.load_phone_number_icon)
    ImageView loadPhoneNumberbtn;
    @Bind(R.id.load_username_icon)
    ImageView loadUserNamebtn;

    private PickupAddress pickupAddress;
    private PurchaseAddress purchaseAddress;
    private boolean isPurchase;
    private String pickUpItemDesc = "";
    private List<Prodcut> pickupDesTypesList;
    private SavedAddress mSavedAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_description);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        setUpToolBar();

        mLocationDescriptionDone.setVisibility(View.GONE);
        mLocationNext.setVisibility(View.VISIBLE);

        mAdresPckupEditText.setKeyListener(null);
        pickupDesTypesList = new ArrayList<>();
        pickupAddress = new PickupAddress();
        purchaseAddress = new PurchaseAddress();

        Bundle bundleObject = getIntent().getExtras();
        if (bundleObject != null) {
            isPurchase = bundleObject.getBoolean("isPurchase", false);
            pickupDesTypesList = (List<Prodcut>) bundleObject.getSerializable("itemDescList");
        }

        //hide and display based on selection from the map page
        if (isPurchase) {
            mPickupDescription.setText(getResources().getString(R.string.purchase_item_desc));
            mPriceRangeButton.setVisibility(View.VISIBLE);
            mPckAmtEstTextView.setVisibility(View.VISIBLE);
            setPurchaseAdrsPreviousData();
        } else {
            setPickupAdrsPreviousData();
            mPriceRangeButton.setVisibility(View.GONE);
            mPckAmtEstTextView.setVisibility(View.GONE);
        }

        if (MyApplication.getInstance().getTripModel() != null) {
            //previous selected price range
            String priceRange = MyApplication.getInstance().getTripModel().getPriceRange();
            if (priceRange != null && priceRange.length() > 0) {
                mPriceRangeButton.setText(priceRange);
            }
        }

        if (pickupDesTypesList == null) {
            pickupDesTypesList = new ArrayList<>();
        }
        //set previous selected data
        if (pickupDesTypesList != null && pickupDesTypesList.size() > 0
                && MyApplication.getInstance().getPickItemPosition() >= 0) {
            mPickupDescription.setText(pickupDesTypesList.get(MyApplication.getInstance().getPickItemPosition()).getName());
        }
        //load product that we will display on the listview dialog
        loadProductData();
    }

    private void setPickupAdrsPreviousData() {
        //set pick up address that was selected in map
        if (MyApplication.getInstance().getTripModel() != null) {
            pickupAddress = MyApplication.getInstance().getTripModel().getPickupAddress();
            Log.e(TAG, "address:" + pickupAddress.toString());

            if (pickupAddress != null) {
                mAdresPckupEditText.setText(pickupAddress.getAddress());
                mPckPhnoEditText.setText(pickupAddress.getContactNo());
                mPckNameEditText.setText(pickupAddress.getName());
                mPckFlatNoEditText.setText(pickupAddress.getFlatNo());
                mPckInstructEditText.setText(pickupAddress.getRemarks());
            }
        }
    }

    private void setPurchaseAdrsPreviousData() {
        //set pick up address that was selected in map
        if (MyApplication.getInstance().getTripModel() != null) {
            purchaseAddress = MyApplication.getInstance().getTripModel().getPurchaseAddress();
            Log.e(TAG, "address:" + purchaseAddress.toString());

            if (purchaseAddress != null) {
                mAdresPckupEditText.setText(purchaseAddress.getAddress());
                mPckPhnoEditText.setText(purchaseAddress.getContactNo());
                mPckNameEditText.setText(purchaseAddress.getName());
                mPckFlatNoEditText.setText(purchaseAddress.getFlatNo());
                mPckInstructEditText.setText(purchaseAddress.getRemarks());
            }
        }
    }


    private void setUpToolBar() {
        mToolbarTitle.setText(getResources().getString(R.string.pickup_description_title));
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        backToSupMapAct();
                    }
                });

        //updating status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void backToSupMapAct() {
        Intent in = new Intent(this, SuperMapActivity.class);
        startActivity(in);
        finish();
    }

    //load product which we will display on pick item description
    private void loadProductData() {
        if (mPickupDescription.isEnabled() && pickupDesTypesList.size() == 0) {

            RestRequestInterface myLogin = RestAPIRequest.getRetrofit().create(RestRequestInterface.class);
            Call<List<Prodcut>> call = myLogin.getProducts(AppPreferences.getInstance().getAccessToken());
            RestAPIRequest.getInstance().doRequest(call, new RequestListener<List<Prodcut>>() {
                @Override
                public void onResponse(List<Prodcut> response) {
                    pickupDesTypesList = response;
                }

                @Override
                public void onDisplayError(String errorMsg) {
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void showPickupPriceRangeDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_price_range_dialog);

        final ListView listView = (ListView) dialog.findViewById(R.id.price_range_listview);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.pickup_ranges)));
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String data = (String) parent.getItemAtPosition(position);
                if (MyApplication.getInstance().getTripModel() != null) {
                    MyApplication.getInstance().getTripModel().setPriceRange(data);
                }
                mPriceRangeButton.setText(data);

                dialog.dismiss();
            }
        });
    }

    private void showPickupDescriptionDialog() {
        // Create custom dialog object
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);

        final ListView instructions_listView = (ListView) dialog.findViewById(R.id.instructions_listView);
        final EditText instructions_editText = (EditText) dialog.findViewById(R.id.instructions_editText);
        Button done_button = (Button) dialog.findViewById(R.id.done_button);

        ProductAdapter productAdapter = new ProductAdapter(this, android.R.id.text1, pickupDesTypesList);
        instructions_listView.setAdapter(productAdapter);

        dialog.show();

        instructions_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //save values to send server
                if (MyApplication.getInstance().getTripModel() != null) {
                    MyApplication.getInstance().getTripModel().setProductId(pickupDesTypesList.get(position).getId());
                }
                MyApplication.getInstance().setPickItemPosition(position);

                //display selected value to user
                pickUpItemDesc = pickupDesTypesList.get(position).getName();
                mPickupDescription.setText(pickUpItemDesc);

                //mark selection type
                setItemSelection(instructions_listView, position);
            }
        });

        //set previous selected item
        if (MyApplication.getInstance().getPickItemPosition() >= 0) {
            setItemSelection(instructions_listView, MyApplication.getInstance().getPickItemPosition());
        }

        //set remarks for purchase dialog
        if (purchaseAddress != null && purchaseAddress.getRemarks() != null && isPurchase) {
            instructions_editText.setText(purchaseAddress.getRemarks());
        }
        if (pickupAddress != null && pickupAddress.getRemarks() != null && !isPurchase) {
            instructions_editText.setText(pickupAddress.getRemarks());
        }

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instruction = instructions_editText.getText().toString().trim();
                if (isPurchase) {
                    purchaseAddress.setRemarks(instruction);
                } else {
                    pickupAddress.setRemarks(instruction);
                }
                dialog.dismiss();
            }
        });
    }

    //mark list view item selection
    private void setItemSelection(ListView instructions_listView, int position) {
        for (int i = 0; i < instructions_listView.getChildCount(); i++) {
            if (position == i) {
                instructions_listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.darkBlue));
            } else {
                instructions_listView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    //TODO
    @OnClick({R.id.location_description_next, R.id.pickup_description,
            R.id.price_range_button, R.id.add_to_favourite_btn,
            R.id.load_phone_number_icon, R.id.load_username_icon, R.id.lead_to_map})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.load_username_icon:
                mPckNameEditText.setText(AppPreferences.getInstance().getUserName());
                mPckPhnoEditText.setText(AppPreferences.getInstance().getPhoneNumber());
                break;
            case R.id.load_phone_number_icon:
                if (AndroidPermissions.getInstance().checkContactPermission(this)) {
                    pickContact();
                } else {
                    AndroidPermissions.getInstance().displayContactPermission(this);
                }
                break;
            case R.id.location_description_next:

                if (pickUpItemDesc.length() == 0) {
                    showToast("Please select pick item description");
                } else if (validateFields()) {

                    if (!isPurchase) {
                        pickupAddress.setContactNo(mPckPhnoEditText.getText().toString());
                        pickupAddress.setFlatNo(mPckFlatNoEditText.getText().toString());
                        pickupAddress.setRemarks(mPckInstructEditText.getText().toString());
                        pickupAddress.setName(mPckNameEditText.getText().toString());

                        if (MyApplication.getInstance().getTripModel() != null) {
                            //no need to send purchase while pick up address selected
                            MyApplication.getInstance().getTripModel().setPurchaseAddress(null);
                            //save pickup
                            MyApplication.getInstance().getTripModel().setPickupAddress(pickupAddress);
                        }
                    } else {
                        pickupAddress = null;
                        purchaseAddress.setContactNo(mPckPhnoEditText.getText().toString());
                        purchaseAddress.setFlatNo(mPckFlatNoEditText.getText().toString());
                        purchaseAddress.setRemarks(mPckInstructEditText.getText().toString());
                        purchaseAddress.setName(mPckNameEditText.getText().toString());

                        if (MyApplication.getInstance().getTripModel() != null) {
                            //no need to send pickup while purchase address selected
                            MyApplication.getInstance().getTripModel().setPickupAddress(null);
                            //save purchase
                            MyApplication.getInstance().getTripModel().setPurchaseAddress(purchaseAddress);
                        }
                    }

                    Intent intent = new Intent(this, DeliveryDescriptionActivity.class);
                    intent.putExtra("isPurchase", isPurchase);
                    intent.putExtra("itemDescList", (Serializable) pickupDesTypesList);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.pickup_description:
                showPickupDescriptionDialog();
                break;
            case R.id.price_range_button:
                showPickupPriceRangeDialog();
                break;
            case R.id.add_to_favourite_btn:
                if (validateFields()) {
                    mSavedAddress = new SavedAddress();
                    mSavedAddress.setContactNo(mPckPhnoEditText.getText().toString());
                    mSavedAddress.setFlatNo(mPckFlatNoEditText.getText().toString());
                    mSavedAddress.setRemarks(mPckInstructEditText.getText().toString());
                    mSavedAddress.setName(mPckNameEditText.getText().toString());
                    mSavedAddress.setAddress(mAdresPckupEditText.getText().toString());

                    if (pickupAddress != null) {
                        mSavedAddress.setGeopoint(pickupAddress.getGeopoint());
                    } else if (purchaseAddress != null) {
                        mSavedAddress.setGeopoint(purchaseAddress.getGeopoint());
                    }
                    showProgress(this, "Please wait adding address to your favourite list ... ");
                    //getLocationFromAddress(mSavedAddress.getAddress());
                    sendAddressToServer();
                }
                break;
            case R.id.lead_to_map:
                //backToSupMapAct();
                break;
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
                showToast("Address Added in your favourite list");
            }

            @Override
            public void onDisplayError(String errorMsg) {
                hideProgress();
                Log.e(TAG, "errorMsg:" + errorMsg);
            }
        });
    }

    private boolean validateFields() {
        String phoneNumber = mPckPhnoEditText.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber) || !Utils.isValidPhoneNumber(phoneNumber)) {
            showToast("Please enter phone number");
            return false;
        } else if (mPckFlatNoEditText.getText().toString().trim().length() == 0) {
            showToast("Please enter your Flat number");
            return false;
        } else if (mPckNameEditText.getText().toString().trim().length() == 0) {
            showToast("Please enter Name");
            return false;
        } else if (mAdresPckupEditText.getText().toString().trim().length() == 0) {
            showToast("Please enter address");
            return false;
        } else {
            return true;
        }
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
                    location.getLatitude();
                    location.getLongitude();

                    geopoint = new Geopoint(location.getLatitude(), location.getLongitude());

                } catch (Exception e) {
                    e.printStackTrace();
                    hideProgress();
                }
                return geopoint;
            }

            @Override
            protected void onPostExecute(Geopoint geopoint) {
                // Log.e(TAG, "geopoint:" + geopoint.toString());
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
            mPckNameEditText.setText(Utils.getOnlyStrings(name.trim()));
            mPckPhnoEditText.setText(Utils.getOnlyDigits(phoneNo.trim()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToSupMapAct();
    }
}