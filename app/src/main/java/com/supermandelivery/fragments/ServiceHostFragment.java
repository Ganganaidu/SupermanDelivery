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

import com.supermandelivery.R;
import com.supermandelivery.pageradpter.ServicePagerAdapter;
import com.supermandelivery.pageradpter.TrackPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Office on 3/13/2016.
 */
public class ServiceHostFragment extends Fragment {

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    public static ServiceHostFragment trackHostFragment;

    private ServicePagerAdapter adapter;
    private FragmentManager manager;

    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public ServiceHostFragment() {

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

        loadFragmentAdapter();
    }

    private void loadFragmentAdapter() {
        //we will call child fragments from pager adapter
        manager = getActivity().getSupportFragmentManager();
        adapter = new ServicePagerAdapter(manager, getActivity());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        // mTabLayout.setupWithViewPager(mPager1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);

        //viewPager.setOffscreenPageLimit(1); // if you use 3 tabs
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
