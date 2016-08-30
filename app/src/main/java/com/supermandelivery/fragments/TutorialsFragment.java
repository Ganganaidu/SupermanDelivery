package com.supermandelivery.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.supermandelivery.R;
import com.supermandelivery.adapters.SlidingImage_Adapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TutorialsFragment extends Fragment {

    @Bind(R.id.pager)
    ViewPager mPager;
    @Bind(R.id.indicator)
    CirclePageIndicator indicator;

    //private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] IMAGES = {R.drawable.tutorial_1, R.drawable.tutorial_2, R.drawable.tutorial_3,
            R.drawable.tutorial_4, R.drawable.tutorial_5, R.drawable.tutorial_6, R.drawable.tutorial_7};
    private ArrayList<Integer> ImagesArray = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorials, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

        mPager.setAdapter(new SlidingImage_Adapter(getActivity(), ImagesArray));

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(4 * density);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

}

