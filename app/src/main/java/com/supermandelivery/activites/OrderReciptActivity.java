package com.supermandelivery.activites;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.supermandelivery.MyApplication;
import com.supermandelivery.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderReciptActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.order_number_textview)
    TextView order_number_textview;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_recipt);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setUpToolBar();

        String orderNumber = getIntent().getStringExtra("confirmationid");
        order_number_textview.setText(orderNumber);
    }

    private void setUpToolBar() {
        toolbarTitle.setText(getResources().getString(R.string.order_receipt));
        mToolbar.setNavigationIcon(R.drawable.close);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyApplication.getInstance().setIsOrderConfirmed(1);
                        MyApplication.getInstance().resetData();
                        finish();
                    }
                });

        //updating status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }
}
