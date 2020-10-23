package com.example.androidproject01.models;

import com.google.firebase.database.DatabaseError;


public interface retrieveDataRealTime {
         void onStart();
         void onSuccess(Object data);
         void onFailed(DatabaseError databaseError);
}
