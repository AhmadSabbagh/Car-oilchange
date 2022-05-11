package ebtkar.app.oilex2.services;

/**
 * Created by Luminance on 5/4/2018.
 */


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Objects;

import ebtkar.app.oilex2.MapsActivity;
import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.activities.SplashActivity;
import ebtkar.app.oilex2.utils.NotificationUtils;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "OOIILLIIEXX";
    private static final String CHANNELID = "OILEX_ID_FB_6376";

    Date date ;
    String CHANNEL_ID = "my_channel_01";// The id of the channel.
    private NotificationChannel mChannel;

    private NotificationManager notifManager;



    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
         date=new Date();
        Log.e(TAG, "From: " + remoteMessage.getFrom());


//                JSONObject json = new JSONObject(remoteMessage.getData().toString());
//                handleDataMessage(json);
        if (remoteMessage.getData().get("order_id") != null)
            sendNotification(remoteMessage.getData().get("title").toString(),
                    remoteMessage.getData().get("body").toString(),
                    remoteMessage.getData().get("order_id").toString());
        else
            sendNotification(remoteMessage.getData().get("title").toString(),
                    remoteMessage.getData().get("body").toString());





    }





    private void sendNotification(String title, String content) {

        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService
                    (Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel == null) {
                NotificationChannel mChannel = new NotificationChannel
                        ("0", title, importance);
                mChannel.setDescription (content);
                mChannel.enableVibration (true);
                mChannel.setVibrationPattern (new long[]
                        {100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel (mChannel);
            }
            builder = new NotificationCompat.Builder (this, "0");

            intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity (this, 0, intent, 0);
            builder.setContentTitle (title)  // flare_icon_30
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText (content)  // required
                    .setDefaults (Notification.DEFAULT_ALL)
                    .setAutoCancel (true)
                    .setContentIntent (pendingIntent)
                    .setSound (RingtoneManager.getDefaultUri
                            (RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate (new long[]{100, 200, 300, 400,
                            500, 400, 300, 200, 400});
        }

        else
        {
            builder = new NotificationCompat.Builder (this);

            intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity (this, 0, intent, 0);
            builder.setContentTitle (title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText (content)  // required
                    .setDefaults (Notification.DEFAULT_ALL)
                    .setAutoCancel (true)

                    .setContentIntent (pendingIntent)
                    .setSound (RingtoneManager.getDefaultUri
                            (RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate (new long[]{100, 200, 300, 400, 500,
                            400, 300, 200, 400})
                    .setPriority (Notification.PRIORITY_HIGH);

        }

        Notification notification = builder.build ();
        notifManager.notify((int)date.getTime(), notification);

    }



    private void sendNotification(String title, String content, String order_id) {
        PendingIntent pendingIntent;
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.putExtra("order_id", order_id);
        intent.putExtra("message", content);

        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService
                    (Context.NOTIFICATION_SERVICE);
        }
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    int importance = NotificationManager.IMPORTANCE_HIGH;
    if (mChannel == null) {
        NotificationChannel mChannel = new NotificationChannel
                ("0", title, importance);
        mChannel.setDescription (content);
        mChannel.enableVibration (true);
        mChannel.setVibrationPattern (new long[]
                {100, 200, 300, 400, 500, 400, 300, 200, 400});
        notifManager.createNotificationChannel (mChannel);
    }
    builder = new NotificationCompat.Builder (this, "0");

    intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP |
            Intent.FLAG_ACTIVITY_SINGLE_TOP);
    pendingIntent = PendingIntent.getActivity (this, 0, intent, 0);
    builder.setContentTitle (title)  // flare_icon_30
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText (content)  // required
            .setDefaults (Notification.DEFAULT_ALL)
            .setAutoCancel (true)
            .setContentIntent (pendingIntent)
            .setSound (RingtoneManager.getDefaultUri
                    (RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate (new long[]{100, 200, 300, 400,
                    500, 400, 300, 200, 400});


        }

        else
        {
            builder = new NotificationCompat.Builder (this);

            intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity (this, 0, intent, 0);
            builder.setContentTitle (title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText (content)  // required
                    .setDefaults (Notification.DEFAULT_ALL)
                    .setAutoCancel (false)

                    .setContentIntent (pendingIntent)
                    .setSound (RingtoneManager.getDefaultUri
                            (RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate (new long[]{100, 200, 300, 400, 500,
                            400, 300, 200, 400})
                    .setPriority (Notification.PRIORITY_HIGH);
        }

        Notification notification = builder.build ();
        notifManager.notify((int)date.getTime(), notification);


    }







}