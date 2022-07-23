package com.regin.reginald.vehicleanddrivers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
//import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class DocumentNotes extends AppCompatActivity {
    private SignaturePad _docnotes;
    String customerOrders, deldate, ordertype, route, InvoiceNo, cash;
    //final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    final DatabaseAdapter dbH = new DatabaseAdapter(AppApplication.getAppContext());
    Button close_notes, save_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_notes);
        //AndroidNetworking.initialize(getApplicationContext());
        Intent returndata = getIntent();

        deldate = returndata.getStringExtra("deldate");
        ordertype = returndata.getStringExtra("ordertype");
        route = returndata.getStringExtra("routes");

        InvoiceNo = returndata.getStringExtra("invoiceno");
        cash = returndata.getStringExtra("cash");
        final String cashPaid = returndata.getStringExtra("cash");

        _docnotes = (SignaturePad) findViewById(R.id.docnotes);
        save_note = (Button) findViewById(R.id.save_note);
        close_notes = (Button) findViewById(R.id.close_note);


        save_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap signatureBitmap = _docnotes.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap, InvoiceNo)) {

                    Toast.makeText(DocumentNotes.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DocumentNotes.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }

            }
        });

        close_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(DocumentNotes.this, InvoiceDetails.class);

                b.putExtra("deldate", deldate);
                b.putExtra("routes", route);
                b.putExtra("ordertype", ordertype);
                b.putExtra("invoiceno", InvoiceNo);
                b.putExtra("cash", cash);

                startActivity(b);
            }
        });
        //docnotes
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo, String InvoiceNo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        //  OutputStream stream = new FileOutputStream(photo);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteImage = outputStream.toByteArray();
        String s = Base64.encodeToString(byteImage, Base64.DEFAULT);


        dbH.updateDeals("Update OrderHeaders set strNotesDrivers='" + s + "' where InvoiceNo ='" + InvoiceNo + "'");
        Intent b = new Intent(DocumentNotes.this, InvoiceDetails.class);

        b.putExtra("deldate", deldate);
        b.putExtra("routes", route);
        b.putExtra("ordertype", ordertype);
        b.putExtra("invoiceno", InvoiceNo);
        b.putExtra("cash", cash);

        startActivity(b);
        // Log.e("********","***************"+s);
        //Log.e("********","***************InvoiceNo----"+InvoiceNo);
        //stream.close();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature, String invoiceNo) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo, invoiceNo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        DocumentNotes.this.sendBroadcast(mediaScanIntent);
    }

    public boolean addSvgSignatureToGallery(String signatureSvg) {
        boolean result = false;
        try {
            File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.svg", System.currentTimeMillis()));
            OutputStream stream = new FileOutputStream(svgFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(signatureSvg);
            writer.close();
            stream.flush();
            stream.close();
            scanMediaFile(svgFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
