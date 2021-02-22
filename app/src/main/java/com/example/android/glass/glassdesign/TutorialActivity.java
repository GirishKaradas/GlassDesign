package com.example.android.glass.glassdesign;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glass.ui.GlassGestureDetector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class TutorialActivity extends Base2Activity {

    private ArrayList<String> arrayList= new ArrayList();
    private TextView textView;
    private int count =0;
    private ConstraintLayout layout;
    String currentPhotoPath;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        prepareArraylist();

        textView = findViewById(R.id.activity_tutorial_textview);
        imageView = findViewById(R.id.activity_tutorial_imageview);
        textView.setText(arrayList.get(count));
        imageView.setImageResource(R.drawable.baseline_touch_app_24);
        layout = findViewById(R.id.activity_tutorial_layout);

    }

    private void prepareArraylist() {
        arrayList.add(arrayList.size(), getString(R.string.tap));
        arrayList.add(arrayList.size(), getString(R.string.double_tap));
        arrayList.add(arrayList.size(), getString(R.string.swipe_f));
        arrayList.add(arrayList.size(), getString(R.string.swipe_b));
        arrayList.add(arrayList.size(), getString(R.string.swipe_d));
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {

        switch (gesture){
            case TAP:
                if (count == 0) {
                    count++;
                    Toast.makeText(this.getApplicationContext(), "Step " + count + "Completed", Toast.LENGTH_SHORT).show();
                  //  takeScreenshot();
                    textView.setText(arrayList.get(count));
                    imageView.setImageResource(R.drawable.two_finger);
                }
                return true;

            case TWO_FINGER_TAP:
                if (count == 1){
                    count++;
                    Toast.makeText(this.getApplicationContext(), "Step " + count + " Completed", Toast.LENGTH_SHORT).show();
                    textView.setText(arrayList.get(count));
                    imageView.setImageResource(R.drawable.baseline_swipe_24);
                }
                return true;

            case SWIPE_FORWARD:
                if (count == 2){
                    count++;
                    Toast.makeText(this.getApplicationContext(), "Step " + count + " Completed", Toast.LENGTH_SHORT).show();
                    textView.setText(arrayList.get(count));
                    imageView.setImageResource(R.drawable.baseline_swipe_24);
                }
                return true;

            case SWIPE_BACKWARD:
                if (count == 3){
                    count++;
                    Toast.makeText(this.getApplicationContext(), "Step " + count + " Completed", Toast.LENGTH_SHORT).show();
                    textView.setText(arrayList.get(count));
                    imageView.setImageResource(R.drawable.baseline_play_for_work_24);
                }
                return true;

            case SWIPE_DOWN:
                if (count==4){
                    count++;
                    Toast.makeText(this.getApplicationContext(), "Step " + count + " Completed", Toast.LENGTH_SHORT).show();
                    finish();
                    return super.onGesture(gesture);
                }else {
                    return  true;
                }

            default:
                return super.onGesture(gesture);

        }

    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            File imageFile = createImageFile(now.toString());
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            //  File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            //    openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private File createImageFile(String imageID) throws IOException {
        // Create an image file name
        Log.e("Report: ", imageID);

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
   /*     File image = File.createTempFile(
                imageID,
                ".jpg",
                storageDir
        );

    */
        File file = new File(storageDir.getPath() + "/" + imageID +".jpg");
        Boolean aBoolean;
        aBoolean = file.createNewFile();
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = file.getAbsolutePath();
        Log.e("Report: ", currentPhotoPath.toString() +aBoolean.toString());
        return file;
    }
}