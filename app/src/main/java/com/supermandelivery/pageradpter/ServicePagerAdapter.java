package com.supermandelivery.pageradpter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.supermandelivery.R;
import com.supermandelivery.fragments.DeliveredFragment;
import com.supermandelivery.fragments.ErrandsFragment;
import com.supermandelivery.fragments.IntransitFragment;
import com.supermandelivery.fragments.OthersFragment;
import com.supermandelivery.fragments.PurchaseFragment;
import com.supermandelivery.fragments.ServiceFragment;
import com.supermandelivery.fragments.TrackFragment;

/**
 * Created by Ganga on 3/13/2016.
 */
public class ServicePagerAdapter extends FragmentStatePagerAdapter {

    Context mContext;

    public ServicePagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ServiceFragment.newInstance(position);
        } else if (position == 1) {
            return PurchaseFragment.newInstance(position);
        } else if (position == 2) {
            return ErrandsFragment.newInstance(position);
        } else {
            return OthersFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = " ";
        switch (position) {
            case 0:
                title = mContext.getResources().getString(R.string.pickup_drop_and);
                break;
            case 1:
                title = mContext.getResources().getString(R.string.purchase_button);
                break;
            case 2:
                title = mContext.getResources().getString(R.string.errands);
                break;
            case 3:
                title = mContext.getResources().getString(R.string.others);
                break;
        }
        return title;
    }
}
