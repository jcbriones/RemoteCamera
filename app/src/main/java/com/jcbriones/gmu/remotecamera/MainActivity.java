package com.jcbriones.gmu.remotecamera;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.context = getApplicationContext();
    }

    public void onRemoteCameraButtonClick(View v) {
        Intent cameraIntent = new Intent(this, RemoteCameraActivity.class);
        startActivity(cameraIntent);
    }

    public void onCameraControllerButtonClick(View v) {
        Intent cameraIntent = new Intent(this, CameraControllerActivity.class);
        startActivity(cameraIntent);
    }

    public void onRemoteMyCameraButtonClick(View v) {
        Intent cameraIntent = new Intent(this, MyCamera.class);
        startActivity(cameraIntent);
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }
}
