package com.example.tomas.helloworld_v1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.HttpResponseCache;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MyMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

   // public static final String CONTACT_ID_EXTRA = "com.example.tomas.helloworld_v1.Contact Identifier";
    public static final String CONTACT_NAME_EXTRA = "com.example.tomas.helloworld_v1.CONTACT Name";
    public static final String CONTACT_SURNAME_EXTRA = "com.example.tomas.helloworld_v1.CONTACT Surname";
    public static final String CONTACT_IMGURL_EXTRA = "com.example.tomas.helloworld_v1.CONTACT Image";
    public static final String CONTACT_PHONE_EXTRA = "com.example.tomas.helloworld_v1.CONTACT Phone";
    public static final String CONTACT_EMAIL_EXTRA = "com.example.tomas.helloworld_v1.CONTACT Email";
    public static final String CONTACT_NOTES_EXTRA = "com.example.tomas.helloworld_v1.CONTACT Notes";

    // Shared pref storage
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String MyKEYS = "MyKeys";

    // Json Contact list online demo file //private static String url = "http://api.androidhive.info/contacts/";
    // my new jason file link: http://www.mocky.io/v2/57f93fd80f00009f175a7ced

    private static Context mContext;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get context
        mContext = getApplicationContext();

        setContentView(R.layout.activity_my_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*        // GET orientation for correct layout
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                setContentView(R.layout.aportrait);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                setContentView(R.layout.alandscape);
                break;
        }
*/
        // floating button removed

        //Drawer removed

        // Turn on response cache for profile images
        //enableHttpResponseCache();
    }

    // Enable cache for contact images
    private void enableHttpResponseCache() {
        try {
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            File httpCacheDir = new File(getCacheDir(), "http");
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
        } catch (Exception httpResponseCacheNotAvailable) {
            //Log.d(TAG, "HTTP response cache is unavailable.");
            Log.d("TAG", "HTTP response cache is unavailable.");
        }
    }

    protected void onStop() {
        super.onStop();
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    // Button main page - handler "Download Data"
    public void downloadButtonHandler (View v) {

        // Check internet connection
        boolean isInternet = isNetworkAvailable(); //hardcode "true" for dev mode

        // Display info Toast and continue
        if (isInternet) {

            // delay
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    // Download Json, parse, save to SP and continue
                    new MyAsyncTask().execute();
                }
            }, 1500);

        } else {
            // Display toast - no net access
            Toast myToast = Toast.makeText(
                    getApplicationContext(),
                    "Please get internet access!", //+isInternet
                    Toast.LENGTH_LONG);
            myToast.show();

        }
    }
    // Button main page - handler "Cache Clear"
    public void clearCacheButtonHandler (View v) {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        Log.d("ButtonOnMainPage:", "CacheClearPressed"+cache);
//        if (cache != null) { // NOT SURE ABOUT THIS bit
//            cache.flush();
            Toast myToast = Toast.makeText(
                    getApplicationContext(),
                    "CachedOut!",
                    Toast.LENGTH_LONG);
            myToast.show();
//        }

    }

    // My async JSON Download START
    class MyAsyncTask extends AsyncTask<String, String, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(MyMainActivity.this);

        URL url = null;
        InputStream inputStream = null;
        String result = "";

        protected void onPreExecute() {

            Log.e("Main onPreExecute", "active");

            try {
                url = new URL("http://www.mocky.io/v2/57f93fd80f00009f175a7ced");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            progressDialog.setMessage("Downloading Json data");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    MyAsyncTask.this.cancel(true);
                }
            });
        }

        @Override
        protected Void doInBackground(String... params) {

            //ArrayList<String> param = new ArrayList<String>();
            HttpURLConnection urlConnection = null;

            try {
                // Set up URL post
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();

            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncodingExp", e1.toString());
                e1.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }
            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

            } catch (Exception e) {
                Log.e("StringBuild&BufferedRdr", "Error converting result " + e.toString());
            }

            return null;
        }

        protected void onPostExecute(Void v) {
            // Shared Preferences setup
            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            // data to be saved - dataKeys made of IDs, data other values
            String dataKeys = "";
            String data = "";

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray contactsArr = new JSONArray(jsonObject.getString("contacts"));

                // PARSE json data
                for (int i = 0; i < contactsArr.length(); i++) {
                    JSONObject contact = contactsArr.getJSONObject(i);

                    String id = contact.optString("id").toString();
                    String name = contact.optString("name").toString();
                    String surname = contact.optString("surname").toString();
                    String url = contact.optString("imgurl").toString();
                    String mobile = contact.optString("mobile").toString();
                    String email = contact.optString("email").toString();
                    String notes = contact.optString("notes").toString();

                    // build and set strings
                    dataKeys = dataKeys+id+",";
                    data = ""+name+","+surname+","+url+","+mobile+","+email+","+notes;

                    // save data per ID key
                    editor.putString(id, data);
                    editor.commit();
                }
                // save keys per constant MyKEYS
                editor.putString(MyKEYS, dataKeys);
                editor.commit();
                // stop preloder
                progressDialog.cancel();

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)

            // add some delay - just for development
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    gotoNextSlide();
                }
            }, 1000);

        } // protected void onPostExecute(Void v)
    } //class MyAsyncTask extends AsyncTask<String, String, Void>

    // Call next page activity with blank list view adding data on fly
    private void gotoNextSlide() {
        // Display new Activity slide via Intent
        Intent getNameScreenIntent = new Intent(this, ContactSlide.class);
        final int result = 1;
        getNameScreenIntent.putExtra("callingActivity", "MyMainActivity");
        startActivityForResult(getNameScreenIntent, result);
    }
/*
    public void readSPdata(){

        // set up SP connection
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // prepare vars
        List<String> stringKeys = new ArrayList<String>();

        // get key string from SP
        String keyString = sharedpreferences.getString(MyKEYS, "");

        // split ids and add them to array
        StringTokenizer tkns = new StringTokenizer(keyString, ",");
        while(tkns.hasMoreTokens()){
            String token = tkns.nextToken();
            stringKeys.add(token);
        }
        //Log.d("read SP data: ","" + stringKeys.toString());

        // get data strings per ID key from SP
        String keyID = "";
        int len = stringKeys.size();
        for (int i = 0; i < len; ++i) {
            keyID = stringKeys.get(i);
            String dataString = sharedpreferences.getString(keyID, "");

            Log.d("read SP data: ","" + dataString);
        }

/*                  String tname = tokens.nextToken();
                    String tsurname = tokens.nextToken();
                    String turl = tokens.nextToken();
                    String tmobile = tokens.nextToken();
                    String temail = tokens.nextToken();
                    String tnotes = tokens.nextToken();

                    Log.d("onPostExecute", i+": "+tid+" "+tname+" "+tsurname+" "+turl+" "+tmobile+" "+temail+" "+tnotes);
* /
    }
*/
    // My Download END

    // My Orientation Change START
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE)
        {
            Log.d("Main_ConfigCH:","orientation LANDSCAPE");
        }
        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT)
        {
            Log.d("Main_ConfigCH: ","orientation PORTRAIT");
        }

    }
    // My Orientation Change END

    // My Other
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        // Get wifi info & display toast
        WifiManager wifiManager = (WifiManager) getSystemService (Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo ();
        String ssid  = info.getSSID();
        Log.d("Main_WIFI_CH: ","SSID"+ssid);
        Toast myToast = Toast.makeText(
                getApplicationContext(),
                "Connected to wifi: "+ssid,
                Toast.LENGTH_LONG);
        myToast.show();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
