package com.axlecho.jtsviewer.action.download;

import com.axlecho.jtsviewer.untils.JtsTextUnitls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class JtsDownloadFunction implements Function<Response<ResponseBody>, String> {
    private static final String TAG = JtsDownloadFunction.class.getSimpleName();
    private String path;

    public JtsDownloadFunction(String path) {
        this.path = path;
    }

    @Override
    public String apply(Response<ResponseBody> res) throws Exception {
        boolean result = false;
        File dir = new File(path);
        if (!dir.exists()) {
            result = dir.mkdirs();
        }
        if (!result) {
            return null;
        }

        File output = new File(path, JtsTextUnitls.getFileNameFromResponse(res));
        result = writeResponseBodyToDisk(res.body(), output);
        if (!result) {
            return null;
        }

        return output.getAbsolutePath();
    }

    // refer to https://www.jianshu.com/p/92bb85fc07e8
    private boolean writeResponseBodyToDisk(ResponseBody body, File output) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        byte[] fileReader = new byte[4096];
        long fileSize = body.contentLength();
        long fileSizeDownloaded = 0;
        inputStream = body.byteStream();
        outputStream = new FileOutputStream(output);
        while (true) {
            int read = inputStream.read(fileReader);
            if (read == -1) {
                break;
            }
            outputStream.write(fileReader, 0, read);
            fileSizeDownloaded += read;
        }
        outputStream.flush();
        return true;
    }
}
