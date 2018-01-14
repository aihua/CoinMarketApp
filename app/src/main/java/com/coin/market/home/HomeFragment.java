package com.coin.market.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.coin.market.BaseFragment;
import com.coin.market.R;
import com.coin.market.event.NotifyEvent;
import com.coin.market.home.all.AllCoinFargment;
import com.coin.market.home.fav.FavouriteFragment;
import com.coin.market.main.MainActivity;

/**
 * Created by t430 on 1/7/2018.
 */

public class HomeFragment extends BaseFragment{
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getChildFragmentManager()));
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position ==0){
                    sendEvent(new NotifyEvent(NotifyEvent.Type.SHOW_SEARCH));
                }
                if (position == 1){
                    sendEvent(new NotifyEvent(NotifyEvent.Type.HIDE_SEARCH));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (getActivity() != null){
            ((MainActivity)getActivity()).setTitle("Home");
            ((MainActivity)getActivity()).showAllToolBar();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }
    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[] { "All coins", "Favourites"};

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            if (position ==1) return  new FavouriteFragment();
            return new AllCoinFargment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }
}
