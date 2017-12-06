package com.jcbriones.gmu.remotecamera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CameraControllerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_controller);
    }


    public void onViewAllPicturesButtonClick(View v) {
        Intent controllerIntent = new Intent(this, PhotoGalleryActivity.class);
        startActivity(controllerIntent);
    }
}
