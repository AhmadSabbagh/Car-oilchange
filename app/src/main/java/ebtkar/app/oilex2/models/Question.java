package ebtkar.app.oilex2.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Luminance on 11/2/2018.
 */

public class Question {
    private String Request ,Reply,date;

    public String getRequest() {
        return Request;
    }

    public void setRequest(String request) {
        Request = request;
    }

    public String getReply() {
        return Reply;
    }

    public void setReply(String reply) {
        Reply = reply;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Question(JSONObject j) {
        try {
            this.Request = j.getString("Request");
            this.Reply = j.getString("Reply");
            this.date = j.getString("date");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
