package com.example.tomas.helloworld_v1;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Tomas on 6. 10. 2016.
 */




public class JSONTask extends AsyncTask<String, String, Boolean> {
    @Override
    protected Boolean doInBackground(String... params) {
        return null;
    }
    /*
    private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

    InputStream inputStream = null;
    String result = "";

    JSONObject jsonObject; // downloaded JSON file
    URL url = null;
    InputStream inputStream;

    //new JSONTask().execute(stringUrl);


    @Override
    protected void onPreExceute() throws MalformedURLException {
        super.onPreExecute();
        // define URL
        url = new URL("http://www.mocky.io/v2/57f65fdd270000e8171b115a");
        // create new JSON obj
        jsonObject = new JSONObject();

        //waitDialog = ProgressDialog.show(AsyncTaskActivity.this, "Please wait", "Downloading Image");
    }

    @Override
    protected void onProgresUpdate(Integer... value){
        return null;
    }

    @Override
    protected void onPostExceute(Boolean s){
        super.onPostExecute(s);

        //waitDialog.dismiss();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //inputStream.close();
        }
        return null;
    }*/
}
