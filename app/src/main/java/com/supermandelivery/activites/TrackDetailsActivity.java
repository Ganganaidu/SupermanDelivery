package com.supermandelivery.activites;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.supermandelivery.MyApplication;
import com.supermandelivery.R;
import com.supermandelivery.models.TripsModel;
import com.supermandelivery.utils.ConstantTypes;
import com.supermandelivery.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ADMIN on 3/22/2016.
 */
public class TrackDetailsActivity extends BaseActivity {
    private static final String TAG = "TrackDetailsActivity";

    @Bind(R.id.tracking_id_textView)
    TextView trackingIdTextView;
    @Bind(R.id.request_tick_imgBtn)
    ImageButton requestTickImgBtn;
    @Bind(R.id.requset_status_line_imageview)
    ImageView requsetStatusLineImageview;
    @Bind(R.id.confirmed_tick_imgBtn)
    ImageButton confirmedTickImgBtn;
    @Bind(R.id.confirmed_status_line_imageview)
    ImageView confirmedStatusLine_Imageview;
    @Bind(R.id.pick_tick_imgBtn)
    ImageButton pickTickImgBtn;
    @Bind(R.id.pickup_status_line_imageview)
    ImageView pickupStatusLineImageview;
    @Bind(R.id.deliver_tick_imgBtn)
    ImageButton deliverTickImgBtn;
    @Bind(R.id.deliver_status_line_imageview)
    ImageView deliverStatusLineImageview;
    @Bind(R.id.completed_tick_imgBtn)
    ImageButton completedTickImgBtn;
    @Bind(R.id.request_time_textView)
    TextView requestTimeTextView;
    @Bind(R.id.confirmed_status_textView)
    TextView confirmedStatusTextView;
    /*@Bind(R.id.delivery_status_line_imageview)
    ImageView deliveryStatusLineImageview;*/
    @Bind(R.id.confirmed_time_textView)
    TextView confirmedTimeTextView;
    @Bind(R.id.puckup_time_textView)
    TextView puckupTimeTextView;
    @Bind(R.id.deliver_time_textView)
    TextView deliverTimeTextView;
    @Bind(R.id.completed_time_textView)
    TextView completedTimeTextView;
    @Bind(R.id.track_user_name)
    TextView trackUserName;
    @Bind(R.id.track_delivered_name)
    TextView trackDeliveredName;
    @Bind(R.id.completed_status_textView)
    TextView completedStatusTextView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.deliver_type)
    TextView deliverType;
    @Bind(R.id.deliver_charges_collect)
    TextView deliverChargesCollect;
    @Bind(R.id.total_deliver_charges)
    Button totalDeliverCharges;

    TripsModel tripsModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_details);
        ButterKnife.bind(this);

        //set up tool bar
        setSupportActionBar(toolbar);
        tripsModel = MyApplication.getInstance().getTripModel();

        if (tripsModel != null) {
            setTrackDetails();
        }

        setUpToolBar();
    }

    private void setUpToolBar() {
        //set title for this activity
        toolbarTitle.setText(getResources().getString(R.string.parcel_tracking));

        //navigation close
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyApplication.getInstance().setTripsModel(null);
                        finish();
                    }
                });
        //updating status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void setTrackDetails() {

        trackingIdTextView.setText(tripsModel.getId());

        if (tripsModel.getStatus().equals(ConstantTypes.REQUESTSENT)) {

            requestTickImgBtn.setImageResource(R.drawable.tick);

            if (tripsModel.getPickupEndTime() != null
                    && tripsModel.getPickupStartTime().length() != 0) {
                requestTimeTextView.setText(Utils.convertTime(tripsModel.getPickupEndTime()));
            }

        } else if (tripsModel.getStatus().equals(ConstantTypes.requestAccepted)) {

            requsetStatusLineImageview.setBackgroundColor(getResources().getColor(R.color.darkBlue));
//            confirmedStatusLine_Imageview.setBackgroundColor(getResources().getColor(R.color.darkBlue));

            requestTickImgBtn.setImageResource(R.drawable.tick);
            confirmedTickImgBtn.setImageResource(R.drawable.tick);
//            pickTickImgBtn.setImageResource(R.drawable.tick);

            /*if (tripsModel.getPickupEndTime() != null && tripsModel.getPickupEndTime().length() != 0) {
                puckupTimeTextView.setText(Utils.convertTime(tripsModel.getPickupEndTime()));
            }*/

            /*if (tripsModel != null) {
                if (tripsModel.getPickupAddress() != null) {
                    trackUserName.setText(getResources().getString(R.string.pickup_from_name) + " FROM " +
                            tripsModel.getPickupAddress().getName());
                }
            }*/

        } else if (tripsModel.getStatus().equals(ConstantTypes.assigned)) {

            requsetStatusLineImageview.setBackgroundColor(getResources().getColor(R.color.darkBlue));
            confirmedStatusLine_Imageview.setBackgroundColor(getResources().getColor(R.color.darkBlue));

            requestTickImgBtn.setImageResource(R.drawable.tick);
            confirmedTickImgBtn.setImageResource(R.drawable.tick);
            pickTickImgBtn.setImageResource(R.drawable.tick);

            if (tripsModel.getPickupEndTime() != null && tripsModel.getPickupEndTime().length() != 0) {
                puckupTimeTextView.setText(Utils.convertTime(tripsModel.getPickupEndTime()));
            }

            if (tripsModel != null) {
                if (tripsModel.getPickupAddress() != null) {
                    String pickedUpName = tripsModel.getPickupAddress().getName().toUpperCase();
                    trackUserName.setText(getResources().getString(R.string.pickup_from_name) + " FROM " +
                            pickedUpName);
                }
            }

        } else if (tripsModel.getStatus().equals(ConstantTypes.delivered)) {

            requsetStatusLineImageview.setBackgroundColor(getResources().getColor(R.color.darkBlue));
            confirmedStatusLine_Imageview.setBackgroundColor(getResources().getColor(R.color.darkBlue));
            pickupStatusLineImageview.setBackgroundColor(getResources().getColor(R.color.darkBlue));
            deliverStatusLineImageview.setBackgroundColor(getResources().getColor(R.color.darkBlue));

            requestTickImgBtn.setImageResource(R.drawable.tick);
            confirmedTickImgBtn.setImageResource(R.drawable.tick);
            pickTickImgBtn.setImageResource(R.drawable.tick);
            deliverTickImgBtn.setImageResource(R.drawable.tick);
            completedTickImgBtn.setImageResource(R.drawable.tick);

            if (tripsModel.getPickupEndTime() != null && tripsModel.getPickupEndTime().length() != 0) {
                deliverTimeTextView.setText(Utils.convertTime(tripsModel.getPickupEndTime()));
            }

            if (tripsModel != null) {
                if (tripsModel.getPickupAddress() != null) {
                    String pickedUpName = tripsModel.getPickupAddress().getName().toUpperCase();
                    trackUserName.setText(getResources().getString(R.string.pickup_from_name) + " FROM " +
                            pickedUpName);
                }
                if (tripsModel.getDropAddress() != null) {
                    String deliveredToName = tripsModel.getDropAddress().getName().toUpperCase();
                    trackDeliveredName.setText(getResources().getString(R.string.deliver_to_set) + " TO " +
                            deliveredToName);
                }
            }
        } else if (tripsModel.getStatus().equals(ConstantTypes.cancelled)) {

            requsetStatusLineImageview.setBackgroundColor(getResources().getColor(R.color.darkBlue));

            requestTickImgBtn.setImageResource(R.drawable.tick);
            confirmedTickImgBtn.setImageResource(R.drawable.tick);
            confirmedStatusTextView.setText("CANCELLED");

            trackUserName.setVisibility(View.GONE);
            trackDeliveredName.setVisibility(View.GONE);
            completedStatusTextView.setVisibility(View.GONE);

            confirmedStatusLine_Imageview.setVisibility(View.GONE);
            pickTickImgBtn.setVisibility(View.GONE);
            pickupStatusLineImageview.setVisibility(View.GONE);
            deliverTickImgBtn.setVisibility(View.GONE);
            deliverStatusLineImageview.setVisibility(View.GONE);
            completedTickImgBtn.setVisibility(View.GONE);

        }

        String collectAt = getResources().getString(R.string.delivery_charges_collected_from) + " " + tripsModel.getCollectAt();
        deliverChargesCollect.setText(collectAt);

        String deliveryType = "";
        if (tripsModel.getType().equals(ConstantTypes.PICKUPANDDROP)) {
            deliveryType = getResources().getString(R.string.pickup_drop_button);
        } else {
            deliveryType = getResources().getString(R.string.purchase_button);
        }
        deliverType.setText(deliveryType);

        int total = (int) tripsModel.getNet();
        totalDeliverCharges.setText(getResources().getString(R.string.delivery_charge) + " RS." + total);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyApplication.getInstance().setTripsModel(null);
    }
}
