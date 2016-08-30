package com.supermandelivery.pageradpter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.supermandelivery.R;
import com.supermandelivery.fragments.CancelledFragment;
import com.supermandelivery.fragments.DeliveredFragment;
import com.supermandelivery.fragments.IntransitFragment;
import com.supermandelivery.fragments.TrackFragment;

/**
 * Created by Ganga on 3/13/2016.
 */
public class TrackPagerAdapter extends FragmentStatePagerAdapter {

    Context mContext;

    public TrackPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return IntransitFragment.newInstance(position);
        } else if (position == 1) {
            return TrackFragment.newInstance(position);
        } else if (position == 2) {
            return DeliveredFragment.newInstance(position);
        } else {
            return CancelledFragment.newInstance(position);
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
                title = mContext.getResources().getString(R.string.intransit);
                break;
            case 1:
                title = mContext.getResources().getString(R.string.scheduled);
                break;
            case 2:
                title = mContext.getResources().getString(R.string.completed);
                break;
            case 3:
                title = mContext.getResources().getString(R.string.cancelled);
                break;
        }
        return title;
    }
}
