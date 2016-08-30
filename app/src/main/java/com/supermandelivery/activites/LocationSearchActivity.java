package com.supermandelivery.activites;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.supermandelivery.MyApplication;
import com.supermandelivery.R;
import com.supermandelivery.adapters.SavedAddressAdapter;
import com.supermandelivery.listeners.RequestListener;
import com.supermandelivery.models.Geopoint;
import com.supermandelivery.models.SavedAddress;
import com.supermandelivery.network.RestAPIRequest;
import com.supermandelivery.network.RestRequestInterface;
import com.supermandelivery.utils.AppPreferences;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import retrofit2.Call;

public class LocationSearchActivity extends AppCompatActivity {
    private static final String TAG = "LocationSearchActivity";

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recently_used)
    TextView recentlyUsed;
    @Bind(R.id.recently_used_list)
    ListView recentlyUsedList;
    @Bind(R.id.recently_used_layout)
    LinearLayout recentlyUsedLayout;
    @Bind(R.id.favourite_location)
    TextView favouriteLocation;
    @Bind(R.id.favourites_list)
    ListView favouritesList;
    @Bind(R.id.favourite_address_layout)
    LinearLayout favouriteAddressLayout;

    private List<SavedAddress> favAdrsList;
    private PlaceAutocompleteFragment autocompleteFragment;
    private SavedAddress savedAddress;
    private SavedAddressAdapter adapter;
    private int selectionType;
    private Place mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        savedAddress = new SavedAddress();
        savedAddress.setId(AppPreferences.getInstance().getUserId());
        savedAddress.setName(AppPreferences.getInstance().getUserName());

        selectionType = getIntent().getIntExtra("mTypeSelected", 0);
        //updating tool bar with name and button
        setUpToolBar();

        //search location fragment
        setUpPlaceSearch();
        loadFavAddress();

        //need to enable this when we get address from local database
        recentlyUsedLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:

                //when user select place from search we will save place in place object and later we will use her
                if (mPlace != null) {
                    Log.i(TAG, "Place: " + mPlace.getName());
                    savedAddress.setAddress(mPlace.getAddress().toString());
                    savedAddress.setGeopoint(new Geopoint(mPlace.getLatLng().latitude, mPlace.getLatLng().longitude));

                    MyApplication.getInstance().setGeoPoint(mPlace.getLatLng());
                    MyApplication.getInstance().setSelectedAddress(savedAddress);
                }
                //we need to close this activity only if user has address
                if (savedAddress != null && savedAddress.getAddress() != null) {

                    MyApplication.getInstance().setMapSelectionType(selectionType);
                    //DbHelper.getInstance().insertRecentAddress(savedAddress);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.select_location), Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @OnItemClick(R.id.favourites_list)
    void onItemClick(int position) {
        savedAddress.setAddress(favAdrsList.get(position).getAddress());
        savedAddress.setGeopoint(favAdrsList.get(position).getGeopoint());
        savedAddress.setContactNo(favAdrsList.get(position).getContactNo());
        savedAddress.setFlatNo(favAdrsList.get(position).getFlatNo());
        savedAddress.setName(favAdrsList.get(position).getName());

        LatLng latLng = null;
        if (favAdrsList.get(position).getGeopoint() != null) {
            latLng = new LatLng(favAdrsList.get(position).getGeopoint().getLat(), favAdrsList.get(position).getGeopoint().getLng());
        }
        MyApplication.getInstance().setGeoPoint(latLng);
        MyApplication.getInstance().setSelectedAddress(savedAddress);

        //set selected address to search edit text
        autocompleteFragment.setText(adapter.getSavedAdrsList().get(position).getAddress());
        MyApplication.getInstance().setMapSelectionType(selectionType);

        finish();
    }

    private void setUpPlaceSearch() {
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mPlace = place;
                //        placeDetailsStr = place.getName() + "\n"
//                        + place.getId() + "\n"
//                        + place.getLatLng().toString() + "\n"
//                        + place.getAddress() + "\n"
//                        + place.getAttributions();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        autocompleteFragment.setHint("Search your address here");
    }

    private void loadFavAddress() {
        RestRequestInterface myLogin = RestAPIRequest.getRetrofit().create(RestRequestInterface.class);//this is how retrofit create your api
        Call<List<SavedAddress>> call = myLogin.getSavedAddress(AppPreferences.getInstance().getAccessToken(), AppPreferences.getInstance().getUserId());
        RestAPIRequest.getInstance().doRequest(call, new RequestListener<List<SavedAddress>>() {
                    @Override
                    public void onResponse(List<SavedAddress> response) {
                        favAdrsList = response;
                        //if no data to display open Add Address activity to add new address
                        if (response != null && response.size() != 0) {
                            favouriteAddressLayout.setVisibility(View.VISIBLE);

                            Collections.reverse(favAdrsList);
                            adapter = new SavedAddressAdapter(LocationSearchActivity.this, response);
                            favouritesList.setAdapter(adapter);

                        } else {
                            favouriteAddressLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onDisplayError(String errorMsg) {

                    }
                }
        );
    }

    private void setUpToolBar() {
        mToolbarTitle.setTextColor(getResources().getColor(R.color.darkBlue));
        if (selectionType == 1) {
            mToolbarTitle.setText(getResources().getString(R.string.pickup_address));
        } else if (selectionType == 2) {
            mToolbarTitle.setText(getResources().getString(R.string.destination_address));
        } else {
            mToolbarTitle.setText(getResources().getString(R.string.favourite_address));
        }
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
