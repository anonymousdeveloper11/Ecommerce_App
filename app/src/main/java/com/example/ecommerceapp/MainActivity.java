package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Users;
import Prevalent.Prevalent;
import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {

    private Button loginbtn, joinbtn;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginbtn = (Button) findViewById(R.id.loginbtn);
        joinbtn = (Button) findViewById(R.id.joinbtn);

        loadingBar = new ProgressDialog(this);
        Paper.init(this);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        joinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordKey = Paper.book().read(Prevalent.userPasswordKey);

        if(userPhoneKey!= "" && userPasswordKey!= ""){

        }
        if(!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPasswordKey)){

            AllowAccess(userPhoneKey, userPasswordKey);

            loadingBar.setTitle("Already Login");
            loadingBar.setMessage("Please wait....");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        }
    }

    private void AllowAccess(final String userPhoneKey, final String userPasswordKey) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Users").child(userPhoneKey).exists()){

                    Users userData = dataSnapshot.child("Users").child(userPhoneKey).getValue(Users.class);

                    if (userData.getPhone().equals(userPhoneKey)){

                        if(userData.getPassword().equals(userPasswordKey)){

                        }

                        Toast.makeText(MainActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }

                    else{
                        loadingBar.dismiss();
                        Toast.makeText(MainActivity.this, "Password is Incorrect.", Toast.LENGTH_SHORT).show();
                    }


                }
                else{
                    Toast.makeText(MainActivity.this, "Account with this" + userPhoneKey + "number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}