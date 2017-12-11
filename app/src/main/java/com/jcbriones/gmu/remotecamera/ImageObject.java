package com.jcbriones.gmu.remotecamera;

import org.json.JSONObject;

/**
 * Created by jayzybriones on 12/7/17.
 */

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
