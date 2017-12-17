package com.jcbriones.gmu.remotecamera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jayzybriones on 12/5/17.
 */

public class RemotePhotoGalleryActivity extends AppCompatActivity {
    private static String IMAGES_URL = "http://madeby.jcbriones.com/api_477/get_images.php";

    static GridView gridView;
    static ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_photo_gallery);

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

                        imageAdapter= new ImageAdapter(RemotePhotoGalleryActivity.this, imageObjects);

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

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<ImageObject> imageObjects;

        private LayoutInflater mLayoutInflate;

        public ImageAdapter (Context context, ArrayList<ImageObject> imageObjects){
            this.context = context;
            this.imageObjects = imageObjects;

            this.mLayoutInflate = LayoutInflater.from(context);
        }

        public int getCount() {
            if(imageObjects != null) return  imageObjects.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if(imageObjects != null && imageObjects.size() > position) return  imageObjects.get(position);

            return null;
        }

        @Override
        public long getItemId(int position) {
            if(imageObjects != null && imageObjects.size() > position) return  imageObjects.get(position).getId();
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {

                viewHolder = new ViewHolder();

                convertView = mLayoutInflate.inflate(R.layout.image_object, parent,
                        false);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final ImageObject imageObject = (ImageObject) getItem(position);
            if(imageObject != null) {
                Glide
                        .with(context)
                        .load(imageObject.getImageUrl())
                        .centerCrop()
                        .crossFade()
                        .into(viewHolder.imageView);
            }

            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Create intent
                    Intent intent = new Intent(RemotePhotoGalleryActivity.this, FullPhotoViewActivity.class);
                    intent.putExtra("url", imageObject.getImageUrl());

                    //Start details activity
                    startActivity(intent);
                }
            });

            return convertView;
        }

        private class ViewHolder {
            public ImageView imageView;
        }
    }

    public class ImageObject {
        private int id;
        private String imageUrl;

        public  ImageObject(JSONObject jsonObject){
            if(jsonObject == null) return;
            this.id = jsonObject.optInt("id");
            this.imageUrl = "http://madeby.jcbriones.com/api_477/" + jsonObject.optString("url");
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

}
