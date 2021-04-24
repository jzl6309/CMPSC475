package com.example.myskydivelogbook;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.myskydivelogbook.db.Log;
import com.example.myskydivelogbook.db.LogDatabase;
import com.example.myskydivelogbook.db.LogViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.myskydivelogbook.db.LogDatabase.delete;
import static com.example.myskydivelogbook.db.LogDatabase.getCount;
import static com.example.myskydivelogbook.db.LogDatabase.getCurrentLogs;
import static com.example.myskydivelogbook.db.LogDatabase.getLog;
import static com.example.myskydivelogbook.db.LogDatabase.getMaxJump;

public class myFirestore extends AppCompatActivity {
    public interface OnAuthenticateListener {
        void onAuthenticated(boolean success, String status);
    }

    private FirebaseUser user;
    private FirebaseFirestore db;
    private static myFirestore INSTANCE;

    private myFirestore() {}

    public static synchronized myFirestore getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new myFirestore();
        }
        return INSTANCE;
    }

    public void writeToDatabase(FirebaseUser user) {
        db = FirebaseFirestore.getInstance();

        getCurrentLogs(logs -> {
            int i = 0;
            while (i < logs.size()-1) {
                Log log = logs.get(i);
                int jumpNum = log.jumpNum;

                Map<String, Object> jump = new HashMap<>();
                jump.put("date", log.date);
                jump.put("location", log.location);
                jump.put("aircraft", log.aircraft);
                jump.put("equipment", log.equipment);
                jump.put("altitude", log.altitude);
                jump.put("delay", log.delay);
                jump.put("wind", log.wind);
                jump.put("target", log.target);
                jump.put("signature", log.signature);
                jump.put("notes", log.notes);

                db.collection(user.getEmail() + " logbook").document("jump " + Integer.toString(jumpNum))
                        .set(jump)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                System.out.println("SUCCESS! HOORAY!!! " + jumpNum );
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("BUMMER! NO GOOD!!!");
                            }
                        });
                delete(jumpNum);
                i++;
            }
        });
    }
}
