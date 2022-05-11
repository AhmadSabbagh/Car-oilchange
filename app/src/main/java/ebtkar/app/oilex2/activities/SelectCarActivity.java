package ebtkar.app.oilex2.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.adapters.BrandsAdapter;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.BackgroundServices;
import ebtkar.app.oilex2.helper.PostAction;
import ebtkar.app.oilex2.models.CarModel;

public class SelectCarActivity extends RootActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_car);
//        init();
        init(0);



    }







    BrandsAdapter adapter;
    void init(final int t){
        BackgroundServices.getInstance(this).CallPost(APIUrl.SERVER + "getdata_car",new HashMap<String, String>(), new PostAction() {
            @Override
            public void doTask(String s) {
                if(s.equals("")){
                    if(t<20)init(t+1);
                    else{
                        Toast.makeText(SelectCarActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    try {
                        JSONArray array = new JSONArray(s);
                        if(array.length()>0) {
                            ArrayList arraylist = new ArrayList();
                            for (int i = 0; i < array.length(); i++)
                                arraylist.add(new CarModel(array.getJSONObject(i)));
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(SelectCarActivity.this));
                            adapter = new BrandsAdapter(arraylist, SelectCarActivity.this);
                            recyclerView.setAdapter(adapter);
                            findViewById(R.id.loading).setVisibility(View.GONE);
                            findViewById(R.id.heart).setVisibility(View.VISIBLE);
                            ((EditText)  findViewById(R.id.search)).addTextChangedListener(new TextWatcher() {

                                public void onTextChanged(CharSequence s, int start, int before,
                                                          int count) {
                                    if(!s.equals("") ) {
                                       adapter.getFilter().filter(s);
                                    }
                                }



                                public void beforeTextChanged(CharSequence s, int start, int count,
                                                              int after) {

                                }

                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }else
                            Toast.makeText(SelectCarActivity.this, R.string.no_results_found, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }



}
