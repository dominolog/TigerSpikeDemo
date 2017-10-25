package pl.cubesoft.tigerspiketest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pl.cubesoft.tigerspiketest.utils.CollectionUtils;
import rx.Observable;


public class DownloaderManager {

    private final OnGetHeadersCallback onGetHeadersCallback;

    enum State {
        COMPLETED,
        ERROR
    }

    public class DownloadBroadcastReceiver extends BroadcastReceiver {
        public DownloadBroadcastReceiver() {

        }
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                //Show a notification
                final long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                final DownloadObject downloadObject = getDownloadObject(downloadId);
                if (downloadObject != null) {

                    final Query q = new Query();
                    q.setFilterById(downloadId);
                    final DownloadManager downloadManager = (DownloadManager) context
                            .getSystemService(Context.DOWNLOAD_SERVICE);
                    Cursor c = downloadManager.query(q);
                    if (c.moveToFirst()) {
                        final int totalSizeBytes = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        final int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            downloadObject.setState(State.COMPLETED);
                        } else {
                            downloadObject.setState(State.ERROR);
                        }
                        downloadObject.setTotal(totalSizeBytes);
                        downloadObject.setDownloadedBytes(totalSizeBytes);
                        //downloadObject.setDownloadedFile(new File(localFile));
                        c.close();
                    }


