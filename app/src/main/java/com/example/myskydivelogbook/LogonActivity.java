package com.example.myskydivelogbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LogonActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);

        auth = FirebaseAuth.getInstance();
    }

    public void create(View view) {
        String email = ((EditText) findViewById(R.id.editEmailAddress)).getText().toString();
        String password = ((EditText) findViewById(R.id.editPassword)).getText().toString();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("SUCCESSFUL!!!");
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText(LogonActivity.this,"Please login",Toast.LENGTH_LONG);
                            startActivity(new Intent(LogonActivity.this,LogonActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println("createUserWithEmail:failure " + task.getException());
                            Toast.makeText(LogonActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,ActivityView.class));

    }

    public void signIn(View view) {
        String email = ((EditText) findViewById(R.id.editEmailAddress)).getText().toString();
        String password = ((EditText) findViewById(R.id.editPassword)).getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            //updateUI(user);
                            //myFirestore.getInstance().writeToDatabase(user);
                            myFirestore.getInstance().restoreFromDatabase(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println("signInWithEmail:failure " + task.getException());
                            Toast.makeText(LogonActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,ActivityView.class));
    }
}