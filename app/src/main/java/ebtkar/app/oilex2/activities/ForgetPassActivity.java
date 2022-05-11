package ebtkar.app.oilex2.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.BackgroundServices;
import ebtkar.app.oilex2.helper.ConnectionUtils;
import ebtkar.app.oilex2.helper.PostAction;

public class ForgetPassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        findViewById(R.id.forget_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!ConnectionUtils.isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), R.string.connect_please, Toast.LENGTH_SHORT).show();
                    return;
                }
                final String phone = ((EditText) findViewById(R.id.phone)).getText().toString().trim();
                if (phone.equals("")) {
                    Toast.makeText(ForgetPassActivity.this, R.string.please_enter_phone_number, Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean isCustomer = ((RadioButton) findViewById(R.id.is_customer)).isChecked();
                v.setEnabled(false);

                HashMap args = new HashMap<String, String>();
                args.put("phone", phone);
                args.put("flag", (isCustomer) ? "false" : "true");

                BackgroundServices.getInstance(ForgetPassActivity.this).CallPost(APIUrl.SERVER + "forget_password", args
                        , new PostAction() {
                            @Override
                            public void doTask(String s) {
                                try {
                                    v.setEnabled(true);
                                    JSONObject obj = new JSONObject(s);
                                    if (obj.has("message")) {
                                        if (obj.getString("message").equalsIgnoreCase("null")) {

                                            Toast.makeText(ForgetPassActivity.this,
                                                    R.string.check_password_data, Toast.LENGTH_SHORT).show();
                                        } else {

                                            Toast.makeText(ForgetPassActivity.this, R.string.password_sent,
                                                    Toast.LENGTH_SHORT).show();
                                            ForgetPassActivity.this.finish();
                                        }
                                    }else {
                                        Toast.makeText(ForgetPassActivity.this,
                                                R.string.check_password_data, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception c) {
                                    Toast.makeText(ForgetPassActivity.this,
                                            R.string.check_password_data, Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


            }
        });
    }
}
