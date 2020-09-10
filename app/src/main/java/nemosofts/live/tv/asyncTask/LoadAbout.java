package nemosofts.live.tv.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import nemosofts.live.tv.JSONParser.JSONParser;
import nemosofts.live.tv.Methods.Methods;
import nemosofts.live.tv.SharedPref.Settings;
import nemosofts.live.tv.interfaces.AboutListener;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class LoadAbout extends AsyncTask<String, String, String> {

    private Methods methods;
    private AboutListener aboutListener;
    private String message = "", verifyStatus = "0";

    public LoadAbout(Context context, AboutListener aboutListener) {
        this.aboutListener = aboutListener;
        methods = new Methods(context);
    }

    @Override
    protected void onPreExecute() {
        aboutListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JSONParser.okhttpPost(Settings.SERVER_URL, methods.getAPIRequest(Settings.METHOD_APP_DETAILS, 0, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", null));
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has(Settings.TAG_ROOT)) {
                JSONArray jsonArray = jsonObject.getJSONArray(Settings.TAG_ROOT);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    if (!c.has(Settings.TAG_SUCCESS)) {
                        Settings.ad_banner_id = c.getString("banner_ad_id");
                        Settings.ad_inter_id = c.getString("interstital_ad_id");
                        Settings.isAdmobBannerAd = Boolean.parseBoolean(c.getString("banner_ad"));
                        Settings.isAdmobInterAd = Boolean.parseBoolean(c.getString("interstital_ad"));
                        Settings.ad_publisher_id = c.getString("publisher_id");

                        if(!c.getString("interstital_ad_click").equals("")) {
                            Settings.adShow = Integer.parseInt(c.getString("interstital_ad_click"));
                        }

                        Settings.fb_ad_banner_id = c.getString("facebook_banner_ad_id");
                        Settings.fb_ad_inter_id = c.getString("facebook_interstital_ad_id");
                        Settings.isFBBannerAd = Boolean.parseBoolean(c.getString("facebook_banner_ad"));
                        Settings.isFBInterAd = Boolean.parseBoolean(c.getString("facebook_interstital_ad"));

                        if(!c.getString("facebook_interstital_ad_click").equals("")) {
                            Settings.adShowFB = Integer.parseInt(c.getString("facebook_interstital_ad_click"));
                        }

                        Settings.ad_native_id = c.getString("admob_native_ad_id");
                        Settings.isAdmobNativeAd = Boolean.parseBoolean(c.getString("admob_nathive_ad"));
                        if(!c.getString("admob_native_ad_click").equals("")) {
                            Settings.admobNativeShow = Integer.parseInt(c.getString("admob_native_ad_click"));
                        }

                        Settings.fb_ad_native_id = c.getString("facebook_native_ad_id");
                        Settings.isFBNativeAd = Boolean.parseBoolean(c.getString("facebook_native_ad"));
                        if(!c.getString("facebook_native_ad_click").equals("")) {
                            Settings.fbNativeShow = Integer.parseInt(c.getString("facebook_native_ad_click"));
                        }

                        Settings.company = c.getString("company");
                        Settings.email = c.getString("email");
                        Settings.website = c.getString("website");
                        Settings.contact = c.getString("contact");

                        Settings.purchase_code = c.getString("purchase_code");
                        Settings.nemosofts_key = c.getString("nemosofts_key");

                        Settings.in_app = Boolean.parseBoolean(c.getString("in_app"));
                        Settings.SUBSCRIPTION_ID = c.getString("subscription_id");
                        Settings.MERCHANT_KEY =  c.getString("merchant_key");
                        if(!c.getString("subscription_days").equals("")) {
                            Settings.SUBSCRIPTION_DURATION = Integer.parseInt(c.getString("subscription_days"));
                        }


                    } else {
                        verifyStatus = c.getString(Settings.TAG_SUCCESS);
                        message = c.getString(Settings.TAG_MSG);
                    }
                }
            }
            return "1";
        } catch (Exception ee) {
            ee.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        aboutListener.onEnd(s, verifyStatus, message);
        super.onPostExecute(s);
    }
}