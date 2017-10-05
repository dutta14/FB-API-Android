package anindya.fb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;

import anindya.fb.datamodel.Datum;
import anindya.fb.detailmodel.ItemDetail;

public class DetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    int[] tabs = {R.string.albums, R.string.posts};
    int[] tabIcons = {R.drawable.albums, R.drawable.posts};
    Menu mMenu;
    Context mContext;
    Datum curData;
    String mType;
    CallbackManager cbMgr;

    public static final int NUMBER_TABS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = getApplicationContext();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        cbMgr = CallbackManager.Factory.create();
        setupTabIcons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);
        mMenu = menu;
        mMenu.setGroupVisible(R.id.details, false);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cbMgr.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FavoritesList.initList(mContext);

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add:
                Toast.makeText(mContext, R.string.fav_added, Toast.LENGTH_SHORT).show();
                mMenu.getItem(0).setVisible(false);
                mMenu.getItem(1).setVisible(true);
                FavoritesList.add(curData, mType);
                return true;
            case R.id.action_remove:
                Toast.makeText(mContext, R.string.fav_removed, Toast.LENGTH_SHORT).show();
                mMenu.getItem(0).setVisible(true);
                mMenu.getItem(1).setVisible(false);
                FavoritesList.remove(curData);
                return true;
            case R.id.action_share:
                Toast.makeText(mContext, String.format(getString(R.string.share_progress), curData.getName()), Toast.LENGTH_SHORT).show();
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(RowAdapter.BASE_URL))
                        .setImageUrl(Uri.parse(curData.getPicture().getData().getUrl()))
                        .setContentTitle(curData.getName())
                        .setContentDescription(getString(R.string.share_heading))
                        .build();

                ShareDialog dialog = new ShareDialog(this);
                dialog.registerCallback(cbMgr, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        if(result.getPostId() != null)
                            Toast.makeText(mContext, R.string.share_success, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancel() {
                        Toast.makeText(mContext, R.string.share_cancel, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(mContext, R.string.share_cancel, Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show(content);
                return true;
        }
        return true;
    }

    private void setupTabIcons() {
        for(int i=0; i<NUMBER_TABS; i++) {
            TextView tab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tab.setText(getString(tabs[i]));
            tab.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[i], 0, 0);
            tabLayout.getTabAt(i).setCustomView(tab);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        DetailsActivity.ViewPagerAdapter adapter = new DetailsActivity.ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new AlbumsFragment(), getString(tabs[0]));
        adapter.addFrag(new PostsFragment(), getString(tabs[1]));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, new IntentFilter(RowAdapter.DETAIL_COMPLETE));
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
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
                ItemDetail detail = (ItemDetail) intent.getExtras().get(RowAdapter.DETAIL_MESSAGE);
                curData = (Datum) intent.getExtras().get("profile");
                mType = intent.getStringExtra("title");
                String profilePic = curData.getPicture().getData().getUrl();
                String profileName = curData.getName();
                String profileId = curData.getId();
                if(mMenu != null) {
                    mMenu.setGroupVisible(R.id.details, true);
                    mMenu.getItem(FavoritesList.contains(profileId)? 0: 1).setVisible(false);
                }
                ((AlbumsFragment) mFragmentList.get(0)).setData(detail == null? null : detail.getAlbums());
                ((PostsFragment) mFragmentList.get(1)).setData(detail == null? null: detail.getPosts(), profileName, profilePic);
            }
        };
    }
}
