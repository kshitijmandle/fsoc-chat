package com.fsociety.fsocietyin.Activies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fsociety.fsocietyin.MainActivity;
import com.fsociety.fsocietyin.R;
import com.fsociety.fsocietyin.models.Loader;
import com.fsociety.fsocietyin.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    EditText edtusername;
    EditText edtpassword;
    EditText edtemail;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    MaterialButton btnsignUp;
    Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        edtusername = findViewById(R.id.edtusernameid);
        edtpassword = findViewById(R.id.edtpasswordid);
        edtemail = findViewById(R.id.edtemailid);
        btnsignUp = findViewById(R.id.signupbtn);
        
        btnsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtusername.getText().toString();
                String pass = edtpassword.getText().toString();
                String email = edtemail.getText().toString();
                //Toast.makeText(SignUp.this, name + " " + pass + " " + email , Toast.LENGTH_LONG).show();
                if (name.matches("") || pass.matches("") || email.matches("")) {
                    Toast.makeText(SignUp.this, "Check Empty fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean check_email_is_valid = isEmailValid(email);
                    if (check_email_is_valid){
                        if(pass.length() < 6){
                            Toast.makeText(SignUp.this, "Password must be 6 letters ", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            loader = new Loader(SignUp.this);
                            loader.startloading();
                            newsign(email,pass,name);
                            //Toast.makeText(SignUp.this, "Noice working fine", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(SignUp.this, " Please type valid email ", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }

    public void addusertoDatabase(String name , String email , String UserId){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.child("users").child(UserId).setValue(new UserModel(name,email,UserId));

    }
 public void newsign(String email , String password, String username){
     mAuth.createUserWithEmailAndPassword(email, password)
             .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()) {
                         // Sign in success, update UI with the signed-in user's information
                         Toast.makeText(SignUp.this, "noice ok ", Toast.LENGTH_SHORT).show();
                         addusertoDatabase(username , email , mAuth.getCurrentUser().getUid());

                         // Storing data into SharedPreferences
                         SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                         SharedPreferences.Editor myEdit = sharedPreferences.edit();
                         myEdit.putString("email", email );
                         myEdit.putString("password", password);
                         myEdit.commit();

                         Intent intent = new Intent(SignUp.this, MainActivity.class);
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         loader.startloading();
                         startActivity(intent);
                         finish();
                     } else {
                         // If sign in fails, display a message to the user.
                         Toast.makeText(SignUp.this, "fucked", Toast.LENGTH_SHORT).show();

                     }
                 }
             });
 }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}