package ebtkar.app.oilex2.activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.helper.SharedPrefManager;

public class ModalMessageActivity extends Activity {

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
        setContentView(R.layout.activity_modal_message);
        ((TextView)findViewById(R.id.message)).setText(getIntent().getStringExtra("message"));
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
