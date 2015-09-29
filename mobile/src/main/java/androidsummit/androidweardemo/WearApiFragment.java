package androidsummit.androidweardemo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import androidsummit.androidweardemo.utils.GoogleApiUtils;
import androidsummit.androidweardemo.utils.ThreadUtils;

public class WearApiFragment extends android.support.v4.app.Fragment {

    public static final String TAG = WearApiFragment.class.getSimpleName();

    private static final int NUMBER_OF_TABS = 3;

    private WearApiViewPagerAdapter pagerAdapter;

    private ViewPager viewPager;

    public static WearApiFragment newInstance() {
        return new WearApiFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view == null) {
            return;
        }

        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        setupTabLayout(tabLayout);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        setupViewPager(toolbar, tabLayout, viewPager);
        setupTabLayoutListener(toolbar, tabLayout, viewPager);

        ThreadUtils.postOnUiThread(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle(pagerAdapter.getPageTitle(viewPager.getCurrentItem()));
                tabLayout.getTabAt(viewPager.getCurrentItem()).setIcon(getResources().getDrawable(
                    pagerAdapter.getIconHighLightAtPosition(viewPager.getCurrentItem())));
            }
        });
    }

    private void setupTabLayout(TabLayout tabLayout) {
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.mipmap.icon_settings_default)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.mipmap.icon_edit_default)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.mipmap.icon_memo_default)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void setupViewPager(Toolbar toolbar, TabLayout tabLayout, ViewPager viewPager) {
        viewPager.setOffscreenPageLimit(NUMBER_OF_TABS);

        pagerAdapter = new WearApiViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new WearApiFragmentPageChangeListener(tabLayout, toolbar,
            (WearApiViewPagerAdapter) viewPager.getAdapter()));
    }

    private void setupTabLayoutListener(final Toolbar toolbar, final TabLayout tabLayout, final ViewPager viewPager) {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                toolbar.setTitle(pagerAdapter.getPageTitle(tab.getPosition()));
                tabLayout.getTabAt(tab.getPosition()).setIcon(
                    getResources().getDrawable(pagerAdapter.getIconHighLightAtPosition(tab.getPosition())));
                switch (tab.getPosition()) {
                    case 0:
                        GoogleApiUtils.sendMessageToAllNodes(((MainActivity) getActivity()).getGoogleApiClient(),
                            "/show_message_api");
                        break;
                    case 1:
                        GoogleApiUtils.sendMessageToAllNodes(((MainActivity) getActivity()).getGoogleApiClient(),
                            "/show_data_api");
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).setIcon(
                    getResources().getDrawable(pagerAdapter.getIconDefaultAtPosition(tab.getPosition())));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public class WearApiFragmentPageChangeListener implements ViewPager.OnPageChangeListener {

        private final WeakReference<Toolbar> mToolbarRef;

        private final WeakReference<TabLayout> mTabLayoutRef;

        private final WeakReference<WearApiViewPagerAdapter> mPagerAdapterRef;

        private int mScrollState;

        public WearApiFragmentPageChangeListener(TabLayout tabLayout, Toolbar toolbar, WearApiViewPagerAdapter pagerAdapter) {
            this.mTabLayoutRef = new WeakReference(tabLayout);
            this.mToolbarRef = new WeakReference(toolbar);
            this.mPagerAdapterRef = new WeakReference(pagerAdapter);
        }

        public void onPageScrollStateChanged(int state) {
            this.mScrollState = state;
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            TabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null) {
                tabLayout.setScrollPosition(position, positionOffset, this.mScrollState == 1);
            }
        }

        public void onPageSelected(int position) {
            TabLayout tabLayout = mTabLayoutRef.get();
            WearApiViewPagerAdapter pagerAdapter = mPagerAdapterRef.get();
            if (tabLayout != null && pagerAdapter != null) {
                tabLayout.getTabAt(position).select();
                tabLayout.getTabAt(position).setIcon(getResources().getDrawable(pagerAdapter.getIconHighLightAtPosition(position)));
            }

            Toolbar toolbar = mToolbarRef.get();
            if (toolbar != null && pagerAdapter != null) {
                toolbar.setTitle(pagerAdapter.getPageTitle(position));
            }
        }
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public WearApiViewPagerAdapter getPagerAdapter() {
        return pagerAdapter;
    }
}
