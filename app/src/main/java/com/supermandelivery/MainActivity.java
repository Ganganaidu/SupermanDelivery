package com.supermandelivery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.supermandelivery.activites.DropMapActivity;
import com.supermandelivery.fragments.ContactUsFragment;
import com.supermandelivery.fragments.MapPickUpFragment;
import com.supermandelivery.fragments.RatecardFragment;
import com.supermandelivery.fragments.SavedAddressFragment;
import com.supermandelivery.fragments.ServiceHostFragment;
import com.supermandelivery.fragments.TrackHostFragment;
import com.supermandelivery.fragments.TutorialsFragment;
import com.supermandelivery.listeners.RequestListener;
import com.supermandelivery.models.Fare;
import com.supermandelivery.models.SavedAddress;
import com.supermandelivery.network.RestAPIRequest;
import com.supermandelivery.network.RestRequestInterface;
import com.supermandelivery.otpactivities.OTPActivity;
import com.supermandelivery.otpactivities.UserProfileActivity;
import com.supermandelivery.utils.AppPreferences;
import com.supermandelivery.utils.CustomTypefaceSpan;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MapPickUpFragment.OnMapSelectedListener {

    private static final String TAG = "MainActivity";
    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private final Handler mDrawerActionHandler = new Handler();

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.nav_view)
    NavigationView navView;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    ImageView imageview;
    TextView title_textView;

    ActionBarDrawerToggle toggle;

    public ArrayList<LatLng> latLngArrayList;
    public ArrayList<SavedAddress> savedAddressArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false); //disable "hamburger to arrow" drawable
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer); //set your own
        toggle.syncState();
        navView.setItemIconTintList(null);
        navView.setNavigationItemSelectedListener(this);

        //Navigation View Menu Item Font change
        Menu menu = navView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            applyFontToMenuItem(menuItem);
        }

        openPickUpMapFragment();

        navigateToProfileView();

        setHomeClick();

        //Set Actionbar logo here (Superman Delivers logo)
        getFareChart();

        setActionBarTitle(false, "");

        logOutView();

    }

    private void applyFontToMenuItem(MenuItem menuItem) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/ufonts.com_clanpro-news.ttf");
        SpannableString mNewTitle = new SpannableString(menuItem.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        menuItem.setTitle(mNewTitle);
    }

    private void setActionBarTitle(boolean isTextEnabled, String text) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v;
        if (isTextEnabled) {
            v = inflater.inflate(R.layout.toolbar_title_layout, null);
            title_textView = (TextView) v.findViewById(R.id.title_textView);
            title_textView.setText(text);
        } else {
            v = inflater.inflate(R.layout.toolbar_logo, null);
            imageview = (ImageView) v.findViewById(R.id.title_logo_imageView);
        }
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    private void logOutView() {
        //change this based on user login state
        Menu menu = navView.getMenu();
        MenuItem nav_login = menu.findItem(R.id.nav_login);
        nav_login.setTitle(R.string.nav_logout);
        applyFontToMenuItem(nav_login);
    }

    private void setHomeClick() {
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    //Navigation view header view on click
    private void navigateToProfileView() {
        navView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(in);
            }
        });
    }

    //update username
    private void updateUserName() {
        TextView username_textView = (TextView) navView.getHeaderView(0).findViewById(R.id.username_textview);
        username_textView.setText(AppPreferences.getInstance().getUserName());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*update user name .. we will get this from profile page*/
        updateUserName();

        //reset map fragment
        if (MyApplication.getInstance().getIsOrderConfirmed() == 1) {
            MyApplication.getInstance().setIsOrderConfirmed(0);
            openPickUpMapFragment();
        }
    }

    private void openPickUpMapFragment() {
        latLngArrayList = new ArrayList<>();
        savedAddressArrayList = new ArrayList<>();

        MyApplication.getInstance().setSavedAddressArrayList(savedAddressArrayList);
        MyApplication.getInstance().setLatLngArrayList(latLngArrayList);

        MapPickUpFragment superMapFragment = new MapPickUpFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, superMapFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        drawerLayout.closeDrawer(GravityCompat.START);
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(item.getItemId());
            }
        }, DRAWER_CLOSE_DELAY_MS);
        return true;
    }

    private void navigate(final int id) {
        if (id == R.id.nav_home) {

            openPickUpMapFragment();
            //getSupportActionBar().setTitle(getResources().getString(R.string.nav_home));
            setActionBarTitle(false, getResources().getString(R.string.nav_home));

        } else if (id == R.id.nav_track) {

            //getSupportActionBar().setTitle(getResources().getString(R.string.nav_track));
            setActionBarTitle(true, getResources().getString(R.string.nav_track));
            // Instantiate a new fragment.
            Fragment newFragment = new TrackHostFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();

        } else if (id == R.id.nav_save_address) {
            // getSupportActionBar().setTitle(getResources().getString(R.string.nav_save_address));
            setActionBarTitle(true, getResources().getString(R.string.nav_save_address));

            Fragment newFragment = new SavedAddressFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();

        } else if (id == R.id.nav_ratecard) {
            //getSupportActionBar().setTitle(getResources().getString(R.string.nav_rate_card));
            setActionBarTitle(true, getResources().getString(R.string.nav_rate_card));

            // Instantiate a new fragment.
            Fragment newFragment = new RatecardFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();


        } else if (id == R.id.nav_services) {
            setActionBarTitle(true, getResources().getString(R.string.nav_services));

            // Instantiate a new fragment.
            Fragment newFragment2 = new ServiceHostFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment2);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();

        } else if (id == R.id.nav_share) {
            // setActionBarTitle(true, getResources().getString(R.string.nav_share));

            String shareBody = "http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName();

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Superman Delivery (Open it in Google Play Store to Download the Application)");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));


        } else if (id == R.id.nav_contact_us) {

            setActionBarTitle(true, getResources().getString(R.string.nav_contact));
            // Instantiate a new fragment.
            Fragment newFragment2 = new ContactUsFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment2);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();

        } else if (id == R.id.nav_rate_us) {

        } else if (id == R.id.nav_tutorials) {

            setActionBarTitle(true, getResources().getString(R.string.nav_tutorials));

            // Instantiate a new fragment.
            Fragment newFragment = new TutorialsFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();

//            startActivity(new Intent(MainActivity.this, TutorialsActivity.class));

        } else if (id == R.id.nav_login) {
            MyApplication.getInstance().resetData();
            AppPreferences.getInstance().deletePref();
            Intent newIntent = new Intent(MainActivity.this, OTPActivity.class);
            startActivity(newIntent);
            finish();
        }
    }


    private void getFareChart() {
        RestRequestInterface myLogin = RestAPIRequest.getRetrofit().create(RestRequestInterface.class);
        Call<List<Fare>> call = myLogin.getFareChart(AppPreferences.getInstance().getAccessToken());
        RestAPIRequest.getInstance().doRequest(call, new RequestListener<List<Fare>>() {
            @Override
            public void onResponse(List<Fare> response) {
                if (response != null && response.size() > 0) {
                    MyApplication.getInstance().saveFareChart(response.get(0));
                }
            }

            @Override
            public void onDisplayError(String errorMsg) {

            }
        });
    }

    @Override
    public void onMapSelected(int position) {
        MyApplication.getInstance().setMapSelectionType(0);//reset

        Intent in = new Intent(this, DropMapActivity.class);
        startActivity(in);
    }
}



