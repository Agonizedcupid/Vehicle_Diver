package com.regin.reginald.vehicleanddrivers;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
//import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.regin.reginald.vehicleanddrivers.PrinterControl.BixolonPrinter;

public class TestTextFragment extends AppCompatActivity {

    private EditText textData = null;

    private CheckBox checkBold = null;
    private CheckBox checkUnderline = null;
    private CheckBox checkReverse = null;

    private TextView deviceMessagesTextView;

    private int spinnerAlignment = 0;
    private int spinnerFont = 0;
    private int spinnerSize = 0;
    private static BixolonPrinter bxlPrinter = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int ANDROID_NOUGAT = 24;
        if(Build.VERSION.SDK_INT >= ANDROID_NOUGAT)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

       // bxlPrinter = new BixolonPrinter(TestTextFragment.this);


        String strData = "My test  JHJFDSDHXVDSNBJVBJSDBJVBJDS XCJ NDXBUHFJENBJ UDS  MDUVIBNSJ CUD HC J EAGBVUIFBA J ABUIVBAEJBGRIJ djhsbdsguifjbsa csjbjchjksdbjkchijsdbjfkch ijdsfjshvjdsbjichsjjsjsjsjsjs   sjsbjicshjkdbsjhfjksbdjishdjsdhcsj" + "\n";
        int alignment, attribute = 0;
        //alignment = MainActivity.getPrinterInstance().ALIGNMENT_LEFT;
       // attribute |= MainActivity.getPrinterInstance().ATTRIBUTE_FONT_A;
        LandingPage.getPrinterInstance().printText(strData,  LandingPage.getPrinterInstance().ALIGNMENT_LEFT, LandingPage.getPrinterInstance().ATTRIBUTE_FONT_A, 100);
    }
    public static BixolonPrinter getPrinterInstance()
    {
        return bxlPrinter;
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);

        view.findViewById(R.id.buttonPrintText).setOnClickListener(this);

        textData = view.findViewById(R.id.editTextData);
        textData.setText("Bixolon Text Print!!\n");

        checkBold = view.findViewById(R.id.checkBoxBold);
        checkUnderline = view.findViewById(R.id.checkBoxUnderline);
        checkReverse = view.findViewById(R.id.checkBoxReverse);

        deviceMessagesTextView = view.findViewById(R.id.textViewDeviceMessages);
        deviceMessagesTextView.setMovementMethod(new ScrollingMovementMethod());
        deviceMessagesTextView.setVerticalScrollBarEnabled(true);

        Spinner textAlignment = view.findViewById(R.id.textAlignment);
        Spinner textFont = view.findViewById(R.id.textFont);
        Spinner textSize = view.findViewById(R.id.textSize);

        ArrayAdapter alignmentAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.Alignment, android.R.layout.simple_spinner_dropdown_item);
        alignmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textAlignment.setAdapter(alignmentAdapter);
        textAlignment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                spinnerAlignment = position;
            }

            public void onNothingSelected(AdapterView<?> parent)
            {}
        });

        ArrayAdapter fontAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.textFont, android.R.layout.simple_spinner_dropdown_item);
        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textFont.setAdapter(fontAdapter);
        textFont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                spinnerFont = position;
            }

            public void onNothingSelected(AdapterView<?> parent)
            {}
        });

        ArrayAdapter sizeAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.textSize, android.R.layout.simple_spinner_dropdown_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textSize.setAdapter(sizeAdapter);
        textSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                spinnerSize = position;
            }

            public void onNothingSelected(AdapterView<?> parent)
            {}
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.buttonPrintText:
                String strData = textData.getText().toString() + "\n";
                int alignment, attribute = 0;

                switch(spinnerAlignment)
                {
                    case 0:		alignment = MainActivity.getPrinterInstance().ALIGNMENT_LEFT;		break;
                    case 1:		alignment = MainActivity.getPrinterInstance().ALIGNMENT_CENTER;	    break;
                    case 2:		alignment = MainActivity.getPrinterInstance().ALIGNMENT_RIGHT;		break;
                    default:	alignment = MainActivity.getPrinterInstance().ALIGNMENT_LEFT;		break;
                }

                switch(spinnerFont)
                {
                    case 0:		attribute |= MainActivity.getPrinterInstance().ATTRIBUTE_FONT_A;	break;
                    case 1:		attribute |= MainActivity.getPrinterInstance().ATTRIBUTE_FONT_B;	break;
                    case 2:		attribute |= MainActivity.getPrinterInstance().ATTRIBUTE_FONT_C;	break;
                    default:	attribute |= MainActivity.getPrinterInstance().ATTRIBUTE_FONT_A;	break;
                }

                if(checkBold.isChecked())
                {
                    attribute |= MainActivity.getPrinterInstance().ATTRIBUTE_BOLD;
                }

                if(checkUnderline.isChecked())
                {
                    attribute |= MainActivity.getPrinterInstance().ATTRIBUTE_UNDERLINE;
                }

                if(checkReverse.isChecked())
                {
                    attribute |= MainActivity.getPrinterInstance().ATTRIBUTE_REVERSE;
                }

                MainActivity.getPrinterInstance().printText(strData, alignment, attribute, (spinnerSize + 1));

                break;
        }
    }

    public void setDeviceLog(String data)
    {
        mHandler.obtainMessage(0, 0, 0, data).sendToTarget();
    }

    public final Handler mHandler = new Handler(new Handler.Callback()
    {
        @SuppressWarnings("unchecked")
        @Override
        public boolean handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    deviceMessagesTextView.append((String)msg.obj + "\n");

                    Layout layout = deviceMessagesTextView.getLayout();
                    if (layout != null) {
                        int y = layout.getLineTop(
                                deviceMessagesTextView.getLineCount()) - deviceMessagesTextView.getHeight();
                        if (y > 0) {
                            deviceMessagesTextView.scrollTo(0, y);
                            deviceMessagesTextView.invalidate();
                        }
                    }
                    break;
            }
            return false;
        }
    });*/
}