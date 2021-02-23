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
    String currentPhotoPath;
    private Boolean FLAG = false;
    private TextView textView;
    private int step;

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



        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {
        switch (gesture) {
            case TAP:
                if (!FLAG){
                    imageView.setImageResource(R.drawable.image);
                    textView.setText(getString(R.string.upload_tip));
                    FLAG = true;
                }else{
                    if (step == 5){
                        ManualActivity.FLAG5 = true;
                        ManualActivity.viewPager.setPagingEnabled(true);
                    }else if (step == 8){
                        ManualActivity.FLAG8 = true;
                        ManualActivity.viewPager.setPagingEnabled(true);
                    }
                    Toast.makeText(CameraActivity.this, "image Upload Succesful", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return true;
            default:
                return super.onGesture(gesture);
        }
    }


}