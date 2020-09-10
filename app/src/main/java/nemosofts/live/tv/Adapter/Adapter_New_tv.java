package nemosofts.live.tv.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import nemosofts.live.tv.Listltem.Listltem;
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

public class Adapter_New_tv extends RecyclerView.Adapter {

    private Methods methods;

    private ArrayList<Listltem> arrayList;
    private Context context;
    private RecyclerItemClickListener listener;
    private final int VIEW_PROG = -1;
    private int columnWidth = 0;
    private Boolean isAdLoaded = false;
    private NativeAdsManager mNativeAdsManager;
    private AdLoader adLoader = null;
    private List<UnifiedNativeAd> mNativeAdsAdmob = new ArrayList<>();
    private ArrayList<NativeAd> mNativeAdsFB = new ArrayList<>();

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public RoundedImageView image;
        private RelativeLayout pay;

        private MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.text);
            image = (RoundedImageView) view.findViewById(R.id.image);
            pay = view.findViewById(R.id.pay);
        }

        public void bind(final Listltem listltem, final RecyclerItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Settings.in_app){
                        if (Settings.getPurchases){
                            listener.onClickListener(listltem, getLayoutPosition());
                        }else {
                            String pay_name = listltem.getPay();
                            if (pay_name.equals("premium")){
                                methods.ShowDialog_pay();
                            }else {
                                listener.onClickListener(listltem, getLayoutPosition());
                            }
                        }
                    }else {
                        listener.onClickListener(listltem, getLayoutPosition());
                    }
                }
            });
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private static ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

    private static class ADViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_native_ad;

        private ADViewHolder(View view) {
            super(view);
            rl_native_ad = view.findViewById(R.id.rl_native_ad);
        }
    }

    public Adapter_New_tv(Context context, ArrayList<Listltem> arrayList, RecyclerItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
        if (Settings.getPurchases){
        }else {
           loadNativeAds();
        }

        methods = new Methods(context);

        switch (Settings.grid){
            case 0: columnWidth = methods.getColumnWidth(2, 0);
                break;
            case 1: columnWidth = methods.getColumnWidth(3, 0);
                break;
            case 2: columnWidth = methods.getColumnWidth(4, 0);
                break;
            default: columnWidth = methods.getColumnWidth(2, 0);
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_PROG) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progressbar, parent, false);
            return new ProgressViewHolder(v);
        } else if (viewType >= 1000) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ads, parent, false);
            return new ADViewHolder(itemView);
        } else {
            View itemView;
            switch (Settings.Album){
                case 0: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_card_normal, parent, false);
                    break;
                case 1: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_card_card, parent, false);
                    break;
                case 2: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_card_style, parent, false);
                    break;
                case 3: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_card_image, parent, false);
                    break;
                default: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_card_normal, parent, false);
            }
            return new MyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {

            final Listltem item = arrayList.get(position);

            ((MyViewHolder) holder).name.setText(item.getName());

            ((MyViewHolder) holder).image.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, columnWidth));

            int step = 1;
            int final_step = 1;
            for (int i = 1; i < position + 1; i++) {
                if (i == position + 1) {
                    final_step = step;
                }
                step++;
                if (step > 5) {
                    step = 1;
                }
            }

            String imageurl = methods.getImageThumbSize(item.getImageUrl());
            if(imageurl.equals("")) {
                imageurl = "null";
            }

            switch (step) {
                case 1:
                    Picasso.get()
                            .load(imageurl)
                            .placeholder(R.color.news1)
                            .into(((MyViewHolder) holder).image);
                    break;
                case 2:
                    Picasso.get()
                            .load(imageurl)
                            .placeholder(R.color.news2)
                            .into(((MyViewHolder) holder).image);
                    break;
                case 3:
                    Picasso.get()
                            .load(imageurl)
                            .placeholder(R.color.news3)
                            .into(((MyViewHolder) holder).image);
                    break;
                case 4:
                    Picasso.get()
                            .load(imageurl)
                            .placeholder(R.color.news4)
                            .into(((MyViewHolder) holder).image);
                    break;
                case 5:
                    Picasso.get()
                            .load(imageurl)
                            .placeholder(R.color.news5)
                            .into(((MyViewHolder) holder).image);
                    break;
            }

            if (Settings.in_app){
                if (Settings.getPurchases){
                    ((MyViewHolder) holder).pay.setVisibility(View.GONE);
                }else {
                    String pay_name = item.getPay();
                    if (pay_name.equals("premium")){
                        ((MyViewHolder) holder).pay.setVisibility(View.VISIBLE);
                    }else {
                        ((MyViewHolder) holder).pay.setVisibility(View.GONE);
                    }
                }
            }else {
                ((MyViewHolder) holder).pay.setVisibility(View.GONE);
            }



            ((MyViewHolder) holder).bind(item, listener);

        } else if (holder instanceof ADViewHolder) {
            if (isAdLoaded) {
                if (((ADViewHolder) holder).rl_native_ad.getChildCount() == 0) {
                    if (Settings.isAdmobNativeAd) {
                        if (mNativeAdsAdmob.size() >= 5) {

                            int i = new Random().nextInt(mNativeAdsAdmob.size() - 1);

//                            CardView cardView = (CardView) ((Activity) context).getLayoutInflater().inflate(R.layout.layout_native_ad_admob, null);

                            UnifiedNativeAdView adView = (UnifiedNativeAdView) ((Activity) context).getLayoutInflater().inflate(R.layout.layout_native_ad_admob, null);
                            populateUnifiedNativeAdView(mNativeAdsAdmob.get(i), adView);
                            ((ADViewHolder) holder).rl_native_ad.removeAllViews();
                            ((ADViewHolder) holder).rl_native_ad.addView(adView);

                            ((ADViewHolder) holder).rl_native_ad.setVisibility(View.VISIBLE);
                        }
                    } else {

                        LinearLayout ll_fb_native = (LinearLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.layout_native_ad_fb, null);

                        com.facebook.ads.MediaView mvAdMedia, ivAdIcon;
                        TextView tvAdTitle;
                        TextView tvAdBody;
                        TextView tvAdSocialContext;
                        TextView tvAdSponsoredLabel;
                        Button btnAdCallToAction;
                        LinearLayout adChoicesContainer, ll_main;

                        mvAdMedia = ll_fb_native.findViewById(R.id.native_ad_media);
                        tvAdTitle = ll_fb_native.findViewById(R.id.native_ad_title);
                        tvAdBody = ll_fb_native.findViewById(R.id.native_ad_body);
                        tvAdSocialContext = ll_fb_native.findViewById(R.id.native_ad_social_context);
                        tvAdSponsoredLabel = ll_fb_native.findViewById(R.id.native_ad_sponsored_label);
                        btnAdCallToAction = ll_fb_native.findViewById(R.id.native_ad_call_to_action);
                        ivAdIcon = ll_fb_native.findViewById(R.id.native_ad_icon);
                        adChoicesContainer = ll_fb_native.findViewById(R.id.ad_choices_container);
                        ll_main = ll_fb_native.findViewById(R.id.ad_unit);


                        NativeAd ad;

                        if (mNativeAdsFB.size() >= 5) {
                            ad = mNativeAdsFB.get(new Random().nextInt(5));
                        } else {
                            ad = mNativeAdsManager.nextNativeAd();
                            mNativeAdsFB.add(ad);
                        }

                        ADViewHolder adHolder = (ADViewHolder) holder;

                        if (ad != null) {

                            tvAdTitle.setText(ad.getAdvertiserName());
                            tvAdBody.setText(ad.getAdBodyText());
                            tvAdSocialContext.setText(ad.getAdSocialContext());
                            tvAdSponsoredLabel.setText(ad.getSponsoredTranslation());
                            btnAdCallToAction.setText(ad.getAdCallToAction());
                            btnAdCallToAction.setVisibility(
                                    ad.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                            //AdChoicesView adChoicesView = new AdChoicesView(context, ad, true);
                            //adChoicesContainer.addView(adChoicesView, 0);

                            ArrayList<View> clickableViews = new ArrayList<>();
                            clickableViews.add(ivAdIcon);
                            clickableViews.add(mvAdMedia);
                            clickableViews.add(btnAdCallToAction);
                            ad.registerViewForInteraction(
                                    adHolder.itemView,
                                    mvAdMedia,
                                    ivAdIcon,
                                    clickableViews);

                            ((ADViewHolder) holder).rl_native_ad.addView(ll_fb_native);
                        }
                    }
                }
            }
        } else {
            if (getItemCount() == 1) {
                ProgressViewHolder.progressBar.setVisibility(View.GONE);
            }
        }
    }

    public String format(Number number) {
        char[] arrc = new char[]{' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long l = number.longValue();
        double d = l;
        int n = (int)Math.floor((double)Math.log10((double)d));
        int n2 = n / 3;
        if (n >= 3 && n2 < arrc.length) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(new DecimalFormat("#0.0").format(d / Math.pow((double)10.0, (double)(n2 * 3))));
            stringBuilder.append(arrc[n2]);
            return stringBuilder.toString();
        }
        return new DecimalFormat("#,##0").format(l);
    }

    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void hideHeader() {
        try {
            ProgressViewHolder.progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isHeader(int position) {
        return position == arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return VIEW_PROG;
        } else if (arrayList.get(position) == null) {
            return 1000 + position;
        } else {
            return position;
        }
    }


    public interface RecyclerItemClickListener{
        void onClickListener(Listltem listltem, int position);
    }


    private void loadNativeAds() {
        if (Settings.isAdmobNativeAd) {
            AdLoader.Builder builder = new AdLoader.Builder(context, Settings.ad_native_id);
            adLoader = builder.forUnifiedNativeAd(
                    new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            // A native ad loaded successfully, check if the ad loader has finished loading
                            // and if so, insert the ads into the list.
                            mNativeAdsAdmob.add(unifiedNativeAd);
                            isAdLoaded = true;
                        }
                    }).withAdListener(
                    new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {

                        }
                    }).build();

            // Load the Native Express ad.
            adLoader.loadAds(new AdRequest.Builder().build(), 5);
        } else if (Settings.isFBNativeAd) {
            mNativeAdsManager = new NativeAdsManager(context, Settings.fb_ad_native_id, 5);
            mNativeAdsManager.setListener(new NativeAdsManager.Listener() {
                @Override
                public void onAdsLoaded() {
                    isAdLoaded = true;
                }

                @Override
                public void onAdError(AdError adError) {

                }
            });
            mNativeAdsManager.loadAds();
        }
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);
    }

    public void destroyNativeAds() {
        try {
            for (int i = 0; i < mNativeAdsAdmob.size(); i++) {
                mNativeAdsAdmob.get(i).destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}