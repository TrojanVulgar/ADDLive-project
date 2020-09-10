package nemosofts.live.tv.interfaces;

import nemosofts.live.tv.Listltem.ItemHomeBanner;
import nemosofts.live.tv.Listltem.Listltem;



import java.util.ArrayList;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public interface HomeListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemHomeBanner> arrayListBanner, ArrayList<Listltem> arrayList_latest, ArrayList<Listltem> arrayList_most);
}
