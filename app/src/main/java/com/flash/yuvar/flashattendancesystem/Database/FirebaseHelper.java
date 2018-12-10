package com.flash.yuvar.flashattendancesystem.Database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class FirebaseHelper {

    DatabaseReference db;
    ArrayList<Request_access> request_accesses =new ArrayList<> ();
    public FirebaseHelper(DatabaseReference db)  {
        this.db=db;
    }




    public ArrayList<Request_access> retreive () {
        db.addChildEventListener (new ChildEventListener ( ) {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Request_access request_access=ds.getValue(Request_access.class);
                    request_accesses.add(request_access);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return request_accesses;

    }
}
