package com.hoyouly.baidunews.util;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientUtil {

    public static String getRss_old(String webUrl) {
        if (TextUtils.isEmpty(webUrl)) {
            return null;
        }
        String rss = null;
        byte[] buffer = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null;
        byte[] bytesResult = null;
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(webUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(20000);
            urlConnection.setReadTimeout(50000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "text/html; charset=gbk");
            if (urlConnection.getResponseCode() == 200) {
                bis = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
                baos = new ByteArrayOutputStream();//字节数组
                bos = new BufferedOutputStream(baos, 8 * 1024);
                buffer = new byte[1024];
                int len = 0;
                while ((len = bis.read(buffer, 0, 1024)) != -1) {
                    bos.write(buffer, 0, len);
                }
                bos.flush();
                baos.flush();
                bytesResult = baos.toByteArray();
                rss = new String(bytesResult, "gbk");
            }
        } catch (java.net.SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                    bos = null;
                }
                if (baos != null) {
                    baos.close();
                    baos = null;
                }
                if (bis != null) {
                    bis.close();
                    bis = null;
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                    urlConnection = null;
                }
                buffer = null;
                bytesResult = null;
                url = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rss;
    }


}
