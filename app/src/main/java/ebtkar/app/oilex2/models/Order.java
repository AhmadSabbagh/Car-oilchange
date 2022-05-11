package ebtkar.app.oilex2.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Luminance on 12/4/2018.
 */

public class Order {
    private String id,oil_name ,agent_name ,price ,date ,time;
    boolean status;

    public Order(String id, String oil_name, String agent_name, String price, String date, String time, boolean status) {
        this.id = id;
        this.oil_name = oil_name;
        this.agent_name = agent_name;
        this.price = price;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Order(JSONObject j) {
        try {
            this.id = j.getString("id");
            this.oil_name =  j.getString("oil_name");
            this.agent_name =  j.getString("agent_name");
            this.price =  j.getString("price");
            this.date =  j.getString("date");
            this.time =  j.getString("time");
            this.status =  j.getBoolean("state");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Order(String id, String oil_name, String agent_name, String price, String date, String time) {
        this.id = id;
        this.oil_name = oil_name;
        this.agent_name = agent_name;
        this.price = price;
        this.date = date;
        this.time = time;
    }
}
