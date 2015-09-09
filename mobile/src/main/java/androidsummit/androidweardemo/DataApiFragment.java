package androidsummit.androidweardemo;

import android.os.Bundle;

public class DataApiFragment extends WearApiTabFragment {
    public static DataApiFragment newInstance() {
        return new DataApiFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
