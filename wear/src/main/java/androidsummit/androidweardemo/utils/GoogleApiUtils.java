package androidsummit.androidweardemo.utils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GoogleApiUtils {

    private static final String TAG = GoogleApiUtils.class.getSimpleName();

    /**
     * Puts the given data in the DataMap, triggering the onDataChanged method in any listening nodes.
     *
     * @param path the path for the PutDataMapRequest
     * @param key  the key for the DataMap
     * @param data the data to put in the DataMap for the given key
     */
    public static void postToDataMap(final GoogleApiClient googleApiClient,
        final String path,
        final String key,
        final Object data) {
        if (googleApiClient == null || TextUtils.isEmpty(path) || TextUtils.isEmpty(key)
            || data == null) {
            Log.e(TAG, "postToDataMap passed a null param");
            return;
        }

        try {
            PutDataMapRequest request = PutDataMapRequest.create(path);
            DataMap dataMap = request.getDataMap();

            if (data instanceof String) {
                dataMap.putString(key, (String) data);
            } else if (data instanceof Asset) {
                dataMap.putAsset(key, (Asset) data);
            } else {
                final String message = "Encountered unexpected data type";
                Log.e(TAG, message);

                throw new IllegalArgumentException(message);
            }

            dataMap.putLong(DataConstants.TIMESTAMP_KEY, System.currentTimeMillis());

            PutDataRequest dataRequest = request.asPutDataRequest();
            PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(googleApiClient, dataRequest);

            pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(final DataApi.DataItemResult result) {
                    if (result.getStatus().isSuccess()) {
                        Log.d(TAG, "Data item set: " + result.getDataItem().getUri());
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Sends an RPC using Google API's
     *
     * @param googleApiClient the API client to use for sending RPC's
     * @param messagePath     the path of the message
     */
    public static void sendMessageToAllNodes(final GoogleApiClient googleApiClient,
        final String messagePath) {
        sendMessageToAllNodes(googleApiClient, messagePath, null, null);
    }

    /**
     * Sends an RPC using Google API's
     *
     * @param googleApiClient the API client to use for sending RPC's
     * @param messagePath     the path of the message
     * @param resultCallback  the callback for the request
     */
    public static void sendMessageToAllNodes(final GoogleApiClient googleApiClient,
        final String messagePath,
        final ResultCallback<MessageApi.SendMessageResult> resultCallback) {
        sendMessageToAllNodes(googleApiClient, messagePath, resultCallback, null);
    }

    /**
     * Sends an RPC using Google API's
     *
     * @param googleApiClient the API client to use for sending RPC's
     * @param messagePath     the path of the message
     * @param payload         the data to send with the message
     */
    public static void sendMessageToAllNodes(final GoogleApiClient googleApiClient,
        final String messagePath,
        final byte[] payload) {
        sendMessageToAllNodes(googleApiClient, messagePath, null, payload);
    }

    /**
     * Sends an RPC using Google API's
     *
     * @param googleApiClient the API client to use for sending RPC's
     * @param messagePath     the path of the message
     * @param resultCallback  a callback that gets called after completion of the RPC
     * @param payload         the data to send with the message
     */
    public static void sendMessageToAllNodes(final GoogleApiClient googleApiClient,
        final String messagePath,
        final ResultCallback<MessageApi.SendMessageResult> resultCallback,
        final byte[] payload) {
        if (googleApiClient == null || TextUtils.isEmpty(messagePath)) {
            Log.e(TAG, "sendMessageToAllNodes passed a null param");
            return;
        }

        PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient);
        nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult result) {
                List<Node> nodes = result.getNodes();

                // send the RPC to all connected nodes
                for (Node node : nodes) {
                    PendingResult<MessageApi.SendMessageResult> messageResult =
                        Wearable.MessageApi.sendMessage(googleApiClient, node.getId(),
                            messagePath, payload);

                    if (resultCallback == null) {
                        messageResult.setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                            @Override
                            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                Status status = sendMessageResult.getStatus();

                                if (!status.isSuccess()) {
                                    Log.d(TAG, "Failed to send message: " + messagePath);
                                }
                            }
                        });
                    } else {
                        messageResult.setResultCallback(resultCallback);
                    }
                }
            }
        });
    }

    /**
     * Creates a Google API Asset object from a bitmap
     *
     * @param bitmap the bitmap for which to create the Asset
     */
    public static Asset createAssetFromBitmap(final Bitmap bitmap) {
        if (bitmap == null) {
            Log.e(TAG, "createAssetFromBitmap passed a null bitmap");
            return null;
        }

        ByteArrayOutputStream byteStream = null;

        try {
            byteStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            return Asset.createFromBytes(byteStream.toByteArray());
        } finally {
            if (byteStream != null) {
                try {
                    byteStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Creates a bitmap from a Google API Asset
     *
     * @param googleApiClient the client to use for Google API actions
     * @param asset           the asset for which to load a bitmap
     */
    public static Bitmap createBitmapFromAsset(final GoogleApiClient googleApiClient,
        final Asset asset) {
        if (googleApiClient == null || asset == null) {
            Log.e(TAG, "createBitmapFromAsset passed a null param");
            return null;
        }

        ConnectionResult result = googleApiClient.blockingConnect(100, TimeUnit.MILLISECONDS);

        if (!result.isSuccess()) {
            return null;
        }

        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = Wearable.DataApi
            .getFdForAsset(googleApiClient, asset)
            .await()
            .getInputStream();

        googleApiClient.disconnect();

        if (assetInputStream == null) {
            Log.w(TAG, "Requested an unknown Asset.");
            return null;
        }

        return BitmapFactory.decodeStream(assetInputStream);
    }

    //TODO: Do the core libs have a method for this?

    /**
     * Saves a given bitmap to the given filename
     *
     * @param bitmap   the bitmap to save to a file
     * @param filename the filename of the saved bitmap
     * @param context  the context used to open a file output
     */
    public static void saveBitmapToFile(final Bitmap bitmap,
        final String filename,
        final Context context) {
        if (bitmap == null || TextUtils.isEmpty(filename) || context == null) {
            Log.e(TAG, "saveBitmapToFile passed a null param");
            return;
        }

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                }
            }

            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    public static List<String> getStringListFromDataMap(final DataMap dataMap, final String key) {
        final byte[] byteArray = dataMap.getByteArray(key);
        final Parcel parcel = Parcel.obtain();

        parcel.unmarshall(byteArray, 0, byteArray.length);
        parcel.setDataPosition(0);

        List<String> dataList = new ArrayList<>();

        parcel.readStringList(dataList);

        parcel.recycle();

        return dataList;
    }
}
