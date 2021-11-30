package com.example.outdoorpartners;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.TimeFormat;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class CreateEventActivity extends AppCompatActivity {


    Spinner spinnerEventType;
    EditText editTextEventName;
    EditText editTextDescription;
    Button buttonDate;
    Button buttonTime;
    Button buttonUpload;
    Button buttonCancel;



    String eventName;
    DateFormat eventDate;
    TimeFormat eventTime;
    String eventType;
    String description;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_activity);






        editTextEventName = findViewById(R.id.editTextEventName);
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

        MyEditTextDatePicker datePicker = new MyEditTextDatePicker(this,R.id.buttonSetDate);


    }


    public class MyEditTextDatePicker  implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
        Button buttonText;
        private int _day;
        private int _month;
        private int _year;
        private Context _context;

        public MyEditTextDatePicker(Context context, int editTextViewID)
        {
            Activity act = (Activity)context;
            this.buttonText = (Button) act.findViewById(editTextViewID);
            this.buttonText.setOnClickListener(this);
            this._context = context;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _year = year;
            _month = monthOfYear;
            _day = dayOfMonth;
            updateDisplay();
        }
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

            DatePickerDialog dialog = new DatePickerDialog(_context, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();

        }

        // updates the date in the birth date EditText
        private void updateDisplay() {

            buttonDate.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(_day).append("/").append(_month + 1).append("/").append(_year).append(" "));
        }
    }
}
