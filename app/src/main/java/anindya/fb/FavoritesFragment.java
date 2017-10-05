package anindya.fb;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FavoritesFragment extends Fragment {

    Context mContext;
    private int[] tabIcons = { R.drawable.users, R.drawable.pages, R.drawable.events, R.drawable.places, R.drawable.groups };
    private int[] tabs = {R.string.user, R.string.page, R.string.event, R.string.place, R.string.group};
    int NUMBER_TABS = 5;

    static TabLayout tabLayout;
    static View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(v == null) {
            v = inflater.inflate(R.layout.fragment_favorites, container, false);
            getActivity().setTitle(getString(R.string.favorites));

            mContext = getContext();
            ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) v.findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();
        }
        return v;
    }

    private void setupTabIcons() {
        for(int i=0; i<NUMBER_TABS; i++) {
            TextView tab = (TextView) LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
            tab.setText(mContext.getString(tabs[i]));
            tab.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[i], 0, 0);
            tabLayout.getTabAt(i).setCustomView(tab);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
            menu.findItem(R.id.action_settings).setVisible(false);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager(), mContext);
        for(int i=0; i<NUMBER_TABS; i++) {
            TableFragment frag = new TableFragment();
            Bundle b = new Bundle();
            b.putString("title", getString(tabs[i]));
            b.putString("type", "favorite");
            frag.setArguments(b);
            adapter.addFrag(frag, getString(tabs[i]));
        }
        viewPager.setAdapter(adapter);
    }
}
