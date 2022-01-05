package com.jensyl.scannyxam;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.room.Room;

import com.jensyl.scannyxam.database.Badging;
import com.jensyl.scannyxam.database.ScannyXamDatabase;
import com.jensyl.scannyxam.database.User;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements PdfUtil.OnDocumentClose {
    private NfcAdapter adapter;
    private PendingIntent mPendingIntent;
    private EditText examName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Uncomment to create dummy data:
        // new Thread(() -> Feeder.feed(getApplicationContext())).start();

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

    public void generatePdf(View view) throws Exception {
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawText("test", 80, 50, paint);
        // finish the page
        document.finishPage(page);


        // write the document content
        String directory_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/";
        File root = new File(directory_path);
        if (!root.exists()) {
            root.mkdirs();
        }
        File gpxfile = new File(root, "releve.pdf");
        try {
            PdfUtil.createPdf(this,this, getSampleData(),gpxfile.getPath());

        } catch (IOException e) {
            Log.e("Error", "Something wrong: " + e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }

    @Override
    public void onPDFDocumentClose(File file)
    {
        Uri path = FileProvider.getUriForFile(this, getApplicationContext().getPackageName()+ ".provider", file);;
        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }

    private List<String[]> getSampleData()
    {
        int count = 20;
        List<String[]> temp = new ArrayList<>();
        for (int i = 0; i < count; i++)
        {
            temp.add(new String[] {"C1-R"+ (i+1),"C2-R"+ (i+1)});
        }
        return  temp;
    }
}