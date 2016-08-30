package com.supermandelivery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.supermandelivery.R;
import com.supermandelivery.models.Prodcut;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by syarlagadda on 3/20/16.
 */
public class ProductAdapter extends ArrayAdapter<Prodcut> {

    List<Prodcut> mProdcutArrayList;
    Context mContext;
    LayoutInflater inflater;

    public ProductAdapter(Context context, int resource, List<Prodcut> objects) {
        super(context, resource, objects);

        this.mProdcutArrayList = objects;
        this.mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mProdcutArrayList.size();
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.item_simple_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.productName.setText(mProdcutArrayList.get(position).getName());
        return convertView;
    }


    class ViewHolder {
        @Bind(R.id.text1)
        TextView productName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
