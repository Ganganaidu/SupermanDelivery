package com.supermandelivery.activites;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.supermandelivery.MyApplication;
import com.supermandelivery.R;
import com.supermandelivery.listeners.RequestListener;
import com.supermandelivery.models.TripsModel;
import com.supermandelivery.models.TripsResponseModel;
import com.supermandelivery.network.RestAPIRequest;
import com.supermandelivery.network.RestRequestInterface;
import com.supermandelivery.utils.AppPreferences;
import com.supermandelivery.utils.ConstantTypes;
import com.supermandelivery.utils.Utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class ConfirmationActivity extends BaseActivity {
    private static final String TAG = "ConfirmationActivity";

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.pickup_name_textView)
    TextView pickupNameTextView;
    @Bind(R.id.pickup_num_textView)
    TextView pickupNumTextView;
    @Bind(R.id.pickup_address_textView)
    TextView pickupAddressTextView;
    @Bind(R.id.pay_pickup_textView)
    TextView payPickUpTextView;
    @Bind(R.id.pay_drop_textView)
    TextView payDropTextView;
    @Bind(R.id.delivery_name_textView)
    TextView deliveryNameTextView;
    @Bind(R.id.deliver_num_textview)
    TextView deliverNumTextview;
    @Bind(R.id.delivery_address_textView)
    TextView deliveryAddressTextView;
    @Bind(R.id.delvry_time_textView)
    TextView delvryTimeTextView;
    @Bind(R.id.current_date_textView)
    Button currentDateTextView;
    @Bind(R.id.current_time_textView)
    Button currentTimeTextView;
    @Bind(R.id.collect_delivery_textView)
    TextView collectDeliveryTextView;
    @Bind(R.id.pickup_imgBtn)
    RadioButton pickupImgBtn;
    @Bind(R.id.delivery_imgbtn)
    RadioButton deliveryImgbtn;
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;
    @Bind(R.id.collect_delivery_layout)
    LinearLayout collectDeliveryLayout;
    @Bind(R.id.deliver_charges_textview)
    TextView deliverChargesTextview;
    @Bind(R.id.delivery_charges_layout)
    LinearLayout deliveryChargesLayout;
    @Bind(R.id.confirmation_continue_btn)
    Button confirmationContinueBtn;

    private TripsModel mTripsModel;

    private static final int DATE_ID = 0;
    private static final int TIME_ID = 1;

    private int year_x, month_x, date_x;
    private int hours_x, minutes_x;

    String selectedDate = "";
    String selectedTime = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setUpToolBar();

        mTripsModel = MyApplication.getInstance().getTripModel();

        if (mTripsModel.getType().equals(ConstantTypes.PICKUPANDDROP)) {
           /*Set pickup details from pickupAddress model class*/
            pickupNameTextView.setText(mTripsModel.getPickupAddress().getName());
            pickupNumTextView.setText(mTripsModel.getPickupAddress().getContactNo());
            pickupAddressTextView.setText(mTripsModel.getPickupAddress().getAddress());
        } else {
            mTripsModel.setCollectAt("PURCHASE");
            pickupNameTextView.setText(mTripsModel.getPickupAddress().getName());
            pickupNumTextView.setText(mTripsModel.getPurchaseAddress().getContactNo());
            pickupAddressTextView.setText(mTripsModel.getPurchaseAddress().getAddress());
        }

        //need to disable deliver type for purchase
        if (mTripsModel.getType().equals(ConstantTypes.PURCHASE)) {
            payPickUpTextView.setVisibility(View.GONE);
            payDropTextView.setVisibility(View.GONE);
            collectDeliveryTextView.setVisibility(View.GONE);
            collectDeliveryLayout.setVisibility(View.GONE);
        } else {
            mTripsModel.setCollectAt("PICKUP");
            setDeliveryType();
        }

        /*Set Drop details from DropAddress model class*/
        deliverNumTextview.setText(mTripsModel.getDropAddress().getContactNo());
        deliveryAddressTextView.setText(mTripsModel.getDropAddress().getAddress());
        deliveryNameTextView.setText(mTripsModel.getDropAddress().getName());

        setPriceAndDiscountDetails();
        setDefaultDateAndTime();
    }

    //calculating and displaying final fare details here
    private void setPriceAndDiscountDetails() {
        payPickUpTextView.setText("PAY RS. " + String.valueOf(mTripsModel.getNet()) + " AT PICKUP");
        payDropTextView.setText("PAY RS. " + String.valueOf(mTripsModel.getNet()) + " AT DROP");
        if (mTripsModel.getDiscount() != null) {

            Boolean type = AppPreferences.getInstance().getFlatRateState();

            if (type) {
                deliverChargesTextview.setText(String.valueOf("RS." + mTripsModel.getDiscount().getAmount()));
                payPickUpTextView.setText("PAY RS. " + mTripsModel.getDiscount().getAmount() + " AT PICKUP");
                payDropTextView.setText("PAY RS. " + mTripsModel.getDiscount().getAmount() + " AT DROP");
                mTripsModel.setNet(mTripsModel.getDiscount().getAmount());
                AppPreferences.getInstance().saveFlatRateState(false);
            } else {
                //if total fare is less than 50.. need to display min fare
                double totalAfterDisc = (mTripsModel.getNet() - mTripsModel.getDiscount().getAmount());
                if (totalAfterDisc < 50) {
                    totalAfterDisc = MyApplication.getInstance().getFare().getMinFare();
                }
                deliverChargesTextview.setText(String.valueOf("RS." + totalAfterDisc));//cost after coupon discount
            }
        } else {
            deliverChargesTextview.setText(String.valueOf("RS." + mTripsModel.getNet()));//cost after coupon discount
        }
    }


    private void setDeliveryType() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.pickup_imgBtn) {
                    payDropTextView.setVisibility(View.GONE);
                    payPickUpTextView.setVisibility(View.VISIBLE);
                    mTripsModel.setCollectAt("PICKUP");
                }

                if (checkedId == R.id.delivery_imgbtn) {
                    payDropTextView.setVisibility(View.VISIBLE);
                    payPickUpTextView.setVisibility(View.GONE);
                    mTripsModel.setCollectAt("DROP");
                }

            }
        });
    }

    private void setUpToolBar() {
        toolbarTitle.setText(getResources().getString(R.string.title_activity_confirmation));
        toolbarTitle.setTextColor(getResources().getColor(R.color.darkBlue));
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ConfirmationActivity.this, DeliveryDescriptionActivity.class);
                        intent.putExtra("isPurchase", getIntent().getBundleExtra("isPurchase"));
                        intent.putExtra("itemDescList", getIntent().getBundleExtra("itemDescList"));
                        startActivity(intent);
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
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_ID) {
            return new DatePickerDialog(this, dateChangeListener, year_x, month_x, date_x);
        }

        if (id == TIME_ID) {
            return new TimePickerDialog(this, timePickerListener, hours_x, minutes_x, false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dateChangeListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear + 1;
            date_x = dayOfMonth;

            DecimalFormat mFormat = new DecimalFormat("00");

            String currentSelectedDate = Utils.getCurrentDate() + " 00" + ":00" + ":00";
            String selectedDate = year_x + "-" + mFormat.format(Double.valueOf(month_x)) + "-" + date_x + " 00" + ":00" + ":00";

            //compare current date with selected date
            //we need to save current or greater than current date
            if (Utils.compareDate(selectedDate, currentSelectedDate)) {
                selectedDate = year_x + "-" + mFormat.format(Double.valueOf(month_x)) + "-" + date_x;
                currentDateTextView.setText(Utils.convertToString(selectedDate));
            } else {
                showToast("Choose date > today");
                currentDateTextView.setText(Utils.convertToString(currentSelectedDate));
            }
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            hours_x = hourOfDay;
            minutes_x = minute;

//            String currentTime = Utils.getCurrentDateTime();
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

            String currentSelectedTime = selectedDate + " " + hourOfDay + ":" + minute + ":" + "00";

            //compare current date with selected time
            //we need to save current or greater than current time
            if (Utils.compareDateTime(currentTime, currentSelectedTime)) {
                int hourRange = hourOfDay + 1;
                selectedTime = String.valueOf(hourOfDay);

                mTripsModel.setPickupStartTime(selectedDate + " " + hourOfDay + ":" + minute);
                mTripsModel.setPickupEndTime(selectedDate + " " + hourRange + ":" + minute);

                String startTime = Utils.convert24To12(String.valueOf(hourOfDay + ":" + minute));

                currentTimeTextView.setText(String.valueOf(startTime));
            } else {
                showToast("Choose current time or greater");
            }
        }
    };

    //default is current date and time
    private void setDefaultDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        date_x = calendar.get(Calendar.DATE);
        hours_x = Integer.parseInt(Utils.getCurrentHour());

        selectedDate = Utils.getCurrentDate();
        mTripsModel.setPickupStartTime(Utils.getCurrentDateTime());
        mTripsModel.setPickupEndTime(Utils.getEndTime(1));

        String startTime = Utils.convert24To12(String.valueOf((hours_x + 1) + ":00"));
        int hourRange = hours_x + 1;
        String endTime = Utils.convert24To12(String.valueOf(hourRange + ":00"));

        currentTimeTextView.setText(String.valueOf(startTime));
    }

    @OnClick({R.id.confirmation_continue_btn, R.id.current_date_textView, R.id.current_time_textView, R.id.delivery_charges_layout})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.delivery_charges_layout:
                costEstimation();
                break;
            case R.id.current_date_textView:
                showDialog(DATE_ID);
                break;

            case R.id.current_time_textView:
                showDialog(TIME_ID);
                break;
            case R.id.confirmation_continue_btn:

                mTripsModel.setEndTime(Utils.getEndTime(1));
                mTripsModel.setStatus(ConstantTypes.REQUESTSENT);

                showProgress(this, "loading please wait...");
                //Gson gson = new Gson();
                //String json = gson.toJson(mTripsModel);
                //Log.e(TAG, "json:" + json);

                RestRequestInterface myLogin = RestAPIRequest.getRetrofit().create(RestRequestInterface.class);
                Call<TripsResponseModel> call = myLogin.postTrips(AppPreferences.getInstance().getAccessToken(), mTripsModel);
                RestAPIRequest.getInstance().doRequest(call, new RequestListener<TripsResponseModel>() {
                    @Override
                    public void onResponse(TripsResponseModel response) {
                        hideProgress();
                        nextActivity(response.getId());
                    }

                    @Override
                    public void onDisplayError(String errorMsg) {
                        hideProgress();
                    }
                });
                break;
        }
    }

    //display cost estimation dialog
    private void costEstimation() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cost_estimation_dialog);
        dialog.show();

        TextView total_distance_km = (TextView) dialog.findViewById(R.id.total_distance_km);
        TextView textView = (TextView) dialog.findViewById(R.id.cost_estimate_distance);

        int distance = (int) mTripsModel.getDistance();
        total_distance_km.setText(String.valueOf(distance + " KM"));
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

    private void nextActivity(String id) {
        Intent newIntent = new Intent(this, OrderReciptActivity.class);
        newIntent.putExtra("confirmationid", id);
        startActivity(newIntent);
        finish();
    }
}
