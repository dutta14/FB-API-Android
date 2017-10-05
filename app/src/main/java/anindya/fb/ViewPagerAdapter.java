package anindya.fb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import anindya.fb.datamodel.Result;

/**
 * Created by anind on 4/16/2017.
 */

class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<TableFragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    Context mContext;

    public ViewPagerAdapter(FragmentManager manager, Context context) {
        super(manager);
        mContext = context;
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, new IntentFilter(SearchFragment.DOWNLOAD_COMPLETE));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(TableFragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                for (TableFragment data : mFragmentList) {
                    data.setResult((Result) intent.getExtras().get(SearchFragment.DOWNLOAD_MESSAGE));
                }
        }
    };
}