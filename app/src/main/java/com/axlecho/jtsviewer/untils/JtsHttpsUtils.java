package com.axlecho.jtsviewer.untils;

import com.axlecho.jtsviewer.network.JtsServer;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class JtsHttpsUtils {

    private TrustAllManager trustManager;
    private volatile static JtsHttpsUtils singleton;

    public static JtsHttpsUtils getSingleton() {
        if (singleton == null) {
            synchronized (JtsServer.class) {
                if (singleton == null) {
                    singleton = new JtsHttpsUtils();
                }
            }
        }
        return singleton;
    }

    private JtsHttpsUtils() {
        trustManager = new TrustAllManager();
    }

    public SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return ssfFactory;
    }

    public HostnameVerifier createHostnameVerifier() {
        return new TrustAllHostnameVerifier();
    }

    public TrustAllManager getTrustManager() {
        return trustManager;
    }

    private class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}