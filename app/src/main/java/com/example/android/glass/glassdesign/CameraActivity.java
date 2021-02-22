package com.example.android.glass.glassdesign;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.glass.glassdesign.data.DataRecord;
import com.example.glass.ui.GlassGestureDetector;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraActivity extends BaseActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView, ivIcon;
    private StorageReference storageReference, storageReference1;
    private DatabaseReference reference, ref1, ref2;
    private Date now = new Date();
    String currentPhotoPath;
    private Uri uri, photoURI;
    private int step;
    private Boolean FLAG = false;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.activity_camera_imageview);
        textView = findViewById(R.id.activity_camera_tv);
        ivIcon = findViewById(R.id.activity_camera_logo);
        textView.setText("Tap to open Camera");
        FLAG = false;

        step = getIntent().getIntExtra("step", 0);
        switch (BaseActivity.theme_code) {
            case 1:
                ivIcon.setColorFilter(ContextCompat.getColor(this, R.color.color_white), android.graphics.PorterDuff.Mode.MULTIPLY);
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.color_white), android.graphics.PorterDuff.Mode.MULTIPLY);
                break;

            case 2:
                ivIcon.setColorFilter(ContextCompat.getColor(this, R.color.color_black), android.graphics.PorterDuff.Mode.MULTIPLY);
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.color_black), android.graphics.PorterDuff.Mode.MULTIPLY);
                break;
        }

        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {
        switch (gesture) {
            case TAP:
                if (!FLAG){
                    dispatchTakePictureIntent();
                }else{
                    if (step == 5){
                        ref1 = reference.child("records");
                        storageReference1 = storageReference.child("records");
                    }else if (step == 8){
                        ref1 = reference.child("batch");
                        storageReference1 = storageReference.child("batch");
                    }
                    String key = ref1.push().getKey();
                    ref2 = ref1.child(key);
                    DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

                    Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();

                    if (photoURI != null && now != null) {
                        UploadTask uploadTask = storageReference1.child(key).putFile(photoURI);
                        Toast.makeText(this, "Uploading", Toast.LENGTH_SHORT).show();

                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return storageReference1.child(key).getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri uri1 = task.getResult();
                                    ref2.setValue(new DataRecord(key, now.toString(), uri1.toString()));
                                    if (step == 5){
                                        ManualActivity.FLAG5 = true;
                                        ManualActivity.viewPager.setPagingEnabled(true);
                                    }else if (step == 8){
                                        ManualActivity.FLAG8 = true;
                                        ManualActivity.viewPager.setPagingEnabled(true);
                                    }
                                    finish();
                                    Toast.makeText(CameraActivity.this, "image Upload Succesful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e(task.getException() + "s", "this");
                                    Toast.makeText(CameraActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(this, "No image", Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            default:
                return super.onGesture(gesture);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && photoURI != null) {
            FLAG = true;
            imageView.setImageURI(photoURI);
            imageView.setColorFilter(null);
            imageView.setVisibility(View.VISIBLE);
            textView.setText(getString(R.string.upload_tip));
            Toast.makeText(this, "Image Saved to gallery", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "No Image Captured", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.e("Error Creating File" , "");
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                List<String> packages = new ArrayList<>();
                List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(takePictureIntent, 0);
                for (ResolveInfo resolveInfo : resolveInfoList){
                    packages.add(resolveInfo.activityInfo.packageName);
                    Log.e("Packages: ", resolveInfo.activityInfo.packageName.toString()+"");
                }
                takePictureIntent.setPackage(packages.get(0));

               startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
}