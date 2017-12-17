package com.jcbriones.gmu.remotecamera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jayzybriones on 12/5/17.
 */

public class CameraControllerActivity extends Activity {

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

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

    public void onViewAllPicturesButtonClick(View v) {
        Intent controllerIntent = new Intent(this, LocalPhotoGalleryActivity.class);
        startActivity(controllerIntent);
    }

    /**
     * Client receives data from Server
     */
    private class ClientRxThread extends Thread {
        String dstAddress;
        int dstPort;
        File file;

        ClientRxThread(String address, int port) {
            dstAddress = address;
            dstPort = port;
        }

        // Create and allot image file with the date as file name
        private File createImageFile() throws IOException {

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
            File albumF = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
            return imageF;
        }

        @Override
        public void run() {
            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                        .format(new Date());
                file = createImageFile();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                byte[] bytes;
                FileOutputStream fos = null;
                try {
                    bytes = (byte[])ois.readObject();
                    fos = new FileOutputStream(file);
                    fos.write(bytes);
                } catch (ClassNotFoundException e) {
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
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
