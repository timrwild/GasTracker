package com.twild.gastracker;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Tim Wildauer on 16-May-17.
 */

public class GetDatabase
{
    private static FirebaseDatabase firebaseDatabase;

    public static FirebaseDatabase getDatabase()
    {
        if (firebaseDatabase == null)
        {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }
        return firebaseDatabase;
    }
}
