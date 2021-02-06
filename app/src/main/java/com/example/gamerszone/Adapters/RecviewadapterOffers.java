package com.example.gamerszone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamerszone.R;

import java.util.ArrayList;

public class RecviewadapterOffers extends RecyclerView.Adapter<RecviewadapterOffers.MyViewHolderS> {

    Context mContext ;
    ArrayList<String> mData;
    private ArrayList<String> GamesList;

    public RecviewadapterOffers(Context mContext, ArrayList<String> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolderS onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v ;
        v= LayoutInflater.from(mContext).inflate(R.layout.cardview_gamesrec,parent,false);
        MyViewHolderS VHolder = new MyViewHolderS(v);

        return VHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderS holder, int position) {
        //holder.pic.setImageResource(mData.get(position).getImage());
        holder.pic.setImageResource(getImageId(mContext, mData.get(position)));
      //  holder.pic.setImageResource(R.drawable.pubg);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolderS extends RecyclerView.ViewHolder{

        ImageView pic ;

        public MyViewHolderS(@NonNull View itemView) {
            super(itemView);

            pic = (ImageView)itemView.findViewById(R.id.img_game);
        }
    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }
}
