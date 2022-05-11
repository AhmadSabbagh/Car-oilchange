package ebtkar.app.oilex2.models;

import org.json.JSONException;
import org.json.JSONObject;

import ebtkar.app.oilex2.helper.APIUrl;

/**
 * Created by Luminance on 11/2/2018.
 */

public class OilBrand {
    private String id ,price,recommend,name,picture;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OilBrand(JSONObject j) {
        try {
            this.price = j.getString("price");
            this.id = j.getString("id");
            this.recommend = j.getString("recommend");
            this.name = j.getString("name");
            if (j.has("picture")) {
                this.picture = APIUrl.MEDIA_SERVER + j.getString("picture");
            }else{
                this.picture = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
