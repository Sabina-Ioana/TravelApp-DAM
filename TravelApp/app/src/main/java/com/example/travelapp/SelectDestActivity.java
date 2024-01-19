package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.travelapp.Connection.ConnectionClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SelectDestActivity extends AppCompatActivity {

    TextView selectDestName;
    Button saveDestBtn;
    CalendarView calendarView;
    Connection con;
    Statement stmt;
    int destId;
    int userId;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dest);

        selectDestName = (TextView)findViewById(R.id.selectDestName);
        saveDestBtn = (Button)findViewById(R.id.saveDestBtn);
        calendarView = (CalendarView)findViewById(R.id.calendarView);

        Intent intent = this.getIntent();
        if (intent != null) {

            destId = intent.getIntExtra("destId", -1);
            userId = intent.getIntExtra("userId", -1);

            selectDestName.setText(intent.getStringExtra("name"));

        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                String  curDate = String.valueOf(dayOfMonth);
                String  Year = String.valueOf(year);
                String  Month = String.valueOf(month + 1);
                date = Year + "-" + Month + "-" + curDate;

                Log.e("date",Year+"/"+Month+"/"+curDate);
            }
        });

        saveDestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                  
                    con = connectionClass(ConnectionClass.username.toString(),ConnectionClass.password.toString(),ConnectionClass.database.toString(),ConnectionClass.ip.toString(), ConnectionClass.port.toString());
                    if(con != null) {
                        String sql = "INSERT INTO trips (userId,destId,date) VALUES ('"+userId+"','"+destId+"','"+date+"');";
                        stmt = con.createStatement();
                        stmt.executeUpdate(sql);

                    }
                    Intent intent = new Intent(SelectDestActivity.this, MyTripsActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);

                }catch (Exception e){
                    Log.e("SQL Error: ", e.getMessage());
                }
            }
        });


    }

    @SuppressLint("NewApi")
    public Connection connectionClass(String user, String password, String database, String ip, String port){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionURL = null;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database;
            connection = DriverManager.getConnection(connectionURL, user, password);
        }catch (Exception e){
            Log.e("SQL Connection Error : ", e.getMessage());
        }

        return connection;
    }
}