package ebtkar.app.oilex2.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Luminance on 11/1/2018.
 */

public class OilType {
    private String id ,name,picture;

    public OilType(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public OilType(JSONObject j) {
        try {
            this.id = j.getString("id");

            this.name = j.getString("name");
            this.picture = j.getString("picture");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
}
