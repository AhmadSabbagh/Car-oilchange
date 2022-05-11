package ebtkar.app.oilex2.helper;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Created by Luminance on 2/11/2018.
 */
public class ConnectionUtils {

    public static  boolean isNetworkAvailable(Context a) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




    public static String sendPostRequest(String requestURL,
                                  HashMap<String, String> params , String type ,String lang) {

        Log.d("okhttp_url"  , requestURL);

        String response = "";
        Request request = null;
        OkHttpClient client = new OkHttpClient();

        if(type.equals("post")){
            FormBody.Builder mb = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            mb.add(entry.getKey(), entry.getValue());
        }
//        mb.addFormDataPart("time", System.currentTimeMillis()+"");
         request = new Request.Builder()
                .url(requestURL)
                 .removeHeader("Content-Type")
                 .addHeader("Content-Type", "application/x-www-form-urlencoded")
                  .post(mb.build())
                  .build();}
        else  if(type.equals("get"))
            request = new Request.Builder()
                    .url(requestURL)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .get()
                    .build();
        else
        {
            MultipartBody.Builder mb =  new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                mb.addFormDataPart(entry.getKey(), entry.getValue());
            }
            mb.addFormDataPart("time", System.currentTimeMillis()+"");
            request = new Request.Builder()
                    .url(requestURL)
                    .put(mb.build())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("X-localization", lang)
                  //  .addHeader("Authorization","Bearer "+ bearer)
                    .build();}
        Response responses = null;

        try {
          //  Log.d("Fuck_ahmad", "sendPostRequest: "+request.headers().toString());

            responses = client.newCall(request).execute();
        } catch (IOException e) {

          Log.d("RET ERR "  , e.getMessage());
        }
        try {
            response = responses.body().string();
            Log.d("OKHTTP3 : "  ,response);

        }catch (IOException e){    Log.d("RET2 ERR "  , e.getMessage());}
   return response;
    }


}
