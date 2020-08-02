package com.example.pmu_projekat.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;

import com.example.pmu_projekat.R;
import com.example.pmu_projekat.constants.Constants;

import java.util.ArrayList;
import java.util.List;

public class SettingsDialog extends DialogFragment {

    private boolean checked[];
    private List<Integer> selectedItems; // Where we track the selected items
    private SettingsReturnValue settingsReturnValue;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.options)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.options, checked,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    selectedItems.add(which);
                                } else if (selectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    selectedItems.remove(Integer.valueOf(which));
                                }
                                Log.d(Constants.GARAGE_ACTIVITY_DEBUG_TAG, which + " " + isChecked);
                                Log.d(Constants.GARAGE_ACTIVITY_DEBUG_TAG, "" + selectedItems.size());
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        boolean music = false;
                        boolean userControl = false;

                        for (int i = 0; i < selectedItems.size(); i++)
                        {
                            if (selectedItems.get(i) == 0)
                            {
                                music = true;
                            }

                            if (selectedItems.get(i) == 1)
                            {
                                userControl = true;
                            }
                        }

                        settingsReturnValue.onReturn(music, userControl);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            settingsReturnValue = (SettingsReturnValue) context;
        }
        catch (ClassCastException e) {
            Log.d(Constants.GARAGE_ACTIVITY_DEBUG_TAG, "Activity doesn't implement the SettingsReturnValue interface");
        }
    }

    public SettingsDialog(boolean[] checked) {
        this.checked = checked;
        selectedItems = new ArrayList();

        if (checked[0] == true) //  music
        {
            selectedItems.add(0);
        }

        if (checked[1] == true) // user control
        {
            selectedItems.add(1);
        }
    }
}
