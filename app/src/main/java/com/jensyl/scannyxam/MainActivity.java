package com.jensyl.scannyxam;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.os.Looper;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.jensyl.scannyxam.database.Badging;
import com.jensyl.scannyxam.database.ScannyXamDatabase;
import com.jensyl.scannyxam.database.User;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private NfcAdapter adapter;
    private PendingIntent mPendingIntent;
    private EditText examName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Uncomment to create dummy data:
        // Feeder.feed(getApplicationContext());

        examName = findViewById(R.id.idNameExam);

        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        adapter = manager.getDefaultAdapter();

        if (adapter != null) {
            mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                    getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adapter != null) {
            adapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getTagInfo(intent);
    }

    private void getTagInfo(Intent intent) {
        byte[] uid = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

        String s = new BigInteger(uid).toString(16);

        new Thread(() -> {
            ScannyXamDatabase db = Room
                    .databaseBuilder(
                            getApplicationContext(),
                            ScannyXamDatabase.class,
                            "scanny-xam")
                    .build();

            User user = db.userDao().getById(s);
            if (user == null) {
                NewUserFragment fragment = new NewUserFragment(s, examName.getText().toString());
                fragment.show(getSupportFragmentManager(), "new_user");
            } else {
                Badging badging = new Badging(
                        UUID.randomUUID().toString(),
                        s,
                        LocalDateTime.now().toString(),
                        examName.getText().toString());

                db.badgingDao().insert(badging);

                Looper.prepare();
                printToast("User: [" + user.idUniversity + " | " + user.idUser + "] " + user.firstName + " " + user.lastName + " (" + examName.getText().toString() + ")");
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();
    }

    public void printToast(String s) {
        Toast.makeText(this, "Scanned nfc id: " + s, Toast.LENGTH_LONG).show();
    }
}