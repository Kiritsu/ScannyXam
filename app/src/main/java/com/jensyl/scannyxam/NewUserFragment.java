package com.jensyl.scannyxam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import com.jensyl.scannyxam.database.Badging;
import com.jensyl.scannyxam.database.ScannyXamDatabase;
import com.jensyl.scannyxam.database.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class NewUserFragment extends DialogFragment {
    private String idLeocard;
    private String examen;

    public NewUserFragment(String idLeocard, String examen) {
        this.idLeocard = idLeocard;
        this.examen = examen;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.new_user_fragment, null);
        EditText idUniv = view.findViewById(R.id.idUniv);
        EditText prenom = view.findViewById(R.id.prenom);
        EditText nom = view.findViewById(R.id.nom);

        builder.setView(view)
                .setMessage(R.string.dialog_new_user)
                .setPositiveButton(R.string.validate, (dialog, id) -> {
                    String idUnivText = idUniv.getText().toString();
                    String prenomText = prenom.getText().toString();
                    String nomText = nom.getText().toString();

                    new Thread(() -> {
                        User user = new User(NewUserFragment.this.idLeocard, idUnivText, prenomText, nomText);
                        ScannyXamDatabase db = Room
                                .databaseBuilder(
                                        NewUserFragment.this.getContext(),
                                        ScannyXamDatabase.class,
                                        "scanny-xam")
                                .build();

                        db.userDao().insert(user);

                        Badging badging = new Badging(
                                UUID.randomUUID().toString(),
                                NewUserFragment.this.idLeocard,
                                LocalDateTime.now().toString(),
                                NewUserFragment.this.examen);

                        db.badgingDao().insert(badging);

                        Looper.prepare();
                        printToast("New user: [" + user.idUniversity + " | " + user.idUser + "] " + user.firstName + " " + user.lastName + " (" + NewUserFragment.this.examen + ")");
                        Looper.loop();
                        Looper.myLooper().quit();
                    }).start();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    dialog.cancel();
                });

        return builder.create();
    }

    public void printToast(String s) {
        Toast.makeText(this.getContext(), "Scanned nfc id: " + s, Toast.LENGTH_LONG).show();
    }
}
