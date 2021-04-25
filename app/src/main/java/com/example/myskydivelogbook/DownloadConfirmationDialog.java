package com.example.myskydivelogbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.ConditionVariable;

import androidx.fragment.app.DialogFragment;

public class DownloadConfirmationDialog extends DialogFragment {
    public interface DownloadConfirmationListener {
        void onDownloadConfirmation();
    }

    DownloadConfirmationListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Return Logs?")
                .setMessage("All jumps will be returned to your device.")
                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDownloadConfirmation();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DownloadConfirmationListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement UploadConformationListener");
        }
    }

}
