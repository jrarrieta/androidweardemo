package androidsummit.androidweardemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class WearApiViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = WearApiViewPagerAdapter.class.getSimpleName();

    public static final int NUMBER_OF_VIEWS = 3;

    private FragmentManager fragmentManager;

    private ArrayList<Fragment> mFragments = new ArrayList<>();

    public WearApiViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
    }

    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case 0:
                mFragments.add(MessageApiFragment.newInstance());
                break;
            case 1:
                mFragments.add(DataApiFragment.newInstance());
                break;
            case 2:
                mFragments.add(ChannelApiFragment.newInstance());
                break;
            default:
                return null;
        }

        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return NUMBER_OF_VIEWS;
    }

    public int getIconDefaultAtPosition(int position) {
        int icon;
        switch (position) {
            case 0:
                icon = R.mipmap.icon_settings_default;
                break;
            case 1:
                icon = R.mipmap.icon_edit_default;
                break;
            case 2:
                icon = R.mipmap.icon_memo_default;
                break;
            default:
                icon = 0;
        }

        return icon;
    }

    public int getIconHighLightAtPosition(int position) {
        int icon;
        switch (position) {
            case 0:
                icon = R.mipmap.icon_settings_white;
                break;
            case 1:
                icon = R.mipmap.icon_edit_white;
                break;
            case 2:
                icon = R.mipmap.icon_memo_white;
                break;
            default:
                icon = 0;
        }

        return icon;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle;
        switch (position) {
            case 0:
                pageTitle = "Message API";
                break;
            case 1:
                pageTitle = "Data API";
                break;
            case 2:
                pageTitle = "Channel API";
                break;
            default:
                pageTitle = null;
        }

        return pageTitle;
    }

    public Fragment getRegisteredFragment(int position) {
        return mFragments.get(position);
    }
}
