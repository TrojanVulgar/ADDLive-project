package nemosofts.live.tv.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nemosofts.live.tv.BuildConfig;
import nemosofts.live.tv.DBHelper.DBHelper;
import nemosofts.live.tv.JSONParser.JSONParser;
import nemosofts.live.tv.asyncTask.LoadAbout;
import nemosofts.live.tv.Login.ItemUser;
import nemosofts.live.tv.Login.LoadLogin;
import nemosofts.live.tv.Login.LoginActivity;
import nemosofts.live.tv.Login.LoginListener;
import nemosofts.live.tv.Methods.Methods;
import nemosofts.live.tv.R;
import nemosofts.live.tv.Receiver.LoadNemosofts;
import nemosofts.live.tv.Receiver.NemosoftsListener;
import nemosofts.live.tv.SharedPref.Settings;
import nemosofts.live.tv.SharedPref.SharedPre;
import nemosofts.live.tv.interfaces.AboutListener;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class SplashActivity extends AppCompatActivity {
    Methods methods;
    DBHelper dbHelper;
    private static int SPLASH_TIME_OUT = 1200;

    SharedPre sharedPre;

    ImageView Logo;
    Animation animation;
    TextView Logo_text;

    private Intent intent;

    IInAppBillingService mService;
    private static final String LOG_TAG = "iabv3";
    // put your Google merchant id here (as stated in public profile of your Payments Merchant Center)
    // if filled library will provide protection against Freedom alike Play Market simulators
    private static final String MERCHANT_ID=null;
    private BillingProcessor bp;
    private boolean readyToPurchase = false;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPre = new SharedPre(this);
        if (sharedPre.getIsFirstPurchaseCode()) {
        } else {
            sharedPre.getPurchaseCode();
            sharedPre.getPurchase();
            initBuy();
        }
        if (sharedPre.getNightMode()) {
            Settings.Dark_Mode = true;
            setTheme(R.style.AppTheme2);
        } else {
            Settings.Dark_Mode = false;
            setTheme(R.style.AppTheme);
        }
        Settings.Album = sharedPre.getAlbum_grid();
        Settings.grid = sharedPre.get_grid();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        animation = AnimationUtils.loadAnimation(this, R.anim.smalltobig);

        Logo = (ImageView) findViewById(R.id.logo);
        Logo.startAnimation(animation);
        Logo_text = (TextView) findViewById(R.id.logo_text);
        Logo_text.startAnimation(animation);


        methods = new Methods(this);
        dbHelper = new DBHelper(this);


        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();


        if (isNetworkAvailable(SplashActivity.this)) {
            loadAboutData();
            initBuy();
        } else {
            IntActivity();
        }
    }
    public void loadAboutData() {
        Toast.makeText(SplashActivity.this, "load About Data", Toast.LENGTH_SHORT).show();
        if (methods.isNetworkAvailable()) { LoadAbout loadAbout = new LoadAbout(SplashActivity.this, new AboutListener() {
            @Override public void onStart() { }
            @Override
            public void onEnd(String success, String verifyStatus, String message) {
                if (success.equals("1")) { if (!verifyStatus.equals("-1")) { Loadnemosofts();sharedPre.setPurchase(); } else { errorDialog(getString(R.string.error_unauth_access), message); } } else { errorDialog(getString(R.string.server_error), getString(R.string.err_server)); }
            }
        });
            loadAbout.execute();
        } else {
            errorDialog(getString(R.string.err_internet_not_conn), getString(R.string.error_connect_net_tryagain));
        }
    }

    public void Loadnemosofts() {
        if (sharedPre.getIsFirstPurchaseCode()) { Toast.makeText(SplashActivity.this, "load Settings ", Toast.LENGTH_SHORT).show(); LoadNemosofts loadAbout = new LoadNemosofts(SplashActivity.this, new NemosoftsListener() {
            @Override
            public void onStart() {
            }
            @Override public void onEnd(String success, String verifyStatus, String message) { if (success.equals("1")) { if (!verifyStatus.equals("-1")) { if (BuildConfig.APPLICATION_ID.equals(Settings.itemAbout.getPackage_name())) { if (JSONParser.isNetworkCheck()) { sharedPre.setIsFirstPurchaseCode(false);sharedPre.setPurchaseCode(Settings.itemAbout);loadSettings(); }else { errorDialog(getString(R.string.error_nemosofts_key), getString(R.string.create_nemosofts_key)); } } else { errorDialog(getString(R.string.error_package_name), getString(R.string.create_nemosofts_key)); } } else { errorDialog(getString(R.string.error_nemosofts_key), message); } } else { errorDialog(getString(R.string.err_internet_not_conn), getString(R.string.error_connect_net_tryagain)); }
            }
        });
            loadAbout.execute();
        } else {
            sharedPre.getPurchaseCode();
            loadSettings();
        }
    }

    public void loadSettings() {
        if (sharedPre.getIsFirst()) {
            openLoginActivity();
        } else {
            if (!sharedPre.getIsAutoLogin()) {
                thiva();
            } else {
                if (methods.isNetworkAvailable()) {
                    loadLogin();
                } else {
                    thiva();
                }
            }
        }
    }

    private void loadLogin() {
        if (methods.isNetworkAvailable()) {
            LoadLogin loadLogin = new LoadLogin(new LoginListener() {
                @Override
                public void onStart() {
                }
                @Override
                public void onEnd(String success, String loginSuccess, String message, String user_id, String user_name) {
                    if (success.equals("1")) {
                        if (loginSuccess.equals("1")) {
                            Settings.itemUser = new ItemUser(user_id, user_name, sharedPre.getEmail(), "");
                            Settings.isLogged = true;
                            thiva();
                        } else {
                            thiva();
                        }
                    } else {
                        thiva();
                    }
                }
            },methods.getAPIRequest(Settings.METHOD_LOGIN, 0, "", "", "", "", "", "", "", "", "", sharedPre.getEmail(), sharedPre.getPassword(), "", "", "", "", null));
            loadLogin.execute();
        } else {
            Toast.makeText(SplashActivity.this, getString(R.string.err_internet_not_conn), Toast.LENGTH_SHORT).show();
        }
    }

    private void errorDialog(String title, String message) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this, R.style.ThemeDialog);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        if (title.equals(getString(R.string.err_internet_not_conn)) || title.equals(getString(R.string.server_error))) {
            alertDialog.setNegativeButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadAboutData();
                }
            });
        }

        alertDialog.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }
    private void openLoginActivity() {
        if (Settings.isLoginOn && sharedPre.getIsFirst()) {
            sharedPre.setIsFirst(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("from", "");
                }
            },SPLASH_TIME_OUT);
        } else {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);

    }

    private void thiva() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent main = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(main);
                finish();
            }
        },SPLASH_TIME_OUT);
    }

    private void IntActivity() {
        Intent mainb = new Intent(SplashActivity.this, intActivity.class);
        startActivity(mainb);
        finish();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static boolean isNetworkAvailable(Context activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initBuy() {
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);


        if(!BillingProcessor.isIabServiceAvailable(this)) {
        }

        bp = new BillingProcessor(this, Settings.MERCHANT_KEY, MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                updateTextViews();
            }
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {

            }
            @Override
            public void onBillingInitialized() {
                readyToPurchase = true;
                updateTextViews();
            }
            @Override
            public void onPurchaseHistoryRestored() {
                for(String sku : bp.listOwnedProducts())
                    Log.d(LOG_TAG, "Owned Managed Product: " + sku);
                for(String sku : bp.listOwnedSubscriptions())
                    Log.d(LOG_TAG, "Owned Subscription: " + sku);
                updateTextViews();
            }
        });
        bp.loadOwnedPurchasesFromGoogle();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unbindService(mServiceConn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTextViews() {
        bp.loadOwnedPurchasesFromGoogle();
        if(isSubscribe(Settings.SUBSCRIPTION_ID)){
            Settings.getPurchases = true;
        } else{
            Settings.getPurchases = false;
        }
    }

    public Bundle getPurchases(){
        if (!bp.isInitialized()) {
            return null;
        }
        try{
            return  mService.getPurchases(Constants.GOOGLE_API_VERSION, getApplicationContext().getPackageName(), Constants.PRODUCT_TYPE_SUBSCRIPTION, null);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean isSubscribe(String SUBSCRIPTION_ID_CHECK){

        if (!bp.isSubscribed(Settings.SUBSCRIPTION_ID))
            return false;


        Bundle b =  getPurchases();
        if (b==null)
            return  false;
        if( b.getInt("RESPONSE_CODE") == 0){
            ArrayList<String> ownedSkus =
                    b.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
            ArrayList<String>  purchaseDataList =
                    b.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            ArrayList<String>  signatureList =
                    b.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
            String continuationToken =
                    b.getString("INAPP_CONTINUATION_TOKEN");


            if(purchaseDataList == null){
                return  false;

            }
            if(purchaseDataList.size()==0){
                return  false;
            }
            for (int i = 0; i < purchaseDataList.size(); ++i) {
                String purchaseData = purchaseDataList.get(i);
                String signature = signatureList.get(i);
                String sku_1 = ownedSkus.get(i);

                try {
                    JSONObject rowOne = new JSONObject(purchaseData);
                    String  productId =  rowOne.getString("productId") ;

                    if (productId.equals(SUBSCRIPTION_ID_CHECK)){

                        Boolean  autoRenewing =  rowOne.getBoolean("autoRenewing");
                        if (autoRenewing){
                            Long tsLong = System.currentTimeMillis()/1000;
                            Long  purchaseTime =  rowOne.getLong("purchaseTime")/1000;
                            return  true;
                        }else{
                            // Toast.makeText(this, "is not autoRenewing ", Toast.LENGTH_SHORT).show();
                            Long tsLong = System.currentTimeMillis()/1000;
                            Long  purchaseTime =  rowOne.getLong("purchaseTime")/1000;
                            if (tsLong > (purchaseTime + (Settings.SUBSCRIPTION_DURATION*86400)) ){
                                //   Toast.makeText(this, "is Expired ", Toast.LENGTH_SHORT).show();
                                return  false;
                            }else{
                                return  true;
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }else{
            return false;
        }

        return  false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
}