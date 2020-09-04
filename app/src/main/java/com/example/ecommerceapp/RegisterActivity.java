package com.example.ecommerceapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccount;
    private EditText InputName, InputNumber, InputPassword;

    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccount = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username);
        InputNumber = (EditText) findViewById(R.id.register_phno);
        InputPassword = (EditText) findViewById(R.id.register_password);
        loadingBar = new ProgressDialog(this);
        
        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                CreateAccount();
            }
        });


    }

    private void CreateAccount() {
        String name = InputName.getText().toString();
        String phone = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please write your name..", Toast.LENGTH_SHORT).show();
        }


        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please write phone number", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please write password", Toast.LENGTH_SHORT).show();
        }

        else{

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            
            ValidatePhoneNumber(name, phone, password);

        }

    }

    private void ValidatePhoneNumber(final String name, final String phone, final String password) {

        final DatabaseReference RootRef;
        //final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists())) {

                    HashMap<String, Object> userdataMap= new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()){

                                   Toast.makeText(RegisterActivity.this, "Congratulation, your account has been created.", Toast.LENGTH_SHORT).show();
                                   loadingBar.dismiss();
                                   Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                                   startActivity(intent);

                               }
                               
                               else{
                                   loadingBar.dismiss();
                                   Toast.makeText(RegisterActivity.this, "Network Error: Please try again", Toast.LENGTH_SHORT).show();
                               }
                                }
                            });

                }

                else {
                    Toast.makeText(RegisterActivity.this, "This"+ phone + " already exits.", Toast.LENGTH_SHORT).show();

                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

                    startActivity(intent);
                }

            }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}