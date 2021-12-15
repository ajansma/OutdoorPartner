package com.example.outdoorpartners;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.outdoorpartners.databinding.ActivityMainBinding;
import com.google.android.gms.instantapps.Launcher;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/*
Final Project 321
 */

public class MainActivity extends AppCompatActivity {
    // sqlite database
    EventDatabaseHelper localEventHelper = new EventDatabaseHelper(this);
    //firebase database
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
    Intent intentMainToMyActivies;

    ActivityResultLauncher<Intent> launcher;

    public static ArrayList<Event> eventsToAdd = new ArrayList<>();
    public static ArrayList<Event> eventsToRemove = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //variables
        intentMainToFind = new Intent(MainActivity.this, FindEventsActivity.class);
        intentMainToCreate = new Intent(MainActivity.this, CreateEventActivity.class);
        intentMainToMyActivies = new Intent(MainActivity.this, MyActivities.class);

        //eventList.add(event1);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // add extra dataase info

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        // set up connection between real-time database and app!
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://outdoorpartner-421fa-default-rtdb.firebaseio.com/");
        mDatabaseReference = mFirebaseDatabase.getReference().child("events");


        // set up the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // set up a custom adapter
        recyclerView.setAdapter(adapter);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // first check if extra things need to be added to database
                        if(eventsToAdd.size() != 0){
                            System.out.println("onCreate");
                            int i = 0;
                            while(eventsToAdd.size() != 0) {
                                localEventHelper.insertContact(eventsToAdd.get(i));
                                eventsToAdd.remove(i);
                                i++;
                            }
                            adapter.notifyDataSetChanged();
                        }

                        if(eventsToRemove.size() != 0){
                            int i = 0;
                            Event e2 = eventsToRemove.get(i);
                            while (eventsToRemove.size() != 0) {
                                List<Event> currEvents = localEventHelper.getSelectAllContacts();
                                for (int j = 0; j < currEvents.size(); j++) {
                                    Event e = currEvents.get(j);
                                    if (e.getName().equals(e2.getName()) && e.getDescription().equals(e2.getDescription())) {
                                        if (e.getDay() == e2.getDay()) {
                                            List<Integer> ids = localEventHelper.getSelectAllIds();
                                            localEventHelper.deleteEventById(ids.get(j));
                                        }
                                    }
                                }
                                eventsToRemove.remove(i);
                                i++;
                            }
                            adapter.notifyDataSetChanged();
                        }
                        // get intent
                        if(result.getResultCode() == RESULT_OK){
                            Intent intent = result.getData();
                            if(intent != null){
                                String event_name = intent.getStringExtra("event_name");
                                System.out.println(event_name);
                                String event_description = intent.getStringExtra("event_description");
                                int month = intent.getIntExtra("month", -1);
                                int year =  intent.getIntExtra("year", -1);
                                int day = intent.getIntExtra("day", -1);
                                int hour = intent.getIntExtra("hour", -1);
                                int eventMin = intent.getIntExtra("min", -1);
                                String type = intent.getStringExtra("type");
                                Double lat = intent.getDoubleExtra("lat",-1);
                                Double lng = intent.getDoubleExtra("lng",-1);
                                String locationName = intent.getStringExtra("locationName");

                                int i = 0;
                                while(eventsToAdd.size() != 0){
                                    localEventHelper.insertContact(eventsToAdd.get(i));
                                    eventsToAdd.remove(i);
                                    i++;
                                }

                                Event event1 = new Event(event_description, 1, R.drawable.bowlpitcher,"Spokane", event_name, year, month, day, hour, eventMin, type, lat, lng, locationName);
                                mDatabaseReference.push().setValue(event1);
                            }
                        }
                        else if(result.getResultCode() == RESULT_FIRST_USER){
                            Intent intent = result.getData();
                            if(intent != null) {
                                Log.d(TAG, "RESULTS");
                                // pull apart intents
                                int event_id = intent.getIntExtra("event_id", -1);
                                String event_name = intent.getStringExtra("event_name");
                                System.out.println(event_name);
                                String event_description = intent.getStringExtra("event_description");
                                int month = intent.getIntExtra("month", -1);
                                int year = intent.getIntExtra("year", -1);
                                int day = intent.getIntExtra("day", -1);
                                int hour = intent.getIntExtra("hour", -1);
                                int eventMin = intent.getIntExtra("min", -1);
                                String type = intent.getStringExtra("type");
                                Double lat = intent.getDoubleExtra("lat", -1);
                                Double lng = intent.getDoubleExtra("lng", -1);
                                String locationName = intent.getStringExtra("locationName");
                                Boolean checked = intent.getBooleanExtra("checked_event", false);
                                System.out.println("CHECKED " + checked);
                                // check if already in list
                                List<Event> currEvents = localEventHelper.getSelectAllContacts();
                                for(int i = 0; i <= currEvents.size(); i++){
                                    Event e = currEvents.get(i);
                                    if(e.getName().equals(event_name) && e.getDescription().equals(event_description)){
                                            if(e.getDay() == day){
                                                if(!checked){
                                                    List<Integer> ids = localEventHelper.getSelectAllIds();
                                                    localEventHelper.deleteEventById(ids.get(i));
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                    }
                                    else if (checked){
                                        Event event = new Event(event_description, -1, R.drawable.placeholder, locationName, event_name, year, month, day, hour, eventMin, type);
                                        localEventHelper.insertContact(event);
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                            }
                        }
                    }
                });

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
                adapter.notifyDataSetChanged();
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
                Log.d(TAG, "error: onCancelled");
            }
        };

        mDatabaseReference.addChildEventListener(childEventListener);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // menu stuff
    /*
   Create menu with add and delete buttons
    */


    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{
        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
            TextView myTitle;
            ImageView myImagePreview;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                myTitle = itemView.findViewById(R.id.myTitle);
                myImagePreview = itemView.findViewById(R.id.imagePreview);

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            public void updateView(Event event) {
                myTitle.setText(event.getName());
                myImagePreview.setImageResource(getDrawableImage(event.type));
            }


            @Override
            public void onClick(View view) {
                // get current vid
                Intent intent = new Intent(MainActivity.this, event_details.class);
                List<Integer> ids = localEventHelper.getSelectAllIds();

                Event event = localEventHelper.getSelectEventById(ids.get(getAdapterPosition()));

                // put together intents
                intent.putExtra("event_name", event.getName());
                intent.putExtra("event_description", event.getDescription());
                intent.putExtra("day", event.getDay());
                intent.putExtra("month", event.getMonth());
                intent.putExtra("year", event.getYear());
                intent.putExtra("hour", event.getHour());
                intent.putExtra("min", event.getMin());
                intent.putExtra("type", event.getType());
                intent.putExtra("checked_event", true);

                launcher.launch(intent);
                Log.d(TAG, "Clicked");
            }

            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Remove Event")
                        .setMessage("Are you sure you want to delete this event?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // delete video if user clicks yes
                                List<Integer> ids = localEventHelper.getSelectAllIds();
                                localEventHelper.deleteEventById(ids.get(getAdapterPosition()));
                                adapter.notifyItemRemoved(getAdapterPosition());
                            }
                        })
                        .setNegativeButton("No", null);
                builder.show();
                return false;
            }
        }

        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.cardview, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            List<Integer> ids = localEventHelper.getSelectAllIds();
            Event event = localEventHelper.getSelectEventById(ids.get(position));
            holder.updateView(event);
        }

        /*
        This method keeps track of how many items are in the recycler view
         */
        @Override
        public int getItemCount() {
            return localEventHelper.getSelectAllContacts().size();
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

// recycler view