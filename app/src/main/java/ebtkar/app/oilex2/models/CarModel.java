package ebtkar.app.oilex2.models;

import org.json.JSONException;
import org.json.JSONObject;

import ebtkar.app.oilex2.helper.APIUrl;

/**
 * Created by Luminance on 10/31/2018.
 */

public class CarModel {

    private String id ,name,picture;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public CarModel(String id, String name, String picture) {
        this.id = id;
        this.name = name;
        this.picture = picture;
    }
    public CarModel(JSONObject j) {

        try {
            this.id = j.getString("id");
            this.name = j.getString("name");
            this.picture = APIUrl.MEDIA_SERVER+j.getString("picture");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
