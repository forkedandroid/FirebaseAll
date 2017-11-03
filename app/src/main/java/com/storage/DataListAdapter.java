package com.storage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.firebaseall.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class DataListAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<User> userList;
    private int layoutRessource;

    public ArrayList<User> getUserList() {
        return userList;
    }

    public DataListAdapter(Context ctx, int layoutResourceId) {
        super(ctx, layoutResourceId);

        this.userList = new ArrayList<User>();
        this.layoutRessource = layoutResourceId;
        this.context = ctx;
    }

    public void addUser(User usr) {
        this.userList.add(usr);
    }

    public void removeUser(String usrId) {

        ArrayList<User> arrUser =  new ArrayList<>();

        arrUser.addAll(userList);

        userList.clear();

        for(int i=0; i<arrUser.size(); i++)
        {
            User user = arrUser.get(i);
            if(user.getId().equalsIgnoreCase(usrId))
            {
                Log.i("===Delete user======","===usrId="+usrId);
                Log.i("===Delete====","===position==="+i);
                arrUser.remove(i);
            }

            if(i == (arrUser.size()-1))
            {
                userList.clear();
                userList.addAll(arrUser);
            }

        }


       /* for (User usr : userList) {
            if (usr.getId().equals(usrId)) {
                this.userList.remove(usr);
            }
        }*/
    }

    @Override
    public int getCount() {
        return this.userList.size();
    }

    @Override
    public User getItem(int position) {
        return this.userList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater li = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = li.inflate(this.layoutRessource, null);
        }

        // Get row user
        User currentUser  = getItem(position);

        // Id
        TextView idLabel = (TextView) row.findViewById(R.id.id);
        idLabel.setText(currentUser.getId());

        // User
        TextView userLabel = (TextView) row.findViewById(R.id.user);
        userLabel.setText(currentUser.getName());

        return row;
    }
}
