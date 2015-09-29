package androidsummit.androidweardemo;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.ChannelApi;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidsummit.androidweardemo.utils.GoogleApiUtils;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, MessageApi.MessageListener,
    DataApi.DataListener, ChannelApi.ChannelListener {

    private TextView mTextView;

    protected GoogleApiClient mGoogleApiClient;

    private String helloText;

    private LinearLayout messageLayout, dataLayout;

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                messageLayout = (LinearLayout) stub.findViewById(R.id.message_api);
                dataLayout = (LinearLayout) stub.findViewById(R.id.data_api);
                mTextView = (TextView) stub.findViewById(R.id.text);
                mImageView = (ImageView) stub.findViewById(R.id.data_image);
                helloText = mTextView.getText().toString();

                Button pokePhone = (Button) stub.findViewById(R.id.poke_phone);
                pokePhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GoogleApiUtils.sendMessageToAllNodes(mGoogleApiClient, "/poke_phone");
                    }
                });

                Button clearPhone = (Button) stub.findViewById(R.id.clear_phone);
                clearPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GoogleApiUtils.sendMessageToAllNodes(mGoogleApiClient, "/clear_phone");
                    }
                });

            }
        });

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
        for (DataEvent event : dataEventBuffer) {
            switch (event.getType()) {
                case DataEvent.TYPE_CHANGED:
                    DataItem dataItem = event.getDataItem();
                    final String path = dataItem.getUri().getPath();

                    if (path.equalsIgnoreCase("")) {
                        DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                        final Asset userImageAsset = dataMapItem.getDataMap().getAsset("");

                        final Bitmap bitmap = GoogleApiUtils.createBitmapFromAsset(mGoogleApiClient, userImageAsset);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mImageView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
                            }
                        });
                    }
                case DataEvent.TYPE_DELETED:
                    break;
            }
        }

        mGoogleApiClient.disconnect();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String path = messageEvent.getPath();

        if (path.equalsIgnoreCase("/show_message_api")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dataLayout.setVisibility(View.GONE);
                    messageLayout.setVisibility(View.VISIBLE);
                }
            });
        } else if (path.equalsIgnoreCase("/show_data_api")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageLayout.setVisibility(View.GONE);
                    dataLayout.setVisibility(View.VISIBLE);
                }
            });
        } else if (path.equalsIgnoreCase("")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText("OWWW!!");
                }
            });
        } else if (path.equalsIgnoreCase("")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText(helloText);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
