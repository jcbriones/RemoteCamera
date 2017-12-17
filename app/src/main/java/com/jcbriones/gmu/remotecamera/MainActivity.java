package com.jcbriones.gmu.remotecamera;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by jayzybriones on 12/5/17.
 * Updated by saraborghei on 12/17/17.
 */

public class MainActivity extends AppCompatActivity {
    private static Context context;

    // Camera and Storage Permissions
    private static final int REQUEST_PERMISSIONS = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.context = getApplicationContext();

        // Ask the user for permissions
        verifyCameraPermissions();
    }

    /**
     * Checks for app camera permissions
     */
    private void verifyCameraPermissions() {
        if (!hasCameraPermissions()) {
            showMessageOKCancel("You need to allow access to Camera and Storage",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    PERMISSIONS_STORAGE,
                                    REQUEST_PERMISSIONS
                            );
                        }
                    });
            return;
        }
    }

    private boolean hasCameraPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            boolean hasWriteExternalPermission = (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            boolean hasReadExternalPermission = (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            boolean hasCameraPermission = (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
            return (hasWriteExternalPermission && hasReadExternalPermission && hasCameraPermission);
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    /**
     * Starts activity_remote_camera
     */
    public void onRemoteCameraButtonClick(View v) {
        if (hasCameraPermissions()) {
            Intent cameraIntent = new Intent(this, RemoteCameraActivity.class);
            startActivity(cameraIntent);
        }
        else {
            Toast.makeText(this, "You need to allow access to Camera and Storage first", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Starts activity_camera_controller
     */
    public void onCameraControllerButtonClick(View v) {
        if (hasCameraPermissions()) {
            Intent cameraIntent = new Intent(this, CameraControllerActivity.class);
            startActivity(cameraIntent);
        }
        else {
            Toast.makeText(this, "You need to allow access to Camera and Storage first", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Start activity_camera_to_server
     */
    public void onCameraToServerButtonClick(View v) {
        if (hasCameraPermissions()) {
            Intent cameraIntent = new Intent(this, CameraToServerActivity.class);
            startActivity(cameraIntent);
        }
        else {
            Toast.makeText(this, "You need to allow access to Camera and Storage first", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Toast how to use app
     */
    public void onHelpClick(View v) {
        Toast.makeText(this, "This app help you remotely take a picture.\nIt requires 2 devices with same app installed.\n" +
                "The Cloud feature uploads pictures taken to the cloud server and available to be viewed in other devices as well.\n\n" +
                "  To take a picture remotely:\n" +
                "  1. Press Camera button (left icon)\n  2. On the other device, press the Controller button (right icon)\n  3. Write the IP address of 1st device on the 2nd device\n" +
                "  4. Press Camera button to take picture on the 2nd device",Toast.LENGTH_LONG).show();
    }
        public static Context getAppContext() {
        return MainActivity.context;
    }
}
