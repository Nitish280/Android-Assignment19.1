package com.example.lenovo.android191;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by lenovo on 9/2/2017.
 */

public class HttpHandler {

    private static final String TAG= HttpHandler.class.getSimpleName();

    public HttpHandler(){

    }
    public String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            InputStream inputStream=new BufferedInputStream(httpURLConnection.getInputStream());
            response = convertStreamToString(inputStream);
        }catch (MalformedURLException e){
            Log.e(TAG,"MalformedURLException: " + e.getMessage());
        }catch (ProtocolException e){

            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;

    }

    private String convertStreamToString(InputStream inStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));
        StringBuilder stringBuilder = new StringBuilder();

        String str;
        try {
            while ((str = bufferedReader.readLine()) != null) {
                stringBuilder.append(str).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }


}
