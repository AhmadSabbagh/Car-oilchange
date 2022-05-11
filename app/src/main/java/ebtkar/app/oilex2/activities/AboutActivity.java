package ebtkar.app.oilex2.activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.BackgroundServices;
import ebtkar.app.oilex2.helper.PostAction;
import ebtkar.app.oilex2.helper.SharedPrefManager;

public class AboutActivity extends Activity {

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
        setContentView(R.layout.activity_about);
        init(0);
    }
    void init(final int t){
        // HashMap<String, String> params =  new HashMap<String, String>();
        //  params.put("car_id",);
        HashMap args =  new HashMap<String, String>();

        BackgroundServices.getInstance(this).CallPost(APIUrl.SERVER + "getdata_About_us",args

                , new PostAction() {
                    @Override
                    public void doTask(String s) {
                        if(s.equals("")){
                            if(t<20)init(t+1);
                            else{
                                Toast.makeText(AboutActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }else{
                            try {
                                JSONObject array = new JSONObject(s);

                                ((TextView)findViewById(R.id.about_text)).setText(array.getString("about_us"));
                                Glide.with(getApplicationContext())
                                        .load(APIUrl.MEDIA_SERVER+array.getString("picture"))
                                        .into((ImageView)findViewById(R.id.logo));
                                findViewById(R.id.loading).setVisibility(View.GONE);
                                findViewById(R.id.about_text).setVisibility(View.VISIBLE);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


    }
}
