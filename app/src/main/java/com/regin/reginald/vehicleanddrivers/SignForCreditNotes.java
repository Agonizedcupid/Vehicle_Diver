package com.regin.reginald.vehicleanddrivers;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.regin.reginald.model.OrderLines;
import com.regin.reginald.model.OtherAttributes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SignForCreditNotes extends AppCompatActivity {
    String deldate,ordertype,route,InvoiceNo,cash,emailaddress,ts,storename,uniqueId;
    EditText enterusername,email_cust,drivername;
    Button finish_signature;

    private SignaturePad mSignaturePad;
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signcredit_request);


        Long tsLong = System.currentTimeMillis()/1000;
        ts = tsLong.toString();
        enterusername = (EditText) findViewById(R.id.enterusername) ;
        email_cust = (EditText) findViewById(R.id.email_cust) ;
        drivername = (EditText) findViewById(R.id.drivername) ;
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        finish_signature = (Button) findViewById(R.id.finish_signature);
        Intent returndata = getIntent();
        finish_signature.setVisibility(View.INVISIBLE);

        uniqueId = returndata.getStringExtra("uniqueId");


        email_cust.setText(emailaddress);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
                Log.e("FFFFF","=============================================================START SIGN");

            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
                Log.e("FFFF","==============================================================DONE SIGNING");
                finish_signature.setVisibility(View.VISIBLE);


            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });
        finish_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (enterusername.getText().toString().trim().length() < 3)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(SignForCreditNotes.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Please Enter Name.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }else{
                    finish_signature.setVisibility(View.INVISIBLE);
                    Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();


                    finish_signature.setVisibility(View.INVISIBLE);
                    //printdata(InvoiceNo);

                    if (addJpgSignatureToGallery(signatureBitmap,uniqueId)) {
                        dbH.updateDeals("UPDATE tblCreditNotesHeader set Completed=1,Drivername='"+drivername.getText().toString()+"',SignedBy='"+enterusername.getText().toString()+"',strEmail='"+email_cust.getText().toString()+"' where UniqueString='"+uniqueId+"'");
                        dbH.updateDeals("UPDATE tblCreditNotesLines set Completed=1 where ReferenceNumber='"+uniqueId+"'");

                        Intent main = new Intent(SignForCreditNotes.this, CreditRequitionLandingPage.class);


                        startActivity(main);
                    }

                }
            }
        });

    }
    public boolean addJpgSignatureToGallery(Bitmap signature,String invoiceNo) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo,invoiceNo);
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
        SignForCreditNotes.this.sendBroadcast(mediaScanIntent);
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
    public void saveBitmapToJPG(Bitmap bitmap, File photo,String InvoiceNo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        //  OutputStream stream = new FileOutputStream(photo);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteImage =outputStream.toByteArray();
        String s = Base64.encodeToString(byteImage,Base64.DEFAULT);
        String newEmail = email_cust.getText().toString();
        newEmail = newEmail.replace("'", "");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 0);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tomorrowDate = dateFormat.format(tomorrow);

        dbH.updateDeals("Update tblCreditNotesHeader set Signature='"+s+"' where ReferenceNumber ='"+InvoiceNo+"'");


    }
    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
       Intent i = new Intent(SignForCreditNotes.this,CreateCreditRequisition.class);
       startActivity(i);
    }

}