                    notifyDownloadSuccess(downloadObject);
                }
            }
        }


    }

    private void notifyDownloadSuccess(DownloadObject downloadObject) {

    }

    public final class DownloadObject {

        private long totalBytes;
        private long downloadedBytes;
        private File downloadedFile;
        private Uri uri;
        private Map<String, String> headers;
        private Map<String, String> params;
        private long id;
        private Query query;
        private Request request;
        private State state;
        private int total;
        private Uri downloadUri;


        private void setId(long id) {
            this.id = id;
        }


        public long retryDownload() {
            downloadManager.remove(id);
            id = downloadManager.enqueue(request);

            return id;
        }


        public State getState() {
            return state;
        }

        public long getId() {
            return id;
        }

        public void setDownloadedBytes(int downloadedBytes) {
            this.downloadedBytes = downloadedBytes;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public void setDownloadedFile(File downloadedFile) {
            this.downloadedFile = downloadedFile;
        }

        public void setDownloadUri(Uri downloadUri) {
            this.downloadUri = downloadUri;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }

        public void setParams(Map<String, String> params) {
            this.params = params;
        }



        public void setQuery(Query query) {
            this.query = query;
        }

        public void setRequest(Request request) {
            this.request = request;
        }

        public void setState(State state) {
            this.state = state;
        }

        public void cancelDownload() {

        }
    }


    private final Context context;
    private final DownloadManager downloadManager;
    private final Handler handler = new Handler();
    private final Runnable refreshDownloadsCallback;
    public List<DownloadObject> downloadListFlat = new ArrayList<>();

    public interface OnGetHeadersCallback {
        Map<String, String> getHeaders();
    }

    public DownloaderManager(Context context, OnGetHeadersCallback onGetHeadersCallback) {
        this.context = context;
        this.onGetHeadersCallback = onGetHeadersCallback;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        refreshDownloadsCallback = new Runnable() {


            @Override
            public void run() {
                final Query q = new Query();
                final List<Long> idList = new ArrayList<Long>();
                for (final DownloadObject downloadObject : downloadListFlat) {
                    if (downloadObject.getState() != State.COMPLETED) {
                        idList.add(downloadObject.getId());
                    }
                }
                if (idList.size() > 0) {
                    final long[] idArray = new long[idList.size()];
                    for (int i = 0; i < idList.size(); ++i) {
                        idArray[i] = idList.get(i);
                    }
                    q.setFilterById(idArray);

                    Cursor cursor = null;
                    try {
                        cursor = downloadManager.query(q);

                        while (cursor.moveToNext()) {
                            final int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            final int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            final long id = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_ID));

                            final String localUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                            final DownloadObject downloadObject = getDownloadObject(id);
                            downloadObject.setDownloadedBytes(bytesDownloaded > 0 ? bytesDownloaded : 0);
                            downloadObject.setTotal(bytesTotal > 0 ? bytesTotal : 0);
                            if (localUri != null) {
                                downloadObject.setDownloadedFile(new File(localUri));
                            }
                        }


                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }

                    notifyDownloadProgress();
                    handler.postDelayed(refreshDownloadsCallback, 1000);
                }


            }
        };

        //context.registerReceiver(new DownloadBroadcastReceiver(), IntentFilter.create(DownloadManager.ACTION_DOWNLOAD_COMPLETE, null));
    }

    private void notifyDownloadProgress() {

    }

    public Observable<DownloadObject> submitDownload(Activity activity, String url, Map<String, String> headers, Map<String, String> params) {
        return new RxPermissions(activity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .concatMapDelayError(granted -> {
                    if (granted) {
                        Observable.fromCallable(() -> submitDownload(url, headers, params));
                    }
                    return Observable.empty();
                });
    }

    @SuppressLint("NewApi")
    private DownloadObject submitDownload(String url, Map<String, String> headers, Map<String, String> params) {

        // for videos, we use original URL
        Uri uri = Uri.parse(url);
        final String filename = url;
        if (params != null && !params.isEmpty()) {
            final Uri.Builder builder = uri.buildUpon();
            for (final Entry<String, String> param : params.entrySet()) {
                builder.appendQueryParameter(param.getKey(), param.getValue());

            }


            uri = builder.build();
        }

        final Request request = new Request(uri);
        // Restrict the types of networks over which this download may
        // proceed.
        int downloadFlags = Request.NETWORK_WIFI;

        request.setAllowedNetworkTypes(downloadFlags);
        // Set whether this download may proceed over a roaming
        // connection.
        request.setAllowedOverRoaming(false);
        // Set the title of this download, to be displayed in
        // notifications (if enabled).
        request.setTitle(filename);
        // Set a description of this download, to be displayed in
        // notifications (if enabled)
        request.setDescription(filename);
        // Set the local destination for the downloaded file to a path
        // within the application's external files directory

        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, filename);

        //final File file = getDownloadFile(setTitle, filename);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setVisibleInDownloadsUi(true);
        // request.setMimeType(mimeType);

        if (headers != null) {
            for (Entry<String, String> header : headers.entrySet()) {
                request.addRequestHeader(header.getKey(), header.getValue());
            }
        }

        for (Entry<String, String> header : onGetHeadersCallback.getHeaders().entrySet()) {
            request.addRequestHeader(header.getKey(), header.getValue());
        }


        final long id = downloadManager.enqueue(request);


        final Query q = new Query();
        q.setFilterById(id);
        /*final Cursor cursor = mDownloadManager.query(q);
        cursor.moveToFirst();
		int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
		String localFilename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
		cursor.close();
		*/

        final DownloadObject downloadObject = new DownloadObject();
        downloadObject.setDownloadUri(uri);
        downloadObject.setHeaders(headers);
        downloadObject.setParams(params);
        downloadObject.setId(id);
        //downloadObject.setDownloadedFile(file);
        downloadObject.setQuery(q);
        downloadObject.setRequest(request);


        //downloads.add(id);
        return downloadObject;


    }

    private File getDownloadFile(String filename) {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
    }

    private DownloadObject getDownloadObject(long downloadId) {
        for (DownloadObject downloadObject : this.downloadListFlat) {
            if (downloadId == downloadObject.getId()) {
                return downloadObject;
            }
        }
        return null;
    }

    public Observable<List<DownloadObject>> submitDownloads(Activity activity, Collection<String> urls,
                                                            Map<String, String> headers, Map<String, String> params) {
        return new RxPermissions(activity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .concatMapDelayError(granted -> {
                    if (granted) {
                        return submitDownloads(urls, headers, params);
                    }
                    return Observable.empty();
                });
    }

    private Observable<List<DownloadObject>> submitDownloads(Collection<String> urls,
                                                             Map<String, String> headers, Map<String, String> params) {

        return Observable.fromCallable(() -> {
            final List<DownloadObject> result = new ArrayList<>();

            if (!CollectionUtils.isEmpty(urls)) {
                for (final String url : urls) {
                    final DownloadObject downloadObject =
                            submitDownload(url, headers, params);
                    result.add(downloadObject);
                }

                //result = submitDownloads(result);
                addDownloadObjects(result);
                notifyDownloadStarted(result);

                if (!handler.hasMessages(0)) {
                    handler.post(refreshDownloadsCallback);
                }


            }
            return result;
        });
    }

    private void notifyDownloadStarted(List<DownloadObject> result) {

    }

    private void addDownloadObjects(List<DownloadObject> downloadObjects) {
        downloadListFlat.addAll(downloadObjects);
    }


    public boolean cancelDownload(DownloadObject downloadObject) {

        if (downloadObject.getState() != State.COMPLETED) {
            downloadObject.cancelDownload();
        }
        boolean result = removeDownloadObject(downloadObject);

        if (downloadListFlat.isEmpty()) {
            handler.removeCallbacks(refreshDownloadsCallback);
        }

        return result;
    }

    private boolean removeDownloadObject(DownloadObject downloadObject) {
        return downloadListFlat.remove(downloadObject);
    }

    private boolean removeDownloadObjects(List<DownloadObject> downloadObjects) {
        return downloadListFlat.removeAll(downloadObjects);
    }

    public boolean cancelDownloads(List<DownloadObject> downloadObjects) {
        for (final DownloadObject downloadObject : downloadObjects) {
            downloadObject.cancelDownload();
        }
        boolean result = removeDownloadObjects(downloadObjects);

        if (downloadListFlat.isEmpty()) {
            handler.removeCallbacks(refreshDownloadsCallback);
        }
        return result;
    }


    public boolean cancelAll() {
        for (final DownloadObject downloadObject : downloadListFlat) {
            downloadObject.cancelDownload();
        }
        downloadListFlat.clear();


        if (downloadListFlat.isEmpty()) {
            handler.removeCallbacks(refreshDownloadsCallback);
        }

        notifyDownloadProgress();
        return true;
    }

    public void retryDownload(DownloadObject downloadObject) {
        if (downloadObject.getState() == State.ERROR) {
            final long oldId = downloadObject.getId();
            final long id = downloadObject.retryDownload();

            if (!handler.hasMessages(0)) {
                handler.post(refreshDownloadsCallback);
            }
        }
        //submitDownload((DownloadObjectImpl)downloadObject);
    }


}
