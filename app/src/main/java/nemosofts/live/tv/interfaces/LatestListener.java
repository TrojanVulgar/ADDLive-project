package nemosofts.live.tv.interfaces;

import java.util.ArrayList;

import nemosofts.live.tv.Listltem.Listltem;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public interface LatestListener {
    void onStart();
    void onEnd(String success, String verifyStatus, String message, ArrayList<Listltem> arrayListTV);
}
