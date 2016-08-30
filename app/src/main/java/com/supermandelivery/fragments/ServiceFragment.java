package com.supermandelivery.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.supermandelivery.Fonts.ButtonViewBold;
import com.supermandelivery.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ganga on 3/13/2016.
 */
public class ServiceFragment extends Fragment {
    private static final String TAG = "ServiceFragment";
    /*@Bind(R.id.delivering_docs_btn)
    ButtonViewBold deliveringDocsBtn;
    @Bind(R.id.delivering_gifts_btn)
    ButtonViewBold deliveringGiftsBtn;
    @Bind(R.id.delivering_files_btn)
    ButtonViewBold deliveringFilesBtn;*/

    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static ServiceFragment newInstance(int position) {
        ServiceFragment f = new ServiceFragment();
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
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /*@OnClick({R.id.delivering_docs_btn, R.id.delivering_gifts_btn, R.id.delivering_files_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delivering_docs_btn:
                break;
            case R.id.delivering_gifts_btn:
                break;
            case R.id.delivering_files_btn:
                break;
        }
    }*/
}
