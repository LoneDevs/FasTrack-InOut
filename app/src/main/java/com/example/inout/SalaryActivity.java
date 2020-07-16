package com.example.inout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class SalaryActivity extends AppCompatActivity {

    private TextView name,empid,balance;
    private EditText amount;
    private ImageButton back;
    private Button withdraw;
    private ProgressBar pg;


    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;


    private double userSalary;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();


        if (mAuth.getCurrentUser()==null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }


        name=findViewById(R.id.name);
        empid=findViewById(R.id.empid);
        balance=findViewById(R.id.balance);
        amount=findViewById(R.id.amount);
        withdraw=findViewById(R.id.withdraw);
        pg=findViewById(R.id.progressBar4);
        back =findViewById(R.id.back);

        pg.setVisibility(View.VISIBLE);


        DocumentReference df = mStore.collection("employees").document(userId);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText("Name : "+documentSnapshot.getString("name"));
                userSalary = documentSnapshot.getDouble("salary");
                empid.setText("EmpId : "+documentSnapshot.getString("empid"));
                balance.setText("Balance : Rs "+Double.toString(userSalary));
            }
        });

        pg.setVisibility(View.INVISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                finish();
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg.setVisibility(View.VISIBLE);
                if (amount.getText().toString().trim().isEmpty()){
                    amount.setError("cannot be empty !");
                    pg.setVisibility(View.INVISIBLE);
                    return;
                }

                if(amount.getText().toString().trim().matches("[0-9]*")) {
                    EmployeeTransaction et = new EmployeeTransaction(userSalary, Double.parseDouble(amount.getText().toString().trim()));
                    if (et.startTransaction()=="valid"){
                        mStore.collection("employees").document(userId).update("salary",et.getUserSalary()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Transaction Successful ",Toast.LENGTH_SHORT).show();
                                System.out.println("Salary updated: ");
                                pg.setVisibility(View.INVISIBLE);
                            }
                        });

                    }else{
                        pg.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),et.startTransaction(),Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    pg.setVisibility(View.INVISIBLE);
                    amount.setError("Must contain only numbers !");
                    return;

                }

            }
        });

    }
}