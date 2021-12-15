package com.example.outdoorpartners;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.example.outdoorpartners.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FindEventsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener{
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    String TAG = "MainActivityTag";
    LatLng userLatLng;
    Marker marker;

    static final int LOCATION_REQUEST_CODE = 1;//needs to be unique within your app
    Button buttonViewDetails;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    List<Event> events;
    int currentEvent;


    ActivityResultLauncher<Intent> launcher;
    Intent intentMarkerToDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentMarkerToDetails = new Intent(FindEventsActivity.this, event_details.class);
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://outdoorpartner-421fa-default-rtdb.firebaseio.com/");
        mDatabaseReference = mFirebaseDatabase.getReference().child("events");
        events = new ArrayList<>();
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currentEvent = -1;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        buttonViewDetails = new Button(this);
        buttonViewDetails.setText("View Details of ");
        addContentView(buttonViewDetails, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
        buttonViewDetails.setBackgroundColor(getResources().getColor(R.color.dark_green));

        buttonViewDetails.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(currentEvent ==-1){
                    Toast.makeText(getApplicationContext(),"Must select a marker event to view details", Toast.LENGTH_SHORT).show();
                    return;
                }

                intentMarkerToDetails.putExtra("event_id",events.get(currentEvent).getEvent_id());
                intentMarkerToDetails.putExtra("event_name",events.get(currentEvent).getName());
                intentMarkerToDetails.putExtra("event_description",events.get(currentEvent).getDescription());
                intentMarkerToDetails.putExtra("image",events.get(currentEvent).getImage());
                intentMarkerToDetails.putExtra("day",events.get(currentEvent).getDay());
                intentMarkerToDetails.putExtra("month",events.get(currentEvent).getMonth());
                intentMarkerToDetails.putExtra("year",events.get(currentEvent).getYear());
                intentMarkerToDetails.putExtra("minute",events.get(currentEvent).getMin());
                intentMarkerToDetails.putExtra("hour",events.get(currentEvent).getHour());
                intentMarkerToDetails.putExtra("lat",events.get(currentEvent).getLatitude());
                intentMarkerToDetails.putExtra("lng",events.get(currentEvent).getLongitude());
                intentMarkerToDetails.putExtra("type",events.get(currentEvent).getType());
                intentMarkerToDetails.putExtra("loc_name",events.get(currentEvent).getLocationName());

                launcher.launch(intentMarkerToDetails);
            }
        });

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // called for each child event that already exists when the listener is first attached
                // called whenever a new event is inserted
                Event event = null;
                if(dataSnapshot != null) {
                    event = dataSnapshot.getValue(Event.class);
                    events.add(event);
                    dropMarkers(event);
                }
                if(event != null){
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
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

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                    }
                });

    }

    public void dropMarkers(Event e){
        LatLng latLng = new LatLng(e.getLatitude(), e.getLongitude());
        String name = e.getName();
        String description = String.valueOf(events.size()-1);
        try {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(name);
            markerOptions.snippet(description);
            markerOptions.position(latLng);
            mMap.addMarker(markerOptions);

        } catch(IllegalArgumentException exception){}
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        enableMyLocation();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                buttonViewDetails.setText("View Details of " + marker.getTitle());
                currentEvent = Integer.parseInt(marker.getSnippet());
                return false;
            }
        });
    }

    private void enableMyLocation(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationClickListener(this);


        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                enableMyLocation();
            }
            else{
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }



}
