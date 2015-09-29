package androidsummit.androidweardemo;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.ChannelApi;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WearApiFragment apiFragment;

    protected GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_layout);

        if (savedInstanceState == null) {
            apiFragment = WearApiFragment.newInstance();
            addContentFragment(apiFragment, WearApiFragment.TAG);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    protected void addContentFragment(Fragment content, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, content, tag);
        if (ft.isAddToBackStackAllowed() && fm.getFragments() != null) {
            ft.addToBackStack(tag);
        }
        ft.commit();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String path = messageEvent.getPath();

        final Fragment fragment = apiFragment.getPagerAdapter().getRegisteredFragment(apiFragment.getViewPager
            ().getCurrentItem());
        if (fragment instanceof MessageApiFragment) {

            if (path.equalsIgnoreCase("")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MessageApiFragment) fragment).setMessageText("OWWW!!!!!");
                    }
                });
            } else if (path.equalsIgnoreCase("")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MessageApiFragment) fragment).setMessageText("Hello World!");
                    }
                });
            }
        }
    }
}
