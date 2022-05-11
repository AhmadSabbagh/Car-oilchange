package ebtkar.app.oilex2.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ebtkar.app.oilex2.MapsActivity;
import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.helper.SharedPrefManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            String order_id = extras.getString("order_id");
            if(order_id!=null){
                Toast.makeText(this, R.string.uuiuye3, Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent(getApplicationContext(), MapsActivity.class);
                resultIntent.putExtra("order_id" ,order_id);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(resultIntent);
                finish();
            }else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                        if (SharedPrefManager.getInstance(SplashActivity.this).IsUserLoggedIn()) {
                            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(mainIntent);
                            finish();
                        } else {

                            if(SharedPrefManager.getInstance(SplashActivity.this).checkIfLanguageExist()){
                                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mainIntent);
                            }else{
                                Intent mainIntent = new Intent(SplashActivity.this, SelectLangActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mainIntent);
                            }


                            finish();
                        }
                        try {
                            PackageInfo info = getPackageManager().getPackageInfo(
                                    "ebtkar.app.oilex2",
                                    PackageManager.GET_SIGNATURES);
                            for (Signature signature : info.signatures) {
                                MessageDigest md = MessageDigest.getInstance("SHA");
                                md.update(signature.toByteArray());
                                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                            }
                        } catch (PackageManager.NameNotFoundException e) {

                        } catch (NoSuchAlgorithmException e) {

                        }
                    }
                }, 1000);
            }


        }
else{

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                    if (SharedPrefManager.getInstance(SplashActivity.this).IsUserLoggedIn()) {
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(mainIntent);
                        finish();
                    } else {

                        if(SharedPrefManager.getInstance(SplashActivity.this).checkIfLanguageExist()){
                            Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                        }else{
                            Intent mainIntent = new Intent(SplashActivity.this, SelectLangActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                        }


                        finish();
                    }
                    try {
                        PackageInfo info = getPackageManager().getPackageInfo(
                                "ebtkar.app.oilex2",
                                PackageManager.GET_SIGNATURES);
                        for (Signature signature : info.signatures) {
                            MessageDigest md = MessageDigest.getInstance("SHA");
                            md.update(signature.toByteArray());
                            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                        }
                    } catch (PackageManager.NameNotFoundException e) {

                    } catch (NoSuchAlgorithmException e) {

                    }
                }
            }, 1000);



    }}
}
