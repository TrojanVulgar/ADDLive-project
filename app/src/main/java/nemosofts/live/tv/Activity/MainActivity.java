package nemosofts.live.tv.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.nemosofts.library.Nemosofts;
import com.nemosofts.library.UpdateManager.UpdateManager;
import com.nemosofts.library.UpdateManager.UpdateManagerConstant;

import java.util.ArrayList;


import nemosofts.live.tv.Adapter.HomePagerAdapter;
import nemosofts.live.tv.Adapter.Home_TV_Adapter;
import nemosofts.live.tv.DBHelper.DBHelper;
import nemosofts.live.tv.JSONParser.JSONParser;
import nemosofts.live.tv.Listltem.ItemHomeBanner;
import nemosofts.live.tv.Listltem.Listltem;

import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import nemosofts.live.tv.SharedPref.NavigationUtil;
import nemosofts.live.tv.asyncTask.LoadHome;
import nemosofts.live.tv.SharedPref.SharedPre;
import nemosofts.live.tv.interfaces.HomeListener;
import nemosofts.live.tv.Methods.Methods;
import nemosofts.live.tv.R;
import nemosofts.live.tv.SharedPref.Settings;
import nemosofts.live.tv.interfaces.InterAdListener;
import nemosofts.live.tv.interfaces.RecyclerViewClickListener;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Methods methods;
    DBHelper dbHelper;

    RecyclerView recyclerView_fav, recyclerView_latest, recyclerView_most, recyclerView_rec;
    ArrayList<Listltem> arrayList_fav, arrayList_latest, arrayList_most, arrayList_rec;
    Home_TV_Adapter adapter_fav, adapter_latest, adapter_most, adapter_rec;
    LinearLayoutManager linearLayoutManager, linearLayoutManager_latest, linearLayoutManager_most, linearLayoutManager_rec;
    LinearLayout view_fav, view_home, view_rec, view_viewPager_home;
    ProgressBar progressBar;
    private EnchantedViewPager enchantedViewPager;
    private HomePagerAdapter homePagerAdapter;
    private ArrayList<ItemHomeBanner> arrayList_banner;
    EditText searchView;
    ImageView search;
    TextView seeall_fav, seeall_latest, seeall_most;


    NavigationView navigationView;

    MenuItem menu_login;

    Nemosofts nemosofts;
    UpdateManager update;

    private Menu menu;
    private static final String MERCHANT_ID=null;
    IInAppBillingService mService;
    private  Boolean DialogOpened = false;
    private TextView text_view_go_pro;
    private BillingProcessor bp;
    private boolean readyToPurchase = false;
    private static final String LOG_TAG = "iabv3";

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            // Toast.makeText(MainActivity.this, "set null", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
            //Toast.makeText(MainActivity.this, "set Stub", Toast.LENGTH_SHORT).show();

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Settings.Dark_Mode ) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initBuy();
        nemosofts = new Nemosofts(this);
        //nemosofts.ShowDialog();
        methods = new Methods(this);
        dbHelper = new DBHelper(this);


        methods = new Methods(this, new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                if (type.equals("latest")){
                    NavigationUtil.Video(MainActivity.this, arrayList_latest.get(position).getVideo_type(), position, arrayList_latest);
                }if (type.equals("most")){
                    NavigationUtil.Video(MainActivity.this, arrayList_most.get(position).getVideo_type(), position, arrayList_most);
                }if (type.equals("fav")){
                    NavigationUtil.Video(MainActivity.this, arrayList_fav.get(position).getVideo_type(), position, arrayList_fav);
                }if (type.equals("rec")){
                    NavigationUtil.Video(MainActivity.this, arrayList_rec.get(position).getVideo_type(), position, arrayList_rec);
                }
            }
        });

        // Initialize the Update Manager with the Activity and the Update Mode
        update = UpdateManager.Builder(this).mode(UpdateManagerConstant.FLEXIBLE);
        update.start();

        update.getAvailableVersionCode(new UpdateManager.onVersionCheckListener() {
            @Override
            public void onReceiveVersionCode(final int code) {
                // Do something here
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        menu_login = menu.findItem(R.id.nav_login);

        changeLoginName();

        progressBar = findViewById(R.id.progressBar);
        view_home = findViewById(R.id.view_home);
        view_home.setVisibility(View.GONE);
        view_rec = findViewById(R.id.view_rec);
        view_viewPager_home = findViewById(R.id.view_viewPager_home);

        arrayList_fav = new ArrayList<>();
        arrayList_latest = new ArrayList<>();
        arrayList_most = new ArrayList<>();
        arrayList_rec = new ArrayList<>();

        arrayList_fav.addAll(dbHelper.loadFavData());

        view_fav = findViewById(R.id.view_fav);
        recyclerView_fav = findViewById(R.id.tv_fav);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_fav.setLayoutManager(linearLayoutManager);
        recyclerView_fav.setItemAnimator(new DefaultItemAnimator());
        recyclerView_fav.setHasFixedSize(true);
        recyclerView_fav.setAdapter(adapter_fav);

        recyclerView_latest = findViewById(R.id.tv_lat);
        linearLayoutManager_latest = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_latest.setLayoutManager(linearLayoutManager_latest);
        recyclerView_latest.setItemAnimator(new DefaultItemAnimator());
        recyclerView_latest.setHasFixedSize(true);
        recyclerView_latest.setAdapter(adapter_latest);

        recyclerView_most = findViewById(R.id.tv_most);
        linearLayoutManager_most = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_most.setLayoutManager(linearLayoutManager_most);
        recyclerView_most.setItemAnimator(new DefaultItemAnimator());
        recyclerView_most.setHasFixedSize(true);
        recyclerView_most.setAdapter(adapter_most);

        recyclerView_rec = findViewById(R.id.tv_rec);
        linearLayoutManager_rec = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_rec.setLayoutManager(linearLayoutManager_rec);
        recyclerView_rec.setItemAnimator(new DefaultItemAnimator());
        recyclerView_rec.setHasFixedSize(true);
        recyclerView_rec.setAdapter(adapter_rec);

        arrayList_banner = new ArrayList<>();
        enchantedViewPager = findViewById(R.id.viewPager_home);
        enchantedViewPager.useAlpha();
        enchantedViewPager.useScale();

        if (Settings.load_arrayList) {
            arrayList_latest.addAll(Settings.arrayList_latest);
            arrayList_most.addAll(Settings.arrayList_most);
            arrayList_banner.addAll(Settings.arrayList_banner);
            SetAdapter();
        }else {
            if (JSONParser.isDataCheck()) {
                if (JSONParser.isNetworkCheck()) {
                    loadHome();
                }
            }
        }

        searchView = findViewById(R.id.search_view);
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchView.getText().toString().trim().isEmpty()) {
                    searchView.setError(getResources().getString(R.string.enter_name_channel));
                    searchView.requestFocus();
                } else {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("text", searchView.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        LinearLayout adView = findViewById(R.id.adView);
        methods.showBannerAd(adView);

        seeall_fav = findViewById(R.id.seeall_fav);
        seeall_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Favourite = new Intent(MainActivity.this, FavouriteActivity.class);
                startActivity(Favourite);
            }
        });

        seeall_latest = findViewById(R.id.seeall_latest);
        seeall_latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent All = new Intent(MainActivity.this,All_Activity.class);
                startActivity(All);
            }
        });

        seeall_most = findViewById(R.id.seeall_most);
        seeall_most.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Most = new Intent(MainActivity.this,Most_Activity.class);
                startActivity(Most);
            }
        });
    }



    private void loadHome() {
        if (methods.isNetworkAvailable()) {
            LoadHome loadHome = new LoadHome(new HomeListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onEnd(String success, ArrayList<ItemHomeBanner> arrayListBanner, ArrayList<Listltem> arrayList_latest_new, ArrayList<Listltem> arrayList_most_new) {
                    if (success.equals("1")) {
                        if (JSONParser.isDataCheck()) {
                            arrayList_latest.addAll(arrayList_latest_new);
                            arrayList_most.addAll(arrayList_most_new);
                            arrayList_banner.addAll(arrayListBanner);

                            Settings.arrayList_latest.addAll(arrayList_latest_new);
                            Settings.arrayList_most.addAll(arrayList_most_new);
                            Settings.arrayList_banner.addAll(arrayListBanner);
                        }


                        Settings.load_arrayList = true;

                        SetAdapter();

                    } else {
                        view_home.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }, methods.getAPIRequest(Settings.METHOD_HOME, 0,"","","","","","","","","","","","","","","", null));
            loadHome.execute();
        } else {

        }
    }

    private void SetAdapter() {
        adapter_latest = new Home_TV_Adapter(MainActivity.this, arrayList_latest , new RecyclerViewClickListener(){
            @Override
            public void onClick(int position) {
                methods.showInter(position, "latest");
            }
        });
        recyclerView_latest.setAdapter(adapter_latest);


        adapter_most = new Home_TV_Adapter(MainActivity.this, arrayList_most , new RecyclerViewClickListener(){
            @Override
            public void onClick(int position) {
                methods.showInter(position, "most");
            }
        });
        recyclerView_most.setAdapter(adapter_most);

        if (arrayList_banner.size() == 0) {
            view_viewPager_home.setVisibility(View.GONE);
        }else {
            homePagerAdapter = new HomePagerAdapter(MainActivity.this, arrayList_banner);
            enchantedViewPager.setAdapter(homePagerAdapter);
            if (homePagerAdapter.getCount() > 2) {
                enchantedViewPager.setCurrentItem(1);
            }
        }

        if (arrayList_fav.size() == 0) {
            view_fav.setVisibility(View.GONE);
        }else {
            adapter_fav = new Home_TV_Adapter(MainActivity.this, arrayList_fav , new RecyclerViewClickListener(){
                @Override
                public void onClick(int position) {
                    methods.showInter(position, "fav");
                }
            });
            recyclerView_fav.setAdapter(adapter_fav);
        }

        loadRecent();

        view_home.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void loadRecent() {
        arrayList_rec.addAll(dbHelper.loadDataRecent("10"));
        if (arrayList_rec.size() == 0) {
            view_rec.setVisibility(View.GONE);
        } else {
            adapter_rec = new Home_TV_Adapter(MainActivity.this, arrayList_rec , new RecyclerViewClickListener(){
                @Override
                public void onClick(int position) {
                    methods.showInter(position, "rec");
                }
            });
            recyclerView_rec.setAdapter(adapter_rec);
        }
    }

    private void changeLoginName() {
        if (menu_login != null) {
            if (Settings.isLoginOn) {
                if (Settings.isLogged) {
                    menu_login.setTitle(getResources().getString(R.string.logout));
                    menu_login.setIcon(getResources().getDrawable(R.drawable.ic_logout));
                } else {
                    menu_login.setTitle(getResources().getString(R.string.login));
                    menu_login.setIcon(getResources().getDrawable(R.drawable.ic_login));
                }
            } else {
                menu_login.setVisible(false);
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_all:
                Intent All = new Intent(MainActivity.this,All_Activity.class);
                startActivity(All);
                break;
            case R.id.nav_cat:
                Intent Category = new Intent(MainActivity.this,CategoryActivity.class);
                startActivity(Category);
                break;
            case R.id.nav_top:
                Intent Most = new Intent(MainActivity.this,Most_Activity.class);
                startActivity(Most);
                break;
            case R.id.nav_favourite:
                Intent Favourite = new Intent(MainActivity.this, FavouriteActivity.class);
                startActivity(Favourite);
                break;
            case R.id.nav_set:
                overridePendingTransition(0, 0);
                overridePendingTransition(0, 0);
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                finish();
                break;
            case R.id.nav_login:
                methods.clickLogin();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        if (Settings.in_app){
            if (Settings.getPurchases){
                menu.clear();
            }
        }else {
            menu.clear();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){

            case R.id.action_pro :
                showDialog_pay();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initBuy() {
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);


        if(!BillingProcessor.isIabServiceAvailable(this)) {
            //  showToast("In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16");
        }

        bp = new BillingProcessor(this, Settings.MERCHANT_KEY, MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                //  showToast("onProductPurchased: " + productId);
                Intent intent= new Intent(MainActivity.this,SplashActivity.class);
                startActivity(intent);
                finish();
                updateTextViews();
            }
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
                // showToast("onBillingError: " + Integer.toString(errorCode));
            }
            @Override
            public void onBillingInitialized() {
                //  showToast("onBillingInitialized");
                readyToPurchase = true;
                updateTextViews();
            }
            @Override
            public void onPurchaseHistoryRestored() {
                // showToast("onPurchaseHistoryRestored");
                for(String sku : bp.listOwnedProducts())
                    Log.d(LOG_TAG, "Owned Managed Product: " + sku);
                for(String sku : bp.listOwnedSubscriptions())
                    Log.d(LOG_TAG, "Owned Subscription: " + sku);
                updateTextViews();
            }
        });
        bp.loadOwnedPurchasesFromGoogle();
    }
    private void updateTextViews() {
        SharedPre prf= new SharedPre(getApplicationContext());
        bp.loadOwnedPurchasesFromGoogle();
    }

    public Bundle getPurchases(){
        if (!bp.isInitialized()) {
            //  Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            return null;
        }
        try{
            // Toast.makeText(this, "good", Toast.LENGTH_SHORT).show();
            return  mService.getPurchases(Constants.GOOGLE_API_VERSION, getApplicationContext().getPackageName(), Constants.PRODUCT_TYPE_SUBSCRIPTION, null);
        }catch (Exception e) {
            //  Toast.makeText(this, "ex", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }


    public void showDialog_pay(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.dialog_subscribe, null);

        final BottomSheetDialog dialog_setas = new BottomSheetDialog(this);
        dialog_setas.setContentView(view);
        dialog_setas.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);

        this.text_view_go_pro=(TextView) dialog_setas.findViewById(R.id.text_view_go_pro);
        RelativeLayout relativeLayout_close_rate_gialog=(RelativeLayout) dialog_setas.findViewById(R.id.relativeLayout_close_rate_gialog);
        relativeLayout_close_rate_gialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_setas.dismiss();
            }
        });
        text_view_go_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bp.subscribe(MainActivity.this, Settings.SUBSCRIPTION_ID);
            }
        });
        dialog_setas.setOnKeyListener(new BottomSheetDialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    dialog_setas.dismiss();
                }
                return true;
            }
        });
        dialog_setas.show();
        DialogOpened=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Continue updates when resumed
        update.continueUpdate();
    }





}
