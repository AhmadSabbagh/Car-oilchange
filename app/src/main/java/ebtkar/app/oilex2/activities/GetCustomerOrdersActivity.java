package ebtkar.app.oilex2.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.adapters.MyOrdersAdapter;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.BackgroundServices;
import ebtkar.app.oilex2.helper.PostAction;
import ebtkar.app.oilex2.helper.SharedPrefManager;
import ebtkar.app.oilex2.models.Order;

public class GetCustomerOrdersActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_customer_orders);
        init();
        init(0);
    }
    ArrayList arraylist=new ArrayList();

    void init(final int t){
        // HashMap<String, String> params =  new HashMap<String, String>();
        findViewById(R.id.loading).setVisibility(View.VISIBLE);

        //  params.put("car_id",);
        HashMap args =  new HashMap<String, String>();
        args.put("customer_id",
                SharedPrefManager.getInstance(this).getUser().getId()
      //  "1"
        );
        Log.d("SANSHI" ,args.toString());

        BackgroundServices.getInstance(this).CallPost(APIUrl.SERVER + "getdata_orders_customer",args

                , new PostAction() {
                    @Override
                    public void doTask(String s) {
                        if(s.equals("")){
                            if(t<20)init(t+1);
                            else{
                                Toast.makeText(GetCustomerOrdersActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }else{
                            try {
                                JSONArray array = new JSONArray(s);
                                if(array.length()>0) {
                                    arraylist = new ArrayList();
                                    for (int i = 0; i < array.length(); i++)
                                        arraylist.add(new Order(array.getJSONObject(i)));
                                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(GetCustomerOrdersActivity.this));
                                    recyclerView.setAdapter(new MyOrdersAdapter(arraylist, GetCustomerOrdersActivity.
                                            this));
                                }else{
                                    Toast.makeText(GetCustomerOrdersActivity.this, R.string.no_results_found, Toast.LENGTH_SHORT).show();
                                }
                                findViewById(R.id.loading).setVisibility(View.GONE);
                                findViewById(R.id.heart).setVisibility(View.VISIBLE);
                                findViewById(R.id.go_pinding).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        ArrayList  aaa = new ArrayList();
                                        for (int i = 0; i < arraylist.size(); i++)
                                            if(!((Order)arraylist.get(i)).isStatus())
                                            aaa.add(arraylist.get(i));
                                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(GetCustomerOrdersActivity.this));
                                        recyclerView.setAdapter(new MyOrdersAdapter(aaa, GetCustomerOrdersActivity.
                                                this));

                                    }
                                });
                                findViewById(R.id.go_finished).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        ArrayList  aaa = new ArrayList();
                                        for (int i = 0; i < arraylist.size(); i++)
                                            if(((Order)arraylist.get(i)).isStatus())
                                        aaa.add(arraylist.get(i));
                                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
                                        recyclerView.setLayoutManager(new
                                                LinearLayoutManager(GetCustomerOrdersActivity.this));
                                        recyclerView.setAdapter(new MyOrdersAdapter(aaa,
                                                GetCustomerOrdersActivity.
                                                this));

                                    }
                                });



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


    }
}
