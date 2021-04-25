package com.example.myskydivelogbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class UploadConfirmationDialog extends DialogFragment {
    public interface UploadConformationListener {
        void onUploadConfirmation();
    }

    UploadConformationListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Remove Logs?")
                .setMessage("Only your most recent jump will remain.")
                .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onUploadConfirmation();
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
            listener = (UploadConformationListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement UploadConformationListener");
        }
    }
}
