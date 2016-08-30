package com.supermandelivery.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.supermandelivery.R;
import com.supermandelivery.activites.AddAddressActivity;
import com.supermandelivery.adapters.SavedAddressAdapter;
import com.supermandelivery.listeners.RequestListener;
import com.supermandelivery.models.SavedAddress;
import com.supermandelivery.network.RestAPIRequest;
import com.supermandelivery.network.RestRequestInterface;
import com.supermandelivery.utils.AppPreferences;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by ADMIN on 3/19/2016.
 */
public class SavedAddressFragment extends Fragment implements SavedAddressAdapter.OnAddressDeleteListener {

    @Bind(R.id.addAdres_listView)
    ListView addAdresListView;
    @Bind(R.id.addAdres_fltBtn)
    FloatingActionButton addAdresFltBtn;

    private SavedAddressAdapter adapter;
    private Menu menu;
    private boolean isEditAddressSelected;

    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static SavedAddressFragment newInstance(int position) {
        SavedAddressFragment f = new SavedAddressFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        //args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saved_address_fragment, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        getAddress();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                changeEditIcon();
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.addAdres_fltBtn)
    public void onClick() {
        callAddressActivity();
    }

    private void callAddressActivity() {
        Intent in = new Intent(getActivity(), AddAddressActivity.class);
        startActivity(in);
    }

    private void changeEditIcon() {
        if (!isEditAddressSelected) {//change edit icon to done button
            isEditAddressSelected = true;
            menu.findItem(R.id.action_edit).setIcon(R.drawable.done);
            if (adapter != null) {//update adapter
                adapter.setDeleteBtnEnabled(true);
                adapter.notifyDataSetChanged();
            }
        } else {//change edit done to edit
            isEditAddressSelected = false;
            menu.findItem(R.id.action_edit).setIcon(R.drawable.edit);
            if (adapter != null) {//update adapter
                adapter.setDeleteBtnEnabled(false);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void getAddress() {

        RestRequestInterface myLogin = RestAPIRequest.getRetrofit().create(RestRequestInterface.class);//this is how retrofit create your api
        Call<List<SavedAddress>> call = myLogin.getSavedAddress(AppPreferences.getInstance().getAccessToken(), AppPreferences.getInstance().getUserId());
        RestAPIRequest.getInstance().doRequest(call, new RequestListener<List<SavedAddress>>() {
                    @Override
                    public void onResponse(List<SavedAddress> response) {
                        if (response == null || response.size() == 0) { //if no data to display open Add Address activity to add new address
                            //callAddressActivity();
                        } else  //display saved address

                            Collections.reverse(response);
                        adapter = new SavedAddressAdapter(getActivity(), response);
                        addAdresListView.setAdapter(adapter);
                        adapter.setListener(SavedAddressFragment.this);
                    }

                    @Override
                    public void onDisplayError(String errorMsg) {

                    }
                }
        );
    }

    @Override
    public void deleteAddress(String addressID, final int position) {
        getActivity().setProgressBarIndeterminateVisibility(true);

        RestRequestInterface myLogin = RestAPIRequest.getRetrofit().create(RestRequestInterface.class);//this is how retrofit create your api
        Call<String> call = myLogin.deleteAddress(AppPreferences.getInstance().getAccessToken(),
                AppPreferences.getInstance().getUserId(), addressID);
        RestAPIRequest.getInstance().doRequest(call, new RequestListener<String>() {
            @Override
            public void onResponse(String response) {
                getActivity().setProgressBarIndeterminateVisibility(false);
                if (adapter != null) {
                    adapter.deleteItem(position);
                    changeEditIcon();
                }
            }

            @Override
            public void onDisplayError(String errorMsg) {
                getActivity().setProgressBarIndeterminateVisibility(false);
            }
        });
    }
}
