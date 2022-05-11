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
import ebtkar.app.oilex2.adapters.OilCaliberAdapter;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.BackgroundServices;
import ebtkar.app.oilex2.helper.PostAction;
import ebtkar.app.oilex2.models.OilType;

public class SelectOilCaliberActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_oil_brand);
//        init();
        init(0);

//        TextView toolbar_title = findViewById(R.id.toolbar_title);
//        toolbar_title.setText(R.string.select_oil_brand);

    }

    OilCaliberAdapter adapter;
    void init(final int t){
        BackgroundServices.getInstance(this).CallPost(APIUrl.SERVER + "getdata_Caliber_oil",new HashMap<String, String>(), new PostAction() {
            @Override
            public void doTask(String s) {
                if(s.equals("")){
                    if(t<20)init(t+1);
                    else{
                        Toast.makeText(SelectOilCaliberActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    try {
                        JSONArray array = new JSONArray(s);
                        ArrayList arraylist = new ArrayList();
                        for(int i=0;i<array.length();i++)
                            arraylist.add(new OilType(array.getJSONObject(i)));
                        RecyclerView recyclerView= (RecyclerView)findViewById(R.id.recycleView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(SelectOilCaliberActivity.this));
                        adapter=new OilCaliberAdapter(arraylist,SelectOilCaliberActivity.this);
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




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }


}
