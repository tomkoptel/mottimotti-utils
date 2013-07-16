package com.mottimotti.android.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.mottimotti.android.R;
import com.mottimotti.android.widget.PagerSlidingTabStrip;

public class PagerSlidingActivity extends SherlockFragmentActivity {
    private int mPosition = 0;
    private PagerSlidingTabStrip tabs;
    private ViewPager viewPager;
    private final String[] TITLES = { "Categories", "Home", "Top Paid", "Top Free", "Top Grossing", "Top New Paid",
            "Top New Free", "Trending" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_sliding_layout);
        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt("position");
        }
        afterViews();
    }

    @Override
    public void onContentChanged() {
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private void afterViews() {
        viewPager.setAdapter(new PagerSlidingAdapter());
        tabs.setOnPageChangeListener(new PageChangeListener());
        tabs.setViewPager(viewPager);
        viewPager.setCurrentItem(mPosition);
    }

    private class PagerSlidingAdapter extends FragmentPagerAdapter {
        public PagerSlidingAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return TestFragment.newInstance(TITLES[position]);
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }

    private class PageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            mPosition = position;
        }
    }
}
