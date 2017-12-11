package com.jcbriones.gmu.remotecamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class LocalPhotoGalleryActivity extends AppCompatActivity {

    static GridView gridView;
    static ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_photo_gallery);

        gridView = (GridView) findViewById(R.id.grid_view);
        getImagesFromLocal();
    }

    public void getImagesFromLocal() {

    }
}
