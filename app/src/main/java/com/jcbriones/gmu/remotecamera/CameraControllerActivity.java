package com.jcbriones.gmu.remotecamera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraControllerActivity extends Activity {
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void onViewAllPicturesButtonClick(View v) {
        Intent controllerIntent = new Intent(this, PhotoGalleryActivity.class);
        startActivity(controllerIntent);
    }

    EditText editTextAddress, editTextPort;
    ImageButton buttonTakePicture;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_controller);

        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextPort = (EditText) findViewById(R.id.editTextPort);
        buttonTakePicture = (ImageButton) findViewById(R.id.controllerTakePicture);
        imageView = (ImageView) findViewById(R.id.image_result);

        buttonTakePicture.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                ClientRxThread clientRxThread =
                        new ClientRxThread(
                                editTextAddress.getText().toString(),
                                Integer.valueOf(editTextPort.getText().toString()));

                clientRxThread.start();
            }});

    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private class ClientRxThread extends Thread {
        String dstAddress;
        int dstPort;
        File file;

        ClientRxThread(String address, int port) {
            dstAddress = address;
            dstPort = port;
        }

        @Override
        public void run() {
            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                        .format(new Date());
                file = new File(
                        Environment.getExternalStorageDirectory(),
                        "IMG_" + timeStamp + ".jpg");

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                byte[] bytes;
                FileOutputStream fos = null;
                try {
                    bytes = (byte[])ois.readObject();
                    fos = new FileOutputStream(file);
                    fos.write(bytes);
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    if(fos!=null){
                        fos.close();
                    }

                }

                socket.close();

                CameraControllerActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (file.exists()) {
                            Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                            //imageView.setImageBitmap(bmp);
                            // Scale the image to fit in the view
                            int nh = (int) ( bmp.getHeight() * (512.0 / bmp.getWidth()) );
                            Bitmap scaled = Bitmap.createScaledBitmap(bmp, 512, nh, true);
                            imageView.setImageBitmap(scaled);
                            Toast.makeText(CameraControllerActivity.this,
                                    "Finished",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(CameraControllerActivity.this,
                                    "Something went wrong",
                                    Toast.LENGTH_LONG).show();
                        }
                    }});

            } catch (IOException e) {

                e.printStackTrace();

                final String eMsg = "Something wrong: " + e.getMessage();
                CameraControllerActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(CameraControllerActivity.this,
                                eMsg,
                                Toast.LENGTH_LONG).show();
                    }});

            } finally {
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
