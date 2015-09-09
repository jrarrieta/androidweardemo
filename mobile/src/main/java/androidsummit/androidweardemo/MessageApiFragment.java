package androidsummit.androidweardemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidsummit.androidweardemo.utils.GoogleApiUtils;

public class MessageApiFragment extends WearApiTabFragment {
    public static MessageApiFragment newInstance() {
        return new MessageApiFragment();
    }

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

        Button pokeButton = (Button) view.findViewById(R.id.message_api_button);

        pokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleApiUtils.sendMessageToAllNodes(((MainActivity) getActivity()).getGoogleApiClient(),
                    "/poke_watch");
            }
        });
    }
}
