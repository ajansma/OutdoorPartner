package com.example.outdoorpartners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.session.PlaybackState;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void updateView() {

            }


            @Override
            public void onClick(View view) {

            }
        }

            @NonNull
            @Override
            public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new CustomViewHolder(parent);
            }

            @Override
            public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
                holder.updateView();
            }

            /*
            This method keeps track of how many items are in the recycler view
             */
            @Override
            public int getItemCount() {
                return 0;
                //return videoList.getList().size();
            }
    }
}

// recycler view