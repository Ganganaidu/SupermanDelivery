package com.supermandelivery;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.supermandelivery.models.Fare;
import com.supermandelivery.models.SavedAddress;
import com.supermandelivery.models.TripsModel;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    //private static final String TAG = "MyApplication";
    public static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication instance = null;

    public static synchronized MyApplication getInstance() {
        return instance;
    }

    private LatLng geoPoint;
    private int mapSelectionType;
    private int pickItemPosition = -1;
    private int isOrderConfirmed = 0;
    private SavedAddress selectedAddress;
    private TripsModel mTripsModel;
    private Fare fare;

    private List<TripsModel> tripsModelList;
    private ArrayList<LatLng> latLngArrayList;
    private ArrayList<SavedAddress> savedAddressArrayList;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    private static ProgressDialog pd;

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void showProgress(Context mContext, String message) {
        pd = new ProgressDialog(mContext);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    public void hideProgress() {
        if (pd != null) {
            pd.dismiss();
        }
    }

    //alert for leave album
    public void showOkAlert(String msg) {

        //final exit of application
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);

        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static boolean isAndroidM() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // only for gingerbread and newer versions
            return true;
        } else {
            return false;
        }
    }

    public void setGeoPoint(LatLng geoPoint) {
        this.geoPoint = geoPoint;
    }

    public LatLng getGeoPoint() {
        return geoPoint;
    }

    public void setMapSelectionType(int type) {
        mapSelectionType = type;
    }

    public int getMapSelectionType() {
        return mapSelectionType;
    }

    /**
     * save address when you search from
     **/
    public void setSelectedAddress(SavedAddress selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public SavedAddress getAddress() {
        return selectedAddress;
    }

    /**
     * we will use this method to save trip details
     * this is the main method where we will save all the trip details like address, fare , cupon etc.
     * and we will use this method in SupmanFragment
     **/
    public void setTripsModel(TripsModel tripsModel) {
        this.mTripsModel = tripsModel;
    }

    /**
     * we will get this in @ConfirmationActivity
     */
    public TripsModel getTripModel() {
        return mTripsModel;
    }

    /**
     * saving fare chart after calculating fare details
     * we will use this in SupManFragment
     **/
    public void saveFareChart(Fare fare) {
        this.fare = fare;
    }

    public Fare getFare() {
        return fare;
    }

    public int getPickItemPosition() {
        return pickItemPosition;
    }

    /**
     * We need to save pick item position
     * which we will use later for displaying same item
     **/
    public void setPickItemPosition(int pickItemPosition) {
        this.pickItemPosition = pickItemPosition;
    }

    public int getIsOrderConfirmed() {
        return isOrderConfirmed;
    }

    /**
     * after getting order success ..
     * we need to clear all local variables and other saved objects
     **/
    public void setIsOrderConfirmed(int isOrderConfirmed) {
        this.isOrderConfirmed = isOrderConfirmed;
    }

    public List<TripsModel> getTripsModelList() {
        return tripsModelList;
    }

    /**
     * save trip model to display this different trip fragment
     *
     * @list trip model list
     **/
    public void setTripsModelList(List<TripsModel> list) {
        tripsModelList = list;
    }

    public ArrayList<LatLng> getLatLngArrayList() {
        return latLngArrayList;
    }

    public void setLatLngArrayList(ArrayList<LatLng> latLngArrayList) {
        this.latLngArrayList = latLngArrayList;
    }

    public ArrayList<SavedAddress> getSavedAddressArrayList() {
        return savedAddressArrayList;
    }

    public void setSavedAddressArrayList(ArrayList<SavedAddress> savedAddressArrayList) {
        this.savedAddressArrayList = savedAddressArrayList;
    }

    public void resetData() {
        setGeoPoint(null);
        setSelectedAddress(null);
        setTripsModel(null);
        setMapSelectionType(0);
        saveFareChart(null);
    }
}
