package nemosofts.live.tv.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import nemosofts.live.tv.Adapter.Adapter_New_tv;
import nemosofts.live.tv.JSONParser.JSONParser;
import nemosofts.live.tv.Listltem.Listltem;
import com.nemosofts.library.EndlessRecycler.EndlessRecyclerViewScrollListener;

import nemosofts.live.tv.SharedPref.NavigationUtil;
import nemosofts.live.tv.interfaces.InterAdListener;
import nemosofts.live.tv.interfaces.LatestListener;
import nemosofts.live.tv.asyncTask.LoadLatest;
import nemosofts.live.tv.Methods.Methods;
import nemosofts.live.tv.R;
import nemosofts.live.tv.SharedPref.Settings;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class CategoryByTvActivity extends AppCompatActivity {
    Methods methods;
    RecyclerView recyclerView;
    Adapter_New_tv adapter;
    ArrayList<Listltem> arrayList, arrayListTemp;
    Boolean isOver = false, isScroll = false;
    int page = 1;
    GridLayoutManager grid;
    LoadLatest load;
    FloatingActionButton fab;
    FrameLayout progressBar;
    private int nativeAdPos = 0;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getExtras().getString("name"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(Settings.isAdmobNativeAd) {
            nativeAdPos = Settings.admobNativeShow;
        } else if(Settings.isFBNativeAd) {
            nativeAdPos = Settings.fbNativeShow;
        }

        arrayList = new ArrayList<>();
        arrayListTemp = new ArrayList<>();
        progressBar = findViewById(R.id.load_video);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.tv);

        switch (Settings.grid){
            case 0: grid = new GridLayoutManager(CategoryByTvActivity.this, 2);
                grid.setSpanCount(2);
                break;
            case 1: grid = new GridLayoutManager(CategoryByTvActivity.this, 3);
                grid.setSpanCount(3);
                break;
            case 2: grid = new GridLayoutManager(CategoryByTvActivity.this, 4);
                grid.setSpanCount(4);
                break;
            default: grid = new GridLayoutManager(CategoryByTvActivity.this, 3);
                grid.setSpanCount(3);
        }

        grid.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (adapter.getItemViewType(position) >= 1000 || adapter.isHeader(position)) ? grid.getSpanCount() : 1;
            }
        });
        recyclerView.setLayoutManager(grid);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(grid) {
            @Override
            public void onLoadMore(int p, int totalItemsCount) {
                if (!isOver) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isScroll = true;
                            getData();
                        }
                    }, 0);
                } else {
                    adapter.hideHeader();
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = grid.findFirstVisibleItemPosition();

                if (firstVisibleItem > 6) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        if (JSONParser.isDataCheck()) {
            if (JSONParser.isNetworkCheck()) {
                getData();
            }
        }

        methods = new Methods(this, new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                NavigationUtil.Video(CategoryByTvActivity.this, arrayList.get(position).getVideo_type(), position, arrayList);
            }
        });


        LinearLayout adView = findViewById(R.id.adView);
        methods.showBannerAd(adView);
    }


    private void getData() {
        load = new LoadLatest(new LatestListener() {
            @Override
            public void onStart() {
                if(arrayList.size() == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onEnd(String success, String verifyStatus, String message, ArrayList<Listltem> arrayListTV) {
                if (success.equals("1")) {
                    if (!verifyStatus.equals("-1")) {
                        if (arrayListTV.size() == 0) {
                            isOver = true;
                            try {
                                adapter.hideHeader();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            arrayListTemp.addAll(arrayListTV);
                            for (int i = 0; i < arrayListTV.size(); i++) {
                                arrayList.add(arrayListTV.get(i));

                                if(Settings.isAdmobNativeAd || Settings.isFBNativeAd) {
                                    int abc = arrayList.lastIndexOf(null);
                                    if (((arrayList.size() - (abc + 1)) % nativeAdPos == 0) && (arrayListTV.size()-1 != i)) {
                                        arrayList.add(null);
                                    }
                                }
                            }

                            page = page + 1;
                            progressBar.setVisibility(View.INVISIBLE);
                            setAdapter();
                        }
                    } else {
                        methods.getVerifyDialog(getString(R.string.error_unauth_access), message);
                    }
                } else {

                }
                progressBar.setVisibility(View.GONE);

            }

        },methods.getAPIRequest(Settings.METHOD_TV_BY_CAT, page, "", "", "", "", getIntent().getExtras().getString("cid"), "", "", "","","","","","","","", null));
        load.execute();


    }

    public void setAdapter() {
        if (!isScroll) {

            adapter = new Adapter_New_tv(CategoryByTvActivity.this,  arrayList,  new Adapter_New_tv.RecyclerItemClickListener() {
                @Override
                public void onClickListener(Listltem listltem, int position) {
                    methods.showInter(position, "");
                }

            });

            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

}
