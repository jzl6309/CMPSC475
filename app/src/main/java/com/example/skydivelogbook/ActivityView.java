package com.example.skydivelogbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.skydivelogbook.db.Log;
import com.example.skydivelogbook.db.LogViewModel;

import java.util.List;

public class ActivityView extends AppCompatActivity {

    private LogViewModel logViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LogListAdapter adapter = new LogListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        logViewModel = new ViewModelProvider(this).get(LogViewModel.class);
        logViewModel.getAllLogs().observe(this,adapter::setLogs);

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