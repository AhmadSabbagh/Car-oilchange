package ebtkar.app.oilex2.activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.RateFinishedRequest;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.SharedPrefManager;
import ebtkar.app.oilex2.helper.v2.BackgroundServices;
import ebtkar.app.oilex2.helper.v2.PostAction;

public class RateUserActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_finished_request);
        ((TextView) findViewById(R.id.message)).setText(R.string.rate_provider
                );
        findViewById(R.id.go_rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.go_rate).setEnabled(false);
                String order_id = getIntent().getStringExtra("order_id");
                String agent_id = SharedPrefManager.getInstance(RateUserActivity.this).getUser().getId();
                String star = ((RatingBar) findViewById(R.id.rating)).getNumStars() + "";
                String comment = ((EditText) findViewById(R.id.cmt_in)).getText().toString();
                HashMap<String, String> keys = new HashMap<>();

                keys.put("order_id", order_id);
                keys.put("user_id", agent_id);
                keys.put("star", star);
                keys.put("comment", comment);
                BackgroundServices.getInstance(RateUserActivity.this).CallPost(APIUrl.SERVER +
                        "Complete_user", keys, new PostAction() {
                    @Override
                    public void whenFinished(String response) {
                        findViewById(R.id.go_rate).setEnabled(true);
                        try {
                            Toast.makeText(RateUserActivity.this,
                                    new JSONObject(response).getString("message")
                                    , Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });


    }
}
