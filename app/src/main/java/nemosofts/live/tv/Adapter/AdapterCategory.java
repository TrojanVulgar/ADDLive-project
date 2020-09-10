package nemosofts.live.tv.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nemosofts.live.tv.Activity.CategoryByTvActivity;
import nemosofts.live.tv.Listltem.Listltem_Category;
import nemosofts.live.tv.Methods.Methods;
import nemosofts.live.tv.R;
import nemosofts.live.tv.interfaces.RecyclerViewClickListener;
import nemosofts.live.tv.interfaces.InterAdListener;


/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class AdapterCategory extends RecyclerView.Adapter {

    private ArrayList<Listltem_Category> arrayList;
    private Context context;
    private RecyclerViewClickListener recyclerViewClickListener;
    private Methods methods;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;
        public LinearLayout clek;


        private MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.text);
            image = (ImageView) view.findViewById(R.id.image);

            clek = (LinearLayout) view.findViewById(R.id.clek);


            methods = new Methods(context, new InterAdListener() {
                @Override
                public void onClick(int position, String type) {
                    Intent intent = new Intent(context, CategoryByTvActivity.class);
                    intent.putExtra("name", arrayList.get(position).getCategory_name());
                    intent.putExtra("cid", arrayList.get(position).getCid());
                    intent.putExtra("image", arrayList.get(position).getCategory_image());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            //on item click
            clek.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        methods.showInter(position, "");
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

    public AdapterCategory(Context context, ArrayList<Listltem_Category> arrayList, RecyclerViewClickListener recyclerViewClickListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
        methods = new Methods(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_card, parent, false);
            return new MyViewHolder(itemView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progressbar, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {

            ((MyViewHolder) holder).name.setText(arrayList.get(position).getCategory_name());

            //load album cover using picasso
            Picasso.get()
                    .load(arrayList.get(position).getCategory_image())
                    .placeholder(R.color.news1)
                    .into(((MyViewHolder) holder).image);

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