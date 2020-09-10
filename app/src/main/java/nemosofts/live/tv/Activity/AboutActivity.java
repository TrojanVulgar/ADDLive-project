package nemosofts.live.tv.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import nemosofts.live.tv.R;
import nemosofts.live.tv.SharedPref.Settings;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class AboutActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView  company, email, website, contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Settings.Dark_Mode) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        company = (TextView) findViewById(R.id.company);
        email = (TextView) findViewById(R.id.email);
        website = (TextView) findViewById(R.id.website);
        contact = (TextView) findViewById(R.id.contact);

        company.setText(Settings.company);
        email.setText(Settings.email);
        website.setText(Settings.website);
        contact.setText(Settings.contact);


    }


}