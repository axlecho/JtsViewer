package com.axlecho.jtsviewer.network.download;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.axlecho.jtsviewer.network.JtsCookieManager;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class DownloadTask extends AsyncTask<Void, Long, String> {
    private static final String TAG = "DownloadTask";
    private static final int DEFAULT_TIMEOUT = 15;

    private String mPath = Environment.getExternalStorageDirectory().getPath();
    private String mUrl;
    private String mFileName;
    private Context mContext;
    private File mFile;
    private long gid;

    private List<DownloadListener> mListeners = new ArrayList<>();

    public DownloadTask(Context context, String url, long gid) {
        mUrl = url;
        mContext = context;
        mFileName = getFileNameByUUID();
        this.gid = gid;
    }

    public void setPath(String path) {
        mPath = path;
        mPath = mPath + File.separator + gid;

    }

    public void setDownloadListener(DownloadListener listener) {
        if (listener == null) {
            JtsViewerLog.e(TAG, "[setDownloadListener] listener is null");
            return;
        }
        mListeners.add(listener);
    }

    public void removeDownloadListener(DownloadListener listener) {
        mListeners.remove(listener);
    }

    public void cancel() {

    }

    private void download(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", JtsCookieManager.getInstance(mContext).getCookie())
                .build();
        Response response = getClient().newCall(request).execute();

        if (response.code() != 200) {
            JtsViewerLog.e(TAG, "[download] failed " + response.code());
            JtsViewerLog.e(TAG, "[download] body " + response.body().string());
            throw new Exception("服务器不想理你并向你扔了" + response.code());
        }

        InputStream is = response.body().byteStream();

        File dir = new File(mPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        BufferedInputStream input = new BufferedInputStream(is);
        OutputStream output = new FileOutputStream(mFile);

        byte[] data = new byte[1024];
        int count;
        while ((count = input.read(data)) != -1) {
            output.write(data, 0, count);
        }

        output.flush();
        output.close();
        input.close();
        response.body().close();
    }

    private OkHttpClient getClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okHttpClientBuilder.networkInterceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(
                        new ProgressResponseBody(originalResponse.body())).build();

            }
        });

        return okHttpClientBuilder.build();
    }

    class ProgressResponseBody extends ResponseBody {
        private final ResponseBody responseBody;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody) {
            this.responseBody = responseBody;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }


        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    onProgressUpdate(totalBytesRead);
                    return bytesRead;
                }
            };
        }
    }

    private String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private String getFileNameByUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    protected void onPreExecute() {
        for (DownloadListener listener : mListeners) {
            listener.onStart(-1);
        }
        mFile = new File(mPath, mFileName);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            download(mUrl);
        } catch (Exception e) {
            e.printStackTrace();
            cancel(true);
            return e.getMessage();
        }
        return mFile.getAbsolutePath();
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        long progress = values[0];
        if (progress < 0) {
            for (DownloadListener listener : mListeners) {
                listener.onStart(-progress);
            }
        } else {
            for (DownloadListener listener : mListeners) {
                listener.onProgress(progress);
            }
        }
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        for (DownloadListener listener : mListeners) {
            listener.onFinish(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected void onCancelled(String msg) {
        for (DownloadListener listener : mListeners) {
            listener.onError(msg);
        }
        super.onCancelled(msg);
    }

    public String getUrl() {
        return mUrl;
    }

    public String getFileName() {
        return mFile.getAbsolutePath();
    }

}
