package androidsummit.androidweardemo;

import android.os.Bundle;

public class MessageApiFragment extends WearApiTabFragment {
    public static MessageApiFragment newInstance() {
        return new MessageApiFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
