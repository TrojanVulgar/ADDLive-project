package nemosofts.live.tv.JSONParser;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import nemosofts.live.tv.Listltem.ItemHomeBanner;
import nemosofts.live.tv.Listltem.Listltem;
import nemosofts.live.tv.Login.ItemUser;
import nemosofts.live.tv.Receiver.ItemNemosofts;
import nemosofts.live.tv.SharedPref.Settings;
import nemosofts.live.tv.constant.Constant;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class JSONParser {
    private Context _context;

    public JSONParser(Context context) {
        this._context = context;
    }

    public static boolean isNetworkAvailable(Context activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static String okhttpPost(String url, RequestBody requestBody) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(25000, TimeUnit.MILLISECONDS)
                .writeTimeout(25000, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String api="speed_api.php";public static String SERVER_URL = Constant.Saver_Url + api;public static String purchase_code = "";public static String nemosofts_key = "";
    public static String company = "";public static String email = "";public static String website = "";public static String contact = "";public static ItemNemosofts itemAbout;
    public static ArrayList<Listltem> arrayList = new ArrayList<>();public static int Album = 0;public static int grid = 0;public static boolean Dark_Mode = false;
    public static ItemUser itemUser;public static Boolean isLogged = false;public static Boolean isLoginOn = true;public static Boolean   isAdmobBannerAd = true, isAdmobInterAd = true, isAdmobNativeAd = false, isFBBannerAd = true, isFBInterAd = true, isFBNativeAd = false;
    public static String ad_publisher_id = "";public static String ad_banner_id = "", ad_inter_id = "", ad_native_id = "", fb_ad_banner_id = "", fb_ad_inter_id = "", fb_ad_native_id = "";
    public static int adShow = 5;public static int adShowFB = 5;public static int adCount = 0;public static int admobNativeShow = 5, fbNativeShow = 5;
    public static Boolean in_app = true;public static  String SUBSCRIPTION_ID = "SUBSCRIPTION_ID";public static  String MERCHANT_KEY = "MERCHANT_KEY";public static int  SUBSCRIPTION_DURATION = 30;
    public static Boolean getPurchases = false;public static boolean isDataCheck() { if (Settings.purchase_code.equals(Settings.itemAbout.getPurchase_code())) { return true; } else { return false; } }
    public static final String METHOD_APP_DETAILS = "app_details";public static final String METHOD_CAT = "cat_list";public static final String METHOD_ALL = "all";public static final String METHOD_HOME = "home";
    public static final String METHOD_MOST_VIEWED = "most_viewed";public static final String METHOD_TV_BY_CAT = "cat_tv";public static final String METHOD_LOGIN = "user_login";
    public static final String METHOD_TV = "tv";public static final String METHOD_REGISTER = "user_register";public static final String TAG_ROOT = "nemosofts";public static final String METHOD_SEARCH = "tv_search";
    public static boolean isNetworkCheck() { if (Settings.nemosofts_key.equals(Settings.itemAbout.getNemosofts_key())) { return true; } else { return false; } }
    public static final String TAG_SUCCESS = "success";public static final String TAG_MSG = "msg";public static ArrayList<ItemHomeBanner> arrayList2 = new ArrayList<>();
    public static Boolean load_arrayList = false;public static ArrayList<Listltem> arrayList_latest = new ArrayList<>();public static ArrayList<Listltem> arrayList_most = new ArrayList<>();public static ArrayList<ItemHomeBanner> arrayList_banner = new ArrayList<>();
}