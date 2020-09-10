package nemosofts.live.tv.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import nemosofts.live.tv.DBHelper.DBHelper;
import nemosofts.live.tv.Methods.Methods;
import nemosofts.live.tv.R;
import nemosofts.live.tv.SharedPref.Settings;

import static android.view.View.VISIBLE;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class ExoPlayerActivity extends AppCompatActivity{

    private DBHelper dbHelper;
    Methods methods;

    private PlayerView exoPlayerView;
    private ExoPlayer player;
    private MediaSource mediaSource;
    private TextView liveTvTextInController;
    private LinearLayout seekBarLayout;
    int RESIZE_MODE = 0;
    private boolean isPlaying;
    private ImageView imageView, imageView_resize_mode;
    private int visible;
    private boolean isFullScreen = false;

    private String video_url, type;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Settings.Dark_Mode) {
            setAppTheme(R.style.AppTheme2);
        } else {
            setAppTheme(R.style.AppTheme);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer);

        dbHelper = new DBHelper(this);
        methods = new Methods(this);

        video_url = Settings.arrayList.get(Settings.position).getUrl();
        type = Settings.arrayList.get(Settings.position).getVideo_type();

        intiViews();
        initVideoPlayer(video_url, type);
    }

    private void intiViews() {
        LinearLayout adView = findViewById(R.id.adView);
        methods.showBannerAd(adView);

        exoPlayerView = findViewById(R.id.player_view);
        imageView = findViewById(R.id.imageView_full_video_play);
        imageView_resize_mode= findViewById(R.id.imageView_resize_mode);
        seekBarLayout = findViewById(R.id.seekbar_layout);
        liveTvTextInController = findViewById(R.id.live_tv);

        progressBar = findViewById(R.id.progresbar_video_play);
        progressBar.setVisibility(View.VISIBLE);


        if (type.equals("Online")) {
            seekBarLayout.setVisibility(View.GONE);
            liveTvTextInController.setVisibility(View.VISIBLE);
        }else if (type.equals("youtube_live")) {
            seekBarLayout.setVisibility(View.GONE);
            liveTvTextInController.setVisibility(View.VISIBLE);
        }else {
            seekBarLayout.setVisibility(VISIBLE);
            liveTvTextInController.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        imageView_resize_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RESIZE_MODE == 0){
                    exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
                    RESIZE_MODE = 1;

                }else if (RESIZE_MODE == 1){
                    exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
                    RESIZE_MODE = 2;

                }else if (RESIZE_MODE == 2){
                    exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                    RESIZE_MODE = 3;

                }else if (RESIZE_MODE == 3){
                    exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                    RESIZE_MODE = 4;

                }else if (RESIZE_MODE == 4){
                    exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                    RESIZE_MODE = 0;
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFullScreen) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(ExoPlayerActivity.this, R.drawable.exo_controls_fullscreen_enter));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) exoPlayerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) ( 200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    exoPlayerView.setLayoutParams(params);
                    isFullScreen = false;
                }else{
                    imageView.setImageDrawable(ContextCompat.getDrawable(ExoPlayerActivity.this, R.drawable.exo_controls_fullscreen_exit));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) exoPlayerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    exoPlayerView.setLayoutParams(params);
                    isFullScreen = true;
                }
            }
        });
    }

    public void initVideoPlayer(String url, String type) {
        if (player != null) {
            player.release();
        }
        progressBar.setVisibility(VISIBLE);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new
                AdaptiveTrackSelection.Factory(bandwidthMeter);

        TrackSelector trackSelector = new
                DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance((Context) ExoPlayerActivity.this, trackSelector);
        player.setPlayWhenReady(true);
        exoPlayerView.setPlayer(player);

        Uri uri = Uri.parse(url);

        if (type.equals("Online")) {
            mediaSource = hlsMediaSource(uri, ExoPlayerActivity.this);
        }else if (type.equals("youtube")) {
            extractYoutubeUrl(url, ExoPlayerActivity.this, 18);
        } else if (type.equals("youtube_live")) {
            Log.e("youtube url  :: ", url);
            extractYoutubeUrl(url, ExoPlayerActivity.this, 133);
        } else if (type.equals("rtmp")) {
            mediaSource = rtmpMediaSource(uri);
        } else if (type.equals("mp3")) {
            mediaSource = mp3MediaSource(uri);
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getApplicationContext());
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
            player.prepare(mediaSource);
        } else {
            mediaSource = mediaSource(uri, ExoPlayerActivity.this);
        }

        player.prepare(mediaSource, true, false);

        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady && playbackState == Player.STATE_READY) {
                    isPlaying = true;
                    progressBar.setVisibility(View.GONE);
                } else if (playbackState == Player.STATE_READY) {
                    isPlaying = false;
                    progressBar.setVisibility(View.GONE);
                } else if (playbackState == Player.STATE_BUFFERING) {
                    isPlaying = false;
                    progressBar.setVisibility(VISIBLE);
                    player.setPlayWhenReady(true);
                } else {
                    // player paused in any state
                    isPlaying = false;
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        exoPlayerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                visible = visibility;
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void extractYoutubeUrl(String url, final Context context, final int tag) {
        new YouTubeExtractor(context) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles != null) {
                    int itag = tag;
                    String downloadUrl = ytFiles.get(itag).getUrl();
                    player.setPlayWhenReady(false);
                    try {
                        MediaSource mediaSource = mediaSource(Uri.parse(downloadUrl), context);
                        player.prepare(mediaSource, true, false);
                        player.setPlayWhenReady(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.extract(url, true, true);
    }

    private MediaSource mp3MediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), "ExoplayerDemo");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        Handler mainHandler = new Handler();
        return new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, mainHandler, null);
    }

    private MediaSource mediaSource(Uri uri, Context context) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer")).
                createMediaSource(uri);
    }

    private MediaSource hlsMediaSource(Uri uri, Context context) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "livetv"), bandwidthMeter);
        MediaSource videoSource = new HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        return videoSource;
    }

    private MediaSource rtmpMediaSource(Uri uri) {
        MediaSource videoSource = null;
        RtmpDataSourceFactory dataSourceFactory = new RtmpDataSourceFactory();
        videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        return videoSource;
    }

    @Override
    public void onBackPressed() {
        if (visible == View.GONE) {
            exoPlayerView.showController();
        } else {
            releasePlayer();
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void releasePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.stop();
            player.release();
            player = null;
            exoPlayerView.setPlayer(null);
        }
    }

    private void setAppTheme(@StyleRes int style) {
        setTheme(style);
    }
}
