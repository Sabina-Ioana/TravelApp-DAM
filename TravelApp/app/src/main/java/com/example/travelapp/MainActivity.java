package com.example.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.travelapp.Connection.ConnectionClass;
import com.example.travelapp.adapters.DestAdapter;
import com.example.travelapp.models.Destination;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Connection connection;
    DestAdapter adapter;
    int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView destListView = (ListView) findViewById(R.id.destListView);
        ArrayList<Destination> data = new ArrayList<Destination>();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.destNav);
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
            connection = connectionClass(ConnectionClass.username.toString(), ConnectionClass.password.toString(), ConnectionClass.database.toString(), ConnectionClass.ip.toString(), ConnectionClass.port.toString());
            if (connection != null) {
                String query = "SELECT * FROM destinations";
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(query);

                while (resultSet.next()) {
                    Destination tab = new Destination();
                    tab.setName(resultSet.getString("name"));
                    tab.setLocation(resultSet.getString("location"));
                    int resID = getResources().getIdentifier(resultSet.getString("image"), "drawable", getPackageName());
                    tab.setImage(resID);
                    tab.setRating(resultSet.getFloat("rating"));
                    data.add(tab);
                }

                adapter = new DestAdapter(MainActivity.this, data);

                destListView.setAdapter(adapter);
                destListView.setClickable(true);
                destListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, DestActivity.class);
                        intent.putExtra("name", data.get(position).getName());
                        intent.putExtra("location", data.get(position).getLocation());
                        intent.putExtra("image", data.get(position).getImage());
                        intent.putExtra("rating", data.get(position).getRating());
                        intent.putExtra("userId", userId);

                        String sql = "SELECT * FROM destinations WHERE name='" + data.get(position).getName() +"'";
                        try {
                            ResultSet result = stmt.executeQuery(sql);
                            if (result.next()) {
                                intent.putExtra("description", result.getString("description"));
                                intent.putExtra("destId", result.getInt("destId"));
                            }
                        } catch (Exception e) {
                            Log.e("SQL error: ", e.getMessage());
                        }
                        startActivity(intent);
                    }
                });

            }

        } catch (Exception e) {
            Log.e("set Error: ", e.getMessage());
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