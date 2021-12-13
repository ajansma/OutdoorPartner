package com.example.outdoorpartners;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.outdoorpartners.databinding.ActivityMainBinding;
import com.google.android.gms.instantapps.Launcher;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    CustomAdapter adapter = new CustomAdapter();
    static final String TAG = "MainActivityTag";
    ArrayList<Event> eventList = new ArrayList<>();

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView navView;

    //intents
    Intent intentMainToFind;
    Intent intentMainToCreate;

    ActivityResultLauncher<Intent> launcher;

    Event event1 = new Event("5/31/21", "Good Hike", 1, R.drawable.bowlpitcher,  "Spokane", "Bowl and Pitcher", "3:30pm", "hike");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //variables
        intentMainToFind = new Intent(MainActivity.this, FindEventsActivity.class);
        intentMainToCreate = new Intent(MainActivity.this, CreateEventActivity.class);

        //eventList.add(event1);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch(itemId){
                    case R.id.create_events:
                        launcher.launch(intentMainToCreate);
                        Log.d(TAG, "maintocreate:");
                        break;
                    case R.id.view_events:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.find_events:
                        launcher.launch(intentMainToFind);
                        break;
                }
                return true;
            }
        });

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set up connection between real-time database and app!
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://outdoorpartner-421fa-default-rtdb.firebaseio.com/");
        mDatabaseReference = mFirebaseDatabase.getReference().child("events");


        // set up the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // set up a custom adapter
        //CustomAdapter adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);

        // TODO: set up click listeners

        // TODO: write to the database
        mDatabaseReference.push().setValue(event1);


        // TODO: Read from the database
        // use child listeners to automatically update data
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // called for each child event that already exists when the listener is first attached
                // called whenever a new event is inserted
                Event event = null;
                if(dataSnapshot != null) {
                    event = dataSnapshot.getValue(Event.class);
                }
                if(event != null){
                     eventList.add(event);
                }
                MainActivity.this.adapter.notifyItemChanged(eventList.size());
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
;

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                    }

                });




    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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