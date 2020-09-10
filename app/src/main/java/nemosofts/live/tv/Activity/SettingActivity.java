package nemosofts.live.tv.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.onesignal.OneSignal;
import nemosofts.live.tv.BuildConfig;
import nemosofts.live.tv.R;
import nemosofts.live.tv.SharedPref.Settings;
import nemosofts.live.tv.SharedPref.SharedPre;
import com.nemosofts.library.SwitchButton.SwitchButton;
import nemosofts.live.tv.constant.Constant;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class SettingActivity extends AppCompatActivity {
    SharedPre sharedPre;
    SwitchButton switch_dark, switch_noti;
    LinearLayout about, share,privacy, album_grid, number;
    TextView version;

    Boolean isNoti = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPre = new SharedPre(this);
        if (sharedPre.getNightMode()) {
            setTheme(R.style.AppTheme2);
            Settings.Dark_Mode = true;
        } else {
            setTheme(R.style.AppTheme);
            Settings.Dark_Mode = false;
        }


        Settings.Album = sharedPre.getAlbum_grid();
        Settings.grid = sharedPre.get_grid();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        isNoti = sharedPre.getIsNotification();

        Toolbar toolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Setting");

        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Dark_mode();

        version = (TextView) findViewById(R.id.version);
        version.setText(BuildConfig.VERSION_NAME);

        about= findViewById(R.id.nav_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent player = new Intent(SettingActivity.this,AboutActivity.class);
                startActivity(player);
            }
        });

        share  = findViewById(R.id.nav_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appName = getPackageName();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
            }
        });

        privacy  = findViewById(R.id.nav_privacy);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOpnsDialog();
            }
        });

        album_grid();
        grid();

        switch_noti = findViewById(R.id.switch_noti);

        if (isNoti) {
            switch_noti.setChecked(true);
        } else {
            switch_noti.setChecked(false);
        }

        switch_noti.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                OneSignal.setSubscription(isChecked);
                sharedPre.setIsNotification(isChecked);
            }
        });

    }


    private void album_grid() {
        album_grid = findViewById(R.id.album_grid);
        album_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_album_grid();
            }
        });
    }

    private void Dialog_album_grid() {
        final String[] listItems = {"Normal", "Card", "Style", "Image"};

        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Settings.Dark_Mode){
                builder = new AlertDialog.Builder(SettingActivity.this, R.style.ThemeDialog2);
            }else {
                builder = new AlertDialog.Builder(SettingActivity.this, R.style.ThemeDialog);
            }
        } else {
            builder = new AlertDialog.Builder(SettingActivity.this);
        }




        builder.setTitle("Layout Style");

        int checkedItem = sharedPre.getAlbum_grid();
        builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPre.setAlbum_grid(which);
            }
        });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Apps_recreate();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void grid() {
        number = findViewById(R.id.number);
        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_grid();
            }
        });
    }

    private void Dialog_grid() {
        final String[] listItems = {"2 Grid", "3 Grid", "4 Grid Use tablet"};

        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Settings.Dark_Mode){
                builder = new AlertDialog.Builder(SettingActivity.this, R.style.ThemeDialog2);
            }else {
                builder = new AlertDialog.Builder(SettingActivity.this, R.style.ThemeDialog);
            }
        } else {
            builder = new AlertDialog.Builder(SettingActivity.this);
        }

        builder.setTitle("Grid Layout");

        int checkedItem = sharedPre.get_grid();
        builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPre.set_grid(which);
            }
        });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Apps_recreate();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void openOpnsDialog() {
        Dialog dialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new Dialog(SettingActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            dialog = new Dialog(SettingActivity.this);
        }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_privacy);

        WebView webview = dialog.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.loadUrl(Constant.privacy_policy_url);

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void Dark_mode() {
        switch_dark = findViewById(R.id.switch_dark);
        if (sharedPre.getNightMode()) {
            switch_dark.setChecked(true);
        } else {
            switch_dark.setChecked(false);
        }
        switch_dark.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                sharedPre.setNightMode(isChecked);
                Apps_recreate();
            }
        });
    }



    private void Apps_recreate() {
        recreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                overridePendingTransition(0, 0);
                overridePendingTransition(0, 0);
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
                finish();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(0, 0);
        overridePendingTransition(0, 0);
        startActivity(new Intent(SettingActivity.this, MainActivity.class));
        finish();
    }
}