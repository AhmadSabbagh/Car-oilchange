package ebtkar.app.oilex2.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
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

public class LoginActivity extends Activity {
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
        setContentView(R.layout.activity_login);
        findViewById(R.id.how_to_be).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(LoginActivity.this, SignupActivity.class);
                ii.putExtra("f", "s");
                startActivity(ii);
                finish();
            }
        });
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception w) {
        }

        Button password_txt =   findViewById(R.id.forget_password);
        password_txt.setPaintFlags(password_txt.getPaintFlags()  | Paint.UNDERLINE_TEXT_FLAG);
        Button signup_txt =   findViewById(R.id.how_to_be);
        signup_txt.setPaintFlags(signup_txt.getPaintFlags()  | Paint.UNDERLINE_TEXT_FLAG);


        findViewById(R.id.forget_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this,ForgetPassActivity.class));


            }
        });

        loginButton = (LoginButton) findViewById(R.id.login_button);
        findViewById(R.id.login_as_customer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConnectionUtils.isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(LoginActivity.this, R.string.connect_please, Toast.LENGTH_SHORT).show();
                    return;
                }
                final String phone = ((EditText) findViewById(R.id.phone)).getText().toString().trim();
                final String password = ((EditText) findViewById(R.id.password)).getText().toString().trim();
                final boolean isCustomer = true;
                login(phone, password, isCustomer, 0, v);

            }
        });
        findViewById(R.id.login_as_provider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConnectionUtils.isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(LoginActivity.this, R.string.connect_please, Toast.LENGTH_SHORT).show();
                    return;
                }
                final String phone = ((EditText) findViewById(R.id.phone)).getText().toString().trim();
                final String password = ((EditText) findViewById(R.id.password)).getText().toString().trim();
                final boolean isCustomer = false;
                login(phone, password, isCustomer, 0, v);
            }
        });

        findViewById(R.id.go_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code
                                        try {


                                            login_fb(Profile.getCurrentProfile()
                                                            .getFirstName(), Profile.getCurrentProfile()
                                                            .getLastName(), object.getString("email").trim(),
                                                    Profile.getCurrentProfile().getId(), ((RadioButton) findViewById(R.id.is_customer_fb)).isChecked()
                                                    , 0, loginButton
                                            );

                                        } catch (Exception J) {
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


    public void login(
            final String phone,
            final String password,
            final Boolean isCustomer,
            final int steps, final View btn

    ) {

        String serviceUrl = isCustomer ? APIUrl.SERVER + "Login_customer" : APIUrl.SERVER + "Login_agent";
        if (phone.equals("")) {
            Toast.makeText(this, R.string.please_enter_phone_number, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equals("")) {
            Toast.makeText(this, R.string.enter_password_please, Toast.LENGTH_SHORT).show();
            return;
        }
        findViewById(R.id.login_as_provider).setEnabled(false);
        findViewById(R.id.login_as_customer).setEnabled(false);
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        //   serviceUrl+="?";
        //    serviceUrl+="phone="+phone;
        //   serviceUrl+="&password="+password;

        HashMap args = new HashMap<String, String>();
        args.put("phone", phone);
        args.put("password", password);

        btn.setEnabled(false);

        BackgroundServices.getInstance(this).CallPost(serviceUrl
                , args
                , new PostAction() {
                    @Override
                    public void doTask(String s) {
                        findViewById(R.id.login_as_provider).setEnabled(true);
                        findViewById(R.id.login_as_customer).setEnabled(true);
                        findViewById(R.id.loading).setVisibility(View.INVISIBLE);


                        if (!s.equals("")) {
                            try {
                                JSONObject response = new JSONObject(s);
                                final String key = isCustomer ? "customer_id" : "agent_id";

                                if (response.getInt(key) > 0) {
                                    Toast.makeText(LoginActivity.this, R.string.login_succesfull
                                            , Toast.LENGTH_SHORT).show();
                                    if (isCustomer) {
                                        SharedPrefManager.getInstance(LoginActivity.this).userLogin(
                                                "", "", phone, password, User.CUSTOMER,
                                                response.getString(key)
                                        );
                                        Intent ii = new Intent(LoginActivity.this, MainActivity.class);
                                        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(ii);
                                        finish();
                                    } else {
                                        if (response.getString("message").equals("login succ")) {
                                            SharedPrefManager.getInstance(LoginActivity.this).userLogin(
                                                    "", "", phone, password, User.SERVICE_PROVIDER,
                                                    response.getString(key)
                                            );
                                            Intent ii = new Intent(LoginActivity.this, MainActivity.class);
                                            ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(ii);
                                            finish();
                                        } else {
                                            Intent ii = new Intent(LoginActivity.this, ModalMessageActivity.class);
                                            ii.putExtra("message", getString(R.string.wait_till_approval));
                                            startActivity(ii);
                                            finish();
                                        }
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                                    LoginManager.getInstance().logOut();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (steps < 50) login(phone, password, isCustomer, steps + 1, btn);
                            else
                                Toast.makeText(LoginActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    public void login_fb(
            final String fname,
            final String lname,
            final String phone,
            final String password,
            final Boolean isCustomer,
            final int steps, final View btn

    ) {

        String serviceUrl = isCustomer ? APIUrl.SERVER + "Login_customer" : APIUrl.SERVER + "Login_agent";
        findViewById(R.id.login_as_provider).setEnabled(false);
        btn.setEnabled(false);
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        //   serviceUrl+="?";
        //    serviceUrl+="phone="+phone;
        //   serviceUrl+="&password="+password;

        HashMap args = new HashMap<String, String>();
        args.put("phone", phone);
        args.put("password", password);

        btn.setEnabled(false);
        findViewById(R.id.login_as_provider).setEnabled(false);
        findViewById(R.id.login_as_customer).setEnabled(false);
        findViewById(R.id.loading).setVisibility(View.VISIBLE);

        BackgroundServices.getInstance(this).CallPost(serviceUrl
                , args
                , new PostAction() {
                    @Override
                    public void doTask(String s) {
                        findViewById(R.id.login_as_provider).setEnabled(true);
                        findViewById(R.id.login_as_customer).setEnabled(true);
                        findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                        btn.setEnabled(true);

                        if (!s.equals("")) {
                            try {
                                JSONObject response = new JSONObject(s);
                                final String key = isCustomer ? "customer_id" : "agent_id";

                                if (response.getInt(key) > 0) {
                                    Toast.makeText(LoginActivity.this, R.string.login_succesfull
                                            , Toast.LENGTH_SHORT).show();
                                    if (isCustomer) {
                                        SharedPrefManager.getInstance(LoginActivity.this).userLogin(
                                                "", "", phone, password, User.CUSTOMER,
                                                response.getString(key)
                                        );
                                        Intent ii = new Intent(LoginActivity.this, MainActivity.class);
                                        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(ii);
                                        finish();
                                    } else {
                                        if (response.getString("message").equals("login succ")) {
                                            SharedPrefManager.getInstance(LoginActivity.this).userLogin(
                                                    "", "", phone, password, User.SERVICE_PROVIDER,
                                                    response.getString(key)
                                            );
                                            Intent ii = new Intent(LoginActivity.this, MainActivity.class);
                                            ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(ii);
                                            finish();
                                        } else {
                                            Intent ii = new Intent(LoginActivity.this
                                                    , ModalMessageActivity.class);
                                          ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            ii.putExtra("message", getString(R.string.wait_till_approval));
                                            startActivity(ii);
                                            finish();
                                        }
                                    }
                                } else {
                                    btn.setEnabled(false);
                                    findViewById(R.id.login_as_provider).setEnabled(false);
                                    findViewById(R.id.login_as_customer).setEnabled(false);
                                    findViewById(R.id.loading).setVisibility(View.VISIBLE);
                                    signup(fname, lname, phone, password, isCustomer, 0, btn);
                                    LoginManager.getInstance().logOut();


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                LoginManager.getInstance().logOut();

                            } catch (Exception e) {
                                e.printStackTrace();
                                LoginManager.getInstance().logOut();

                            }
                        } else {
LoginManager.getInstance().logOut();
                            Toast.makeText(LoginActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    public void signup(final String fname,
                       final String lname,
                       final String phone,
                       final String password,
                       final Boolean isCustomer,
                       final int steps, final View btn

    ) {

        String serviceUrl = isCustomer ? APIUrl.SERVER + "add_customer" : APIUrl.SERVER + "add_agent";


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
                findViewById(R.id.login_as_provider).setEnabled(true);
                findViewById(R.id.login_as_customer).setEnabled(true);
                findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                btn.setEnabled(true);


                if (!s.equals("")) {
                    try {
                        JSONObject response = new JSONObject(s);
                        //  Toast.makeText(SignupActivity.this, s, Toast.LENGTH_SHORT).show();
                        final String key = isCustomer ? "customer_id" : "agent_id";

                        if (response.getInt(key) > 0) {
                            Toast.makeText(LoginActivity.this, R.string.signup_succesfull
                                    , Toast.LENGTH_SHORT).show();


                            if (isCustomer) {
                                SharedPrefManager.getInstance(LoginActivity.this).userLogin(
                                        fname, lname, phone, password, User.CUSTOMER,
                                        response.getString(key)
                                );
                                Intent ii = new Intent(LoginActivity.this, MainActivity.class);
                                ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(ii);
                            } else {

                                Intent ii = new Intent(LoginActivity.this, ModalMessageActivity.class);
                                ii.putExtra("message", fname + "  , " + getString(R.string.wait_till_approval));
                                ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(ii);
                                finish();
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (steps < 50)
                        signup(fname, lname, phone, password, isCustomer, steps + 1, btn);
                    else {
                        LoginManager.getInstance().logOut();

                        Toast.makeText(LoginActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}
