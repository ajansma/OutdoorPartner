package com.example.outdoorpartners;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class event_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // get intent
        Intent intent = getIntent();

        // get text views
        TextView name = findViewById(R.id.event_name);
        TextView description = findViewById(R.id.description);
        Button date = findViewById(R.id.button);
        Button time = findViewById(R.id.button2);

        // display screen
        if(intent != null){
            // pull apart intent
            String event_name = intent.getStringExtra("event_name");
            String event_description = intent.getStringExtra("event_description");
            int month = intent.getIntExtra("month", -1);
            int year =  intent.getIntExtra("year", -1);
            int day = intent.getIntExtra("day", -1);
            int hour = intent.getIntExtra("hour", -1);
            int eventMin = intent.getIntExtra("min", -1);

            // update view
            name.setText(event_name);
            description.setText(event_description);

            // set date
            date.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(month+1).append("/").append(day).append("/").append(year).append(" "));

            // set time button
            if(hour>12) {
                time.setText(new StringBuilder()
                        // Month is 0 based so add 1
                        .append(hour % 12).append(":").append(eventMin).append(" PM"));
            }
            else{
                time.setText(new StringBuilder()
                        // Month is 0 based so add 1
                        .append(hour).append(":").append(eventMin).append(" AM"));
            }

        }
    }
}