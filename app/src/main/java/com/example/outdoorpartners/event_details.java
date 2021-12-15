package com.example.outdoorpartners;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class event_details extends AppCompatActivity {
    CheckBox checkBox;
    Button buttonMain;
    TextView textViewName;
    TextView textViewDescription;
    Button buttonDate;
    Button buttonTime;

    ImageView imageView;
    Intent intentDetailsToMain;
    ActivityResultLauncher<Intent> launcher;

    //Event Data
    int event_id;
    String name;
    int image;
    int day;
    int month;
    int year;
    int hour;
    int min;
    String type = "";
    String description = "";
    Double latitude;
    Double longitude;
    String locationName = "";

    StringBuilder stringBuilderDate;
    StringBuilder stringBuilderTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        intentDetailsToMain = new Intent(event_details.this, MainActivity.class);
        // get intent
        Intent intent = getIntent();
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                    }
                });

        stringBuilderTime = new StringBuilder();
        textViewName = findViewById(R.id.event_name);
        textViewDescription = findViewById(R.id.description);
        buttonDate = findViewById(R.id.button);
        buttonTime = findViewById(R.id.button2);
        checkBox = findViewById(R.id.checkJoinEvent);
        buttonMain = findViewById(R.id.buttonMain);
        imageView = findViewById(R.id.imageView);

        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentDetailsToMain.putExtra("event_id",event_id);
                intentDetailsToMain.putExtra("event_name",name);
                intentDetailsToMain.putExtra("event_description",description);
                intentDetailsToMain.putExtra("image",image);
                intentDetailsToMain.putExtra("day",day);
                intentDetailsToMain.putExtra("month",month);
                intentDetailsToMain.putExtra("year",year);
                intentDetailsToMain.putExtra("minute",min);
                intentDetailsToMain.putExtra("hour",hour);
                intentDetailsToMain.putExtra("lat",latitude);
                intentDetailsToMain.putExtra("lng",longitude);
                intentDetailsToMain.putExtra("type",type);
                intentDetailsToMain.putExtra("loc_name",locationName);
                intentDetailsToMain.putExtra("checked_event", checkBox.isChecked());
                System.out.println("Check" + checkBox.isChecked());

                // send back to main
                setResult(RESULT_FIRST_USER, intentDetailsToMain);

                event_details.this.finish();
           }
        });

        // display screen
        if(intent != null){
            // pull apart intent
            event_id = intent.getIntExtra("event_id", -1);
            name = intent.getStringExtra("event_name");
            description = intent.getStringExtra("event_description");
            image = intent.getIntExtra("image", -1);
            day = intent.getIntExtra("day", -1);
            month = intent.getIntExtra("month", -1);
            year =  intent.getIntExtra("year", -1);
            min = intent.getIntExtra("min", -1);
            hour = intent.getIntExtra("hour", -1);
            latitude = intent.getDoubleExtra("lat", -1);
            longitude = intent.getDoubleExtra("lng", -1);
            type = intent.getStringExtra("type");
            locationName = intent.getStringExtra("loc_name");
            boolean checked = intent.getBooleanExtra("checked_event", false);

            // update view
            textViewName.setText(name);
            textViewDescription.setText(description);
            checkBox.setChecked(checked);
            imageView.setImageResource(getDrawableImage(type));


            // set date
            buttonDate.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(month+1).append("/").append(day).append("/").append(year).append(" "));

            if(hour>12){
                stringBuilderTime.append(hour%12).append(":");
                if(min<10){
                    stringBuilderTime.append("0" + min);
                }
                else{
                    stringBuilderTime.append(min);
                }
                stringBuilderTime.append(" PM");
            }
            else{
                stringBuilderTime.append(hour).append(":");
                if(min<10){
                    stringBuilderTime.append("0" + min);
                }
                else{
                    stringBuilderTime.append(min);
                }
                stringBuilderTime.append(" AM");
            }
            buttonTime.setText(stringBuilderTime);

        }


    }
    public int getDrawableImage(String type){
        if(type == "Hike"){
            return R.drawable.hiking;
        }
        if(type == "Bike"){
            return R.drawable.bike;
        }
        if(type == "Trail Run"){
            return R.drawable.running;
        }
        if(type == "Yoga"){
            return R.drawable.meditation;
        }
        if(type == "Rock Climb"){
            return R.drawable.rock;
        }
        if(type == "Hammock Sesh"){
            return R.drawable.hammock;
        }
        if(type == "Swim"){
            return R.drawable.swimming;
        }
        if(type == "Cliff Jump"){
            return R.drawable.base;
        }

        return R.drawable.placeholder;
    }
}