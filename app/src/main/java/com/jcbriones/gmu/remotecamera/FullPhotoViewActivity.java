package com.jcbriones.gmu.remotecamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FullPhotoViewActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo_view);

        imageView = (ImageView) findViewById(R.id.image);
        if (getIntent().hasExtra("image")) {
            Bitmap bitmap = getIntent().getParcelableExtra("image");
            imageView.setImageBitmap(bitmap);
        }
        else {
            DisplayMetrics size = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(size );
            int w = size.widthPixels;
            int h = size.heightPixels;
            String url = getIntent().getStringExtra("url");
            System.out.println(url + " w: " + w + " h: " + h);
            Glide
                    .with(this)
                    .load(url)
                    .asBitmap()  // переводим его в нужный формат
                    .fitCenter()
                    .into(new SimpleTarget<Bitmap>(w, h) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
        }
    }
}
