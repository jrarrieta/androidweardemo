package androidsummit.androidweardemo;

import com.google.android.gms.common.api.GoogleApiClient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidsummit.androidweardemo.utils.GoogleApiUtils;

public class MessageApiFragment extends WearApiTabFragment {

    public final static String TAG = MessageApiFragment.class.getSimpleName();

    public static MessageApiFragment newInstance() {
        return new MessageApiFragment();
    }

    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.message_api_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view == null) {
            return;
        }

        textView = (TextView) view.findViewById(R.id.message_api_text);

        Button pokeButton = (Button) view.findViewById(R.id.message_api_button);

        pokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleApiUtils.sendMessageToAllNodes(getGoogleApiClient(), "");
            }
        });

        Button clearButton = (Button) view.findViewById(R.id.clear_message_api_button);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleApiUtils.sendMessageToAllNodes(getGoogleApiClient(), "");
            }
        });
    }

    public void setMessageText(String text) {
        textView.setText(text);
    }

    private GoogleApiClient getGoogleApiClient() {
        return ((MainActivity) getActivity()).getGoogleApiClient();
    }
}
