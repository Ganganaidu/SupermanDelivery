package com.supermandelivery.activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.supermandelivery.LocationService.GPSTracker;
import com.supermandelivery.MyApplication;
import com.supermandelivery.R;
import com.supermandelivery.listeners.OnDirectionCallback;
import com.supermandelivery.listeners.RequestListener;
import com.supermandelivery.models.Discount;
import com.supermandelivery.models.DropAddress;
import com.supermandelivery.models.Fare;
import com.supermandelivery.models.Geopoint;
import com.supermandelivery.models.PickupAddress;
import com.supermandelivery.models.PurchaseAddress;
import com.supermandelivery.models.SavedAddress;
import com.supermandelivery.models.TripsModel;
import com.supermandelivery.network.RestAPIRequest;
import com.supermandelivery.network.RestRequestInterface;
import com.supermandelivery.permissions.AndroidPermissions;
import com.supermandelivery.utils.AppPreferences;
import com.supermandelivery.utils.ConstantTypes;
import com.supermandelivery.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class SuperMapActivity extends BaseActivity implements
        OnMapReadyCallback, GPSTracker.UpdateLocationListener {

    private static final String TAG = "SuperMapActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.pickup_layout)
    LinearLayout mPickupLayout;
    @Bind(R.id.drop_layout)
    LinearLayout mDropLayout;
    @Bind(R.id.booknow)
    Button mBooknow;
    @Bind(R.id.map)
    MapView mMapView;
    @Bind(R.id.pickup_location_text)
    TextView pickupLocationText;
    @Bind(R.id.drop_location_text)
    TextView dropLocationText;
    @Bind(R.id.pickup_drop_button)
    Button pickupDropButton;
    @Bind(R.id.purchase_button)
    Button purchaseButton;
    @Bind(R.id.promo_button)
    TextView promoButton;
    @Bind(R.id.cost_estimate_textview)
    TextView costEstimateTextview;
    @Bind(R.id.pick_purchase_layout)
    LinearLayout mPickPurchaseLayout;
    @Bind(R.id.costestimate_purchase_layout)
    LinearLayout mCostestimatePurchaseLayout;
    @Bind(R.id.trip_progressBar)
    ProgressBar tripProgressBar;
    @Bind(R.id.drop_line_imageview)
    ImageView drop_line_imageview;
    @Bind(R.id.drop_tick_imageview)
    ImageView drop_tick_imageview;
    @Bind(R.id.cost_estimation_layout)
    LinearLayout mCostEstimationLayout;
    @Bind(R.id.current_location_layout)
    RelativeLayout current_location_layout;

    private Bundle mBundle;
    private GoogleMap mGoogleMap;
    private GPSTracker gpsTracker;
    private TripsModel mTripsModel;
    private Marker locationMarker, dropMarker;

    private boolean isMapUpdated = true;
    private boolean isPurchase = false;
    double netFare;

    public SuperMapActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        ButterKnife.bind(this);

        mBundle = savedInstanceState;
        mTripsModel = new TripsModel();

        // check if GPS enabled
        gpsTracker = new GPSTracker(this);
        gpsTracker.setLocationListener(this);

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.onCreate(mBundle);
        mMapView.getMapAsync(this);

        current_location_layout.setVisibility(View.GONE);
        setActionBarTitle();
    }

    private void setActionBarTitle() {
        setSupportActionBar(toolbar);
        //navigation close
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        backToDropAct();
                    }
                });
        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.toolbar_logo, null);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
        actionBar.setDisplayShowCustomEnabled(true);

        //updating status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (gpsTracker.checkLocationState()) {
            gpsTracker.startLocationUpdates();

            setMap(googleMap);
            updateMapWithSelectedValues();

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null && !isMapUpdated) {
            //saving lat
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            updateMapWithLocationFirstTime(latLng);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        gpsTracker.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
        updateLocationPermission();
        updateMapFromSelection();
    }

    private void updateLocationPermission() {
        if (gpsTracker != null) {
            gpsTracker.onResume();
            if (!AndroidPermissions.getInstance().checkLocationPermission(this)) {
                AndroidPermissions.getInstance().displayLocationPermissionAlert(this);
            }
            gpsTracker.startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        gpsTracker.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        gpsTracker.onStop();
    }


    //TODO
    @OnClick({R.id.pickup_layout, R.id.drop_layout, R.id.promo_button, R.id.booknow,
            R.id.current_location_layout, R.id.pickup_drop_button, R.id.cost_estimation_layout, R.id.purchase_button})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.pickup_layout:

                Intent pickupIntent = new Intent(this, LocationSearchActivity.class);
                pickupIntent.putExtra("mTypeSelected", 1);
                startActivity(pickupIntent);

                break;
            case R.id.drop_layout:

                Intent dropIntent = new Intent(this, LocationSearchActivity.class);
                dropIntent.putExtra("mTypeSelected", 2);
                startActivity(dropIntent);

                break;
            case R.id.promo_button:
                showPromoDialog();

                break;
            case R.id.pickup_drop_button:

                mTripsModel.setType(ConstantTypes.PICKUPANDDROP);
                pickupDropButton.setTextColor(Color.WHITE);
                pickupDropButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner));
                purchaseButton.setTextColor(getResources().getColor(R.color.darkBlue));
                purchaseButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_white));
                isPurchase = false;

                break;
            case R.id.purchase_button:

                mTripsModel.setType(ConstantTypes.PURCHASE);
                purchaseButton.setTextColor(Color.WHITE);
                purchaseButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner));
                pickupDropButton.setTextColor(getResources().getColor(R.color.darkBlue));
                pickupDropButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corner_white));
                isPurchase = true;

                break;
            case R.id.booknow: //continue from pick up map fragment

                if (MyApplication.getInstance().getLatLngArrayList().size() > 1) {
                    MyApplication.getInstance().setGeoPoint(null);//clear previous values

                    Intent descriptionIntent = new Intent(this, PickUpDescriptionActivity.class);
                    descriptionIntent.putExtra("isPurchase", isPurchase);
                    descriptionIntent.putExtra("latLngArrayList", MyApplication.getInstance().getLatLngArrayList());
                    startActivity(descriptionIntent);
                    finish();

                } else {
                    Toast.makeText(this, getResources().getString(R.string.select_location), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.current_location_layout:
                // updateMapWithLocationFirstTime(getLatLng());
                break;

            case R.id.cost_estimation_layout:
                costEstimation();
                break;
        }
    }

    private void updateMapWithSelectedValues() {
        //update pickup selection
        MyApplication.getInstance().setMapSelectionType(0);//reset

        updateMapSelection(0);//update maps here
        updateMapSelection(1);//update maps here

        //update drop selection
        mBooknow.setText(getResources().getString(R.string.continue_button));
        mCostestimatePurchaseLayout.setVisibility(View.VISIBLE);
        mPickPurchaseLayout.setVisibility(View.VISIBLE);

        loadEstimateCost();
    }

    private void updateMapFromSelection() {
        // Log.e(TAG, "lat:" + MyApplication.getInstance().getGeoPoint());
        //when user select pick/drop location ...we will update location on the map here
        if (MyApplication.getInstance().getGeoPoint() != null) {
            if (MyApplication.getInstance().getMapSelectionType() == 1) { //picup location
                MyApplication.getInstance().setMapSelectionType(0);//reset

                MyApplication.getInstance().getLatLngArrayList().add(0, MyApplication.getInstance().getGeoPoint());
                MyApplication.getInstance().getSavedAddressArrayList().add(0, MyApplication.getInstance().getAddress());
                updateMapSelection(0);//update maps here

            } else if (MyApplication.getInstance().getMapSelectionType() == 2) {//drop location
                MyApplication.getInstance().setMapSelectionType(0);//reset

                mBooknow.setText(getResources().getString(R.string.continue_button));
                mCostestimatePurchaseLayout.setVisibility(View.VISIBLE);
                mPickPurchaseLayout.setVisibility(View.VISIBLE);
                drop_line_imageview.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                drop_tick_imageview.setImageResource(R.drawable.red_tick);

                MyApplication.getInstance().getLatLngArrayList().add(1, MyApplication.getInstance().getGeoPoint());
                MyApplication.getInstance().getSavedAddressArrayList().add(1, MyApplication.getInstance().getAddress());
                updateMapSelection(1);//update maps here

                loadEstimateCost();
            }
        }
    }

    private void setMap(GoogleMap mGoogleMap) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();

            /*Map configurations*/
            //mGoogleMap.setMyLocationEnabled(true);
            // mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
            mGoogleMap.getUiSettings().setCompassEnabled(false);
            mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
            mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        }
    }

    //first time when fragment opened we will call this and update map with current location and marker
    private void updateMapWithLocationFirstTime(LatLng latLang) {
        if (mGoogleMap != null) {
            MyApplication.getInstance().getLatLngArrayList().add(0, latLang);//default pick up location
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLang, 15);
            mGoogleMap.animateCamera(cameraUpdate);

            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.green_flag);
            updatePickUPMarker(latLang, icon);
            updatePickUpAddress(pickupLocationText);
            pickupLocationText.setTextColor(Color.BLACK);
        }
    }


    private void updatePickUPMarker(LatLng latLng, BitmapDescriptor icon) {
        if (locationMarker != null) {
            locationMarker.remove();
        }
        //default pickup location marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(icon)
                .draggable(true);
        locationMarker = mGoogleMap.addMarker(markerOptions);
        locationMarker.setDraggable(true);
    }

    //using to update address based on current location
    private void updatePickUpAddress(final TextView textView) {
        gpsTracker.reverseGeocode(this, new GPSTracker.OnAddressChangeListener() {
            @Override
            public void updateAddress() {
                SavedAddress currentAddress = new SavedAddress();
                currentAddress.setAddress(gpsTracker.getAddressLine(SuperMapActivity.this));

                MyApplication.getInstance().getSavedAddressArrayList().add(0, currentAddress);
                textView.setText(gpsTracker.getAddressLine(SuperMapActivity.this));
            }
        });
    }

    //update maps based on user selection(pic up and drop location)
    private void updateMapSelection(int position) {
        LatLng latLng;
        if (mGoogleMap != null) {
            BitmapDescriptor icon;
            if (position == 0) { //pick up details
                latLng = MyApplication.getInstance().getLatLngArrayList().get(0);//pick up location

                icon = BitmapDescriptorFactory.fromResource(R.drawable.green_flag);
                pickupLocationText.setText(MyApplication.getInstance().getSavedAddressArrayList().get(0).getAddress());//pick up location text
                pickupLocationText.setTextColor(Color.BLACK);
                updatePickUPMarker(latLng, icon);
            } else { //drop location details
                latLng = MyApplication.getInstance().getLatLngArrayList().get(1);//drop up location
                icon = BitmapDescriptorFactory.fromResource(R.drawable.red_flag);
                dropLocationText.setText(MyApplication.getInstance().getSavedAddressArrayList().get(1).getAddress());//drop location text
                dropLocationText.setTextColor(Color.BLACK);
                if (dropMarker != null) {
                    dropMarker.remove();
                }
                //default pickup location marker options
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .icon(icon)
                        .draggable(true);
                dropMarker = mGoogleMap.addMarker(markerOptions);
                dropMarker.setDraggable(true);
            }

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
            mGoogleMap.animateCamera(cameraUpdate);
        }
    }


    private int totalDistance;

    private void loadEstimateCost() {
        tripProgressBar.setVisibility(View.VISIBLE);
        MyApplication.getInstance().showProgress(this, "Estimating price please wait...");

        RestAPIRequest.getInstance().getDirectionGoogleAPI(getResources().getString(R.string.google_api_server_key),
                MyApplication.getInstance().getLatLngArrayList().get(0), MyApplication.getInstance().getLatLngArrayList().get(1), new OnDirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        MyApplication.getInstance().hideProgress();
                        String status = direction.getStatus();
                        if (status.equals(RequestResult.OK)) {

                            Fare fare = MyApplication.getInstance().getFare();

                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            //removing space and
                            // String distance = leg.getDistance().getText().replace(" ", "").replaceAll("\\D+", "");
                            // String duration = leg.getDuration().getText().replace(" ", "").replaceAll("\\D+", "");

                            double distance = Double.parseDouble(leg.getDistance().getValue().trim()); // KM
                            double duration = Double.parseDouble(leg.getDuration().getValue().trim()); //min

                            distance = Math.round(Utils.calculateKilometers(distance));
                            duration = Math.round(Utils.secondsToMin(duration));
                            totalDistance = (int) distance;

                            if (fare != null) {
                                double totalKm = (distance - fare.getMinDistance());

                                double distanceFare = totalKm * fare.getPerKmCost();
                                double fareCalculation = fare.getMinFare() + distanceFare;//(total fare)
//                                double waitingTimeFare = duration * fare.getWaitingCharge();
                                double waitingTimeFare = 0.0;

                                double grossFare = fareCalculation + waitingTimeFare;
                                netFare = grossFare - 0;
                                //if fare is less than zero ...put normal min fare
                                if (netFare < 50) {
                                    netFare = fare.getMinFare();
                                }

                                mTripsModel.setNet(netFare);
                                mTripsModel.setDistance(distance);
                                mTripsModel.setMinFare(fare.getMinFare());
                                mTripsModel.setWaitingCharge(waitingTimeFare);
                                mTripsModel.setPerKmCost(fare.getPerKmCost());//total fare
                                mTripsModel.setTotalFare(fareCalculation);//total fare
                                mTripsModel.setStartTime(Utils.getCurrentDateTime());
                                mTripsModel.setEndTime(String.valueOf(duration));

                                //Log.d(TAG, "distance:" + distance);
                                //Log.d(TAG, "duration:" + duration);

                                setTripsModel();
                                tripProgressBar.setVisibility(View.GONE);
                                costEstimateTextview.setText(String.valueOf("RS " + netFare));
                            }
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        MyApplication.getInstance().hideProgress();
                        tripProgressBar.setVisibility(View.GONE);
                        Log.e(TAG, "error:" + t.getMessage());
                    }
                }
        );
        tripProgressBar.setVisibility(View.GONE);
    }

    //set all trip details here
    private void setTripsModel() {
        PickupAddress pickupAddress = new PickupAddress();

        pickupAddress.setAddress(MyApplication.getInstance().getSavedAddressArrayList().get(0).getAddress());
        pickupAddress.setGeopoint(new Geopoint(MyApplication.getInstance().getLatLngArrayList().get(0).latitude,
                MyApplication.getInstance().getLatLngArrayList().get(0).longitude));
        pickupAddress.setContactNo(MyApplication.getInstance().getSavedAddressArrayList().get(0).getContactNo());
        pickupAddress.setName(MyApplication.getInstance().getSavedAddressArrayList().get(0).getName());
        pickupAddress.setFlatNo(MyApplication.getInstance().getSavedAddressArrayList().get(0).getFlatNo());
        //pickupAddress.setAddress(leg.getStartAddress());
        //pickupAddress.setGeopoint(new Geopoint(leg.getStartLocation().getLatitude(), leg.getStartLocation().getLongitude()));

        PurchaseAddress purchaseAddress = new PurchaseAddress();
        purchaseAddress.setAddress(MyApplication.getInstance().getSavedAddressArrayList().get(0).getAddress());
        purchaseAddress.setGeopoint(new Geopoint(MyApplication.getInstance().getLatLngArrayList().get(0).latitude,
                MyApplication.getInstance().getLatLngArrayList().get(0).longitude));
        purchaseAddress.setContactNo(MyApplication.getInstance().getSavedAddressArrayList().get(0).getContactNo());
        purchaseAddress.setName(MyApplication.getInstance().getSavedAddressArrayList().get(0).getName());
        purchaseAddress.setFlatNo(MyApplication.getInstance().getSavedAddressArrayList().get(0).getFlatNo());
        //purchaseAddress.setGeopoint(new Geopoint(leg.getStartLocation().getLatitude(), leg.getStartLocation().getLongitude()));
        // purchaseAddress.setAddress(leg.getStartAddress());

        DropAddress dropAddress = new DropAddress();
        dropAddress.setAddress(MyApplication.getInstance().getSavedAddressArrayList().get(1).getAddress());
        purchaseAddress.setGeopoint(new Geopoint(MyApplication.getInstance().getLatLngArrayList().get(1).latitude,
                MyApplication.getInstance().getLatLngArrayList().get(1).longitude));
        dropAddress.setContactNo(MyApplication.getInstance().getSavedAddressArrayList().get(1).getContactNo());
        dropAddress.setName(MyApplication.getInstance().getSavedAddressArrayList().get(1).getName());
        dropAddress.setFlatNo(MyApplication.getInstance().getSavedAddressArrayList().get(1).getFlatNo());
        //dropAddress.setGeopoint(new Geopoint(leg.getEndLocation().getLatitude(), leg.getEndLocation().getLongitude()));
        // dropAddress.setAddress(leg.getEndAddress());
        // dropAddress.setGeopoint(new Geopoint(leg.getEndLocation().getLatitude(), leg.getEndLocation().getLongitude()));

        mTripsModel.setPickupAddress(pickupAddress);
        mTripsModel.setPurchaseAddress(purchaseAddress);
        mTripsModel.setDropAddress(dropAddress);
        mTripsModel.setRating(0);
        mTripsModel.setUserId(AppPreferences.getInstance().getUserId());
        mTripsModel.setPickupTime(Utils.getCurrentDateTime());
        mTripsModel.setRemarks("");
        mTripsModel.setCouponId("");
        mTripsModel.setDiscount(new Discount());

        if (mTripsModel.getType() == null || mTripsModel.getType().length() == 0) {
            mTripsModel.setType(ConstantTypes.PICKUPANDDROP);
        }

        MyApplication.getInstance().setTripsModel(mTripsModel);
    }

    //display promo code dialog
    private void showPromoDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.promo_dialog);
        dialog.setCancelable(false);

        dialog.show();
        Button mPromoDoneButton = (Button) dialog.findViewById(R.id.promo_dialog_done);
        Button mPromoCancelButton = (Button) dialog.findViewById(R.id.promo_dialog_cancel);
        final EditText promo_code_editText = (EditText) dialog.findViewById(R.id.promo_code_editText);

        mPromoDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String promoCode = promo_code_editText.getText().toString().trim().toUpperCase();

                if (promoCode.length() != 0) { //sending promo code details to server
                    RestRequestInterface myLogin = RestAPIRequest.getRetrofit().create(RestRequestInterface.class);
                    Call<Discount> call = myLogin.validateCoupon(AppPreferences.getInstance().getAccessToken(), promoCode,
                            String.valueOf(mTripsModel.getNet()), AppPreferences.getInstance().getUserId());
                    RestAPIRequest.getInstance().doRequest(call, new RequestListener<Discount>() {
                        @Override
                        public void onResponse(Discount response) {
                            hideKeyBoard(promo_code_editText);
                            doCouponCalculation(response);
                            dialog.dismiss();
                        }

                        @Override
                        public void onDisplayError(String errorMsg) {
                            hideKeyBoard(promo_code_editText);
                            Toast.makeText(SuperMapActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    promo_code_editText.setError("Enter promo code");
                }
            }
        });
        mPromoCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard(promo_code_editText);
                dialog.dismiss();
            }
        });
    }


    //display cost estimation dialog
    private void costEstimation() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cost_estimation_dialog);
        dialog.show();

        TextView total_distance_km = (TextView) dialog.findViewById(R.id.total_distance_km);
        TextView textView = (TextView) dialog.findViewById(R.id.cost_estimate_distance);

        total_distance_km.setText(String.valueOf(totalDistance + " KM"));
        if (mTripsModel != null) {
            textView.setText(String.valueOf("RS " + mTripsModel.getMinFare() + " PER KM"));
        }
        Button cost_estimation_done = (Button) dialog.findViewById(R.id.cost_estimation_done);
        cost_estimation_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //calculating coupon with net fare
    private void doCouponCalculation(Discount response) {
        if (response != null && response.isSuccess()) {

            mTripsModel.setDiscount(response);

            mTripsModel.setCouponId(response.getCouponId());

            if (response.getType().equals("flat_rate")) {
                AppPreferences.getInstance().saveFlatRateState(true);
                costEstimateTextview.setText(String.valueOf("RS " + response.getAmount()));
            } else {
                mTripsModel.setDiscount(response);

                mTripsModel.setCouponId(response.getCouponId());
                double netFare = mTripsModel.getNet() - response.getAmount();
                if (netFare < 50) {
                    netFare = MyApplication.getInstance().getFare().getMinFare();
                }
                costEstimateTextview.setText(String.valueOf("RS " + netFare));
                MyApplication.getInstance().setTripsModel(mTripsModel);
            }

            /*double netFare = mTripsModel.getNet() - response.getAmount();
            if (netFare < 50) {
                netFare = MyApplication.getInstance().getFare().getMinFare();
            }
            costEstimateTextview.setText(String.valueOf("RS " + netFare));
            MyApplication.getInstance().setTripsModel(mTripsModel);*/

        } else {
            Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void hideKeyBoard(EditText myEditText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToDropAct();
    }

    private void backToDropAct() {
        Intent in = new Intent(this, DropMapActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AndroidPermissions.REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocationPermission();
                } else {
                    AndroidPermissions.getInstance().displayAlert(this, AndroidPermissions.REQUEST_LOCATION);
                }
                break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}
