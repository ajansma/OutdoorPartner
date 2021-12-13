package com.example.outdoorpartners;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.TimeZone;

public class CreateEventActivity extends AppCompatActivity {
    static final String TAG = "MainActivityTag";

    Spinner spinnerEventType;
    EditText editTextEventName;
    EditText editTextDescription;
    Button buttonDate;
    Button buttonTime;
    Button buttonUpload;
    Button buttonCancel;

    String eventName;
    int eventDay;
    int eventMonth;
    int eventYear;
    int eventMin;
    int eventHour;

    TimeFormat eventTime;
    String eventType;
    String description;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_activity);

        editTextEventName = findViewById(R.id.editTextEventName);
        editTextDescription = findViewById(R.id.editTextEventDescription);
        buttonDate = findViewById(R.id.buttonSetDate);
        buttonTime = findViewById(R.id.buttonSetTime);
        buttonUpload = findViewById(R.id.buttonUploadEvent);
        buttonCancel = findViewById(R.id.buttonCancel);
        spinnerEventType = findViewById(R.id.spinnerEventType);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerEventTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEventType.setAdapter(adapter);
        spinnerEventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                eventType = spinnerEventType.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });



        MyDatePicker datePicker = new MyDatePicker(this,R.id.buttonSetDate);

        MyTimePicker timePicker = new MyTimePicker(this,R.id.buttonSetTime);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pull in information
                description = editTextEventName.getText().toString();
                eventName = editTextDescription.getText().toString();


                Intent intent = new Intent(CreateEventActivity.this, MainActivity.class);

                // put together intents
                intent.putExtra("event_name", eventName);
                intent.putExtra("event_description", description);
                intent.putExtra("day", eventDay);
                intent.putExtra("month", eventMonth);
                intent.putExtra("year", eventYear);
                intent.putExtra("hour", eventHour);
                intent.putExtra("min", eventMin);
                intent.putExtra("type", eventType);

                System.out.println(eventName);

                Log.d(TAG, "onClick");

                // send back to main
                setResult(Activity.RESULT_OK, intent);
                CreateEventActivity.this.finish();


            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                CreateEventActivity.this.finish();
            }
        });
    }



    public class MyTimePicker implements View.OnClickListener, TimePickerDialog.OnTimeSetListener{

        Button buttonText;
        Context context;

        public MyTimePicker(Context context, int editButtonTextId) {
            Activity act = (Activity)context;
            this.buttonText = (Button) act.findViewById(editButtonTextId);
            this.buttonText.setOnClickListener(this);
            this.context = context;
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            eventHour = i;
            eventMin = i1;
            updateDisplay();
        }

        @Override
        public void onClick(View view) {
            TimePickerDialog tpd = new TimePickerDialog(context,this,eventHour,eventMin,false);
            tpd.show();
        }
        private void updateDisplay(){
            if(eventHour>12) {
                buttonTime.setText(new StringBuilder()
                        // Month is 0 based so add 1
                        .append(eventHour % 12).append(":").append(eventMin).append(" PM"));
            }
            else{
                buttonTime.setText(new StringBuilder()
                        // Month is 0 based so add 1
                        .append(eventHour).append(":").append(eventMin).append(" AM"));
            }
        }
    }

    public class MyDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
        Button buttonText;
        private Context context;

        public MyDatePicker(Context context, int editButtonTextId)
        {
            Activity act = (Activity)context;
            this.buttonText = (Button) act.findViewById(editButtonTextId);
            this.buttonText.setOnClickListener(this);
            this.context = context;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            eventYear = year;
            eventMonth = monthOfYear;
            eventDay = dayOfMonth;
            updateDisplay();
        }
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

            DatePickerDialog dialog = new DatePickerDialog(context, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();

        }

        // updates the date in the birth date EditText
        private void updateDisplay() {

            buttonDate.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(eventMonth+1).append("/").append(eventDay).append("/").append(eventYear).append(" "));
        }


    }
}
