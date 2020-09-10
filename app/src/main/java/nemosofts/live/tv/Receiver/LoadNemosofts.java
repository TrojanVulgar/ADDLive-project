package nemosofts.live.tv.Receiver;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;


import nemosofts.live.tv.BuildConfig;
import nemosofts.live.tv.JSONParser.JSONParser;
import nemosofts.live.tv.Methods.Methods;
import nemosofts.live.tv.SharedPref.Settings;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class LoadNemosofts extends AsyncTask<String, String, String> {

    private Methods methods;
    private NemosoftsListener aboutListener;
    private String message = "", verifyStatus = "0";

    public LoadNemosofts(Context context, NemosoftsListener aboutListener) {
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
            String json = JSONParser.okhttpPost(BuildConfig.NEMOSOFTS+Settings.api, methods.getAPIRequest(Settings.TAG_ROOT, 0, Settings.nemosofts_key, "", "", "", "", "", "", "", "", "", "", "", "", "", "", null));
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject.has(Settings.TAG_ROOT)) {
                JSONArray jsonArray = jsonObject.getJSONArray(Settings.TAG_ROOT);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objJson = jsonArray.getJSONObject(i);

                    if (!objJson.has(Settings.TAG_SUCCESS)) {
                        String purchase_code = objJson.getString("purchase_code");
                        String product_name = objJson.getString("product_name");
                        String purchase_date = objJson.getString("purchase_date");
                        String buyer_name = objJson.getString("buyer_name");
                        String license_type = objJson.getString("license_type");
                        String nemosofts_key = objJson.getString("nemosofts_key");
                        String package_name = objJson.getString("package_name");

                        Settings.itemAbout = new ItemNemosofts(purchase_code, product_name, purchase_date, buyer_name, license_type, nemosofts_key, package_name);
                    }else {
                        verifyStatus = objJson.getString("success");
                        message = objJson.getString("msg");
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