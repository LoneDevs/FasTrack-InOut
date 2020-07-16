package com.example.inout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class DashboardActivity extends AppCompatActivity {

    private TimePicker tpIn,tpOut;
    private TextView username;
    private ImageButton logoutImg;
    private Button submit,viewBalance;
    private ProgressBar pg;

    private double userSalary;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;

    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();

        if (mAuth.getCurrentUser()==null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        username =findViewById(R.id.username);
        tpIn = findViewById(R.id.timein);
        tpOut = findViewById(R.id.timeout);
        submit = findViewById(R.id.submit);
        viewBalance = findViewById(R.id.balance);
        logoutImg =findViewById(R.id.logout);
        pg=findViewById(R.id.progressBar3);
        pg.setVisibility(View.VISIBLE);


        tpIn.setIs24HourView(true);
        tpOut.setIs24HourView(true);

        DocumentReference df = mStore.collection("employees").document(userId);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                username.setText("Hello "+documentSnapshot.getString("name"));
                userSalary = documentSnapshot.getDouble("salary");
            }
        });
        pg.setVisibility(View.INVISIBLE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg.setVisibility(View.VISIBLE);
                SalaryCalc sc = new SalaryCalc();
                String time_validate=sc.validateInterval(tpIn.getHour(),tpIn.getMinute(),tpOut.getHour(),tpOut.getMinute());
                if (time_validate!="valid"){
                    Toast.makeText(getApplicationContext(),"Incorrect Time Interval",Toast.LENGTH_SHORT).show();
                    pg.setVisibility(View.INVISIBLE);
                    return;

                }else{
                    userSalary +=sc.calculateSalary();
                    Map<String,Object> employee = new HashMap<>();
                    employee.put("salary",userSalary);
                    mStore.collection("employees").document(userId).update("salary",userSalary).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Salary updated Successfully",Toast.LENGTH_SHORT).show();
                            System.out.println("Salary updated: "+userSalary);
                            pg.setVisibility(View.INVISIBLE);
                        }
                    });
                    System.out.println("user salary : "+userSalary);
                }
                return;

            }
        });

        logoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        viewBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SalaryActivity.class));
                finish();
            }
        });


    }

    protected void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}