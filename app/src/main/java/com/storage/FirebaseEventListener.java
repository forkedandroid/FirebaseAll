package com.storage;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class FirebaseEventListener implements ChildEventListener {

    private DataListAdapter userList;


    public FirebaseEventListener(DataListAdapter userList) {
        this.userList = userList;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
        Log.i("==onChildAdded==","==data===dataSnapshot.getValue()==="+dataSnapshot.getValue());
        Log.i("==onChildAdded==","==data===dataSnapshot.getKey()==="+dataSnapshot.getKey());

        User newUser = new User(""+dataSnapshot.getValue(), ""+dataSnapshot.getKey());
        if (!(userList.getUserList().contains(newUser))) {
            userList.addUser(newUser);
            userList.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        // Child moved
        // ...
        Log.i("==onChildMoved==","==data=="+s);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        userList.removeUser(dataSnapshot.getKey());
        userList.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        // Child changed
        // ...
        Log.i("==onChildChanged==","==data=="+s);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Error
        // ...

        Log.i("==onCancelled==","==Errr=="+databaseError.getMessage());
        Log.i("==onCancelled==","==Errr==");
    }
}
