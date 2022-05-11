package ebtkar.app.oilex2.helper.v2;

/**
 * Created by Luminance on 5/19/2018.
 */
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ebtkar.app.oilex2.helper.SharedPrefManager;


public class BackgroundServices {

    private static BackgroundServices mInstance;
    private static Activity mCtx;
    private BackgroundServices(Activity context) {
        mCtx = context;
    }
    public static synchronized BackgroundServices getInstance(Activity context) {
        if (mInstance == null) {
            mInstance = new BackgroundServices(context);
        }
        return mInstance;
    }

    public void CallGet(final String url ,final PostAction pa ) {
        CallInternal( url ,  null , pa,0 ,"get");
    }
    public void CallPost(final String url , final HashMap<String, String> args ,final PostAction pa ) {
        CallInternal( url ,  args , pa,0 ,"post");
    }
    private void CallInternal(final String url , final HashMap<String, String> args , final PostAction pa, final int try_
     ,final String method) {


        class CostumTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("")) {
                        Snackbar.make(mCtx.getWindow()
                                .getDecorView().getRootView(),
                                "Netwrik error", Snackbar.LENGTH_LONG)
                                .setAction("Again", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CallInternal( url ,  args , pa,try_+1,method);
                                    }
                                })
                                .show();

                }else
                    pa.whenFinished(s);
            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String result =
                            ConnectionUtils.sendPostRequest(url,
                                    args, method , SharedPrefManager.getInstance(mCtx).getSavedLanguage());
                    return result;

                } catch (Exception e) {
                    Log.d("ERROR IN CALLBACK "+url+" : "  , e.getMessage());
                }

                return "";
            }
        }
        CostumTask ru = new CostumTask();
        ru.execute();
    }

}
