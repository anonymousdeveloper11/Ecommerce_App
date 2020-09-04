package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {
    
    private String categoryName , description, price , pname;

    private Button addNewProduct;
    private EditText addProductName, addProductDescription, addProductPrice;
    private ImageView addProductImage;
    private static final int GalleryPick = 1;
    private Uri imageUri;
    private String saveCurrentDate, saveCurrentTime , productRandomKey, downloadImageUrl ;

    private StorageReference productImageRef;

    private DatabaseReference productRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        categoryName = getIntent().getExtras().get("Category").toString();
        Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();

        addNewProduct = (Button) findViewById(R.id.add_btn);
        addProductName = (EditText) findViewById(R.id.product_name);
        addProductDescription = (EditText) findViewById(R.id.product_description);
        addProductPrice = (EditText) findViewById(R.id.product_price);
        addProductImage = (ImageView) findViewById(R.id.add_product_Image);

        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        loadingBar = new ProgressDialog(this);


        addProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

    }



    private void openGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){

            imageUri = data.getData();

            addProductImage.setImageURI(imageUri);
        }
    }

    private void ValidateProductData(){
        description = addProductDescription.getText().toString();
        price = addProductPrice.getText().toString();
        pname = addProductName.getText().toString();

        if(imageUri == null){
            Toast.makeText(this, "Product image is required..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description)){
            Toast.makeText(this, "Product description is required..", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(price)){
            Toast.makeText(this, "Please give Product price..", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(pname)){
            Toast.makeText(this, "Product name is required", Toast.LENGTH_SHORT).show();
        }
        else{
            storeProductInformation();

        }
    }

    private void storeProductInformation() {

        loadingBar.setTitle("Add new product");
        loadingBar.setMessage("Please wait, While we are adding the product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(AdminActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                Toast.makeText(AdminActivity.this, "Product Image upload successfully", Toast.LENGTH_SHORT).show();


                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return  filePath.getDownloadUrl();
                    }


                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                     if(task.isSuccessful()){
                         downloadImageUrl = task.getResult().toString();
                         Toast.makeText(AdminActivity.this, "Got the product URl successfully", Toast.LENGTH_SHORT).show();
                     SaveProductInfoToDatabase();
                     }

                    }
                });
            }

        });


    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap =  new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", categoryName);
        productMap.put("price", price);
        productMap.put("pname", pname);

        productRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
           if(task.isSuccessful()){

               Intent intent = new Intent(AdminActivity.this, AdminCategoryActivity.class);
               startActivity(intent);
               loadingBar.dismiss();
               Toast.makeText(AdminActivity.this, "Product is added successfully", Toast.LENGTH_SHORT).show();
           }
           else{
               loadingBar.dismiss();
               String message = task.getException().toString();
               Toast.makeText(AdminActivity.this, "Error" +message, Toast.LENGTH_SHORT).show();
           }
            }
        });

    }
}