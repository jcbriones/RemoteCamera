package com.jcbriones.gmu.remotecamera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRemoteCameraButtonClick(View v) {
        Intent remoteIntent = new Intent(this, RemoteCameraActivity.class);
        startActivity(remoteIntent);
    }

    public void onCameraControllerButtonClick(View v) {
        Intent controllerIntent = new Intent(this, CameraControllerActivity.class);
        startActivity(controllerIntent);
    }


}
