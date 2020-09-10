package nemosofts.live.tv.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nemosofts.library.EndlessRecycler.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;

import nemosofts.live.tv.Adapter.Adapter_tv;
import nemosofts.live.tv.JSONParser.JSONParser;
import nemosofts.live.tv.Listltem.Listltem;
import nemosofts.live.tv.SharedPref.NavigationUtil;
import nemosofts.live.tv.asyncTask.LoadLatest;
import nemosofts.live.tv.Methods.Methods;
import nemosofts.live.tv.R;
import nemosofts.live.tv.SharedPref.Settings;
import nemosofts.live.tv.interfaces.InterAdListener;
import nemosofts.live.tv.interfaces.LatestListener;
import nemosofts.live.tv.interfaces.RecyclerViewClickListener;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter_tv adapter;
    ArrayList<Listltem> arrayList;
    Methods methods;
    Boolean isOver = false, isScroll = false;
    GridLayoutManager grid;
    LoadLatest loadWallpaper;
    FrameLayout progressBar;
    EditText searchView;
    ImageView search;
    Toolbar toolbar;
    String text ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Settings.Dark_Mode) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        methods = new Methods(this);

        methods = new Methods(this, new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                NavigationUtil.Video(SearchActivity.this, arrayList.get(position).getVideo_type(), position, arrayList);
            }
        });

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        setTitle("Search");

        text = getIntent().getExtras().getString("text");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchView = findViewById(R.id.search_view);
        searchView.setText(text);
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        arrayList = new ArrayList<>();
        progressBar = findViewById(R.id.load_video);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        switch (Settings.grid){
            case 0: grid = new GridLayoutManager(SearchActivity.this, 2);
                break;
            case 1: grid = new GridLayoutManager(SearchActivity.this, 3);
                break;
            case 2: grid = new GridLayoutManager(SearchActivity.this, 4);
                break;
            default: grid = new GridLayoutManager(SearchActivity.this, 3);
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
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isScroll = false;
                            try {
                                adapter.hideHeader();
                            }catch (Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                e.printStackTrace();
                            }
                        }
                    }, 0);
                } else {
                    adapter.hideHeader();
                }
            }
        });

        if (JSONParser.isDataCheck()) {
            if (JSONParser.isNetworkCheck()) {
                getData();
            }
        }

    }


    private void getData() {
        loadWallpaper = new LoadLatest(new LatestListener() {
            @Override
            public void onStart() {
                arrayList.clear();
                progressBar.setVisibility(View.VISIBLE);
                text = searchView.getText().toString().replace(" ", "%20");
            }

            @Override
            public void onEnd(String success, String verifyStatus, String message, ArrayList<Listltem> arrayListTV) {
                if(arrayListTV.size() == 0) {
                    isOver = true;
                    try {
                        adapter.hideHeader();
                    }catch (Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                    }
                } else {
                    arrayList.addAll(arrayListTV);
                    progressBar.setVisibility(View.INVISIBLE);
                    setAdapter();
                }
            }


        },methods.getAPIRequest(Settings.METHOD_SEARCH, 0, "", "", text, "", "", "", "", "","","","","","","","", null));
        loadWallpaper.execute();
    }

    public void setAdapter() {
        if(!isScroll) {
            adapter = new Adapter_tv(SearchActivity.this, arrayList , new RecyclerViewClickListener(){
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

