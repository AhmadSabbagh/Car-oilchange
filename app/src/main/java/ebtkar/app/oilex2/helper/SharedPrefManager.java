package ebtkar.app.oilex2.helper;

/**
 * Created by Luminance on 1/18/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;


import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import ebtkar.app.oilex2.models.User;


public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;
    private static final String SHARED_PREF_NAME = "nyx.com.oilorderapp.v4";
    private static final String KEY_USER_ID = "A";
    private static final String KEY_USER_FNAME = "B";
    private static final String KEY_USER_LNAME = "c";
    private static final String KEY_USER_PHONE = "d";
    private static final String KEY_USER_PASSWORD = "E";
    private static final String KEY_USER_TYPE = "F";


    public static final String LANGUAGE_LOCALE= "JEYCARDOSLOCKKATCHAFANDA";


    public SharedPrefManager(Context context) {
        mCtx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }
    public String getSavedLanguage() {
        SharedPreferences sharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String o = sharedPreferences.getString(LANGUAGE_LOCALE, "");
        if(!o.trim().equals("")){
            return  o.trim().toLowerCase();
        }else{
            saveData("ar",LANGUAGE_LOCALE);
            return "ar";
        }
    }

    public boolean checkIfLanguageExist() {
        SharedPreferences sharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String o = sharedPreferences.getString(LANGUAGE_LOCALE, "");
        if(!o.trim().equals("")){
            return  true;
        }else{
            return false;
        }
    }

    public boolean userLogin(String fname,String lname ,String phone ,String password ,
                             String userType ,String id)
            throws Exception {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_USER_FNAME, fname);
        editor.putString(KEY_USER_LNAME, lname);
        editor.putString(KEY_USER_PHONE, phone);
        editor.putString(KEY_USER_PASSWORD, password);
        editor.putString(KEY_USER_TYPE, userType);
        editor.apply();
        return true;
    }
    public boolean saveData(String data , String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, data);
        editor.apply();



        return true;
    }
    public String getData( String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(key, "");
    }



    public boolean IsUserLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String o = sharedPreferences.getString(KEY_USER_ID, null);
        return o != null && !o.trim().equals("");

    }


    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  new User(
                sharedPreferences.getString(KEY_USER_FNAME, "") ,
                sharedPreferences.getString(KEY_USER_LNAME, "") ,
                sharedPreferences.getString(KEY_USER_PASSWORD, "") ,
                sharedPreferences.getString(KEY_USER_PHONE, "") ,
                sharedPreferences.getString(KEY_USER_TYPE, "") ,
                sharedPreferences.getString(KEY_USER_ID, "")
        );

    }
    public void Logout() {

        User user = getUser();
            // main_title.setText(R.string.oil_chg_location);
            if (! FirebaseInstanceId.getInstance().getToken().equals("")) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("customer");
                FirebaseMessaging.getInstance().unsubscribeFromTopic(user.getId()+"_customer");
                FirebaseMessaging.getInstance().unsubscribeFromTopic(user.getId()+"_agent");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("agent");
            }




        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, "");
        editor.putString(KEY_USER_FNAME, "");
        editor.putString(KEY_USER_LNAME, "");
        editor.putString(KEY_USER_PHONE, "");
        editor.putString(KEY_USER_PASSWORD, "");
        editor.putString(KEY_USER_TYPE , "");
        editor.apply();
        LoginManager.getInstance().logOut();
    }
}