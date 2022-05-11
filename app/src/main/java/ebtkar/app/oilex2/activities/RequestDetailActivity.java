package ebtkar.app.oilex2.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.BackgroundServices;
import ebtkar.app.oilex2.helper.PostAction;
import ebtkar.app.oilex2.helper.SharedPrefManager;
import ebtkar.app.oilex2.models.OilBrand;

public class RequestDetailActivity extends RootActivity {

    public  static
    String lat="",lng="",car_id="",car_model_id="",car_icon="",car_name="",oil_id="",oil_name="";
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
        setContentView(R.layout.activity_request_detail);


        init();

         lng = getIntent().getStringExtra("lng");
         lat = getIntent().getStringExtra("lat");


        findViewById(R.id.select_car).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(RequestDetailActivity.this, SelectCarActivity.class);
                startActivity(ii);
            }
        });

        findViewById(R.id.select_oil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(car_model_id.equals("")) {
                    Toast.makeText(RequestDetailActivity.this, R.string.car_type_required,Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent ii = new Intent(RequestDetailActivity.this, SelectOilCaliberActivity.class);
                ii.putExtra("car_model_id" ,car_model_id);
                ii.putExtra("car_id" ,car_id);
                startActivity(ii);
            }
        });
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(oil_id.equals("")) {
                    Toast.makeText(RequestDetailActivity.this, R.string.oil_type_required,Toast.LENGTH_SHORT).show();
                    return;
                }
getDetial(0);

//                boolean filter = ((CheckBox)findViewById(R.id.has_filter)).isChecked();
//                Intent ii = new Intent(RequestDetailActivity.this ,OrderPreviewActivity.class);
//                ii.putExtra("oil_id" ,oil_id);
//                ii.putExtra("car_id" ,car_id);
//                ii.putExtra("car_model_id" ,car_model_id);
//                ii.putExtra("lat" ,lat);
//                ii.putExtra("lng" ,lng);
//                ii.putExtra("address" ,"");
//                ii.putExtra("filter" ,filter?"true":"false");
////                ii.putExtra("price" ,getIntent().getStringExtra("price"));
////                ii.putExtra("car" ,"Toyota");
//                startActivity(ii);
            }
        });
    }






    void getDetial(final int t){
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.loading_text)).setText(R.string.l1);
        findViewById(R.id.send).setEnabled(false);
        HashMap<String, String> params =  new HashMap<String, String>();
        params.put("oil_id",oil_id);
        params.put("car_id",car_id);


        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        BackgroundServices.getInstance(this).CallPost(APIUrl.SERVER +
                        "getdata_oil_descr"
                ,params
                , new PostAction() {
                    @Override
                    public void doTask(String s) {
                        if(s.equals("")){
                            if(t<20)getDetial(t+1);
                            else{
                                findViewById(R.id.loading).setVisibility(View.GONE);
                                Toast.makeText(RequestDetailActivity.this,
                                        R.string.server_error, Toast.LENGTH_SHORT).show();
                                findViewById(R.id.send).setEnabled(true);
                            }
                        }else{
                            try {
                                JSONArray array = new JSONArray(s);
                                OilBrand   oilBrand = new OilBrand(array.getJSONObject(0));
                                sendOrder(0 ,oilBrand);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                         //   findViewById(R.id.loading).setVisibility(View.GONE);
//                              Toast.makeText(OrderPreviewActivity.this, s, Toast.LENGTH_SHORT).show();

//                            Intent intent = new Intent(OrderPreviewActivity.this, RateUserActivity.class);
//                            intent.putExtra("message" ,getString(R.string.please_rate_services));
//                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);

                        }
                    }
                });


    }

    void sendOrder(final int t , final OilBrand oilBrand){
        ((TextView)findViewById(R.id.loading_text)).setText(R.string.l2);

        final boolean filter = ((AppCompatCheckBox)findViewById(R.id.has_filter)).isChecked();
        HashMap<String, String> params =  new HashMap<String, String>();
        params.put("customer_id",SharedPrefManager.getInstance(this).getUser().getId());
        params.put("oil_id",oil_id);
        params.put("price",oilBrand.getPrice());
        params.put("longit",lng);
        params.put("latet",lat);
        params.put("address","");
        params.put("filter",filter?"true":"false");

        BackgroundServices.getInstance(this).CallPost(APIUrl.SERVER +
                        "add_order"
                ,params
                , new PostAction() {
                    @Override
                    public void doTask(String s) {
                        if(s.equals("")){
                            if(t<20)sendOrder(t+1 ,oilBrand);
                            else{
                                findViewById(R.id.loading).setVisibility(View.GONE);
                                Toast.makeText(RequestDetailActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                                findViewById(R.id.send).setEnabled(true);
                            }
                        }else{

                            findViewById(R.id.loading).setVisibility(View.GONE);
                            //  Toast.makeText(OrderPreviewActivity.this, s, Toast.LENGTH_SHORT).show();

                            JSONObject invoice = null;
                            try {
                                invoice = new JSONObject(s);

Intent ii = new Intent(RequestDetailActivity.this ,OrderPreviewActivity.class);
                                ii.putExtra("id" ,invoice.getString("invoice_id"));
                                ii.putExtra("type" ,oilBrand.getName());
                                ii.putExtra("filter",filter?"true":"false");
                                ii.putExtra("price",invoice.getString("price"));
                                try {
                                    ii.putExtra("filter_price", invoice.getString("filter_price"));
                                }catch (JSONException er){}
                                ii.putExtra("pic",oilBrand.getPicture());
                                ii.putExtra("date",invoice.getString("time"));


                                startActivity(ii) ;
                                lat="";
                                lng="";
                                car_id="";
                                car_model_id="";
                                car_icon="";
                                car_name="";
                                oil_id="";
                                oil_name="";
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


//                            try {
//                                new AlertDialog.Builder(OrderPreviewActivity.this)
//                                        .setTitle("Order sent")
//                                        .setMessage("Your invoice number is " +
//                                                new JSONObject(s).getString("invoice_id"))
//                                        .setIcon(android.R.drawable.ic_dialog_info)
//                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                                            public void onClick(DialogInterface dialog, int whichButton) {
//                                                Intent intent = new Intent(OrderPreviewActivity.this, RateUserActivity.class);
//                                                intent.putExtra("message" ,getString(R.string.please_rate_services));
//                                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                startActivity(intent);
//                                                OrderPreviewActivity.this.finish();
//                                            }})
//                                       .show();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }


                        }
                    }
                });

    }




    @Override
    protected void onResume() {
        super.onResume();
        if(!car_name .equals("")){
            ((Button)findViewById(R.id.select_car)).setText(car_name);


        }
        if(!car_icon .equals("")){
            Glide.with(this).load(car_icon)
                    .into(((ImageView)findViewById(R.id.car_image)));

        }
        if(!oil_name .equals("")){
            ((Button)findViewById(R.id.select_oil)).setText(oil_name);

        }
    }
}
