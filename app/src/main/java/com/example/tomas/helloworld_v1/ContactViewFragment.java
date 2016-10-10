package com.example.tomas.helloworld_v1;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * represent single contact list item
 */
public class ContactViewFragment extends Fragment {

    public ContactViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // create new viewFragment
        View fragmentLayout = inflater.inflate(R.layout.fragment_contact_view,container, false);

        // find its text field instances
        TextView firstName = (TextView) fragmentLayout.findViewById(R.id.contact_view_firstName);
        TextView surName = (TextView) fragmentLayout.findViewById(R.id.contact_view_lastName);
        TextView telephone = (TextView) fragmentLayout.findViewById(R.id.contact_view_phoneValue);
        TextView email = (TextView) fragmentLayout.findViewById(R.id.contact_view_emailValue);
        TextView notes = (TextView) fragmentLayout.findViewById(R.id.contact_view_notesValue);
        ImageView image = (ImageView) fragmentLayout.findViewById(R.id.contact_view_image);

        // create new intent
        Intent intent = getActivity().getIntent();

        // set default values to text fields via EXTRA from main app file constants used in DETAIL!
        firstName.setText(intent.getExtras().getString(MyMainActivity.CONTACT_NAME_EXTRA));
        surName.setText(intent.getExtras().getString(MyMainActivity.CONTACT_SURNAME_EXTRA));
        telephone.setText(intent.getExtras().getString(MyMainActivity.CONTACT_PHONE_EXTRA));
        email.setText(intent.getExtras().getString(MyMainActivity.CONTACT_EMAIL_EXTRA));
        notes.setText(intent.getExtras().getString(MyMainActivity.CONTACT_NOTES_EXTRA));

        // Detail Img placement test
        new ImageDownloaderTask(image).execute(intent.getExtras().getString(MyMainActivity.CONTACT_IMGURL_EXTRA));

        // return the layout for this fragment
        return fragmentLayout;
    }

}
