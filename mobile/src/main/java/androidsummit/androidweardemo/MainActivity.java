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

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, MessageApi.MessageListener,
    DataApi.DataListener, ChannelApi.ChannelListener {

    private WearApiFragment apiFragment;

    protected GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setWearableService();
        setContentView(R.layout.content_layout);

        if (savedInstanceState == null) {
            apiFragment = WearApiFragment.newInstance();
            addContentFragment(apiFragment, WearApiFragment.TAG);
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Wearable.API)
            .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        Wearable.MessageApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
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

    @Override
    public void onChannelOpened(Channel channel) {

    }

    @Override
    public void onChannelClosed(Channel channel, int i, int i1) {

    }

    @Override
    public void onInputClosed(Channel channel, int i, int i1) {

    }

    @Override
    public void onOutputClosed(Channel channel, int i, int i1) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
