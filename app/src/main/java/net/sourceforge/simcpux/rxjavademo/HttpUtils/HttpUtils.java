package net.sourceforge.simcpux.rxjavademo.HttpUtils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bluesky on 16/4/20.
 */
public class HttpUtils {
    public static InputStream getInputStream(String path){
        try {
            URL url =new URL(path);
           HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            return connection.getInputStream();
           } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getDataByInputStream(InputStream inputStream){
        Log.e("====", "==is==" + inputStream);
        // TODO Auto-generated method stub
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }
                result = new String(outputStream.toByteArray(), "utf-8");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }
    public static String getDataByInputStream1(InputStream inputStream){
        Log.e("====", "==is==" + inputStream);
        BufferedReader reader =new BufferedReader(new InputStreamReader(inputStream));
        String line="";
        String result ="";
        try{
        while ((line = reader.readLine())!=null){
            result+=line;
        }
            inputStream.close();
        }catch (Exception e){
            e.getMessage();
        }

        return result;
    }
}
