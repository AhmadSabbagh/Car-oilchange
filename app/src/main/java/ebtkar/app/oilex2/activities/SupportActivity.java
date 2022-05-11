package ebtkar.app.oilex2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.adapters.SupportAdapter;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.BackgroundServices;
import ebtkar.app.oilex2.helper.PostAction;
import ebtkar.app.oilex2.helper.SharedPrefManager;
import ebtkar.app.oilex2.models.Question;

public class SupportActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);




        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SupportActivity.this,SendSupportMessageActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        init(0);
    }

    SupportAdapter adapter;
    void init(final int t){

        findViewById(R.id.loading).setVisibility(View.VISIBLE);
         HashMap<String, String> args = new HashMap<String, String>();
        args.put("id", SharedPrefManager.getInstance(this).getUser().getId());
        BackgroundServices.getInstance(this).CallPost(APIUrl.SERVER + "reply_to_help",args, new PostAction() {
            @Override
            public void doTask(String s) {

                if(s.equals("")){
                    if(t<20)init(t+1);
                    else{
                        Toast.makeText(SupportActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    findViewById(R.id.loading).setVisibility(View.GONE);
                    try {
                        JSONArray array = new JSONArray(s);
                        if(array.length()>0) {
                            ArrayList arraylist = new ArrayList();
                            for (int i = 0; i < array.length(); i++)
                                arraylist.add(new Question(array.getJSONObject(i)));
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(SupportActivity.this));
                            adapter = new SupportAdapter(arraylist, SupportActivity.this);
                            recyclerView.setAdapter(adapter);
                            findViewById(R.id.loading).setVisibility(View.GONE);

                        }else
                            Toast.makeText(SupportActivity.this, R.string.no_results_found, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }


}
