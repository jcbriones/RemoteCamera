package com.jcbriones.gmu.remotecamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
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

public class PhotoGalleryActivity extends AppCompatActivity {
    private static String IMAGES_URL = "http://madeby.jcbriones.com/api_477/get_images.php";

    static GridView gridView;
    static ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);

        gridView = (GridView) findViewById(R.id.grid_view);
        getImagesFromServer();
    }

    public void getImagesFromServer() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, IMAGES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray allImageArray = jsonObject.optJSONArray("images");
                    if(allImageArray != null && allImageArray.length() > 0){

                        ArrayList<ImageObject> imageObjects = new ArrayList<>();
                        for(int i = 0; i < allImageArray.length();i++){
                            JSONObject jsonItem = allImageArray.optJSONObject(i);

                            imageObjects.add(new ImageObject(jsonItem));
                        }

                        imageAdapter= new ImageAdapter(PhotoGalleryActivity.this, imageObjects);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gridView.setAdapter(imageAdapter);
                            }
                        });
                    }
                } catch (Exception e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

}
