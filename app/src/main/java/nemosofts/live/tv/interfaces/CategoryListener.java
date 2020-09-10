package nemosofts.live.tv.interfaces;

import java.util.ArrayList;


import nemosofts.live.tv.Listltem.Listltem_Category;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public interface CategoryListener {
    void onStart();
    void onEnd(String success, ArrayList<Listltem_Category> arrayListCat);
}
