package com.example.inout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText name,empid,email,password,repassword;
    private Button signup;
    private TextView login;
    private ProgressBar pg;


    private String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

    private String userID;

    private String name_validate;
    private String empid_validate;
    private String email_validate;
    private String password_validate ;
    private String repassword_validate ;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
            finish();

        }



        name=findViewById(R.id.name);
        empid=findViewById(R.id.empid);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        repassword=findViewById(R.id.repassword);
        signup=findViewById(R.id.signup);
        login=findViewById(R.id.login);
        pg = findViewById(R.id.progressBar2);

        pg.setVisibility(View.INVISIBLE);


        email.setText(intent.getStringExtra("email"));
        password.setText(intent.getStringExtra("password"));
        repassword.setText(intent.getStringExtra("password"));


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg.setVisibility(View.VISIBLE);
                final String emailTxt = email.getText().toString().trim();
                String passwordTxt = password.getText().toString().trim();
                final String nameTxt = name.getText().toString().trim();
                final String empidTxt = empid.getText().toString().trim();

                name_validate = validateName(nameTxt);
                empid_validate= validateEmpId(empidTxt);
                email_validate=validateEmail(emailTxt);
                password_validate=validatePassword(passwordTxt);
                repassword_validate=validatePassword(password.getText().toString().trim());

                if (name_validate!="valid"){
                    name.setError(name_validate);
                    pg.setVisibility(View.INVISIBLE);

                }

                if (email_validate!="valid"){
                    email.setError(email_validate);
                    pg.setVisibility(View.INVISIBLE);

                }

                if (password_validate!="valid"){
                    password.setError(password_validate);
                    pg.setVisibility(View.INVISIBLE);

                }

                if (repassword_validate!="valid"){
                    repassword.setError(repassword_validate);
                    pg.setVisibility(View.INVISIBLE);

                }
                if(empid_validate!="valid"){
                    empid.setError(empid_validate);
                    pg.setVisibility(View.INVISIBLE);
                }

                if (!password.getText().toString().trim().equals(repassword.getText().toString().trim())){
                    password.setError("Passwords do not match ! ");
                    repassword.setError("Passwords do not match ! ");
                    pg.setVisibility(View.INVISIBLE);
                    return;

                }

                pg.setVisibility(View.INVISIBLE);

                if (password_validate.equals("valid") && repassword_validate.equals("valid") && email_validate.equals("valid") && name_validate.equals("valid")) {
                    pg.setVisibility(View.VISIBLE);
                    System.out.println("creating user");
                    mAuth.createUserWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference df = mStore.collection("employees").document(userID);
                            Map<String,Object> employee = new HashMap<>();
                            employee.put("name",nameTxt);
                            employee.put("empid",empidTxt);
                            employee.put("email",emailTxt);
                            employee.put("salary",0);
                            df.set(employee).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                   Toast.makeText(getApplicationContext(),"User details added Successfully !",Toast.LENGTH_SHORT).show();
                                }
                            });
                            Toast.makeText(SignupActivity.this,"User Created successfully",Toast.LENGTH_SHORT).show();
                            pg.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(SignupActivity.this,"Error ! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            pg.setVisibility(View.INVISIBLE);

                        }
                        }
                    });
                }
            }
        });

       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent login_activity = new Intent(SignupActivity.this,MainActivity.class);
               finish();
               startActivity(login_activity);
           }
       });


    }



    protected String validateEmail(String email){

        Pattern email_pattern = Pattern.compile(EMAIL_REGEX);

        Matcher email_matcher = email_pattern.matcher(email);

        if(email.isEmpty())
        {
            return "Email cannot be empty" ;


        }else if(email_matcher.matches()){

            return "valid";

        }else{

            return "Invalid e-mail";


        }

    }



    protected String validatePassword(String password){

        if(password.isEmpty()){

            return "Password cannot be Empty !";
        }else if(password.length()>=8){
            return "valid";


        }
        else{

            return "Password is too short !";

        }

    }

    protected String validateName(String name){

        if(name.isEmpty()){
            return  "Cannot be empty !";

        }else if(name.matches("[A-Z][a-z]*")||name.matches("[a-z]*")||name.matches("[A-Z]*")){

            return  "valid";

        }else{

            return  "can contain only alphabets" ;

        }




    }

    protected String validateEmpId(String empid){

        if(empid.isEmpty()){
            return "cannot be empty !";
        }else if(empid.matches("[A-Z][A-Z][A-Z][0-9][0-9]")){
            return  "valid";
        }else{
            return "Invalid Format !" ;
        }

    }



}
