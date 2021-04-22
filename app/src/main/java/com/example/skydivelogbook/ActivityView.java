package com.example.skydivelogbook;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.skydivelogbook.db.Log;
import com.example.skydivelogbook.db.LogViewModel;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;

public class ActivityView extends AppCompatActivity {

    private LogViewModel logViewModel;
    private Menu optionsMenu;
    private static final String TAG = "LOOK HERE: ";
    private final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String APPLICATION_NAME = "SkyDiveLogBook";
    private static final String SCOPE = "";
    private static final String CLIENT_ID = "1075933492779-pp2f13tvgn8kb76sk5mc2alm5sa5mrut.apps.googleusercontent.com";
    private GoogleSignInAccount account;
    private GoogleSignInOptions gso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        setSupportActionBar(findViewById(R.id.view_toolbar));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LogListAdapter adapter = new LogListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        logViewModel = new ViewModelProvider(this).get(LogViewModel.class);
        logViewModel.getAllLogs().observe(this,adapter::setLogs);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope("https://www.googleapis.com/auth/spreadsheets"))
                .requestEmail()
                .requestIdToken(CLIENT_ID)
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);

        if (account == null ) {
            System.out.println("UGH ITS NULL");
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestScopes(new Scope("https://www.googleapis.com/auth/spreadsheets"))
                    .requestEmail()
                    .requestIdToken(CLIENT_ID)
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        optionsMenu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_view, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuUpload:
                signIn();

                ActivityResultLauncher<String> requestPermissionLauncher =
                    registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                        if (isGranted) {
                            // Permission is granted. Continue the action or workflow in your
                            // app.
                            try {
                                writeData();
                            }
                            catch (GeneralSecurityException e ) {
                                System.out.println("DANG IT: " + e);
                            }
                            catch (IOException e) {
                                System.out.println("DARN IT: " + e);
                            }
                        } else {
                            // Explain to the user that the feature is unavailable because the
                            // features requires a permission that the user has denied. At the
                            // same time, respect the user's decision. Don't link to system
                            // settings in an effort to convince the user to change their
                            // decision.
                        }
                    });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            android.util.Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void writeData() throws IOException, GeneralSecurityException {

        final NetHttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();

        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(ActivityView.this, Collections.singleton("https://www.googleapis.com/auth/spreadsheets"));

        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties().setTitle("logBook"));
        spreadsheet = service.spreadsheets().create(spreadsheet)
                .setFields("spreadsheetID")
                .execute();
        System.out.println("Spreadsheet " + spreadsheet.getSpreadsheetId());
    }

    public void add(View view) {
        Intent intent = new Intent(this, activityAdd.class);
        startActivity(intent);
    }

    public void back(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public class LogListAdapter extends RecyclerView.Adapter<LogListAdapter.LogViewHolder> {

        class LogViewHolder extends RecyclerView.ViewHolder {
            private final TextView jumpNumView;
            private final TextView dateView;
            private final TextView locationView;
            private final TextView aircraftView;
            private final TextView equipmentView;
            private final TextView notesView;
            private Log log;

            private LogViewHolder(View itemView) {
                super(itemView);
                jumpNumView = itemView.findViewById(R.id.listJumpNum);
                dateView = itemView.findViewById(R.id.listDate);
                locationView = itemView.findViewById(R.id.listLocation);
                aircraftView = itemView.findViewById(R.id.listAircraft);
                equipmentView = itemView.findViewById(R.id.listEquipment);
                notesView = itemView.findViewById(R.id.listNotes);

                itemView.setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("jumpNum", jumpNumView.getText().toString());
                    startActivity((new Intent(ActivityView.this, ActivityUpdate.class).putExtras(bundle)));
                });

            }
        }

        private final LayoutInflater layoutInflater;
        private List<Log> logs;

        LogListAdapter(Context context) { layoutInflater = LayoutInflater.from(context); }

        @Override
        public LogViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);

            return new LogViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(LogViewHolder holder, int position) {
            if (logs != null) {
                Log current = logs.get(position);
                holder.log = current;
                holder.jumpNumView.setText(Integer.toString(current.jumpNum));
                holder.dateView.setText(current.date);
                holder.locationView.setText(current.location);
                holder.aircraftView.setText(current.aircraft);
                holder.equipmentView.setText(current.equipment);
                holder.notesView.setText(current.notes);
            }
            else {
                holder.jumpNumView.setText("@string/initializing");
            }
        }

        void setLogs(List<Log> logs) {
            this.logs = logs;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            if (logs != null)
                return logs.size();
            else return 0;
        }
    }

}