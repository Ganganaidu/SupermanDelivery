package com.supermandelivery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.supermandelivery.R;
import com.supermandelivery.models.SavedAddress;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ADMIN on 3/19/2016.
 */
public class SavedAddressAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<SavedAddress> savedAdrsList;
    boolean isDeletedEnabled;

    public SavedAddressAdapter(Context context, List<SavedAddress> arrsList) {
        this.mContext = context;
        this.savedAdrsList = arrsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return savedAdrsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.saved_address_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.addAdresNameTextView.setText(savedAdrsList.get(position).getName());
        holder.addAdresAdresTextView.setText(savedAdrsList.get(position).getAddress());

        if (isDeletedEnabled) {
            holder.deleteAdrsImageView.setVisibility(View.VISIBLE);
        } else {
            holder.deleteAdrsImageView.setVisibility(View.GONE);
        }
        holder.deleteAdrsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteAddress(savedAdrsList.get(position).getId(), position);
            }
        });
        return convertView;
    }

    public void setDeleteBtnEnabled(boolean value) {
        this.isDeletedEnabled = value;
    }

    public void deleteItem(int position) {
        savedAdrsList.remove(position);
        notifyDataSetChanged();
    }

    public List<SavedAddress> getSavedAdrsList() {
        return savedAdrsList;
    }

    OnAddressDeleteListener listener;

    public void setListener(OnAddressDeleteListener listener) {
        this.listener = listener;
    }

    public interface OnAddressDeleteListener {
        void deleteAddress(String addressID, int position);
    }


    static class ViewHolder {
        @Bind(R.id.addAdres_name_textView)
        TextView addAdresNameTextView;
        @Bind(R.id.addAdres_adres_textView)
        TextView addAdresAdresTextView;
        @Bind(R.id.delete_adrs_imageView)
        ImageView deleteAdrsImageView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

