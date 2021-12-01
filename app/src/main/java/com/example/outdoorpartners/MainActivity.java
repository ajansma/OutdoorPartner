package com.example.outdoorpartners;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    static final String TAG = "MainActivityTag";
    ArrayList<Event> eventList = new ArrayList<>();
    Event event1 = new Event(1, "Hike at Bowl and Pitcher", R.drawable.bowlpitcher , "5/31/21", "3:30pm", "hike", "good hike", "Spokane");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set up connection between real-time database and app!
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://outdoorpartner-421fa-default-rtdb.firebaseio.com/");
        mDatabaseReference = mFirebaseDatabase.getReference().child("messages");

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

        // TODO: set up click listeners

        // TODO: write to the database
        // use the setValue() to set the value for the current key reference
        // use push to add a key value pair for the current key


        // TODO: Read from the database
        // use child listeners to automatically update data
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // called for each child event that already exists when the listener is first attached
                // called whenever a new event is inserted
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // called when events content change
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // called when the contents of an existing message is deleted
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    //called when the contents of an existing message moved in the List
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // some sort of error occurred

            }
        };

        mDatabaseReference.addChildEventListener(childEventListener);

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