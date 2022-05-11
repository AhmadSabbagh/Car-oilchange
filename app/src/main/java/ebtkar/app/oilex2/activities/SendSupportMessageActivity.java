package ebtkar.app.oilex2.activities;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.BackgroundServices;
import ebtkar.app.oilex2.helper.PostAction;
import ebtkar.app.oilex2.helper.SharedPrefManager;
import ebtkar.app.oilex2.models.User;

public class SendSupportMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        Locale locale = new Locale(SharedPrefManager.getInstance(this).getSavedLanguage());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_support_message);

        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = ((EditText)findViewById(R.id.message)).getText().toString().trim();
                if(msg.equals("")){
                    Toast.makeText(SendSupportMessageActivity.this,
                            R.string.enter_message, Toast.LENGTH_SHORT).show();
                    return;
                }
                v.setEnabled(false);
                SUPPORT(0,msg);
            }
        });

    }



    void SUPPORT(final int t, final String code) {
        // HashMap<String, String> params =  new HashMap<String, String>();
        //  params.put("car_id",);
        Toast.makeText(this, R.string.please_wait, Toast.LENGTH_SHORT).show();
        HashMap args = new HashMap<String, String>();

        String uer_type = SharedPrefManager.getInstance(this).getUser().getUser_type().equals(User.SERVICE_PROVIDER)?"agent_id":"customer_id";
        String service_type = SharedPrefManager.getInstance(this).getUser().getUser_type().equals(User.SERVICE_PROVIDER)?"Complaints_and_suggestions_agent":"Complaints_and_suggestions";

        args.put(uer_type, SharedPrefManager.getInstance(this).getUser().getId());
        args.put("help", code);

        BackgroundServices.getInstance(this).CallPost(APIUrl.SERVER + service_type, args

                , new PostAction() {
                    @Override
                    public void doTask(String s) {
                        if (s.equals("")) {
                            if (t < 20) SUPPORT(t + 1, code);
                            else {
                                Toast.makeText(SendSupportMessageActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            try {
                                JSONObject array = new JSONObject(s);
                                Toast.makeText(SendSupportMessageActivity.this,
                                        R.string.msg_sent
                                        , Toast.LENGTH_SHORT).show();
                                finish();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


    }
}
