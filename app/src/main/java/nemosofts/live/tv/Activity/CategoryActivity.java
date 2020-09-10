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

import java.util.ArrayList;

import nemosofts.live.tv.Adapter.AdapterCategory;
import nemosofts.live.tv.JSONParser.JSONParser;
import nemosofts.live.tv.Listltem.Listltem_Category;
import nemosofts.live.tv.interfaces.CategoryListener;
import com.nemosofts.library.EndlessRecycler.EndlessRecyclerViewScrollListener;
import nemosofts.live.tv.asyncTask.LoadCategory;
import nemosofts.live.tv.Methods.Methods;
import nemosofts.live.tv.R;
import nemosofts.live.tv.interfaces.RecyclerViewClickListener;
import nemosofts.live.tv.SharedPref.Settings;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class CategoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterCategory adapter;
    ArrayList<Listltem_Category> arrayList;
    Toolbar toolbar;
    Methods methods;
    Boolean isOver = false, isScroll = false;

    int page = 1;
    GridLayoutManager grid;

    LoadCategory loadCategory;

    FrameLayout progressBar;

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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Category");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        arrayList = new ArrayList<>();
        progressBar = findViewById(R.id.load_video);
        
        recyclerView = findViewById(R.id.tv);
        recyclerView.setHasFixedSize(true);
        grid = new GridLayoutManager(CategoryActivity.this, 2);
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
                            isScroll = true;
                            getData();
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

        LinearLayout adView = findViewById(R.id.adView);
        methods.showBannerAd(adView);
    }

    private void getData() {
        loadCategory = new LoadCategory(new CategoryListener(){
            @Override
            public void onStart() {
                if(arrayList.size() == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onEnd(String success, ArrayList<Listltem_Category> arrayListWall) {
                if(arrayListWall.size() == 0) {
                    isOver = true;
                    try {
                        adapter.hideHeader();
                    }catch (Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                    }
                } else {
                    page = page + 1;
                    arrayList.addAll(arrayListWall);
                    progressBar.setVisibility(View.INVISIBLE);
                    setAdapter();
                }
            }
        },methods.getAPIRequest(Settings.METHOD_CAT, page, "", "", "", "", "", "", "", "","","","","","","","", null));
        loadCategory.execute();
    }

    public void setAdapter() {
        if(!isScroll) {
            adapter = new AdapterCategory(CategoryActivity.this, arrayList , new RecyclerViewClickListener(){
                @Override
                public void onClick(int position) {

                }
            });
            recyclerView.setAdapter(adapter);

        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
