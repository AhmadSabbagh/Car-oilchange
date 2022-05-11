package ebtkar.app.oilex2.helper;

/**
 * Created by Luminance on 5/19/2018.
 */

import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class BackgroundServices {

    private static BackgroundServices mInstance;
    private static Context mCtx;
    private BackgroundServices(Context context) {
        mCtx = context;
    }
    public static synchronized BackgroundServices getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new BackgroundServices(context);
        }
        return mInstance;
    }
    public void Call(final String url ,final PostAction pa ) {
        class CostumTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pa.doTask(s);
            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String result =
                            ConnectionUtils.sendPostRequest(url,
                                    new HashMap<String, String>() , "get" ,
                                   "ar");

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
    public void CallPost(final String url , final HashMap<String, String> args ,final PostAction pa ) {

        class CostumTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pa.doTask(s);
            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String result =
                            ConnectionUtils.sendPostRequest(url,
                                    args, "post" ,"ar");

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
