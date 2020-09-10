package nemosofts.live.tv.asyncTask;

import android.os.AsyncTask;



import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nemosofts.live.tv.JSONParser.JSONParser;
import nemosofts.live.tv.Listltem.ItemHomeBanner;
import nemosofts.live.tv.Listltem.Listltem;
import nemosofts.live.tv.SharedPref.Settings;
import nemosofts.live.tv.interfaces.HomeListener;
import okhttp3.RequestBody;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class LoadHome extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private HomeListener homeListener;
    private ArrayList<ItemHomeBanner> arrayListBanner = new ArrayList<>();
    private ArrayList<Listltem> arrayList_latest = new ArrayList<>();
    private ArrayList<Listltem> arrayList_most = new ArrayList<>();


    public LoadHome(HomeListener homeListener, RequestBody requestBody) {
        this.homeListener = homeListener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        homeListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {

            String json = JSONParser.okhttpPost(Settings.SERVER_URL, requestBody);

            JSONObject mainJson = new JSONObject(json);
            JSONObject jsonObject = mainJson.getJSONObject(Settings.TAG_ROOT);

            JSONArray jsonArrayBanner = jsonObject.getJSONArray("home_banner");

            for (int i = 0; i < jsonArrayBanner.length(); i++) {
                JSONObject objJsonBanner = jsonArrayBanner.getJSONObject(i);

                String banner_id = objJsonBanner.getString("bid");
                String banner_title = objJsonBanner.getString("banner_title");
                String banner_desc = objJsonBanner.getString("banner_sort_info");
                String banner_image = objJsonBanner.getString("banner_image");
                String banner_total = objJsonBanner.getString("total_songs");

                JSONArray jABannerSongs = objJsonBanner.getJSONArray("songs_list");
                ArrayList<Listltem> arrayListBannerSongs = new ArrayList<>();
                for (int j = 0; j < jABannerSongs.length(); j++) {
                    JSONObject objJson = jABannerSongs.getJSONObject(j);

                    String id = objJson.getString("id");
                    String name = objJson.getString("name");
                    String url = objJson.getString("url");
                    String pay = objJson.getString("tv_pay");
                    String imageUrl = objJson.getString("image");
                    String video_type = objJson.getString("video_type");
                    String total_views = objJson.getString("total_views");

                    Listltem listltem = new Listltem(id, name, url, pay, imageUrl, video_type, total_views);
                    arrayListBannerSongs.add(listltem);
                }
                arrayListBanner.add(new ItemHomeBanner(banner_id, banner_title, banner_image, banner_desc, banner_total, arrayListBannerSongs));
            }

            JSONArray jsonArrayHome1 = jsonObject.getJSONArray("latest");
            for (int i = 0; i < jsonArrayHome1.length(); i++) {
                JSONObject objJson = jsonArrayHome1.getJSONObject(i);

                String id = objJson.getString("id");
                String name = objJson.getString("name");
                String url = objJson.getString("url");
                String pay = objJson.getString("tv_pay");
                String imageUrl = objJson.getString("image");
                String video_type = objJson.getString("video_type");
                String total_views = objJson.getString("total_views");

                Listltem listltem = new Listltem(id,name,url, pay,imageUrl,video_type,total_views);
                arrayList_latest.add(listltem);
            }

            JSONArray jsonArrayHome2 = jsonObject.getJSONArray("most_view");
            for (int i = 0; i < jsonArrayHome2.length(); i++) {
                JSONObject objJson = jsonArrayHome2.getJSONObject(i);

                String id = objJson.getString("id");
                String name = objJson.getString("name");
                String url = objJson.getString("url");
                String pay = objJson.getString("tv_pay");
                String imageUrl = objJson.getString("image");
                String video_type = objJson.getString("video_type");
                String total_views = objJson.getString("total_views");

                Listltem listltem = new Listltem(id,name,url, pay,imageUrl,video_type,total_views);
                arrayList_most.add(listltem);
            }


            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        homeListener.onEnd(s, arrayListBanner, arrayList_latest, arrayList_most);
        super.onPostExecute(s);
    }
}