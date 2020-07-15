package com.example.inout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText email,password;
    private Button login;
    private TextView signup_text;
    private ProgressBar pg;

    private String email_validate;
    private String password_validate;

    private String emailTxt="",passwordTxt="";

    private FirebaseAuth mAuth;

    private String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
            finish();

        }

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signup_text=findViewById(R.id.signuptxt);
        pg=findViewById(R.id.progressBar);
        pg.setVisibility(View.INVISIBLE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailTxt=email.getText().toString().trim().toLowerCase();
                passwordTxt=password.getText().toString().trim();


                pg.setVisibility(View.VISIBLE);

                email_validate = validateEmail(emailTxt);

                password_validate = validatePassword(passwordTxt);


               if(email_validate!="valid"){
                   email.setError(email_validate);
               }
               if (password_validate!="valid"){
                   password.setError(password_validate);
               }

               if (password_validate=="valid"){
                   password.setError(null);
               }

                signup_text.setEnabled(true);

               if (email_validate=="valid"  && password_validate=="valid"){
                   signup_text.setEnabled(false);

                   System.out.println(emailTxt+passwordTxt);

                   mAuth.signInWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){
                               Toast.makeText(getApplicationContext(),"LogIn Successful",Toast.LENGTH_SHORT).show();
                               finish();
                               startActivity(new Intent(MainActivity.this,DashboardActivity.class));
                               pg.setVisibility(View.INVISIBLE);
                               System.out.println("Done");
                           }else{
                               Toast.makeText(getApplicationContext(),"Error!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                               pg.setVisibility(View.INVISIBLE);
                               signup_text.setEnabled(true);
                           }
                       }
                   });

               }else{
                   pg.setVisibility(View.INVISIBLE);
               }

            }
        });

        signup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg.setVisibility(View.VISIBLE);
                Intent signup_activity = new Intent(MainActivity.this,SignupActivity.class);
                signup_activity.putExtra("email",email.getText().toString());
                signup_activity.putExtra("password",password.getText().toString());
                finish();
                startActivity(signup_activity);

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


}
