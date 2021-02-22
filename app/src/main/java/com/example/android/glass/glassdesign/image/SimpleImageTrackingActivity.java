package com.example.android.glass.glassdesign.image;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.GLException;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.android.glass.glassdesign.BaseActivity;
import com.example.android.glass.glassdesign.R;
import com.example.android.glass.glassdesign.util.RenderTextView;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.wikitude.NativeStartupConfiguration;
import com.wikitude.WikitudeSDK;
import com.wikitude.common.WikitudeError;
import com.wikitude.common.camera.CameraSettings;
import com.wikitude.common.rendering.RenderExtension;
import com.wikitude.rendering.ExternalRendering;
import com.example.android.glass.glassdesign.WikitudeSDKConstants;
import com.example.android.glass.glassdesign.rendering.external.CustomSurfaceView;
import com.example.android.glass.glassdesign.rendering.external.Driver;
import com.example.android.glass.glassdesign.rendering.external.GLRenderer;
import com.example.android.glass.glassdesign.rendering.external.StrokedRectangle;
import com.example.android.glass.glassdesign.util.DropDownAlert;
import com.wikitude.tracker.ImageTarget;
import com.wikitude.tracker.ImageTracker;
import com.wikitude.tracker.ImageTrackerListener;
import com.wikitude.tracker.TargetCollectionResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Date;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

public class SimpleImageTrackingActivity extends BaseActivity implements ImageTrackerListener, ExternalRendering {

    private static final String TAG = "SimpleImageTracking";

    private WikitudeSDK wikitudeSDK;
    private CustomSurfaceView customSurfaceView;
    private Driver driver;
    private GLRenderer glRenderer;

    private DropDownAlert dropDownAlert;
    private ModelRenderable textviewRenderable;
    private String currentPhotoPath;
    private RenderTextView renderTextView;
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wikitudeSDK = new WikitudeSDK(this);
        NativeStartupConfiguration startupConfiguration = new NativeStartupConfiguration();
        startupConfiguration.setLicenseKey(WikitudeSDKConstants.WIKITUDE_SDK_KEY);
        startupConfiguration.setCameraPosition(CameraSettings.CameraPosition.BACK);
        startupConfiguration.setCameraResolution(CameraSettings.CameraResolution.AUTO);
        mTextView = new TextView(SimpleImageTrackingActivity.this);
        wikitudeSDK.onCreate(getApplicationContext(), this, startupConfiguration);

