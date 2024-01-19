package com.example.travelapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.travelapp.R;
import com.example.travelapp.models.Destination;

import java.util.ArrayList;

public class DestAdapter extends ArrayAdapter<Destination> {

    public DestAdapter(@NonNull Context context, ArrayList<Destination> destList) {
        super(context, 0, destList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Destination destination = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_layout, parent, false);
        }

        View destView = convertView.findViewById(R.id.destView);
        TextView name = convertView.findViewById(R.id.destName);
        TextView location = convertView.findViewById(R.id.location);
        RatingBar rating = convertView.findViewById(R.id.ratingBar);

        destView.setBackgroundResource(destination.getImage());
        name.setText(destination.getName());
        location.setText(destination.getLocation());
        rating.setRating(destination.getRating());

        return convertView;
    }
}
