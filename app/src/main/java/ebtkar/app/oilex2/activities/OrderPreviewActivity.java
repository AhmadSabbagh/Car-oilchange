package ebtkar.app.oilex2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.BackgroundServices;
import ebtkar.app.oilex2.helper.PostAction;
import ebtkar.app.oilex2.helper.SharedPrefManager;
import ebtkar.app.oilex2.models.OilBrand;

public class OrderPreviewActivity  extends RootActivity  {

    TextView name,price,detail ,filter;
    ImageView img;
    OilBrand oilBrand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_preview);
        name=((TextView)findViewById(R.id.oil_type));
        detail=((TextView)findViewById(R.id.oil_detail));
        filter=((TextView)findViewById(R.id.oil_filter));

        price=((TextView)findViewById(R.id.oil_price));
//        img=((ImageView)findViewById(R.id.oil_pic));
//        Glide.with(this).load(getIntent().getStringExtra("pic")).into(img);
       name.setText(getIntent().getStringExtra("type"));
       price.setText(getIntent().getStringExtra("price"));
        filter.setText(getIntent().getStringExtra("filter").equals("true")?
                getResources().getString(R.string.yes):getResources().getString(R.string.no));
        if(getIntent().getStringExtra("filter").equals("true") &&
                getIntent().getStringExtra("filter_price")!=null){
           findViewById(R.id.f_p).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.filter_price)).setText(getIntent().getStringExtra("filter_price"));
        }

       name.setText(getIntent().getStringExtra("type"));
        ((TextView)findViewById(R.id.invoice_id)).setText( getIntent().getStringExtra("id"));
        ((TextView)findViewById(R.id.invoice_time)).setText( getIntent().getStringExtra("date"));

        findViewById(R.id.go_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    confirm(getIntent().getStringExtra("id"));

            }
        });

    }


    void confirm(final String inv_id){
        findViewById(R.id.go_continue).setEnabled(false);
        HashMap<String, String> params =  new HashMap<String, String>();
        params.put("customer_id",SharedPrefManager.getInstance(OrderPreviewActivity.this)
                .getUser().getId());

            params.put("invoice" ,inv_id);

        BackgroundServices.getInstance(OrderPreviewActivity.this)
                .CallPost(APIUrl.SERVER +
                                "confirm_order"
                        , params
                        , new PostAction() {
                            @Override
                            public void doTask(String s) {
                                findViewById(R.id.go_continue).setEnabled(true);

                                if(s.equals("")){
                                    Toast.makeText(OrderPreviewActivity.this, R.string.server_error,
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    startActivity(new Intent(OrderPreviewActivity.this,
                                            GetCustomerOrdersActivity.class));
                                    finish();}
                            }
                        });


    }

    void init(final int t){
         HashMap<String, String> params =  new HashMap<String, String>();
          params.put("customer_id",SharedPrefManager.getInstance(this).getUser().getId());
          params.put("oil_id",getIntent().getStringExtra("oil_id"));
          params.put("price",oilBrand.getPrice());
          params.put("longit",getIntent().getStringExtra("lng"));
          params.put("latet",getIntent().getStringExtra("lat"));
          params.put("address",getIntent().getStringExtra("address"));
          params.put("filter",getIntent().getStringExtra("filter"));
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        findViewById(R.id.go_continue).setEnabled(false);
        BackgroundServices.getInstance(this).CallPost(APIUrl.SERVER +
                        "add_order"
                ,params
                , new PostAction() {
                    @Override
                    public void doTask(String s) {
                        if(s.equals("")){
                            if(t<20)init(t+1);
                            else{
                                Toast.makeText(OrderPreviewActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }else{

                            findViewById(R.id.loading).setVisibility(View.GONE);
                          //  Toast.makeText(OrderPreviewActivity.this, s, Toast.LENGTH_SHORT).show();

                            try {
                               final  JSONObject invoice = new JSONObject(s);


                                ((TextView)findViewById(R.id.invoice_id)).setText(getString(R.string.invoice_id)
                                        +  invoice.getString("invoice_id"));
                                ((TextView)findViewById(R.id.time_invoice)).setText(getString(R.string.invoice_time)
                                        +   invoice.getString("date")+" "+ invoice.getString("time"));


                                price.setText(invoice.getString("price")+" "+getString(R.string.j_d));


                                findViewById(R.id.go_continue).setEnabled(true);

                                findViewById(R.id.go_continue).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            confirm(invoice.getString("invoice_id"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

findViewById(R.id.prev).setVisibility(View.VISIBLE);
                                (findViewById(R.id.invoice_id)).setVisibility(View.VISIBLE);
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


    void getDetial(final int t){
        HashMap<String, String> params =  new HashMap<String, String>();
        params.put("oil_id",getIntent().getStringExtra("oil_id"));
        params.put("car_id",getIntent().getStringExtra("car_model_id"));


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
                                Toast.makeText(OrderPreviewActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }else{
                            try {
                                JSONArray array = new JSONArray(s);
                                 oilBrand = new OilBrand(array.getJSONObject(0));
                                name.setText(oilBrand.getName());
                                filter.setText(getIntent().getStringExtra("filter").equals("true")?
                                getResources().getString(R.string.yes):getResources().getString(R.string.no));
                                price.setText(oilBrand.getPrice());
                                detail.setText(oilBrand.getRecommend());

                                RequestOptions options = new RequestOptions()
                                        .fitCenter()
                                        .placeholder(R.drawable.logo)
                                        .error(R.drawable.logo);

                                if(!oilBrand.getPicture().equals(""))
                                    Glide.with(OrderPreviewActivity.this).load(oilBrand.getPicture())
                                            //.apply(options)
                                            .into(img);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            findViewById(R.id.loading).setVisibility(View.GONE);
//                              Toast.makeText(OrderPreviewActivity.this, s, Toast.LENGTH_SHORT).show();

//                            Intent intent = new Intent(OrderPreviewActivity.this, RateUserActivity.class);
//                            intent.putExtra("message" ,getString(R.string.please_rate_services));
//                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);

                        }
                    }
                });


    }
}
