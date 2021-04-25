package com.example.myskydivelogbook;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.myskydivelogbook.db.LogDatabase.delete;
import static com.example.myskydivelogbook.db.LogDatabase.getCount;
import static com.example.myskydivelogbook.db.LogDatabase.getCurrentLogs;
import static com.example.myskydivelogbook.db.LogDatabase.getLog;
import static com.example.myskydivelogbook.db.LogDatabase.getMaxJump;
import static com.example.myskydivelogbook.db.LogDatabase.insert;

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

    public void restoreFromDatabase(FirebaseUser user) {
        db = FirebaseFirestore.getInstance();

        getMaxJump(max -> {
            int i = 0;
            while (i < max) {
                DocumentReference docRef = db.collection(user.getEmail() + " logbook").document("jump " + Integer.toString(i));
                docRef.get().addOnCompleteListener(task -> {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        String[] docKey = ((String) doc.getId()).split(" ");
                        int jump = Integer.parseInt(docKey[1]);
                        String date = (String) doc.get("date");
                        String location = (String) doc.get("location");
                        String aircraft = (String) doc.get("aircraft");
                        String equipment = (String) doc.get("equipment");
                        String altitude = (String) doc.get("altitude");
                        String delay = (String) doc.get("delay");
                        String wind = (String) doc.get("wind");
                        String target = (String) doc.get("target");
                        String sign = (String) doc.get("signature");
                        String notes = (String) doc.get("notes");

                        Log log = new Log(jump, date, location, aircraft, equipment, altitude, delay, wind, target, sign, notes);

                        insert(log);
                        docRef.delete();
                    }
                });
                i++;
            }
        });
    }
}
