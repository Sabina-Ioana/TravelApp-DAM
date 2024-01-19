package com.example.travelapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.travelapp.R;
import com.example.travelapp.models.Trip;

import java.util.ArrayList;

public class TripAdapter extends ArrayAdapter<Trip> {


        public TripAdapter(@NonNull Context context, ArrayList<Trip> tripList) {
            super(context, 0, tripList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Trip trip = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_trip_layout, parent, false);
            }

            ImageView image = convertView.findViewById(R.id.myTripImage);
            TextView name = convertView.findViewById(R.id.myTripName);
            TextView location = convertView.findViewById(R.id.myTripLocation);
            TextView date = convertView.findViewById(R.id.myTripDate);

            image.setBackgroundResource(trip.getImage());
            name.setText(trip.getName());
            location.setText(trip.getLocation());
            date.setText(trip.getDate());

            return convertView;
        }


}
