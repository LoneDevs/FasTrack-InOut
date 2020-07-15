package com.example.inout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private TimePicker tpIn,tpOut;
    private TextView logoutTxt;
    private Button submit,viewBalance;

    private int minutes,hours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tpIn = findViewById(R.id.timein);
        tpOut = findViewById(R.id.timeout);
        submit = findViewById(R.id.submit);
        viewBalance = findViewById(R.id.balance);
        logoutTxt =findViewById(R.id.logout);

        tpIn.setIs24HourView(true);
        tpOut.setIs24HourView(true);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minutes = tpIn.getMinute();
                hours = tpOut.getHour();
            }
        });

        logoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });


    }

    protected void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}