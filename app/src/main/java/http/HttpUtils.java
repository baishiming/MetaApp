package http;

import android.util.Log;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

/**
 * @author bsm
 * @name MetaApp
 * @class name：http
 * @class describe
 * @time 2019/12/17 16:23
 */
public class HttpUtils {

    private static String url = "http://image.so.com/j";

    public static void get(Map map,HttpCallBack callBack){
        get(url,map,callBack);
    }

    public static void get(String url, Map<String,Object> map, final HttpCallBack callBack){
        String mapString = MapToString.transMapToString(map);
        final StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append("?");
        sb.append(mapString);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String getString = sb.toString();
                try {
                    reload(getString,callBack);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public static void reload(String urlStr,HttpCallBack callBack) throws Exception {
        String result = null;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //信任https站点
        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection conns = (HttpsURLConnection) conn;
            conns.setHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext sslCtx = SSLContext.getInstance("SSL");
            sslCtx.init(null, null,null);
            if (sslCtx != null) {
                conns.setSSLSocketFactory(sslCtx.getSocketFactory());
            }
        }
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(50000);
        conn.connect();

        // 302, 301
        if (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP
                || conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM){
            reload(conn.getHeaderField("location"),callBack);
        } else if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            //得到响应流
            InputStream inputStream = conn.getInputStream();
            //将响应流转换成字符串
            result = is2String(inputStream);//将流转换为字符串。
            Log.d("reload","result============="+result);
            conn.disconnect();
            callBack.onCallBack(result);
        }
    }



    public static String is2String(InputStream is) throws IOException {

        //连接后，创建一个输入流来读取response
        BufferedReader bufferedReader = null;
        String response = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is,"utf-8"));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            //每次读取一行，若非空则添加至 stringBuilder
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            //读取所有的数据后，赋值给 response
            response = stringBuilder.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
    }

}
