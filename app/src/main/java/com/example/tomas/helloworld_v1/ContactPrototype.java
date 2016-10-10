package com.example.tomas.helloworld_v1;

/**
 * Created by Tomas on 4. 10. 2016. *
 * My Contact - data structure
 */

public class ContactPrototype {
/*
    public long id;
    public long dateCreatedMilli;
*/
    public final String firstName;
    public final String lastName;
    public final String contactImgURL;
    public final String phoneNumber;
    public final String emailAddress;
    public final String notesText;

    public ContactPrototype(String firstName, String lastName, String contactImgURL, String phoneNumber, String emailAddress, String notesText) {
       //this.id = 0;
        //this.dateCreatedMilli = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactImgURL = contactImgURL;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.notesText = notesText;
    }

    // My getters
/*
    public long getId() { return id; }
    public long getDateCreatedMilli() { return dateCreatedMilli; }
*/
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getUrl() { return contactImgURL; }
    public String getPhone() { return phoneNumber; }
    public String getEmail() { return emailAddress; }
    public String getNotes() { return notesText; }

    // parse object to string method (for console or other use as string)
    @Override
    public String toString() {
        return "name:"+firstName + "surname:"+lastName + "IMGurl:"+contactImgURL + "phone:"+phoneNumber + "email:"+emailAddress + "note:"+notesText; //"cid:"+id + "created:"+dateCreatedMilli +
    }

    // this returns just a placeholder.png for now
    public int getAssociatedDrawable(){
        return R.drawable.placeholder; //categoryToDrawable(category);
    }
}
