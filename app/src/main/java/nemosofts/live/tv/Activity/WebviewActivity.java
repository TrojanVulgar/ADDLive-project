package nemosofts.live.tv.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import nemosofts.live.tv.DBHelper.DBHelper;
import nemosofts.live.tv.JSONParser.JSONParser;
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

public class WebviewActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    Methods methods;
    private String video_url;
    WebView mWebView;
    ProgressDialog pd;
    public Boolean load = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Settings.Dark_Mode ) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        pd = new ProgressDialog(WebviewActivity.this);
        pd.setMessage("loading");

        dbHelper = new DBHelper(this);
        methods = new Methods(this);

        video_url = Settings.arrayList.get(Settings.position).getUrl();

        LinearLayout adView = findViewById(R.id.adView);
        methods.showBannerAd(adView);

        mWebView = findViewById(R.id.player_view);
        mWebView.setWebChromeClient(new MyChrome());
        mWebView.setWebViewClient(new Browser_home());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        loadWebsite();

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {

                String json = JSONParser.okhttpPost(Settings.SERVER_URL, methods.getAPIRequest(Settings.METHOD_TV, 0, "", Settings.arrayList.get(Settings.position).getId(), "", "", "", "", "", "","","","","","","","", null));
                return null;
            }
        }.execute();
    }


    private void loadWebsite() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            mWebView.setBackgroundColor(Color.BLACK);
            mWebView.setBackgroundResource(R.color.black);
            mWebView.loadUrl(video_url);
        } else {
            mWebView.setVisibility(View.GONE);
        }
    }

    class Browser_home extends WebViewClient {

        Browser_home() {
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            setTitle(view.getTitle());
            super.onPageFinished(view, url);

        }
    }

    private class MyChrome extends WebChromeClient {

        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyChrome() {}

        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (load){
                if (progress == 100) {
                    load = false;
                    pd.hide();
                } else {
                    pd.show();
                }
            }else {
                load = false;
            }

        }
        /**
         * The view to be displayed while the fullscreen VideoView is buffering
         * @return the progress view
         */
        @Override
        public View getVideoLoadingProgressView() {
            ProgressBar pb = new ProgressBar(WebviewActivity.this);
            pb.setIndeterminate(true);
            return pb;
        }

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }

}
