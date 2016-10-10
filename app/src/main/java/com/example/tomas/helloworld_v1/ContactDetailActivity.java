package com.example.tomas.helloworld_v1;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class ContactDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        //set custom actionbar
        setActionBar();

        // Display contact Detail adding fragment
        addFragment();
    }

    // Here we add new fragment programmatically (to have better control over it) - (when contact item gets clicked it creates 'ContactDetailActivity' that adds fragment into it self onCreate)
    private void addFragment (){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ContactViewFragment cvf = new ContactViewFragment();
        fragmentTransaction.add(R.id.contact_detail_container, cvf, "CONTACT_VIEW_FRAGMENT");

        fragmentTransaction.commit();
    }

    // custom actionbar - bg tail method
    private void setActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_toolbar_bg_1));
    }
}
