package com.supermandelivery.permissions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.supermandelivery.R;

public class AndroidPermissions {

    /**
     * Id to identify a camera permission request.
     */
    public static final int REQUEST_LOCATION = 0;

    /**
     * Id to identify a contacts permission request.
     */
    public static final int REQUEST_CONTACTS = 1;

    /**
     * Id to identify a SMS permission request.
     */
    public static final int REQUEST_SMS = 2;

    /**
     * Permissions required to read and write contacts.
     */
    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS};

    /**
     * Permissions required to get locations
     */
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    public static AndroidPermissions instance;

    public static AndroidPermissions getInstance() {
        if (instance == null) {
            instance = new AndroidPermissions();
        }
        return instance;
    }

    public boolean checkLocationPermission(Context thisActivity) {
        if (ActivityCompat.checkSelfPermission(thisActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(thisActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public void displayLocationPermissionAlert(Activity thisActivity) {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(thisActivity, PERMISSIONS_LOCATION, REQUEST_LOCATION);
    }


    public boolean checkContactPermission(Context thisActivity) {
        if (ActivityCompat.checkSelfPermission(thisActivity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public void displayContactPermission(Activity thisActivity) {
        // Should we show an explanation?
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(thisActivity, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACTS);
    }


    public boolean checkSmsPermission(Context thisActivity) {
        if (ActivityCompat.checkSelfPermission(thisActivity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public void displaySmsPermission(Activity thisActivity) {
        // Should we show an explanation?
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(thisActivity, new String[]{Manifest.permission.READ_SMS}, REQUEST_SMS);
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void displayAlert(final Activity context, final int position) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        if (position == REQUEST_LOCATION) {
            builder1.setTitle(context.getResources().getString(R.string.location));
            builder1.setMessage(context.getResources().getString(R.string.location_desc));
        } else {
            builder1.setTitle(context.getResources().getString(R.string.contacts));
            builder1.setMessage(context.getResources().getString(R.string.contacts_des));
        }
        builder1.setCancelable(true);

        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (position == REQUEST_LOCATION) {
                            displayLocationPermissionAlert(context);
                        } else if (position == REQUEST_CONTACTS) {
                            displayContactPermission(context);
                        } else {
                            displaySmsPermission(context);
                        }
                    }
                });

        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert1 = builder1.create();
        alert1.show();
    }
}
