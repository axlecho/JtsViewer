package com.axlecho.jtsviewer.network;

import android.content.Context;

import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class JtsNetworkManager {
    private static final String TAG = JtsNetworkManager.class.getSimpleName();
    private static final int DEFAULT_TIMEOUT = 15;
    public static final String WEBPAGE_CONTENT_KEY = "webpage_content";

    private static JtsNetworkManager instance;
    private OkHttpClient client;

    private JtsNetworkManager(Context context) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        clientBuilder.addInterceptor(new LoggingInterceptor());
        clientBuilder.networkInterceptors().add(new ProgressInterceptor());
        clientBuilder.cookieJar(new JtsCookieJar(context.getApplicationContext()));
        client = clientBuilder.build();
    }

    public static JtsNetworkManager getInstance(Context context) {
        if (instance == null) {
            synchronized (JtsNetworkManager.class) {
                if (instance == null) {
                    instance = new JtsNetworkManager(context);
                }
            }
        }
        return instance;
    }

    public void get(String url, JtsBaseAction action) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new JtsNetworkCallback(action));
    }

    public void post(String url, RequestBody data, JtsBaseAction action) {
        Request request = new Request.Builder()
                .url(url)
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(data)
                .build();

        client.newCall(request).enqueue(new JtsNetworkCallback(action));
    }

    public String download(String url, String path) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();

        if (response.code() != 200) {
            JtsViewerLog.e(TAG, "[download] failed " + response.code());
            JtsViewerLog.e(TAG, "[download] body " + response.body().string());
            throw new Exception("服务器不想理你并向你扔了" + response.code());
        }

        InputStream is = response.body().byteStream();
        List<String> contentDispoition = response.headers("Content-Disposition");
        String fileName = null;
        for (String s : contentDispoition) {
            List<String> fileNameGroup = JtsTextUnitls.findByPattern(s, "(?<=filename=\").*?(?=\")");
            if (fileNameGroup.size() > 0) {
                fileName = fileNameGroup.get(0);
                break;
            }
        }

        if (fileName == null) {
            fileName = JtsTextUnitls.getRandomUUID();
        }

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(path, fileName);
        BufferedInputStream input = new BufferedInputStream(is);
        OutputStream output = new FileOutputStream(file);

        byte[] data = new byte[1024];
        int count;
        while ((count = input.read(data)) != -1) {
            output.write(data, 0, count);
        }

        output.flush();
        output.close();
        input.close();
        response.body().close();
        return file.getName();
    }

    private class ProgressResponseBody extends ResponseBody {
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
                    return bytesRead;
                }
            };
        }
    }

    private class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, "Sending request " +
                    request.url() + " on " + request.headers());
            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            JtsViewerLog.d(JtsViewerLog.NETWORK_MODULE, TAG, String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }

    private class ProgressInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder().body(
                    new ProgressResponseBody(originalResponse.body())).build();
        }
    }
}
