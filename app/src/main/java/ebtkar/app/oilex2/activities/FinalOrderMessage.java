package ebtkar.app.oilex2.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ebtkar.app.oilex2.R;

public class FinalOrderMessage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_order_message);

        ((TextView)findViewById(R.id.invoice_id)).setText(getString(R.string.invoice_id)
                + getIntent().getStringExtra("invoice_id"));
        ((TextView)findViewById(R.id.time_invoice)).setText(getString(R.string.invoice_time)
                + getIntent().getStringExtra("invoice_time"));
        findViewById(R.id.go_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              startActivity(new Intent(FinalOrderMessage.this,GetCustomerOrdersActivity.class));


            }
        });
    }
}
