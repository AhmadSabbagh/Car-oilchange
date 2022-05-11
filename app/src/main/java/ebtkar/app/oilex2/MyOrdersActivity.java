package ebtkar.app.oilex2;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import ebtkar.app.oilex2.activities.RootActivity;
import ebtkar.app.oilex2.adapters.OilRequestsAdapter;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.BackgroundServices;
import ebtkar.app.oilex2.helper.PostAction;
import ebtkar.app.oilex2.helper.SharedPrefManager;
import ebtkar.app.oilex2.models.OilRequest;

public class MyOrdersActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        init();
    }
    @Override
    protected void onResume() {
        super.onResume();
        init(0);
    }

    void init(final int t){
        // HashMap<String, String> params =  new HashMap<String, String>();
        //  params.put("car_id",);
        HashMap args =  new HashMap<String, String>();
        args.put("agent_id", SharedPrefManager.getInstance(this).getUser().getId());

        BackgroundServices.getInstance(this).CallPost(APIUrl.SERVER + "getdata_orders_wait",args

                , new PostAction() {
                    @Override
                    public void doTask(String s) {
                        if(s.equals("")){
                            if(t<20)init(t+1);
                            else{
                                Toast.makeText(MyOrdersActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }else{
                            try {
                                JSONArray array = new JSONArray(s);
                                if(array.length()>0) {
                                    ArrayList arraylist = new ArrayList();
                                    for (int i = 0; i < array.length(); i++)
                                        arraylist.add(new OilRequest(array.getJSONObject(i)));
                                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(MyOrdersActivity.this));
                                    recyclerView.setAdapter(new OilRequestsAdapter(arraylist, MyOrdersActivity.
                                            this));
                                }else{
                                    Toast.makeText(MyOrdersActivity.this, R.string.no_results_found, Toast.LENGTH_SHORT).show();
                                }
                                findViewById(R.id.loading).setVisibility(View.GONE);
                                findViewById(R.id.heart).setVisibility(View.VISIBLE);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


    }
}
