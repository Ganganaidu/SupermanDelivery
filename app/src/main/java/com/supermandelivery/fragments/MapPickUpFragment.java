package com.supermandelivery.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.supermandelivery.activites.LocationSearchActivity;
import com.supermandelivery.models.SavedAddress;
import com.supermandelivery.permissions.AndroidPermissions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapPickUpFragment extends Fragment implements
        OnMapReadyCallback, GPSTracker.UpdateLocationListener {

    private static final String TAG = "MapPickUpFragment";

    @Bind(R.id.pickup_layout)
    LinearLayout mPickupLayout;
    @Bind(R.id.booknow)
    Button mBooknow;
    @Bind(R.id.map)
    MapView mMapView;
    @Bind(R.id.pickup_location_text)
    TextView pickupLocationText;
    @Bind(R.id.pickup_line_imageview)
    ImageView pickupLineImageView;
    @Bind(R.id.drop_layout)
    LinearLayout dropLayout;

    private Bundle mBundle;
    private GoogleMap mGoogleMap;
    private GPSTracker gpsTracker;
    private Marker locationMarker;

    private boolean isMapUpdated = true;

    public MapPickUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;

        // check if GPS enabled
        gpsTracker = new GPSTracker(getActivity());
        gpsTracker.setLocationListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, v);

        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        dropLayout.setVisibility(View.GONE);
        pickupLineImageView.setVisibility(View.INVISIBLE);

        mMapView.onCreate(mBundle);
        mMapView.getMapAsync(this);

        return v;
    }


    OnMapSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnMapSelectedListener {
        void onMapSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMapSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
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

        updateLocationPermission();

        //Log.e(TAG, "lat:" + MyApplication.getInstance().getGeoPoint());
        //when user select pick/drop location ...we will update location on the map here
        if (MyApplication.getInstance().getGeoPoint() != null) {
            MyApplication.getInstance().setMapSelectionType(0);//reset
            // Log.e(TAG, "onResume: " + MyApplication.getInstance().getAddress());

            MyApplication.getInstance().getLatLngArrayList().add(0, MyApplication.getInstance().getGeoPoint());
            MyApplication.getInstance().getSavedAddressArrayList().add(0, MyApplication.getInstance().getAddress());

            updateMapSelection();//update maps here
        }
    }

    private void updateLocationPermission() {
        if (gpsTracker != null) {
            gpsTracker.onResume();
            if (!AndroidPermissions.getInstance().checkLocationPermission(getActivity())) {
                AndroidPermissions.getInstance().displayLocationPermissionAlert(getActivity());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //TODO
    @OnClick({R.id.pickup_layout, R.id.booknow, R.id.current_location_layout})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.pickup_layout:

                Intent pickupIntent = new Intent(getActivity(), LocationSearchActivity.class);
                pickupIntent.putExtra("mTypeSelected", 1);
                startActivity(pickupIntent);

                break;
            case R.id.booknow:

                if (MyApplication.getInstance().getLatLngArrayList().size() >= 1) {
                    gpsTracker.onStop();
                    mCallback.onMapSelected(0);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.select_location), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.current_location_layout:
                updateMapWithLocationFirstTime(getLatLng());
                break;
        }
    }

    private LatLng getLatLng() {
        return new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
    }

    private void setMap(GoogleMap mGoogleMap) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();

            /*Map configurations*/
            // mGoogleMap.setMyLocationEnabled(true);
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
        if (gpsTracker.getLatitude() != 0) {
            isMapUpdated = true;
            if (mGoogleMap != null) {
                MyApplication.getInstance().getLatLngArrayList().add(0, latLang);//default pick up location
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLang, 15);
                mGoogleMap.animateCamera(cameraUpdate);

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.green_flag);
                updatePickUPMarker(latLang, icon);
                updatePickUpAddress(pickupLocationText);
                pickupLocationText.setTextColor(Color.BLACK);
            }
        } else {
            isMapUpdated = false;
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

        dragMap();
    }

    //using to update address based on current location
    private void updatePickUpAddress(final TextView textView) {
        gpsTracker.reverseGeocode(getActivity(), new GPSTracker.OnAddressChangeListener() {
            @Override
            public void updateAddress() {
                SavedAddress currentAddress = new SavedAddress();
                currentAddress.setAddress(gpsTracker.getAddressLine(getActivity()));

                MyApplication.getInstance().getSavedAddressArrayList().add(0, currentAddress);
                textView.setText(gpsTracker.getAddressLine(getActivity()));
            }
        });
    }

    //update maps based on user selection(pic up and drop location)
    private void updateMapSelection() {
        LatLng latLng;
        if (mGoogleMap != null) {
            BitmapDescriptor icon;

            latLng = MyApplication.getInstance().getLatLngArrayList().get(0);//pick up location
            icon = BitmapDescriptorFactory.fromResource(R.drawable.green_flag);
            pickupLocationText.setText(MyApplication.getInstance().getSavedAddressArrayList().get(0).getAddress());//pick up location text
            pickupLocationText.setTextColor(Color.BLACK);
            updatePickUPMarker(latLng, icon);

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
                if (markerDragEnd.getId().equals(locationMarker.getId())) {
                    latLng = markerDragEnd.getPosition();
                    convertLatLngToAddress(0, pickupLocationText, latLng);
                    pickupLocationText.setTextColor(Color.BLACK);
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
        gpsTracker.reverseGeocodeUsingLatlng(latLng, getActivity(), new GPSTracker.OnAddressChangeListener() {
            @Override
            public void updateAddress() {
                SavedAddress currentAddress = new SavedAddress();
                currentAddress.setAddress(gpsTracker.getAddressLine(getActivity()));

                MyApplication.getInstance().getSavedAddressArrayList().add(position, currentAddress);
                textView.setText(gpsTracker.getAddressLine(getActivity()));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AndroidPermissions.REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocationPermission();
                    isMapUpdated = false;
                } else {
                    AndroidPermissions.getInstance().displayAlert(getActivity(), AndroidPermissions.REQUEST_LOCATION);
                }
                break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}
