package com.regin.reginald.vehicleanddrivers;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Environment;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.regin.reginald.model.OrderLines;
import com.regin.reginald.model.OtherAttributes;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SignaturePage extends AppCompatActivity {

    String deldate,ordertype,route,InvoiceNo,cash,emailaddress,ts,storename;
    EditText enterusername,email_cust;
    Button finish_signature,savecard,closedialog;
    TextView uid;
    final static String TAG = "nfc_test";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    private SignaturePad mSignaturePad;
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_page);

        Long tsLong = System.currentTimeMillis()/1000;
        ts = tsLong.toString();
        enterusername = (EditText) findViewById(R.id.enterusername) ;
        email_cust = (EditText) findViewById(R.id.email_cust) ;
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        finish_signature = (Button) findViewById(R.id.finish_signature);
        Intent returndata = getIntent();
        finish_signature.setVisibility(View.INVISIBLE);

        deldate = returndata.getStringExtra("deldate");
        ordertype =returndata.getStringExtra("ordertype");
        route = returndata.getStringExtra("routes");
        InvoiceNo = returndata.getStringExtra("invoiceno");
        cash = returndata.getStringExtra("cash");
        emailaddress = returndata.getStringExtra("emailaddress");
        storename = returndata.getStringExtra("storename");

      /*  nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //If no NfcAdapter, display that the device has no NFC
        if (nfcAdapter == null){
            Toast.makeText(this,"NO NFC Capabilities",
                    Toast.LENGTH_SHORT).show();
            finish();
        }else
        {

        }
*/
      //  pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);

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
                    AlertDialog alertDialog = new AlertDialog.Builder(SignaturePage.this).create();
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
                    Log.e("InvoiceNo","InvoiceNo************"+InvoiceNo);

                   /* if(dbH.returnLoyaltyPoints(InvoiceNo) > 0)
                    {
                        Dialog dialogView = new Dialog(SignaturePage.this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogView.setCancelable(false);
                        dialogView.setContentView(R.layout.loyaltycard);

                        savecard = (Button) dialogView.findViewById(R.id.savecard);
                        uid = (TextView) dialogView.findViewById(R.id.uid);
                        closedialog = (Button) dialogView.findViewById(R.id.closedialog);


                        closedialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialogView.dismiss();
                            }
                        });

                        savecard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DimsIp+"BriefcaseSpecials/"+custcode.getText().toString()));
                               // startActivity(browserIntent);
                                String id = uid.getText().toString();

                                //dbH.updateDeals("update OrderHeaders set Loyalty='"+id+"' where InvoiceNo = '"+InvoiceNo+"'");
                                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();

                               // createandDisplayPdf(InvoiceNo,ts);
                                finish_signature.setVisibility(View.INVISIBLE);
                                //printdata(InvoiceNo);

                                if (addJpgSignatureToGallery(signatureBitmap,InvoiceNo)) {

                                    Intent main = new Intent(SignaturePage.this, FinishActivity.class);
                                    main.putExtra("invoiceno",InvoiceNo);
                                    main.putExtra("email", email_cust.getText().toString());
                                    main.putExtra("signedby", enterusername.getText().toString());
                                    main.putExtra("deldate",deldate);
                                    main.putExtra("ordertype",ordertype);
                                    main.putExtra("route",route);

                                    startActivity(main);
                                }
                                dialogView.dismiss();
                            }
                        });


                        dialogView.show();

                    }else{*/
                        Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();

                       // createandDisplayPdf(InvoiceNo,ts);
                        finish_signature.setVisibility(View.INVISIBLE);
                        //printdata(InvoiceNo);

                        if (addJpgSignatureToGallery(signatureBitmap,InvoiceNo)) {

                            Intent main = new Intent(SignaturePage.this, FinishActivity.class);
                            main.putExtra("invoiceno",InvoiceNo);
                            main.putExtra("email", email_cust.getText().toString());
                            main.putExtra("signedby", enterusername.getText().toString());
                            main.putExtra("deldate",deldate);
                            main.putExtra("ordertype",ordertype);
                            main.putExtra("route",route);

                            startActivity(main);
                        }
                    //}





                }
            }
        });

    }

   /* @Override
    protected void onResume() {
        super.onResume();
        assert nfcAdapter != null;
        //nfcAdapter.enableForegroundDispatch(context,pendingIntent,
        //                                    intentFilterArray,
        //                                    techListsArray)
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    protected void onPause() {
        super.onPause();
        //Onpause stop listening
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            Log.d("TAG", "tag ID = " + tag.getId().toString());
            assert tag != null;
            byte[] payload = detectTagData(tag).getBytes();

        }
    }
    private String detectTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        uid.setText(bytesToHexString(id ));
        sb.append("ID 00 ").append(bytesToHexString(id )).append('\n');
        sb.append("ID  ").append(id.toString()).append('\n');
        sb.append("ID (hex): ").append(toHex(id)).append('\n');
        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {

            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";

                try {
                    MifareClassic mifareTag = MifareClassic.get(tag);

                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }
        Log.v(TAG,sb.toString());
        return sb.toString();
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }
    public void writeTag(MifareUltralight mifareUlTag) {
        try {
            mifareUlTag.connect();
            mifareUlTag.writePage(4, "get ".getBytes(Charset.forName("US-ASCII")));
            mifareUlTag.writePage(5, "fast".getBytes(Charset.forName("US-ASCII")));
            mifareUlTag.writePage(6, " NFC".getBytes(Charset.forName("US-ASCII")));
            mifareUlTag.writePage(7, " now".getBytes(Charset.forName("US-ASCII")));
        } catch (IOException e) {
            Log.e(TAG, "IOException while writing MifareUltralight...", e);
        } finally {
            try {
                mifareUlTag.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException while closing MifareUltralight...", e);
            }
        }
    }
    public String readTag(MifareUltralight mifareUlTag) {
        try {
            mifareUlTag.connect();
            byte[] payload = mifareUlTag.readPages(4);
            return new String(payload, Charset.forName("US-ASCII"));
        } catch (IOException e) {
            Log.e(TAG, "IOException while reading MifareUltralight message...", e);
        } finally {
            if (mifareUlTag != null) {
                try {
                    mifareUlTag.close();
                }
                catch (IOException e) {
                    Log.e(TAG, "Error closing tag...", e);
                }
            }
        }
        return null;
    }
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }*/
    public boolean addJpgSignatureToGallery(Bitmap signature,String invoiceNo) {
        boolean result = false;
        try {
           // File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature,invoiceNo);
           // scanMediaFile(photo);
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
        SignaturePage.this.sendBroadcast(mediaScanIntent);
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
    public void saveBitmapToJPG(Bitmap bitmap,String InvoiceNo) throws IOException {
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
        try{
            Log.e("updating","*********************************************************************************update order header");
            dbH.updateDeals("Update OrderHeaders set imagestring='"+s+"', strCustomerSignedBy='"+enterusername.getText().toString()+"', strEmailCustomer='"+ newEmail+"', EndTripTime='"+tomorrowDate+"', Uploaded=0 ,  offloaded=1 where InvoiceNo ='"+InvoiceNo+"'");
        }catch (Exception e)
        {

        }

    }
    public void createandDisplayPdf(String InvoiceNo,String FileName) {

        Document doc = new Document();
        ArrayList<com.regin.reginald.model.OrderLines> lines= dbH.returnOrderLines(InvoiceNo);
        ArrayList<com.regin.reginald.model.OrderLines> returns= dbH.returnCreditRequisition(InvoiceNo);
        try {
            String path =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/PDF";

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir, InvoiceNo+"_"+FileName+".pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            Font colfont = new Font(Font.FontFamily.HELVETICA, 23 );



           /* Paragraph p1 = new Paragraph(text);
            Font paraFont = new Font(Font.FontFamily.COURIER);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);*/
            PdfPTable table = new PdfPTable(3);
            PdfPTable tabler = new PdfPTable(3);
            table.setTotalWidth(1500);
            table.setWidthPercentage(100);
            tabler.setTotalWidth(PageSize.A4.getWidth());
            // table.setLockedWidth(true);
            //tabler.setLockedWidth(true);
            //table.setHorizontalAlignment(Element.ALIGN_LEFT);
            //tabler.setHorizontalAlignment(Element.ALIGN_LEFT);
// Header

            PdfPCell cell1 = new PdfPCell( new Phrase("Code",colfont));
            PdfPCell cell2 = new PdfPCell(new Phrase("Name",colfont));
            PdfPCell cell3 = new PdfPCell(new Phrase("Qty",colfont));
            PdfPCell cell4 = new PdfPCell(new Phrase("Price",colfont));

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            for (com.regin.reginald.model.OrderLines orderAttributes: lines){


                cell1 = new PdfPCell(new Phrase(orderAttributes.getPastelCode(),colfont));
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell1.setBorder(Rectangle.NO_BORDER);
                cell2 = new PdfPCell(new Phrase(orderAttributes.getPastelDescription(),colfont));
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2.setBorder(Rectangle.NO_BORDER);
                cell3 = new PdfPCell(new Phrase(orderAttributes.getQty(),colfont));
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setBorder(Rectangle.NO_BORDER);
                cell4 = new PdfPCell(new Phrase(orderAttributes.getPrice(),colfont));
                cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell4.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);

            }

            PdfPCell cell5 = new PdfPCell(new Phrase("Code",colfont));
            PdfPCell cell6 = new PdfPCell(new Phrase("Name",colfont));
            PdfPCell cell7 = new PdfPCell(new Phrase("Qty",colfont));
            PdfPCell cell8 = new PdfPCell(new Phrase("Reason",colfont));

            tabler.addCell(cell5);
            tabler.addCell(cell6);
            tabler.addCell(cell7);
            tabler.addCell(cell8);
            for (OrderLines orderAttributes: returns){


                cell5 = new PdfPCell(new Phrase(orderAttributes.getPastelCode(),colfont));
                cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell5.setBorder(Rectangle.NO_BORDER);
                cell6 = new PdfPCell(new Phrase(orderAttributes.getPastelDescription(),colfont));
                cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell6.setBorder(Rectangle.NO_BORDER);
                cell7 = new PdfPCell(new Phrase(orderAttributes.getreturnQty(),colfont));
                cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell7.setBorder(Rectangle.NO_BORDER);
                cell8 = new PdfPCell(new Phrase(orderAttributes.getstrCustomerReason()+"**",colfont));
                cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell8.setBorder(Rectangle.NO_BORDER);
                tabler.addCell(cell5);
                tabler.addCell(cell6);
                tabler.addCell(cell7);
                tabler.addCell(cell8);

            }
            doc.open();
            // PdfContentByte cb = doc.getDirectContent();
            Paragraph storenameva=new Paragraph(storename,colfont);
            //storenameva.setAlignment(Element.ALIGN_LEFT);
            storenameva.setAlignment(Paragraph.ALIGN_LEFT);
            Paragraph invNum=new Paragraph(InvoiceNo,colfont);
            invNum.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(invNum);
            doc.add(storenameva);
            doc.add(table);
            Paragraph CRQ = new Paragraph("---------------------CREDIT REQUISITION---------------------",colfont);
            CRQ.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(CRQ);
            doc.add(tabler);
            Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            image.scaleToFit(1200,152);
            Paragraph paragraph = new Paragraph(".",colfont);
            Paragraph paragraph2 = new Paragraph("------------------------Thank You------------------------------------",colfont);
            Paragraph p=new Paragraph(dateFormat.format(date),colfont);

            p.setAlignment(Paragraph.ALIGN_RIGHT);


            doc.add(paragraph);
            doc.add(image);

            doc.add(paragraph);
            doc.add(paragraph2);
            doc.add(p);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();

        }

    }
    public void printdata(String InvoiceNo)
    {
        //get printer
        ArrayList<OtherAttributes> oD= dbH.getPrinterInfo();
        ArrayList<OrderLines> lines= dbH.returnOrderLines(InvoiceNo);
        int prototype=0;
        String logicalName="";
        String address="";


        for (OtherAttributes orderAttributes: oD){

            prototype  = Integer.parseInt(orderAttributes.getprototype()) ;
            logicalName  = orderAttributes.getprintermodelLogicalName();
            address  = orderAttributes.getaddress();
        }

        boolean syncMode = true;
        if (LandingPage.getPrinterInstance().printerOpen(prototype, logicalName, address)) {


            Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
            //  LandingPage.getPrinterInstance().printImage(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)+"/saved_images/"+InvoiceNo+".jpg",  500,2,100);
            //something for later
            //  LandingPage.getPrinterInstance().printBarcode("http://zappersomething",  500,2,100);
            String filen = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/PDF/" + InvoiceNo+"_"+ts+".pdf";
            // String filen ="/storage/emulated/0/Movies/PDF/IV135717_1547457203.pdf";;
            Log.e("*****","*****file name***"+filen);
            LandingPage.getPrinterInstance().printPdf(filen,  534,-1,0,20,30);
            //LandingPage.getPrinterInstance().printText(System.getProperty ("line.separator"),  LandingPage.getPrinterInstance().ALIGNMENT_LEFT,LandingPage.getPrinterInstance().ATTRIBUTE_FONT_A,100);
            //   LandingPage.getPrinterInstance().printText(System.getProperty ("line.separator"),  LandingPage.getPrinterInstance().ALIGNMENT_LEFT,LandingPage.getPrinterInstance().ATTRIBUTE_FONT_A,100);
            // LandingPage.getPrinterInstance().printText(System.getProperty ("line.separator"),  LandingPage.getPrinterInstance().ALIGNMENT_LEFT,LandingPage.getPrinterInstance().ATTRIBUTE_FONT_A,100);




        } else {

            Toast.makeText(getApplicationContext(), "Fail to printer open!! ",
                    Toast.LENGTH_LONG).show();
        }

    }

}
