package nemosofts.live.tv.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nemosofts.live.tv.DBHelper.DBHelper;
import nemosofts.live.tv.Listltem.Listltem;
import nemosofts.live.tv.Methods.Methods;
import nemosofts.live.tv.R;
import nemosofts.live.tv.SharedPref.Settings;
import nemosofts.live.tv.interfaces.RecyclerViewClickListener;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class Adapter_tv_Favourite extends RecyclerView.Adapter {
    private DBHelper dbHelper;
    private ArrayList<Listltem> arrayList;
    private Context context;
    private Methods methods;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int columnWidth = 0;
    private RecyclerViewClickListener recyclerViewClickListener;


    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public RoundedImageView image;
        private RelativeLayout pay;

        private MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.text);
            image = view.findViewById(R.id.image);
            pay = view.findViewById(R.id.pay);
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private static ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        }
    }

    public Adapter_tv_Favourite(Context context, ArrayList<Listltem> arrayList,  RecyclerViewClickListener recyclerViewClickListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
        dbHelper = new DBHelper(context);
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
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
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progressbar, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {

            final Listltem item = arrayList.get(position);

            ((MyViewHolder) holder).name.setText(arrayList.get(position).getName());

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

            ((MyViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Settings.in_app){
                        if (Settings.getPurchases){
                            recyclerViewClickListener.onClick(holder.getAdapterPosition());
                        }else {
                            String pay_name = item.getPay();
                            if (pay_name.equals("premium")){
                                methods.ShowDialog_pay();
                            }else {
                                recyclerViewClickListener.onClick(holder.getAdapterPosition());
                            }
                        }
                    }else {
                        recyclerViewClickListener.onClick(holder.getAdapterPosition());
                    }
                }
            });


        } else {
            if (getItemCount() == 1) {
                ProgressViewHolder.progressBar.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Boolean checkFav(int pos) {
        return dbHelper.checkFav(arrayList.get(pos).getId());
    }

    public void hideHeader() {
        ProgressViewHolder.progressBar.setVisibility(View.GONE);
    }

    public boolean isHeader(int position) {
        return position == arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? VIEW_PROG : VIEW_ITEM;
    }
}