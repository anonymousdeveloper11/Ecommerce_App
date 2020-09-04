package com.example.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView tshirt, sportTshirt, sweaters, femaleDress;
    private ImageView glasses, purse, hats, shoes;
    private ImageView headPhones, laptops, watches, mobiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        tshirt = (ImageView) findViewById(R.id.tshirt);
        sportTshirt = (ImageView) findViewById(R.id.sport);
        sweaters = (ImageView) findViewById(R.id.sweater);
        femaleDress = (ImageView) findViewById(R.id.female);

        glasses = (ImageView) findViewById(R.id.glasses);
        purse =(ImageView) findViewById(R.id.purse);
        hats = (ImageView) findViewById(R.id.hats);
        shoes = (ImageView) findViewById(R.id.shoes);

        headPhones = (ImageView) findViewById(R.id.headphone);
        laptops = (ImageView) findViewById(R.id.laptops);
        watches = (ImageView) findViewById(R.id.watches);
        mobiles = (ImageView) findViewById(R.id.mobiles);


        tshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminActivity.class);
                intent.putExtra("Category", "tshirts");
                startActivity(intent);
            }
        });

        sportTshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminActivity.class);
                intent.putExtra("Category", "Sports Tshirts");
                startActivity(intent);
            }
        });

        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminActivity.class);
                intent.putExtra("Category", "Female Dresses");
                startActivity(intent);
            }
        });

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminActivity.class);
                intent.putExtra("Category", "glasses");
                startActivity(intent);
            }
        });

        purse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminActivity.class);
                intent.putExtra("Category", "Wallets, Bags , Purses");
                startActivity(intent);
            }
        });

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminActivity.class);
                intent.putExtra("Category","Hats");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminActivity.class);
                intent.putExtra("Category","shoes");
                startActivity(intent);
            }
        });

        headPhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminActivity.class);
                intent.putExtra("Category","Headphone, Handfree");
                startActivity(intent);

            }
        });

        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminActivity.class);
                intent.putExtra("Category", "Laptops");
                startActivity(intent);
            }
        });

        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminActivity.class);
                intent.putExtra("Category", "Watches");
                startActivity(intent);
            }
        });

        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminActivity.class);
                intent.putExtra("Category", "Mobiles Phones");
                startActivity(intent);
            }
        });
    }
}