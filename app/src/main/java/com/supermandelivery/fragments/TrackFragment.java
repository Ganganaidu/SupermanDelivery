package com.supermandelivery.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.supermandelivery.MyApplication;
import com.supermandelivery.R;
import com.supermandelivery.adapters.TrackingAdapter;
import com.supermandelivery.models.TripsModel;
import com.supermandelivery.utils.ConstantTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ganga on 3/13/2016.
 */
public class TrackFragment extends Fragment {
    private static final String TAG = "TrackFragment";

    @Bind(R.id.track_recycler_view)
    RecyclerView trackTecyclerView;
    @Bind(R.id.main_layout)
    RelativeLayout mainLayout;

    private TrackingAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private List<TripsModel> tripsModelList;

    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static TrackFragment newInstance(int position) {
        TrackFragment f = new TrackFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.track_frgament, container, false);
        ButterKnife.bind(this, view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        trackTecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        trackTecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadData();
        showResults();
    }

    private void loadData() {
        tripsModelList = new ArrayList<>();

        if (MyApplication.getInstance().getTripsModelList() != null) {
            for (TripsModel tripModel : MyApplication.getInstance().getTripsModelList()) {
                //filter based on request type
                if (tripModel.getStatus().equals(ConstantTypes.REQUESTSENT)) {
                    tripsModelList.add(tripModel);//request sent
                }
            }
        }
    }

    public void showResults() {

        if (tripsModelList != null && tripsModelList.size() > 0) {
            //Log.e(TAG, "list:" + MyApplication.getInstance().getTripsModelList().size());
            Collections.reverse(tripsModelList);

            adapter = new TrackingAdapter(getActivity(), tripsModelList);
            trackTecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
