package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DestActivity extends AppCompatActivity {

    Button selectDestBtn;
    int destId;
    String name;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dest);

        selectDestBtn = (Button)findViewById(R.id.selectDestBtn);

        Intent intent = this.getIntent();
        if (intent != null) {
            name = intent.getStringExtra("name");
            String location = intent.getStringExtra("location");
            int image = intent.getIntExtra("image", 0);
            float rating = intent.getFloatExtra("rating", 0);
            String description = intent.getStringExtra("description");
            destId = intent.getIntExtra("destId", -1);
            userId = intent.getIntExtra("userId", -1);

            TextView destName = (TextView)findViewById(R.id.destinationName);
            TextView destLocation = (TextView)findViewById(R.id.destinationLocation);
            View destView = (View)findViewById(R.id.destinationView);
            RatingBar destRating = (RatingBar)findViewById(R.id.destinationRating);
            TextView destDescription = (TextView)findViewById(R.id.destinationDescription);

            destName.setText(name);
            destLocation.setText(location);
            destView.setBackgroundResource(image);
            destRating.setRating(rating);
            destDescription.setText(description);
            destDescription.setMovementMethod(new ScrollingMovementMethod());
        }

        selectDestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DestActivity.this, SelectDestActivity.class);
                intent.putExtra("destId", destId);
                intent.putExtra("name", name);
                intent.putExtra("userId", userId);
                startActivity(intent);
                //finish();
            }
        });
    }
}