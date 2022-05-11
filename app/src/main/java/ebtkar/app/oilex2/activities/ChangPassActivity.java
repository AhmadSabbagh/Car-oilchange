package ebtkar.app.oilex2.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.BackgroundServices;
import ebtkar.app.oilex2.helper.ConnectionUtils;
import ebtkar.app.oilex2.helper.PostAction;
import ebtkar.app.oilex2.helper.SharedPrefManager;
import ebtkar.app.oilex2.models.User;

public class ChangPassActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chang_pass);
            findViewById(R.id.forget_password).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (!ConnectionUtils.isNetworkAvailable(getApplicationContext())) {
                        Toast.makeText(getApplicationContext(), R.string.connect_please, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final String old_pass = ((EditText) findViewById(R.id.old_password)).getText().toString().trim();
                    final String new_password = ((EditText) findViewById(R.id.new_password)).getText().toString().trim();
                    if (old_pass.equals("")||new_password.equals("")) {
                        Toast.makeText(ChangPassActivity.this, R.string.enter_password_please, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    v.setEnabled(false);

                    HashMap args = new HashMap<String, String>();
                    User user = SharedPrefManager.getInstance(ChangPassActivity.this)
                            .getUser();
                    String uer_type = user.getUser_type().equals(User.SERVICE_PROVIDER)
                            ?"agent_id":"customer_id";
                    String user_id = user.getId();
                    String service = user.getUser_type().equals(User.SERVICE_PROVIDER)
                            ?"change_agent_password":"change_customer_password";

                    args.put(uer_type, user_id);
                    args.put("old_password", old_pass);
                    args.put("new_password", new_password);
                    findViewById(R.id.loading).setVisibility(View.VISIBLE);
                    BackgroundServices.getInstance(ChangPassActivity.this).CallPost(APIUrl.SERVER + service, args
                            , new PostAction() {
                                @Override
                                public void doTask(String s) {
                                    try {
                                        findViewById(R.id.loading).setVisibility(View.GONE);
                                        v.setEnabled(true);
                                        JSONObject obj = new JSONObject(s);
                                        if (obj.has("flag")) {
                                            if (obj.getString("flag").equalsIgnoreCase("1")) {

                                                Toast.makeText(ChangPassActivity.this,
                                                        R.string.check_password_data, Toast.LENGTH_SHORT).show();
                                            } else {

                                                Toast.makeText(ChangPassActivity.this, R.string.password_changed_success,
                                                        Toast.LENGTH_SHORT).show();
                                                ChangPassActivity.this.finish();
                                            }
                                        }else {
                                            Toast.makeText(ChangPassActivity.this,
                                                    R.string.check_password_data, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception c) {
                                        Toast.makeText(ChangPassActivity.this,
                                                R.string.check_password_data, Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });


                }
            });
        }
    }
