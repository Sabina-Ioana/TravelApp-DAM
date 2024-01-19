package com.example.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.travelapp.Connection.ConnectionClass;
import com.example.travelapp.adapters.TripAdapter;
import com.example.travelapp.models.Trip;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MyTripsActivity extends AppCompatActivity {

    Connection con;
    TripAdapter adapter;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);

        ListView tripsListView = (ListView)findViewById(R.id.myTripsListView);
        ArrayList<Trip> data = new ArrayList<>();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.myTripsNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.myTripsNav:
                        Intent intent = new Intent(getApplicationContext(), MyTripsActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.destNav:
                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                        intent2.putExtra("userId", userId);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.logoutNav:
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        Intent intent = this.getIntent();
        if (intent != null) {
            userId = intent.getIntExtra("userId", -1);
        }

        try {
            con = connectionClass(ConnectionClass.username.toString(), ConnectionClass.password.toString(), ConnectionClass.database.toString(), ConnectionClass.ip.toString(), ConnectionClass.port.toString());
            if (con != null) {
                String query = "SELECT destinations.name, destinations.location, destinations.image, trips.date from destinations inner join trips on destinations.destId = trips.destId where trips.userId ='" + userId + "'";
                Statement stmt = con.createStatement();
                ResultSet resultSet = stmt.executeQuery(query);

                while (resultSet.next()) {
                    Trip tab = new Trip();
                    tab.setName(resultSet.getString("name"));
                    tab.setLocation(resultSet.getString("location"));
                    int resID = getResources().getIdentifier(resultSet.getString("image"), "drawable", getPackageName());
                    tab.setImage(resID);
                    tab.setDate(resultSet.getDate("date").toString());
                    data.add(tab);
                }

                adapter = new TripAdapter(MyTripsActivity.this, data);

                tripsListView.setAdapter(adapter);
            }
        }
        catch(Exception e) {
            Log.e("Set error: ", e.getMessage());
        }

    }

    @SuppressLint("NewApi")
    public Connection connectionClass(String user, String password, String database, String ip, String port) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database;
            connection = DriverManager.getConnection(connectionURL, user, password);
        } catch (Exception e) {
            Log.e("SQL Connection Error : ", e.getMessage());
        }

        return connection;
    }
}