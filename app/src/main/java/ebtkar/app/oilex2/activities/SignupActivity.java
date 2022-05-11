package ebtkar.app.oilex2.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.BackgroundServices;
import ebtkar.app.oilex2.helper.ConnectionUtils;
import ebtkar.app.oilex2.helper.PostAction;
import ebtkar.app.oilex2.helper.SharedPrefManager;
import ebtkar.app.oilex2.models.User;

public class SignupActivity extends Activity {
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static final String EMAIL = "email";
    CallbackManager callbackManager = CallbackManager.Factory.create();
    LoginButton loginButton;


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
        setContentView(R.layout.activity_signup);
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception w) {
        }
        if (getIntent().getStringExtra("f") != null) {
            ((RadioButton) findViewById(R.id.is_provider)).setChecked(true);
        }

        findViewById(R.id.go_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });
        findViewById(R.id.go_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConnectionUtils.isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(SignupActivity.this, R.string.connect_please, Toast.LENGTH_SHORT).show();
                    return;
                }
                final String fname = ((EditText) findViewById(R.id.fname)).getText().toString().trim();
                final String lname = ((EditText) findViewById(R.id.lname)).getText().toString().trim();
                final String phone = ((EditText) findViewById(R.id.phone)).getText().toString().trim();
                final String password = ((EditText) findViewById(R.id.password)).getText().toString().trim();
                final boolean isCustomer = ((RadioButton) findViewById(R.id.is_customer)).isChecked();
                signup(fname, lname, phone, password, isCustomer, 0);


            }
        });

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    private ProfileTracker mProfileTracker;

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(final JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code
                                        try {

                                            if (Profile.getCurrentProfile() == null) {
                                                mProfileTracker = new ProfileTracker() {
                                                    @Override
                                                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                                                        try {
                                                            signup(Profile.getCurrentProfile()
                                                                            .getFirstName(), Profile.getCurrentProfile().getLastName(),
                                                                    object.getString("email").trim(),
                                                                    Profile.getCurrentProfile().getId(),
                                                                    ((RadioButton) findViewById(R.id.is_customer_fb)).isChecked(),
                                                                    0
                                                            );
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        mProfileTracker.stopTracking();
                                                    }
                                                };
                                                // no need to call startTracking() on mProfileTracker
                                                // because it is called by its constructor, internally.
                                            } else {
                                                signup(Profile.getCurrentProfile()
                                                                .getFirstName(), Profile.getCurrentProfile()
                                                                .getLastName(),
                                                        object.getString("email").trim(),
                                                        Profile.getCurrentProfile().getId(),
                                                        ((RadioButton) findViewById(R.id.is_customer)).isChecked(),
                                                        0
                                                );
                                            }

                                        } catch (Exception J) {
                                            Toast.makeText(SignupActivity.this, J.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        // Callback registration


    }

    public void signup(final String fname,
                       final String lname,
                       final String phone,
                       final String password,
                       final Boolean isCustomer,
                       final int steps

    ) {

        String serviceUrl = isCustomer ? APIUrl.SERVER + "add_customer" : APIUrl.SERVER + "add_agent";
        if (fname.equals("")) {
            Toast.makeText(this, R.string.please_enter_fname, Toast.LENGTH_SHORT).show();
            return;
        }
        if (lname.equals("")) {
            Toast.makeText(this, R.string.please_enter_lname, Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.equals("")) {
            Toast.makeText(this, R.string.please_enter_phone_number, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equals("")) {
            Toast.makeText(this, R.string.enter_password_please, Toast.LENGTH_SHORT).show();
            return;
        }
        findViewById(R.id.loading).setVisibility(View.VISIBLE);

//        serviceUrl+="?";
//        serviceUrl+="first_name="+fname;
//        serviceUrl+="&last_name="+lname;
//        serviceUrl+="&phone="+fname;
//        serviceUrl+="&password="+password;

        HashMap args = new HashMap<String, String>();
        args.put("first_name", fname);
        args.put("last_name", lname);
        args.put("phone", phone);
        args.put("password", password);


        findViewById(R.id.go_signup).setEnabled(false);

        BackgroundServices.getInstance(this).CallPost(serviceUrl, args, new PostAction() {
            @Override
            public void doTask(String s) {
                findViewById(R.id.go_signup).setEnabled(true);
                findViewById(R.id.loading).setVisibility(View.INVISIBLE);


                if (!s.equals("")) {
                    try {
                        JSONObject response = new JSONObject(s);
                        //  Toast.makeText(SignupActivity.this, s, Toast.LENGTH_SHORT).show();
                        final String key = isCustomer ? "customer_id" : "agent_id";

                        if (response.getInt(key) > 0) {
                            Toast.makeText(SignupActivity.this, R.string.signup_succesfull
                                    , Toast.LENGTH_SHORT).show();


                            if (isCustomer) {
                                SharedPrefManager.getInstance(SignupActivity.this).userLogin(
                                        fname, lname, phone, password, User.CUSTOMER,
                                        response.getString(key)
                                );
                                Intent ii = new Intent(SignupActivity.this, MainActivity.class);
                                ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(ii);
                            } else {

                                Intent ii = new Intent(SignupActivity.this, ModalMessageActivity.class);
                                ii.putExtra("message", fname + "  , " + getString(R.string.wait_till_approval));
                                ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(ii);
                                finish();
                            }
                        }else {
                            Toast.makeText(SignupActivity.this, R.string.create_account_fail
                                    , Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (steps < 50) signup(fname, lname, phone, password, isCustomer, steps + 1);
                    else {
                        LoginManager.getInstance().logOut();

                        Toast.makeText(SignupActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }


}
