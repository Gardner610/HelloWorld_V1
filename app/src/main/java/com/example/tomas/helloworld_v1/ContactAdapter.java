package com.example.tomas.helloworld_v1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tomas on 5. 10. 2016.
 *
 * Display values in ITEM as specified in my tatastructure ContactPrototype
 *
 */

public class ContactAdapter extends ArrayAdapter<ContactPrototype> {

    public static class ViewHolder{

        TextView firstname;
        TextView surname;
        ImageView contactImage;
        //ImageView telephone;
    }

    public ContactAdapter(Context context, ArrayList<ContactPrototype> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get my contact item per position
        ContactPrototype contact = getItem(position);

        // Here I need to get prototype references and fill them with data based on ID? If they don't exits yet
        ViewHolder viewHolder;

        // if NEW - inflate new view from blueprint
        if(convertView == null) {

            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_prototype, parent, false);

            // Grab ID references of views (instances) and keep it stored in ViewHolder for performance
            viewHolder.firstname = (TextView) convertView.findViewById(R.id.listItemFirstName);
            viewHolder.surname = (TextView) convertView.findViewById(R.id.listItemSurName);
            viewHolder.contactImage = (ImageView) convertView.findViewById(R.id.listItemImage);

            // set view holder to keep reference
            convertView.setTag(viewHolder);

        // ELSE - already exists just use it
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate data to template view using data object (works i ndetail)
        viewHolder.firstname.setText(contact.getFirstName());
        viewHolder.surname.setText(contact.getLastName());

        // Set image - BUT! placeholder will show async download will follow
        viewHolder.contactImage.setImageResource(contact.getAssociatedDrawable());
        if (viewHolder.contactImage != null) {

            Log.d("CHECKING IMG", "is not null"+viewHolder.contactImage);
            new ImageDownloaderTask(viewHolder.contactImage).execute(contact.getUrl());

        }else{
            //Log.d("CHECKING IMG", "is not null");
        }

        return convertView;

    }
}
