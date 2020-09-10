package nemosofts.live.tv.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import nemosofts.live.tv.Login.ItemUser;
import nemosofts.live.tv.Receiver.ItemNemosofts;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class SharedPre {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static String TAG_UID = "uid" ,TAG_USERNAME = "name", TAG_EMAIL = "email", TAG_MOBILE = "mobile", TAG_REMEMBER = "rem",
            TAG_PASSWORD = "pass", SHARED_PREF_AUTOLOGIN = "autologin";

    public SharedPre(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public Boolean getNightMode() {
        return sharedPreferences.getBoolean("night_mode", false);
    }

    public void setNightMode(Boolean state) {
        editor.putBoolean("night_mode", state);
        editor.apply();
    }

    public int getAlbum_grid() {
        return sharedPreferences.getInt("album_grid", 0);
    }
    public void setAlbum_grid(int state) {
        editor.putInt("album_grid", state);
        editor.apply();
    }

    public int get_grid() {
        return sharedPreferences.getInt("grid", 1);
    }
    public void set_grid(int state) {
        editor.putInt("grid", state);
        editor.apply();
    }

    public void setIsFirst(Boolean flag) {
        editor.putBoolean("firstopen", flag);
        editor.apply();
    }

    public Boolean getIsFirst() {
        return sharedPreferences.getBoolean("firstopen", true);
    }

    public void setIsFirstPurchaseCode(Boolean flag) {
        editor.putBoolean("firstopenpurchasecode", flag);
        editor.apply();
    }

    public Boolean getIsFirstPurchaseCode() {
        return sharedPreferences.getBoolean("firstopenpurchasecode", true);
    }

    public void setPurchaseCode(ItemNemosofts itemNemosofts) {
        editor.putString("purchase_code", itemNemosofts.getPurchase_code());
        editor.putString("product_name", itemNemosofts.getProduct_name());
        editor.putString("purchase_date", itemNemosofts.getPurchase_date());
        editor.putString("buyer_name", itemNemosofts.getBuyer_name());
        editor.putString("license_type", itemNemosofts.getLicense_type());
        editor.putString("nemosofts_key", itemNemosofts.getNemosofts_key());
        editor.putString("package_name", itemNemosofts.getPackage_name());
        editor.apply();
    }

    public void getPurchaseCode() {
        Settings.itemAbout = new ItemNemosofts(
                sharedPreferences.getString("purchase_code",""),
                sharedPreferences.getString("product_name",""),
                sharedPreferences.getString("purchase_date",""),
                sharedPreferences.getString("buyer_name",""),
                sharedPreferences.getString("license_type",""),
                sharedPreferences.getString("nemosofts_key",""),
                sharedPreferences.getString("package_name","")
        );
    }

    public void setLoginDetails(ItemUser itemUser, Boolean isRemember, String password) {
        editor.putBoolean(TAG_REMEMBER, isRemember);
        editor.putString(TAG_UID, itemUser.getId());
        editor.putString(TAG_USERNAME, itemUser.getName());
        editor.putString(TAG_MOBILE, itemUser.getMobile());
        editor.putString(TAG_EMAIL, itemUser.getEmail());
        editor.putBoolean(TAG_REMEMBER, isRemember);
        editor.putString(TAG_PASSWORD, password);
        editor.apply();
    }

    public void setRemeber(Boolean isRemember) {
        editor.putBoolean(TAG_REMEMBER, isRemember);
        editor.putString(TAG_PASSWORD, "");
        editor.apply();
    }

    public void getUserDetails() {
        Settings.itemUser = new ItemUser(sharedPreferences.getString(TAG_UID,""), sharedPreferences.getString(TAG_USERNAME,""), sharedPreferences.getString(TAG_EMAIL,""), sharedPreferences.getString(TAG_MOBILE,""));
    }

    public String getEmail() {
        return sharedPreferences.getString(TAG_EMAIL,"");
    }

    public String getPassword() {
        return sharedPreferences.getString(TAG_PASSWORD,"");
    }

    public Boolean isRemember() {
        return sharedPreferences.getBoolean(TAG_REMEMBER, false);
    }

    public Boolean getIsAutoLogin() {
        return sharedPreferences.getBoolean(SHARED_PREF_AUTOLOGIN, false);
    }

    public void setIsAutoLogin(Boolean isAutoLogin) {
        editor.putBoolean(SHARED_PREF_AUTOLOGIN, isAutoLogin);
        editor.apply();
    }

    public Boolean getIsRemember() {
        return sharedPreferences.getBoolean(TAG_REMEMBER, false);
    }

    public Boolean getIsNotification() {
        return sharedPreferences.getBoolean("noti", true);
    }

    public void setIsNotification(Boolean isNotification) {
        editor.putBoolean("noti", isNotification);
        editor.apply();
    }

    public void setPurchase() {
        editor.putBoolean("in_app", Settings.in_app);
        editor.putString("subscription_id", Settings.SUBSCRIPTION_ID);
        editor.putString("merchant_key", Settings.MERCHANT_KEY);
        editor.putInt("sub_dur", Settings.SUBSCRIPTION_DURATION);
        editor.apply();
    }

    public void getPurchase() {
        Settings.in_app = sharedPreferences.getBoolean("in_app", true);
        Settings.SUBSCRIPTION_ID = sharedPreferences.getString("subscription_id","SUBSCRIPTION_ID");
        Settings.MERCHANT_KEY = sharedPreferences.getString("merchant_key","MERCHANT_KEY");
        Settings.SUBSCRIPTION_DURATION = sharedPreferences.getInt("sub_dur",30);
    }


}
