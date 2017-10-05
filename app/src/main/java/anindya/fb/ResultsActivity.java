package anindya.fb;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = { R.drawable.users, R.drawable.pages, R.drawable.events, R.drawable.places, R.drawable.groups };
    private int[] tabs = {R.string.user, R.string.page, R.string.event, R.string.place, R.string.group};

    private int NUMBER_TABS = 5;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = getApplicationContext();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons(tabLayout, mContext);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupTabIcons(TabLayout tabLayout, Context context) {
        for(int i=0; i<NUMBER_TABS; i++) {
            TextView tab = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
            tab.setText(context.getString(tabs[i]));
            tab.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[i], 0, 0);
            tabLayout.getTabAt(i).setCustomView(tab);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), mContext);
        for(int i=0; i<NUMBER_TABS; i++) {
            TableFragment frag = new TableFragment();
            Bundle b = new Bundle();
            b.putString("title", getString(tabs[i]));
            b.putString("type", "result");
            frag.setArguments(b);
            adapter.addFrag(frag, getString(tabs[i]));
        }
        viewPager.setAdapter(adapter);
    }
}
