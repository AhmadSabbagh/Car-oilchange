package ebtkar.app.oilex2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import ebtkar.app.oilex2.activities.RootActivity;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.SharedPrefManager;
import ebtkar.app.oilex2.helper.v2.BackgroundServices;
import ebtkar.app.oilex2.helper.v2.PostAction;

public class RateFinishedRequest extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_finished_request);
        ((TextView)findViewById(R.id.message)).setText(R.string.plz_rate_cus);




       findViewById(R.id.go_rate).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               findViewById(R.id.go_rate).setEnabled(false);
               String order_id = getIntent().getStringExtra("order_id");
               String agent_id = SharedPrefManager.getInstance(RateFinishedRequest.this).getUser().getId();
               String star = ((RatingBar)findViewById(R.id.rating)).getNumStars()+"";
               String comment = ((EditText)findViewById(R.id.cmt_in)).getText().toString();

               HashMap<String ,String> keys= new HashMap<String, String>();
               keys.put("order_id" ,order_id);
               keys.put("agent_id" ,agent_id);
               keys.put("star" ,star);
               keys.put("comment" ,comment);
               BackgroundServices.getInstance(RateFinishedRequest.this).CallPost(APIUrl.SERVER +
                       "Complete", keys, new PostAction() {
                   @Override
                   public void whenFinished(String response) {
                       findViewById(R.id.go_rate).setEnabled(true);
                       try {
                           Toast.makeText(RateFinishedRequest.this,
                                   new JSONObject(response).getString("message")
                                   , Toast.LENGTH_SHORT).show();
                           finish();
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               });


           }
       });


    }
}
