package com.supermandelivery.activites;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.supermandelivery.BuildConfig;
import com.supermandelivery.LocationService.GPSTracker;
import com.supermandelivery.MyApplication;
import com.supermandelivery.R;
import com.supermandelivery.models.SavedAddress;
import com.supermandelivery.permissions.AndroidPermissions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DropMapActivity extends AppCompatActivity implements
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
    @Bind(R.id.drop_line_imageview)
    ImageView drop_line_imageview;
    @Bind(R.id.drop_tick_imageview)
    ImageView drop_tick_imageview;

    private Bundle mBundle;
    private GoogleMap mGoogleMap;
    private GPSTracker gpsTracker;
    private Marker dropMarker;

    private boolean isMapUpdated = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        ButterKnife.bind(this);

        mBundle = savedInstanceState;
        setActionBarTitle();

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

        //update drop selection
        mBooknow.setText(getResources().getString(R.string.continue_button));
        updateMapWithSelectedValues();
    }

    private void setActionBarTitle() {
        setSupportActionBar(toolbar);
        //navigation close
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
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
            LatLng latLang = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            //First time if you don't have latitude and longitude user default address
            if (gpsTracker.getLatitude() == 0) {
                latLang = new LatLng(17.3700, 78.4800);
                gpsTracker.setDefaultAddress("Hyderabad, Telangana");
            }
            updateMapWithLocationFirstTime(latLang);

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
        updateLocation();
        updateMapFromSelection();
    }

    private void updateLocation() {
        if (!AndroidPermissions.getInstance().checkLocationPermission(this)) {
            AndroidPermissions.getInstance().displayLocationPermissionAlert(this);
        }
        if (gpsTracker != null) {
            gpsTracker.onResume();
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
    @OnClick({R.id.pickup_layout, R.id.drop_layout, R.id.promo_button, R.id.booknow, R.id.current_location_layout})
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
            case R.id.booknow:

                Log.e(TAG, "first:" + MyApplication.getInstance().getSavedAddressArrayList().get(0));
                Log.e(TAG, "second:" + MyApplication.getInstance().getSavedAddressArrayList().get(1));

                if (MyApplication.getInstance().getLatLngArrayList().size() > 1) {
                    MyApplication.getInstance().setMapSelectionType(0);//reset

                    Intent promoDescriptionIntent = new Intent(this, SuperMapActivity.class);
                    startActivity(promoDescriptionIntent);
                    finish();

                } else {
                    Toast.makeText(this, getResources().getString(R.string.select_location), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.current_location_layout:
                updateMapWithLocationFirstTime(getLatLng());
                break;
        }
    }

    private void updateMapWithSelectedValues() {
        //update pickup selection
        MyApplication.getInstance().setMapSelectionType(0);//reset

        //Log.e(TAG, "address:" + MyApplication.getInstance().getSavedAddressArrayList().get(0).getAddress());
        pickupLocationText.setText(MyApplication.getInstance().getSavedAddressArrayList().get(0).getAddress());//pick up location text
        pickupLocationText.setTextColor(Color.BLACK);
    }

    private void updateMapFromSelection() {
        // Log.e(TAG, "lat:" + MyApplication.getInstance().getGeoPoint());

        //when user select pick/drop location ...we will update location on the map here
        if (MyApplication.getInstance().getGeoPoint() != null) {
            if (MyApplication.getInstance().getMapSelectionType() == 1) { //picup location
                MyApplication.getInstance().setMapSelectionType(0);//reset

                MyApplication.getInstance().getLatLngArrayList().add(0, MyApplication.getInstance().getGeoPoint());
                MyApplication.getInstance().getSavedAddressArrayList().add(0, MyApplication.getInstance().getAddress());
                updateMapPinSelection(0);//update maps here

            } else if (MyApplication.getInstance().getMapSelectionType() == 2) {//drop location
                MyApplication.getInstance().setMapSelectionType(0);//reset

                mBooknow.setText(getResources().getString(R.string.continue_button));

                drop_line_imageview.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                drop_tick_imageview.setImageResource(R.drawable.red_tick);

                MyApplication.getInstance().getLatLngArrayList().add(1, MyApplication.getInstance().getGeoPoint());
                MyApplication.getInstance().getSavedAddressArrayList().add(1, MyApplication.getInstance().getAddress());
                updateMapPinSelection(1);//update maps here
            }
        }
    }


    private void setMap(GoogleMap mGoogleMap) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();

            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
            mGoogleMap.getUiSettings().setCompassEnabled(false);
            mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
            mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        }
    }

    //first time when fragment opened we will call this and update map with current location and marker
    private void updateMapWithLocationFirstTime(LatLng latLang) {
        if (gpsTracker.getLatitude() != 0) {
            isMapUpdated = true;
            if (mGoogleMap != null) {
                MyApplication.getInstance().getLatLngArrayList().add(1, latLang);//default pick up location
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLang, 15);
                mGoogleMap.animateCamera(cameraUpdate);

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.red_flag);
                updateDropUPMarker(latLang, icon);
                updateDropAddress(dropLocationText);

                pickupLocationText.setText(MyApplication.getInstance().getSavedAddressArrayList().get(0).getAddress());
                dropLocationText.setTextColor(Color.BLACK);
                dropLocationText.setTextColor(Color.BLACK);
            }
        } else {
            isMapUpdated = false;
        }
    }


    private void updateDropUPMarker(LatLng latLng, BitmapDescriptor icon) {
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

        dragMap();
    }

    //using to update address based on current location
    private void updateDropAddress(final TextView textView) {
        gpsTracker.reverseGeocode(this, new GPSTracker.OnAddressChangeListener() {
            @Override
            public void updateAddress() {
                SavedAddress currentAddress = new SavedAddress();
                currentAddress.setAddress(gpsTracker.getAddressLine(DropMapActivity.this));

                MyApplication.getInstance().getSavedAddressArrayList().add(1, currentAddress);
                textView.setText(gpsTracker.getAddressLine(DropMapActivity.this));
            }
        });
    }

    //update maps based on user selection(pic up and drop location)
    private void updateMapPinSelection(int position) {
        LatLng latLng;
        if (mGoogleMap != null) {
            BitmapDescriptor icon;
            if (position == 0) { //pick up details
                latLng = MyApplication.getInstance().getLatLngArrayList().get(0);//pick up location
                //icon = BitmapDescriptorFactory.fromResource(R.drawable.green_flag);
                pickupLocationText.setText(MyApplication.getInstance().getSavedAddressArrayList().get(0).getAddress());//pick up location text
                pickupLocationText.setTextColor(Color.BLACK);
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
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
            mGoogleMap.animateCamera(cameraUpdate);
        }
    }

    private void dragMap() {
        mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker markerDragStart) {
                //Log.i("Marker drag", "start");
            }

            @Override
            public void onMarkerDragEnd(Marker markerDragEnd) {
                if (BuildConfig.DEBUG)
                    Log.i("Marker drag", "start: getId" + markerDragEnd.getId());
                LatLng latLng;
                if (markerDragEnd.getId().equals(dropMarker.getId())) {
                    latLng = markerDragEnd.getPosition();
                    convertLatLngToAddress(1, dropLocationText, latLng);
                    dropLocationText.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onMarkerDrag(Marker markerDrag) {
                //  Log.i("Marker drag", "start");
            }
        });
    }

    //converting lat and lng to address
    private void convertLatLngToAddress(final int position, final TextView textView, LatLng latLng) {
        MyApplication.getInstance().getLatLngArrayList().add(position, latLng);
        gpsTracker.reverseGeocodeUsingLatlng(latLng, this, new GPSTracker.OnAddressChangeListener() {
            @Override
            public void updateAddress() {
                SavedAddress currentAddress = new SavedAddress();
                currentAddress.setAddress(gpsTracker.getAddressLine(DropMapActivity.this));

                MyApplication.getInstance().getSavedAddressArrayList().add(position, currentAddress);
                textView.setText(gpsTracker.getAddressLine(DropMapActivity.this));
            }
        });
    }

    private LatLng getLatLng() {
        return new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyApplication.getInstance().resetData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AndroidPermissions.REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocation();
                    isMapUpdated = false;
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
