package androidsummit.androidweardemo;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidsummit.androidweardemo.utils.GoogleApiUtils;

public class DataApiFragment extends WearApiTabFragment {

    public static DataApiFragment newInstance() {
        return new DataApiFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.data_api_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view == null) {
            return;
        }

        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        final ImageAdapter imageAdapter = new ImageAdapter(getActivity());
        gridview.setAdapter(imageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Asset asset;
                asset =
                    GoogleApiUtils.createAssetFromBitmap(BitmapFactory.decodeResource(getResources(), imageAdapter.getImageId(position)));
                GoogleApiUtils.postToDataMap(getGoogleApiClient(), "/data_image", "data_image", asset);
            }
        });
    }

    public class ImageAdapter extends BaseAdapter {

        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(350, 500));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        // references to our images
        private Integer[] mThumbIds = {
            R.mipmap.straight_outta_compton_ver8, R.mipmap.transporter_refueled_ver3,
            R.mipmap.mission_impossible__rogue_nation, R.mipmap.no_escape,
            R.mipmap.sinister_two, R.mipmap.inside_out_ver13
        };

        public int getImageId(int position) {
            return mThumbIds[position];
        }
    }

    private GoogleApiClient getGoogleApiClient() {
        return ((MainActivity) getActivity()).getGoogleApiClient();
    }
}