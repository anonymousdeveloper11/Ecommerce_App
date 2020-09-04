package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import Model.Users;
import Prevalent.Prevalent;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputNumber, InputPassword;
    private Button Loginbtn;
    private ProgressDialog loadingBar;
    private String ParentDbName = "Users";
    private CheckBox chkBox;

    private TextView adminLink , notAdminLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InputNumber =  (EditText) findViewById(R.id.phno);

        InputPassword = (EditText) findViewById(R.id.password);

        Loginbtn = (Button) findViewById(R.id.login_btn);
        loadingBar = new ProgressDialog(this);
        chkBox = (CheckBox) findViewById(R.id.remember);

        adminLink = (TextView) findViewById(R.id.admin);
        notAdminLink = (TextView) findViewById(R.id.not_admin);
        Paper.init(this);

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
            
        });

    }

    private void LoginUser() {

        String phone = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Enter Phone Number.", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }
        else{

        }
        loadingBar.setTitle("Login Account");
        loadingBar.setMessage("Please wait, While we are checking the credentials.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        AllowAccessToAccount(phone, password);
    }

    private void AllowAccessToAccount(final String phone, final String password) {

        if(chkBox.isChecked()){

        }
        Paper.book().write(Prevalent.userPhoneKey, phone);
        Paper.book().write(Prevalent.userPasswordKey,password);


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(ParentDbName).child(phone).exists()){

                    Users userData = dataSnapshot.child(ParentDbName).child(phone).getValue(Users.class);

                    if (userData.getPhone().equals(phone)){

                        if(userData.getPassword().equals(password)){

                            if(ParentDbName.equals("Admins")){

                                Toast.makeText(LoginActivity.this, "Welcom, you are Login successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this,AdminCategoryActivity.class);
                                startActivity(intent);

                            }

                            else if(ParentDbName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }


                        }



                    }

                    else{
                        loadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Password is Incorrect.", Toast.LENGTH_SHORT).show();
                    }


                }
                else{
                    Toast.makeText(LoginActivity.this, "Account wit thid" + phone + "number do not exists.", Toast.LENGTH_SHORT).show();
              loadingBar.dismiss();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginbtn.setText("Login Admin");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                ParentDbName ="Admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginbtn.setText("Login");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                ParentDbName ="Users";
            }
        });
    }
}