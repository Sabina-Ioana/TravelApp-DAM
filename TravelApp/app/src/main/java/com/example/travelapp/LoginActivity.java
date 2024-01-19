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
import android.widget.Toast;


import com.example.travelapp.Connection.ConnectionClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    EditText emailLogin, passwordLogin;
    Button loginBtn, registerBtn;

    Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = (EditText)findViewById(R.id.emailLogin);
        passwordLogin = (EditText)findViewById(R.id.passwordLogin);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        registerBtn = (Button)findViewById(R.id.registerBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginActivity.checkLogin().execute("");
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public class checkLogin extends AsyncTask<String, String, String> {
        String z = null;
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {

        }

        @Override
        protected String doInBackground(String... strings) {
            con = connectionClass(ConnectionClass.username.toString(),ConnectionClass.password.toString(),ConnectionClass.database.toString(),ConnectionClass.ip.toString(), ConnectionClass.port.toString());

            if (con == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Check your internet connection", Toast.LENGTH_LONG).show();
                    }
                });
                z = "On internet connection";
            }
            else {
                try{
                    String sql = "SELECT * FROM register WHERE email = '" + emailLogin.getText() + "' AND password = '" + passwordLogin.getText() +"'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);

                    if (rs.next()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                            }
                        });
                        z = "Success";
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userId", rs.getInt("userId"));
                        startActivity(intent);
                        finish();
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Check email or password", Toast.LENGTH_LONG).show();
                            }
                        });
                        emailLogin.setText("");
                        passwordLogin.setText("");
                    }
                }catch(Exception e){
                    isSuccess = false;
                    Log.e("SQL Error: ", e.getMessage());
                }
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
            // Setarea politicii de thread-uri pentru a permite operațiile pe firul principal
            Class.forName("net.sourceforge.jtds.jdbc.Driver");  // Încărcarea driverului JDBC
            connectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database;  // Construirea URL-ului de conexiune
            connection = DriverManager.getConnection(connectionURL, user, password);  // Crearea conexiunii efective
        } catch (Exception e){
            Log.e("SQL Connection Error : ", e.getMessage());  // Gestionarea excepțiilor și înregistrarea eventualelor erori
        }
        return connection;  // Returnarea conexiunii pentru a fi utilizată în operațiile ulterioare cu baza de date
    }
}