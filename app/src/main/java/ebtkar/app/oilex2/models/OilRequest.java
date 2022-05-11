package ebtkar.app.oilex2.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Luminance on 11/3/2018.
 */

public class OilRequest {
    private String id ,oil_name,phone;

    public OilRequest(String id, String oil_name, String phone) {
        this.id = id;
        this.oil_name = oil_name;
        this.phone = phone;
    }
    public OilRequest(JSONObject j) {
        try {
            this.id = j.getString("id");
            this.oil_name = j.getString("oil_name");
            this.phone = j.getString("phone");
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

    public String getOil_name() {
        return oil_name;
    }

    public void setOil_name(String oil_name) {
        this.oil_name = oil_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
