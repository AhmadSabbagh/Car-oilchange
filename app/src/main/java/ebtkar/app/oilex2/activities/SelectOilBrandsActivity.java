package ebtkar.app.oilex2.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.adapters.OilBrandAdapter;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.BackgroundServices;
import ebtkar.app.oilex2.helper.PostAction;
import ebtkar.app.oilex2.models.OilBrand;

public class SelectOilBrandsActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_oil_brands);
//        init();
        init(0);

        ImageView oil_pic = findViewById(R.id.oil_img);
        TextView oil_title  =findViewById(R.id.oil_title);
        oil_title.setText(getIntent().getStringExtra("oil_name"));
        if(!(getIntent().getStringExtra("oil_pic").equals("-")||getIntent().getStringExtra("oil_pic").equals("")))
        Glide.with(this).load(getIntent().getStringExtra("oil_pic"))
                .into(oil_pic);
    }

    OilBrandAdapter adapter;
    void init(final int t){
         HashMap<String, String> params =  new HashMap<String, String>();
          params.put("car_id",getIntent().getStringExtra("car_id"));
          params.put("scar_id",getIntent().getStringExtra("car_model_id"));
          params.put("Caliber_oil_id",getIntent().getStringExtra("calib_id"));


        BackgroundServices.getInstance(this).CallPost(APIUrl.SERVER + "getdata_oils" ,params
                , new PostAction() {
                    @Override
                    public void doTask(String s) {
                        if(s.equals("")){
                            if(t<20)init(t+1);
                            else{
                                Toast.makeText(SelectOilBrandsActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }else{
                            try {
                                JSONArray array = new JSONArray(s);
                                ArrayList arraylist = new ArrayList();
                                for(int i=0;i<array.length();i++)
                                    arraylist.add(new OilBrand(array.getJSONObject(i)));
                                RecyclerView recyclerView= (RecyclerView)findViewById(R.id.recycleView);
                                recyclerView.setLayoutManager(new LinearLayoutManager(SelectOilBrandsActivity.this));
                               adapter=new OilBrandAdapter(arraylist,SelectOilBrandsActivity.
                                       this);
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
