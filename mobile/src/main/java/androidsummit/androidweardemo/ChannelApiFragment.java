package androidsummit.androidweardemo;

import android.os.Bundle;

public class ChannelApiFragment extends WearApiTabFragment {

    public static ChannelApiFragment newInstance() {
        return new ChannelApiFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
