package nemosofts.live.tv.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

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

public class Home_TV_Adapter extends RecyclerView.Adapter<Home_TV_Adapter.ViewHolder> {
    private Methods methods;
    private List<Listltem> arrayList;
    private Context context;
    private RecyclerViewClickListener recyclerViewClickListener;

    public Home_TV_Adapter(Context context, List<Listltem> listltem_video, RecyclerViewClickListener recyclerViewClickListener) {
        this.arrayList = listltem_video;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
        methods = new Methods(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tv_card_home,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Listltem item = arrayList.get(position);

        holder.name.setText(item.getName());


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
                        .into(holder.image);
                break;
            case 2:
                Picasso.get()
                        .load(imageurl)
                        .placeholder(R.color.news2)
                        .into(holder.image);
                break;
            case 3:
                Picasso.get()
                        .load(imageurl)
                        .placeholder(R.color.news3)
                        .into(holder.image);
                break;
            case 4:
                Picasso.get()
                        .load(imageurl)
                        .placeholder(R.color.news4)
                        .into(holder.image);
                break;
            case 5:
                Picasso.get()
                        .load(imageurl)
                        .placeholder(R.color.news5)
                        .into(holder.image);
                break;
        }

        if (Settings.in_app){
            if (Settings.getPurchases){
                holder.pay.setVisibility(View.GONE);
            }else {
                String pay_name = item.getPay();
                if (pay_name.equals("premium")){
                    holder.pay.setVisibility(View.VISIBLE);
                }else {
                   holder.pay.setVisibility(View.GONE);
                }
            }
        }else {
             holder.pay.setVisibility(View.GONE);
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public RoundedImageView image;
        private RelativeLayout pay;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.text);
            image = (RoundedImageView) itemView.findViewById(R.id.image);
            pay = itemView.findViewById(R.id.pay);
        }
    }
}
