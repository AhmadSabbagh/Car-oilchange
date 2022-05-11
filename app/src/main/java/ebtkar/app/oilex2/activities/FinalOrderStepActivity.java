package ebtkar.app.oilex2.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.helper.SharedPrefManager;

public class FinalOrderStepActivity extends Activity {

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
        setContentView(R.layout.activity_final_order_step);
    //    init();
        findViewById(R.id.go_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = ((EditText)findViewById(R.id.address)).getText().toString().trim();
                if(address.equals("")){
                    Toast.makeText(FinalOrderStepActivity.this, R.string.enter_address_please, Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean filter = ((CheckBox)findViewById(R.id.has_filter)).isChecked();
                Intent ii = new Intent(FinalOrderStepActivity.this ,OrderPreviewActivity.class);
                ii.putExtra("oil_id" ,getIntent().getStringExtra("oil_id"));
                ii.putExtra("lat" ,getIntent().getStringExtra("lat"));
                ii.putExtra("lng" ,getIntent().getStringExtra("lng"));
                ii.putExtra("address" ,address);
                ii.putExtra("filter" ,filter?"true":"false");
                ii.putExtra("price" ,getIntent().getStringExtra("price"));
                ii.putExtra("car" ,"Toyota");
                startActivity(ii);

                //init(0,address ,filter);
            }
        });
    }




}
