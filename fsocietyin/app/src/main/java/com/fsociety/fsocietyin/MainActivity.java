package com.fsociety.fsocietyin;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fsociety.fsocietyin.Activies.LoginA;
import com.fsociety.fsocietyin.adapters.UserAdapter;
import com.fsociety.fsocietyin.models.Loader;
import com.fsociety.fsocietyin.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    UserAdapter adapter;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView action_bar_name_home;
    ImageView profile;
    TextView logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Loader loader = new Loader(MainActivity.this);
        loader.startloading();
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.id_recyclerview);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        ArrayList<UserModel> userlist = new ArrayList<>();
        action_bar_name_home = findViewById(R.id.action_bar_chat);
        logout = findViewById(R.id.logoutbtn);
        profile = findViewById(R.id.userchatimage);
        String My_Account_id = mAuth.getCurrentUser().getUid();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.clear();
                myEdit.commit();
                Intent intent = new Intent(MainActivity.this, LoginA.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();

            }
        });

        profile.setImageDrawable(getDrawable(R.drawable.elliotfs));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userlist.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    UserModel temp = ds.getValue(UserModel.class);


                    if(!temp.getUserId().equals(mAuth.getCurrentUser().getUid())){
                        userlist.add(temp);
                    }
                    else{
                        getusername(temp.getName());
                    }
                    //Log.d("KALI  ", "kshitij : " + temp.getEmail());


                }
                adapter = new UserAdapter(getApplicationContext() , userlist);
                adapter.updatedata(userlist);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                loader.loadstop();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

public void getusername(String my_id){
    action_bar_name_home.setText(my_id);
}



}