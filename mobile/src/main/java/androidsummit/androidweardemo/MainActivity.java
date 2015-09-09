package androidsummit.androidweardemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private WearApiFragment apiFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWearableService();
        setContentView(R.layout.content_layout);

        if (savedInstanceState == null) {
            apiFragment = WearApiFragment.newInstance();
            addContentFragment(apiFragment, WearApiFragment.TAG);
        }
    }

    private void setWearableService() {
        Intent intent = new Intent(this, DataLayerListenerService.class);
        startService(intent);
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
}
