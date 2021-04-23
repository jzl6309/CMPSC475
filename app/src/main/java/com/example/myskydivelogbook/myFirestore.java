package com.example.myskydivelogbook;

import android.app.Activity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myskydivelogbook.db.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.myskydivelogbook.db.LogDatabase.getCount;
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

    public void authenticate(Activity activity, final OnAuthenticateListener listener) {
        if (user == null) {
            db = FirebaseFirestore.getInstance();

            final FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.signInAnonymously()
                    .addOnCompleteListener(activity, task -> {
                        if (task.isSuccessful()) {
                            user = auth.getCurrentUser();
                            listener.onAuthenticated(true, "User ID: " + user.getUid());
                        }
                        else {
                            listener.onAuthenticated(false, "login failure");
                        }
                    });
        }
        else {
            listener.onAuthenticated(true, "You are already logged in: " + user.getUid());
        }
    }

    public void writeToDatabase() {
        int max = 3;
        int jumpNum = max -1;

        while (true) {
            Map<String, Object> jump = new HashMap<>();

            getLog(jumpNum, log1 -> {
                if (log1 != null) {
                    Log log = log1;
                    jump.put("date", log.date);
                    jump.put("location", log.location);
                    jump.put("aircraft", log.aircraft);
                    jump.put("equipment", log.equipment);
                    jump.put("altitude", log.altitude);
                    jump.put("delay", log.delay);
                    jump.put("wind", log.wind);
                    jump.put("target", log.target);
                    jump.put("signature", log.signature);
                    jump.put("notes", log.notes);;
                }
            });

            System.out.println("HERE " + jump.get("wind"));

            if (jump.isEmpty()) {
                System.out.println("I GOT HERE!!");
                break;
            }
            db.collection("logbook").document("jump " + Integer.toString(jumpNum))
                    .set(jump)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.out.println("SUCCESS! HOORAY!!!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("BUMMER! NO GOOD!!!");
                        }
                    });
            jumpNum--;
        }

    }

}
