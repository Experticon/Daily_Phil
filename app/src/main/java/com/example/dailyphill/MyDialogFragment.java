package com.example.dailyphill;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {

    OnButtonClick onClick;

    public interface OnButtonClick {
        void onDialogClickListener();
    }
    public void setOnDialogClickListener (OnButtonClick buttonClick){
        onClick = buttonClick;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "Подтвердите удаление";
        String button1String = "Отмена";
        String button2String = "Удалить";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);  // заголовок
        builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onClick.onDialogClickListener();
                dialog.cancel();
            }
        });
        builder.setCancelable(true);

        return builder.create();
    }
}
