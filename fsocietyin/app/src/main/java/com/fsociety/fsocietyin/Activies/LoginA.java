package com.fsociety.fsocietyin.Activies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fsociety.fsocietyin.MainActivity;
import com.fsociety.fsocietyin.R;
import com.fsociety.fsocietyin.models.Loader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginA extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView singuppage;
    //Button login;
    EditText username;
    EditText password;
    MaterialButton login;
    Loader loader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();


        singuppage = findViewById(R.id.signUppage);
        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.logInbtn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loader = new Loader(LoginA.this);



           //nothing here to code;
            keepuserlogin();

            login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String pass = password.getText().toString();

                if(name.matches("") || pass.matches("")){

                    Toast.makeText(LoginA.this, "Check Fiels , friend ", Toast.LENGTH_SHORT).show();
                }
                else{
                    loader.startloading();
                    checklogin(name,pass);
                }

            }
        });

        singuppage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginA.this,SignUp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    void checklogin(String email , String pass){
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("email", email );
                            myEdit.putString("password", pass);
                            myEdit.commit();
                            Intent intent = new Intent(LoginA.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            loader.loadstop();
                            Toast.makeText(LoginA.this, "Logged in as " + email , Toast.LENGTH_SHORT).show();



                        } else {
                            // If sign in fails, display a message to the user.er
                            Toast.makeText(LoginA.this, "user does not exist", Toast.LENGTH_SHORT).show();
                            loader.loadstop();

                        }
                    }
                });

    }

    public void keepuserlogin(){
        loader.startloading();
        Toast.makeText(this, "Checking , if you already login ", Toast.LENGTH_SHORT).show();
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String email = sh.getString("email" , "nothing");
        String password = sh.getString("password" , "nothing");
        checklogin(email,password);

    }

}