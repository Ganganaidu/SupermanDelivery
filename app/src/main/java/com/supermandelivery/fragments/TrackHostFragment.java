package com.supermandelivery.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.supermandelivery.MyApplication;
import com.supermandelivery.R;
import com.supermandelivery.listeners.RequestListener;
import com.supermandelivery.models.TripsModel;
import com.supermandelivery.network.RestAPIRequest;
import com.supermandelivery.network.RestRequestInterface;
import com.supermandelivery.pageradpter.TrackPagerAdapter;
import com.supermandelivery.utils.AppPreferences;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by Office on 3/13/2016.
 */
public class TrackHostFragment extends Fragment {

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    public static TrackHostFragment trackHostFragment;

    private TrackPagerAdapter adapter;
    private FragmentManager manager;

    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public TrackHostFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trackHostFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.track_host_frgament, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MyApplication.getInstance().showProgress(getActivity(), "Loading track details");
        getTrackDetails();

    }

    private void loadFragmentAdapter() {
        //we will call child fragments from pager adapter
        manager = getActivity().getSupportFragmentManager();
        adapter = new TrackPagerAdapter(manager, getActivity());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        // mTabLayout.setupWithViewPager(mPager1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);

//        viewPager.setOffscreenPageLimit(1); // if you use 3 tabs
    }

    //http://stackoverflow.com/questions/23142956/sending-data-from-nested-fragments-to-parent-fragment

    private void getTrackDetails() {

        RestRequestInterface myLogin = RestAPIRequest.getRetrofit().create(RestRequestInterface.class);//this is how retrofit create your api
        Call<List<TripsModel>> call = myLogin.getTrips(AppPreferences.getInstance().getAccessToken(), AppPreferences.getInstance().getUserId());
        RestAPIRequest.getInstance().doRequest(call, new RequestListener<List<TripsModel>>() {
                    @Override
                    public void onResponse(List<TripsModel> response) {
                        MyApplication.getInstance().hideProgress();
                        MyApplication.getInstance().setTripsModelList(response);

                        loadFragmentAdapter();
                    }

                    @Override
                    public void onDisplayError(String errorMsg) {
                        MyApplication.getInstance().hideProgress();
                        loadFragmentAdapter();
                    }
                }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
