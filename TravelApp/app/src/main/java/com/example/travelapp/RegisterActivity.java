package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.travelapp.Connection.ConnectionClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, password;
    Button registerBtn;
    TextView status;
    Connection con;
    Statement stmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        status = (TextView)findViewById(R.id.status);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegisterActivity.registerUser().execute("");
            }
        });
    }

    public class registerUser extends AsyncTask<String, String , String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            status.setText("Sending Data to Database");
        }

        @Override
        protected void onPostExecute(String s) {
            status.setText("Registration Successful");
            name.setText("");
            email.setText("");
            password.setText("");
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                con = connectionClass(ConnectionClass.username.toString(),ConnectionClass.password.toString(),ConnectionClass.database.toString(),ConnectionClass.ip.toString(), ConnectionClass.port.toString());
                if(con == null){
                    z = "Check Your Internet Connection";
                }
                else{
                    String sql = "INSERT INTO register (name,email,password) VALUES ('"+name.getText()+"','"+email.getText()+"','"+password.getText()+"');";
                    stmt = con.createStatement();
                    stmt.executeUpdate(sql);
                }

            }catch (Exception e){
                isSuccess = false;
                z = e.getMessage();
            }

            return z;
        }
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


