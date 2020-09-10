package nemosofts.live.tv.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import nemosofts.live.tv.Listltem.Listltem;

/**
 * Created by thivakaran
 */
public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "ttv.db";

    private static String TAG_ID = "id";

    private static String  TAG_tid = "tid";
    private static String TAG_name = "name";
    private static String TAG_url = "url";
    private static String TAG_paytv = "urlpay";
    private static String TAG_image = "image";
    private static String TAG_video_type = "video_type";
    private static String TAG_total_views = "total_views";

    private SQLiteDatabase db;
    private final Context context;

    private static final String TABLE_FAV_TV = "channel";
    private static final String TABLE_RECENT = "recent";

    // Creating table query
    private static final String CREATE_TABLE_FAV = "create table " + TABLE_FAV_TV + "(" +
            TAG_ID + " integer PRIMARY KEY AUTOINCREMENT," +
            TAG_tid + " TEXT," +
            TAG_name + " TEXT," +
            TAG_url + " TEXT," +
            TAG_paytv + " TEXT," +
            TAG_image + " TEXT," +
            TAG_video_type + " TEXT," +
            TAG_total_views + " TEXT);";

    // Creating table query
    private static final String CREATE_TABLE_RECENT = "create table " + TABLE_RECENT + "(" +
            TAG_ID + " integer PRIMARY KEY AUTOINCREMENT," +
            TAG_tid + " TEXT," +
            TAG_name + " TEXT," +
            TAG_url + " TEXT," +
            TAG_paytv + " TEXT," +
            TAG_image + " TEXT," +
            TAG_video_type + " TEXT," +
            TAG_total_views + " TEXT);";

    private String[] columns_channel = new String[]{TAG_ID, TAG_tid, TAG_name, TAG_url, TAG_paytv, TAG_image, TAG_video_type, TAG_total_views};

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 5);
        this.context = context;
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_FAV);
            db.execSQL(CREATE_TABLE_RECENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean addORremoveFav(Listltem itemRadio) {
        Cursor cursor = db.query(TABLE_FAV_TV, columns_channel, TAG_tid + "=" + itemRadio.getId(), null, null, null, null);
        if(cursor.getCount() == 0) {
            addToFav(itemRadio);
            return true;
        } else {
            removeFromFav(itemRadio.getId());
            return false;
        }
    }

    public void addToFav(Listltem itemSong) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_tid, itemSong.getId());
        contentValues.put(TAG_name, itemSong.getName());
        contentValues.put(TAG_url, itemSong.getUrl());
        contentValues.put(TAG_paytv, itemSong.getPay());
        contentValues.put(TAG_image, itemSong.getImageUrl());
        contentValues.put(TAG_video_type, itemSong.getVideo_type());
        contentValues.put(TAG_total_views, itemSong.getTotal_views());

        db.insert(TABLE_FAV_TV, null, contentValues);
    }

    public void removeFromFav(String id) {
        db.delete(TABLE_FAV_TV, TAG_tid + "=" + id, null);
    }


    public Boolean checkFav(String id) {
        Cursor cursor = db.query(TABLE_FAV_TV, columns_channel, TAG_tid + "=" + id, null, null, null, null);
        Boolean isFav = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return isFav;
    }

    public ArrayList<Listltem> loadFavData() {
        ArrayList<Listltem> arrayList = new ArrayList<>();
        try {
            Cursor cursor = db.query(TABLE_FAV_TV, columns_channel, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {

                    String id = cursor.getString(cursor.getColumnIndex(TAG_tid));
                    String name = cursor.getString(cursor.getColumnIndex(TAG_name));
                    String url = cursor.getString(cursor.getColumnIndex(TAG_url));
                    String pay_url = cursor.getString(cursor.getColumnIndex(TAG_paytv));
                    String image = cursor.getString(cursor.getColumnIndex(TAG_image));
                    String video_type = cursor.getString(cursor.getColumnIndex(TAG_video_type));
                    String total_views = cursor.getString(cursor.getColumnIndex(TAG_total_views));

                    Listltem objItem = new Listltem(id, name, url,pay_url, image, video_type, total_views);
                    arrayList.add(objItem);

                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrayList;
    }


    public void addToRecent(Listltem listltem) {
        Cursor cursor_delete = db.query(TABLE_RECENT, columns_channel, null, null, null, null, null);
        if (cursor_delete != null && cursor_delete.getCount() > 20) {
            cursor_delete.moveToFirst();
            db.delete(TABLE_RECENT, TAG_tid + "=" + cursor_delete.getString(cursor_delete.getColumnIndex(TAG_tid)), null);
        }
        cursor_delete.close();

        if (checkRecent(listltem.getId())) {
            db.delete(TABLE_RECENT, TAG_tid + "=" + listltem.getId(), null);
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_tid, listltem.getId());
        contentValues.put(TAG_name, listltem.getName());
        contentValues.put(TAG_url, listltem.getUrl());
        contentValues.put(TAG_paytv, listltem.getPay());
        contentValues.put(TAG_image, listltem.getImageUrl());
        contentValues.put(TAG_video_type, listltem.getVideo_type());
        contentValues.put(TAG_total_views, listltem.getTotal_views());

        db.insert(TABLE_RECENT, null, contentValues);
    }


    private Boolean checkRecent(String id) {
        Cursor cursor = db.query(TABLE_RECENT, columns_channel, TAG_tid + "=" + id, null, null, null, null);
        Boolean isFav = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return isFav;
    }


    public ArrayList<Listltem> loadDataRecent(String limit) {
        ArrayList<Listltem> arrayList = new ArrayList<>();
        try {
            Cursor cursor = db.query(TABLE_RECENT, columns_channel, null, null, null, null, TAG_ID + " DESC", limit);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {

                    String id = cursor.getString(cursor.getColumnIndex(TAG_tid));
                    String name = cursor.getString(cursor.getColumnIndex(TAG_name));
                    String url = cursor.getString(cursor.getColumnIndex(TAG_url));
                    String pay_url = cursor.getString(cursor.getColumnIndex(TAG_paytv));
                    String image = cursor.getString(cursor.getColumnIndex(TAG_image));
                    String video_type = cursor.getString(cursor.getColumnIndex(TAG_video_type));
                    String total_views = cursor.getString(cursor.getColumnIndex(TAG_total_views));

                    Listltem objItem = new Listltem(id, name, url,pay_url, image, video_type, total_views);
                    arrayList.add(objItem);

                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
