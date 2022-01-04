package com.jensyl.scannyxam;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.jensyl.scannyxam.database.ScannyXamDatabase;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {
    private NfcAdapter adapter;
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(() -> {
            ScannyXamDatabase db = Room
                    .databaseBuilder(
                            getApplicationContext(),
                            ScannyXamDatabase.class,
                            "scanny-xam")
                    .build();

            //List<UserWithBadgings> users = db.userDao().getUsersWithBadgings();

            //db.userDao().insert(new User("test123", "ma173017", "Allan", "Mercou"));
            //db.badgingDao().insert(new Badging("test", "test123", "2022-01-03 10:00:00", "Anglais"));
        }).start();

        Intent intent;
        intent = this.getIntent();

        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        adapter = manager.getDefaultAdapter();

        if (adapter != null) {
            if (adapter.isEnabled()) {
                if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
                    Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                }
            }

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
        printToast(s);
    }

    public void printToast(String s) {
        Toast.makeText(this, "Scanned nfc id: " + s, Toast.LENGTH_LONG).show();
    }
}