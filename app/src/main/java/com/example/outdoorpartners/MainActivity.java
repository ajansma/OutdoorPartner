package com.example.outdoorpartners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.session.PlaybackState;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;

    static final String TAG = "MainActivityTag";
    ArrayList<Event> eventList = new ArrayList<>();
    Event event1 = new Event(1, "Hike at Bowl and Pitcher", R.drawable.bowlpitcher , "5/31/21", "3:30pm", "hike", "good hike", "Spokane");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://outdoorpartner-421fa-default-rtdb.firebaseio.com/");
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://outdoorpartner-421fa-default-rtdb.firebaseio.com/");
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");


        super.onCreate(savedInstanceState);
        eventList.add(event1);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // set up the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // set up a custom adapter
        CustomAdapter adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);

        // set up listeners

    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{
        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView myTitle;
            ImageView myImagePreview;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                myTitle = itemView.findViewById(R.id.myTitle);
                myImagePreview = itemView.findViewById(R.id.imagePreview);

                itemView.setOnClickListener(this);
            }

            public void updateView(Event event) {
                myTitle.setText(event.getName());
                myImagePreview.setImageResource(event.getImage());
            }


            @Override
            public void onClick(View view) {
                Log.d(TAG, "Clicked");
            }
        }

            @NonNull
            @Override
            public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.cardview, parent, false);
                return new CustomViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
                Log.d(TAG, "Good luck");
                Event event = eventList.get(position);
                holder.updateView(event);
            }

            /*
            This method keeps track of how many items are in the recycler view
             */
            @Override
            public int getItemCount() {
                return eventList.size();
                //return videoList.getList().size();
            }
    }
}

// recycler view