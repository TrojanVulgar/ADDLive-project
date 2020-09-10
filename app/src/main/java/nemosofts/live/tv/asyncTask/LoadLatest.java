package nemosofts.live.tv.asyncTask;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nemosofts.live.tv.JSONParser.JSONParser;
import nemosofts.live.tv.interfaces.LatestListener;
import nemosofts.live.tv.Listltem.Listltem;
import nemosofts.live.tv.SharedPref.Settings;
import okhttp3.RequestBody;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class LoadLatest extends AsyncTask<String, String, String> {

    private LatestListener latestListener;
    private ArrayList<Listltem> arrayList;
    private RequestBody requestBody;
    private String verifyStatus = "0", message = "";

    public LoadLatest(LatestListener latestListener, RequestBody requestBody) {
        this.latestListener = latestListener;
        arrayList = new ArrayList<>();
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        latestListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected  String doInBackground(String... strings)  {
        String json = JSONParser.okhttpPost(Settings.SERVER_URL, requestBody);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Settings.TAG_ROOT);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objJson = jsonArray.getJSONObject(i);

                if (!objJson.has(Settings.TAG_SUCCESS)) {

                    String id = objJson.getString("id");
                    String name = objJson.getString("name");
                    String url = objJson.getString("url");
                    String pay = objJson.getString("tv_pay");
                    String imageUrl = objJson.getString("image");
                    String video_type = objJson.getString("video_type");
                    String total_views = objJson.getString("total_views");

                    Listltem listltem = new Listltem(id,name,url,pay,imageUrl,video_type,total_views);
                    arrayList.add(listltem);
                } else {
                    verifyStatus = objJson.getString(Settings.TAG_SUCCESS);
                    message = objJson.getString(Settings.TAG_MSG);
                }

            }
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }

    }

    @Override
    protected void onPostExecute(String s) {
        latestListener.onEnd(s, verifyStatus, message, arrayList);
        super.onPostExecute(s);
    }

}