        final TargetCollectionResource targetCollectionResource = wikitudeSDK.getTrackerManager().createTargetCollectionResource("file:///android_asset/tracker.wtc");
        wikitudeSDK.getTrackerManager().createImageTracker(targetCollectionResource, this, null);
/*
        dropDownAlert = new DropDownAlert(this);
        dropDownAlert.setText("Scan Targets");
        dropDownAlert.setTextWeight(0.5f);
      //  dropDownAlert.addImages("surfer.png", "bike.png");
        dropDownAlert.show();


 */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wikitudeSDK.onResume();
        customSurfaceView.onResume();
        driver.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        customSurfaceView.onPause();
        driver.stop();
        wikitudeSDK.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wikitudeSDK.onDestroy();
    }

    @Override
    public void onRenderExtensionCreated(final RenderExtension renderExtension) {
        glRenderer = new GLRenderer(renderExtension);
        wikitudeSDK.getCameraManager().setRenderingCorrectedFovChangedListener(glRenderer);
        customSurfaceView = new CustomSurfaceView(getApplicationContext(), glRenderer);
        driver = new Driver(customSurfaceView, 30);
        setContentView(customSurfaceView);
    }

    @Override
    public void onTargetsLoaded(ImageTracker tracker) {
        Log.v(TAG, "Image tracker loaded");
    }

    @Override
    public void onErrorLoadingTargets(ImageTracker tracker, WikitudeError error) {
        Log.v(TAG, "Unable to load image tracker. Reason: " + error.getMessage());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onImageRecognized(ImageTracker tracker, final ImageTarget target) {
        Log.v(TAG, "Recognized target " + target.getName());
     //   dropDownAlert.dismiss();

        StrokedRectangle strokedRectangle = new StrokedRectangle(StrokedRectangle.Type.STANDARD);
        strokedRectangle.setColor(64.0f, 209.0f, 107.0f);
        glRenderer.setRenderablesForKey(target.getName() + target.getUniqueId(), strokedRectangle, null);
        Log.e("Location: " , target.getTargetScale().x +" And "+ target.getTargetScale().y);
        Log.e("Location: ", target.getViewMatrix().toString());
        Log.e("Location: ", strokedRectangle.getXScale() +" and "+  strokedRectangle.getYScale());
        renderTextView = new RenderTextView(SimpleImageTrackingActivity.this);
        renderTextView.setText("Object Recognized is: "+ target.getName());
        renderTextView.setTextWeight(0.5f);
        //  dropDownAlert.addImages("surfer.png", "bike.png");
    //    renderTextView.show();

        Rect rect = target.getTargetAreaInCameraFrame();
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(5);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(mTextView.getParent() == null) {

                                    mTextView.setText("Object: " + target.getName().toString());
                                    mTextView.setTextColor(getResources().getColor(R.color.design_green));
                                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    params.gravity = Gravity.CENTER;
                                    params.setMargins(rect.left, rect.top, rect.right, rect.bottom);
                                    mTextView.setVisibility(View.VISIBLE);
                                    addContentView(mTextView, params);
                                }
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        };
        thread.start();
/*
        takeScreenshot();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(this);

        textView.setText("Object is Recognized");
        textView.setLayoutParams(params);
        linearLayout.addView(textView);

        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(20);

        canvas.drawText("Object Recognized", strokedRectangle.getXScale(), strokedRectangle.getYScale(), paint);

 */
    //    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
     //   this.addContentView(customSurfaceView, layoutParams);
    }


    @Override
    public void onImageTracked(ImageTracker tracker, final ImageTarget target) {
        StrokedRectangle strokedRectangle = (StrokedRectangle) glRenderer.getRenderableForKey(target.getName() + target.getUniqueId());

        if (strokedRectangle != null) {
            strokedRectangle.viewMatrix = target.getViewMatrix();

            strokedRectangle.setXScale(target.getTargetScale().x);
            strokedRectangle.setYScale(target.getTargetScale().y);
            target.getTargetAreaInCameraFrame();
            Log.e("Location: " , target.getTargetScale().x +" And "+ target.getTargetScale().y +" " +target.getTargetAreaInCameraFrame() );
            Rect rect = target.getTargetAreaInCameraFrame();
            Log.e("Sample Check: L ", rect.left + " R " + rect.right + " T " + rect.top + " B " + rect.bottom);
            Log.e("Sample Check", target.getViewMatrix().toString());
            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            wait(5);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                   // params.gravity = Gravity.CENTER;
                                   // params.setMargins(0, 0, rect.left, rect.top);
                                    params.leftMargin = rect.left;
                                    params.topMargin = rect.top;
                                    mTextView.setLayoutParams(params);
                                    mTextView.setVisibility(View.VISIBLE);
                                 //   addContentView(mTextView, params);
                                }
                            });

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };
            };
            thread.start();
        }
    }

    @Override
    public void onImageLost(ImageTracker tracker, final ImageTarget target) {
        Log.v(TAG, "Lost target " + target.getName());
        glRenderer.removeRenderablesForKey(target.getName() + target.getUniqueId());
        renderTextView.dismiss();
     /*   if(mTextView.getParent() != null) {
            ((ViewGroup)mTextView.getParent()).removeView(mTextView); // <- fix
        }

      */
      //  mTextView.setVisibility(View.GONE);
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(5);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mTextView.setVisibility(View.GONE);
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        };
        thread.start();
    }

    @Override
    public void onExtendedTrackingQualityChanged(ImageTracker tracker, final ImageTarget target, final int oldTrackingQuality, final int newTrackingQuality) {

    }
    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            File imageFile = createImageFile(now.toString());
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
        /*    View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);


         */
            EGL10 egl = (EGL10) EGLContext.getEGL();
            GL10 gl = (GL10) egl.eglGetCurrentContext().getGL();
            Bitmap snapshotBitmap = createBitmapFromGLSurface(0, 0, customSurfaceView.getWidth(), customSurfaceView.getHeight(), gl);


            //  File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            assert snapshotBitmap != null;
            snapshotBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
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

    private Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl)
            throws OutOfMemoryError {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }

        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }

}
