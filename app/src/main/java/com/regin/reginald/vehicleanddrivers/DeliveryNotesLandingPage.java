package com.regin.reginald.vehicleanddrivers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DeliveryNotesLandingPage extends AppCompatActivity {


    Button createdeliverynote,viewdeliverynotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deliverynoteslandingpage);

        createdeliverynote = (Button) findViewById(R.id.createdeliverynote);
        viewdeliverynotes = (Button) findViewById(R.id.viewdeliverynotes);

        createdeliverynote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent n = new Intent(DeliveryNotesLandingPage.this,CreateDeliveryNote.class);
                startActivity(n);
            }
        });

        viewdeliverynotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
