package com.jcbriones.gmu.remotecamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class PhotoGalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);

        WebView webview = (WebView) findViewById(R.id.webView);

        webview.loadUrl("http://madeby.jcbriones.com/api_477/index.php");
    }
}
