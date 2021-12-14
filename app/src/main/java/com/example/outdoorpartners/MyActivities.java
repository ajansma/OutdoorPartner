package com.example.outdoorpartners;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyActivities extends AppCompatActivity {
    EventDatabaseHelper localEventHelper;

    /*
    public MyActivities(Event event){
        System.out.println("Printing Event");
        localEventHelper = new EventDatabaseHelper(this);
        localEventHelper.insertContact(event);
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activities);
        RecyclerView recyclerView = findViewById(R.id.thisRecycle);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        CustomAdapter adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
        localEventHelper = new EventDatabaseHelper(this);
        Event event1 = new Event("good hike", 1, R.drawable.bowlpitcher,"spokane", "hike", 2021, 01, 01, 10, 10, "hike");
        localEventHelper.insertContact(event1);
        adapter.notifyDataSetChanged();
    }

    public void insertEvent(Event event){
        System.out.println("Printing Event");
        localEventHelper = new EventDatabaseHelper(this);
        localEventHelper.insertContact(event);
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView myTitle;
            ImageView myImagePreview;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                myTitle = itemView.findViewById(R.id.myTitle);
                myImagePreview = itemView.findViewById(R.id.imagePreview);

                itemView.setOnClickListener(this);
            }

            public void updateView(Event event){
                System.out.println("VIDEO" + event);
                myTitle.setText(event.getName());
                myImagePreview.setImageResource(event.getImage());
            }

            @Override
            public void onClick(View view) {
                List<Integer> ids = localEventHelper.getSelectAllIds();
                Event event = localEventHelper.getSelectEventById(ids.get(getAdapterPosition()));
                Intent intent = new Intent(MyActivities.this, event_details.class);

                // put together intents
                intent.putExtra("event_name", event.getName());
                intent.putExtra("event_description", event.getDescription());
                intent.putExtra("day", event.getDay());
                intent.putExtra("month", event.getMonth());
                intent.putExtra("year", event.getYear());
                intent.putExtra("hour", event.getHour());
                intent.putExtra("min", event.getMin());
                intent.putExtra("type", event.getType());

                startActivity(intent);
                Log.d(MainActivity.TAG, "Clicked");

            }
        }
            @NonNull
            @Override
            public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(MyActivities.this).inflate(R.layout.cardview, parent, false);
                return new CustomViewHolder(view);
            }


            @Override
            public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
                List<Integer> ids = localEventHelper.getSelectAllIds();
                Event event = localEventHelper.getSelectEventById(ids.get(position));
                holder.updateView(event);
            }

            @Override
            public int getItemCount() {
                System.out.println("Size: " + localEventHelper.getSelectAllContacts().size());
                return localEventHelper.getSelectAllContacts().size();

            }

        }
}
