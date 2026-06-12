package com.example.zelacity.data.remote;

import com.example.zelacity.model.Report;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private static final String REPORTS_PATH = "reports";
    private final DatabaseReference databaseReference;

    public FirebaseHelper() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(REPORTS_PATH);
    }

    public Task<Void> uploadReport(Report report) {
        String key = databaseReference.push().getKey();
        if (key != null) {
            return databaseReference.child(key).setValue(report);
        }
        return null;
    }
}
