package com.storage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.firebaseall.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;


public class ListUserActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DataListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listuser);

        // Get Firebase database reference
        this.mDatabase = FirebaseDatabase.getInstance().getReference();

        // Init user list
        ListView list = (ListView) this.findViewById(R.id.dataList);
        Button btnStorgae = (Button) this.findViewById(R.id.btnStorgae);

        this.listAdapter = new DataListAdapter(this, R.layout.list_view_cell);
        list.setAdapter(listAdapter);

        btnStorgae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListUserActivity.this, StorageMainActivity.class));
            }
        });

        // Add listener
        mDatabase.addChildEventListener(new FirebaseEventListener(this.listAdapter));



        // for the insert
        btnStorgae.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
            //    writeNewUser("abcd","jay will","jaywill@gmail.com");
                deleteUser(mDatabase);

                return false;
            }
        });


      /*
      testing for the connnection

      DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    System.out.println("##################connected###############");
                } else {
                    System.out.println("###############not connected###############");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("###############Listener was cancelled###############");
            }
        });*/



    }
/*

    @Override
    protected void onStart() {
        super.onStart();

        // Add listener
        mDatabase.addChildEventListener(new FirebaseEventListener(this.listAdapter));
    }
*/

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        // insert
        // {abcd={name=jay will, id=jaywill@gmail.com}}

        mDatabase.child("users").child(userId).setValue(user);
    }


    private void deleteUser(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User p = mutableData.getValue(User.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

             /*   if (p.getId().containsKey("asd")) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(getUid(), true);
                }*/

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d("====delete===", "postTransaction:onComplete:" + databaseError);
            }
        });
    }



}
