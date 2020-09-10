package nemosofts.live.tv.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nemosofts.library.EndlessRecycler.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;

import nemosofts.live.tv.Adapter.Adapter_tv;
import nemosofts.live.tv.JSONParser.JSONParser;
import nemosofts.live.tv.Listltem.Listltem;
import nemosofts.live.tv.Methods.Methods;
import nemosofts.live.tv.R;
import nemosofts.live.tv.SharedPref.NavigationUtil;
import nemosofts.live.tv.SharedPref.Settings;
import nemosofts.live.tv.interfaces.InterAdListener;
import nemosofts.live.tv.interfaces.RecyclerViewClickListener;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class Banner_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter_tv adapter;
    ArrayList<Listltem> arrayList;
    Methods methods;

    Boolean isOver = false, isScroll = false;

    GridLayoutManager grid;

    FrameLayout progressBar;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Settings.Dark_Mode ) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        methods = new Methods(this);

        methods = new Methods(this, new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                NavigationUtil.Video(Banner_Activity.this, arrayList.get(position).getVideo_type(), position, arrayList);
            }
        });

        position = getIntent().getIntExtra("pos", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(Settings.arrayList2.get(position).getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

        progressBar = findViewById(R.id.load_video);

        recyclerView = findViewById(R.id.tv);
        recyclerView.setHasFixedSize(true);
        switch (Settings.grid){
            case 0: grid = new GridLayoutManager(Banner_Activity.this, 2);
                break;
            case 1: grid = new GridLayoutManager(Banner_Activity.this, 3);
                break;
            case 2: grid = new GridLayoutManager(Banner_Activity.this, 4);
                break;
            default: grid = new GridLayoutManager(Banner_Activity.this, 3);
        }
        grid.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? grid.getSpanCount() : 1;
            }
        });
        recyclerView.setLayoutManager(grid);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(grid) {
            @Override
            public void onLoadMore(int p, int totalItemsCount) {
                if(!isOver) {
                    isOver = true;
                    try {
                        adapter.hideHeader();
                    }catch (Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });


        if (JSONParser.isDataCheck()) {
            if (JSONParser.isNetworkCheck()) {
                getData();
            }
        }

        LinearLayout adView = findViewById(R.id.adView);
        methods.showBannerAd(adView);
    }



    private void getData() {
        arrayList = (ArrayList<Listltem>)Settings.arrayList2.get(position).getArrayListSongs();
        setAdapter();
        progressBar.setVisibility(View.GONE);
    }

    public void setAdapter() {
        if(!isScroll) {
            adapter = new Adapter_tv(Banner_Activity.this, arrayList , new RecyclerViewClickListener(){
                @Override
                public void onClick(int position) {
                    methods.showInter(position, "");
                }
            });
            recyclerView.setAdapter(adapter);

        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
