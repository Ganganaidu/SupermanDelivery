package com.supermandelivery.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.supermandelivery.R;
import com.supermandelivery.permissions.AndroidPermissions;
import com.supermandelivery.utils.ConstantTypes;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsFragment extends Fragment {

    @Bind(R.id.contact_callID)
    TextView mContactCallID;
    @Bind(R.id.contact_facebookID)
    TextView mContactFacebookID;
    @Bind(R.id.contact_emailID)
    TextView mContactEmailID;
    @Bind(R.id.contact_whatsappID)
    TextView mContactWhatsappID;
    @Bind(R.id.contact_websiteID)
    TextView mContactWebsiteID;

    private static final int REQUEST_CALL_PHONE = 1;

    Intent callIntent;


    public ContactUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.contact_callID, R.id.contact_facebookID, R.id.contact_emailID, R.id.contact_whatsappID, R.id.contact_websiteID})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contact_callID:
                /*Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + ConstantTypes.PHONE_NUMBER));
                startActivity(callIntent);*/

                callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ConstantTypes.PHONE_NUMBER));
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                } else {
                    startActivity(callIntent);
                }

                break;
            case R.id.contact_facebookID:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.me/supermandelivers"));
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/appetizerandroid")));
                }
                break;
            case R.id.contact_emailID:

                Intent mailerIntent = new Intent(Intent.ACTION_SEND);
                mailerIntent.setType("message/rfc822");
                mailerIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ConstantTypes.EMAILADDRESS});
                mailerIntent.putExtra(Intent.EXTRA_SUBJECT, "Superman Delivers");
                mailerIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(mailerIntent, "Send email..."));

                break;
            case R.id.contact_whatsappID:

                try {
                    Intent whatsAppIntent = new Intent();
                    whatsAppIntent.setAction(Intent.ACTION_SEND);
                    whatsAppIntent.putExtra(Intent.EXTRA_TEXT, "Superman Delivers");
                    whatsAppIntent.setType("text/plain");
                    whatsAppIntent.setPackage("com.whatsapp");
                    startActivity(whatsAppIntent);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.contact_websiteID:

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                websiteIntent.setData(Uri.parse(ConstantTypes.APPWEBURL));
                startActivity(websiteIntent);

                break;
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PHONE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                }
            }
        }
    }
}
