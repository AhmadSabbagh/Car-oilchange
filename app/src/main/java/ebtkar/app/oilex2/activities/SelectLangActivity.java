package ebtkar.app.oilex2.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.helper.SharedPrefManager;

public class SelectLangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lang);

        findViewById(R.id.go_arabic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(SelectLangActivity.this).saveData("ar"
                        ,SharedPrefManager.LANGUAGE_LOCALE);

                if (SharedPrefManager.getInstance(SelectLangActivity.this).IsUserLoggedIn()) {
                    Intent mainIntent = new Intent(SelectLangActivity.this, SplashActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                    finish();
                }else{
                startActivity(new Intent(SelectLangActivity.this,LoginActivity.class));
                finish();
                }
            }
        });
        findViewById(R.id.go_english).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(SelectLangActivity.this).saveData("en" ,SharedPrefManager.LANGUAGE_LOCALE);
                if (SharedPrefManager.getInstance(SelectLangActivity.this).IsUserLoggedIn()) {
                    Intent mainIntent = new Intent(SelectLangActivity.this, SplashActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                    finish();
                }else{
                startActivity(new Intent(SelectLangActivity.this,LoginActivity.class));
                finish();}
            }
        });


    }
}
