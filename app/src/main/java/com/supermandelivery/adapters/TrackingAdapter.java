package com.supermandelivery.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.supermandelivery.MyApplication;
import com.supermandelivery.R;
import com.supermandelivery.activites.TrackDetailsActivity;
import com.supermandelivery.models.TripsModel;
import com.supermandelivery.utils.ConstantTypes;
import com.supermandelivery.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ADMIN on 3/22/2016.
 */
public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.ViewHolder> {

    Context mContext;
    LayoutInflater inflater;
    List<TripsModel> trackList;

    public TrackingAdapter(Context context, List<TripsModel> arrsList) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
        this.trackList = arrsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item_spec, null);

        //ViewHolder viewHolder = new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        TripsModel tripsModel = trackList.get(position);

        holder.trackIdTextView.setText(tripsModel.getId());

        if (tripsModel.getPickupStartTime() != null && tripsModel.getPickupStartTime().length() != 0) {
            holder.trackDateTextView.setText(Utils.convertDate(tripsModel.getPickupStartTime()));
            holder.trackTimeTextView.setText(Utils.convertTime(tripsModel.getPickupEndTime()));
        }

        if (tripsModel.getDropAddress() == null) {
            holder.trackUsernameTextView.setText("No name");
        } else {
            holder.trackUsernameTextView.setText(tripsModel.getPickupAddress().getName());
            holder.trackDropNameTextView.setText(tripsModel.getDropAddress().getName());
        }

        if ((tripsModel.getStatus().equals(ConstantTypes.assigned) ||
                tripsModel.getStatus().equals(ConstantTypes.requestAccepted))) {
            holder.trackStatusTextView.setText("IN-TRANSIT");
        } else if (tripsModel.getStatus().equals(ConstantTypes.REQUESTSENT)) {
            holder.trackStatusTextView.setText("ORDER SENT");
        } else if (tripsModel.getStatus().equals(ConstantTypes.delivered)) {
            holder.trackStatusTextView.setText("DELIVERED");
        } else if (tripsModel.getStatus().equals(ConstantTypes.cancelled)) {
            holder.trackStatusTextView.setText("CANCELLED");
        }


        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TripsModel tripsModel = trackList.get(holder.getAdapterPosition());

                MyApplication.getInstance().setTripsModel(tripsModel);
                Intent in = new Intent(mContext, TrackDetailsActivity.class);
                mContext.startActivity(in);
            }
        });
    }

    public void clearData() {
        trackList.clear();
    }

    @Override
    public int getItemCount() {
        return (null != trackList ? trackList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.track_id_textView)
        TextView trackIdTextView;
        @Bind(R.id.track_date_textView)
        TextView trackDateTextView;
        @Bind(R.id.track_time_textView)
        TextView trackTimeTextView;
        @Bind(R.id.track_username_textView)
        TextView trackUsernameTextView;
        @Bind(R.id.track_dropname_textView)
        TextView trackDropNameTextView;
        @Bind(R.id.track_status_textView)
        TextView trackStatusTextView;
        @Bind(R.id.item_layout)
        LinearLayout itemLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
