package ebtkar.app.oilex2.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.FirebaseApp;


/**
 * Created by Luminance on 12/23/2018.
 */

public class Autostart extends BroadcastReceiver
{
    public void onReceive(Context context, Intent arg1)
    {
        Log.d("oilix" ,"BOOTED !");
       // Intent intent = new Intent(context,service.class);
       // context.startService(intent);
       // Log.i("Autostart", "started");
        FirebaseApp.initializeApp(context);
    }
}