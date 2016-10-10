package com.example.tomas.helloworld_v1;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.RangeValueIterator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
// import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Tomas on 5. 10. 2016.
 * A simple (@link Fragment) subclass.
 */

public class ContactActivityFragment extends ListFragment {

    // Shared pref storage
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String MyKEYS = "MyKeys";

    private ArrayList<ContactPrototype> contacts;
    private ContactAdapter contactAdapter;

    @Override // callback on init
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        readSPdata();
    }

    public void readSPdata(){

        // READING MY SAVED STRINGS
        SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

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

        // get data strings per ID key from SP and create new item
        contacts = new ArrayList<ContactPrototype>();

        String keyID = "";
        int len = stringKeys.size();
        for (int i = 0; i < len; ++i) {
            keyID = stringKeys.get(i);
            String dataString = sharedpreferences.getString(keyID, "");
            List<String> contactData = new ArrayList<String>();
            StringTokenizer tokens = new StringTokenizer(dataString, ",");

                    String tname = tokens.nextToken();
                    String tsurname = tokens.nextToken();
                    String turl = tokens.nextToken();
                    String tmobile = tokens.nextToken();
                    String temail = tokens.nextToken();
                    String tnotes = tokens.nextToken();

            // Log.d("read SP data: ","" + String.valueOf(contactData));

            contacts.add(new ContactPrototype(tname, tsurname, turl, tmobile, temail, tnotes));
        }
        contactAdapter = new ContactAdapter(getActivity(), contacts);
        setListAdapter(contactAdapter);
    }

    // Default click method
    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);

        launchDetaiActivity(position);
    }

    // Custom click method with pasing data via EXTRA references
    private void launchDetaiActivity(int position) {

        // Grab contact information associated with clicked item
        ContactPrototype contact = (ContactPrototype) getListAdapter().getItem(position);

        // Start new intent with contact detail activity
        Intent intent = new Intent(getActivity(), ContactDetailActivity.class);

        // pass on values
        intent.putExtra(MyMainActivity.CONTACT_NAME_EXTRA, contact.getFirstName());
        intent.putExtra(MyMainActivity.CONTACT_SURNAME_EXTRA, contact.getLastName());
        intent.putExtra(MyMainActivity.CONTACT_IMGURL_EXTRA, contact.getUrl());
        intent.putExtra(MyMainActivity.CONTACT_PHONE_EXTRA, contact.getPhone());
        intent.putExtra(MyMainActivity.CONTACT_EMAIL_EXTRA, contact.getEmail());
        intent.putExtra(MyMainActivity.CONTACT_NOTES_EXTRA, contact.getNotes());

        // start
        startActivity(intent);
    }

}
