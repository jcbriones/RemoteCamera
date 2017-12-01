package com.jcbriones.gmu.remotecamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Camera cameraObject = null;
    private ShowCamera showCamera;
    private FrameLayout preview;
    private ImageView pic;
    private PictureCallback capturedIt = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data .length);
            if(bitmap==null){
                Toast.makeText(getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "taken", Toast.LENGTH_SHORT).show();
                pic.setImageBitmap(RotateBitmap (bitmap,90));
            }

        }
    };

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
            finish();
        }
        pic = (ImageView)findViewById(R.id.imageView1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraObject == null) {
            cameraObject = isCameraAvailiable();  }
        showCamera = new ShowCamera(this, cameraObject);
        preview.addView(showCamera);
    }
    public static Camera isCameraAvailiable(){
        Camera object = null;
        try {
            object = Camera.open();
        }
        catch (Exception e){
        }
        return object;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (cameraObject != null) {
            cameraObject.release();
            cameraObject = null;
        }
    }

    public void snapIt(View view){
        cameraObject.takePicture(null, null, capturedIt);
    }
}
