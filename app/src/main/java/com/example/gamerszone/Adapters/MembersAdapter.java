package com.example.gamerszone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamerszone.Activities.MessageActivity;
import com.example.gamerszone.Models.User;
import com.example.gamerszone.R;

import java.util.ArrayList;
import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private ArrayList<String> GamesList;

    public MembersAdapter(Context mContext, List<User> mUsers){
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cardview_members, parent, false);
        return new MembersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")){
            holder.profile_pic.setImageResource(R.drawable.pixel);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_pic);
        }

        GamesList = user.getList();
        if(GamesList == null){
            GamesList = new ArrayList<>();
        }
        if(GamesList.size()==1){
            holder.img1.setImageResource(getImageId(mContext, GamesList.get(0)));
        }
        if(GamesList.size()==2){
            holder.img1.setImageResource(getImageId(mContext, GamesList.get(0)));
            holder.img2.setVisibility(View.VISIBLE);
            holder.img2.setImageResource(getImageId(mContext, GamesList.get(1)));
        }
        if(GamesList.size()==3){
            holder.img1.setImageResource(getImageId(mContext, GamesList.get(0)));
            holder.img2.setVisibility(View.VISIBLE);
            holder.img2.setImageResource(getImageId(mContext, GamesList.get(1)));
            holder.img3.setVisibility(View.VISIBLE);
            holder.img3.setImageResource(getImageId(mContext, GamesList.get(2)));
        }
        if(GamesList.size()>3){
            holder.img1.setImageResource(getImageId(mContext, GamesList.get(0)));
            holder.img2.setVisibility(View.VISIBLE);
            holder.img2.setImageResource(getImageId(mContext, GamesList.get(1)));
            holder.img3.setVisibility(View.VISIBLE);
            holder.img3.setImageResource(getImageId(mContext, GamesList.get(2)));
            holder.img4.setVisibility(View.VISIBLE);
        }
        switch (user.getPlatform()){
            case "PS4":
                holder.platform.setImageResource(R.drawable.ic_psfour);
                break;
            case"XBOX":
                holder.platform.setImageResource(R.drawable.ic_xbox);
                break;
            case "PC":
                holder.platform.setImageResource(R.drawable.ic_pc);
                break;
            case "NINTENDO":
                holder.platform.setImageResource(R.drawable.ic_nin);
                break;
            case "MOBILE":
                holder.platform.setImageResource(R.drawable.ic_mobile);
                break;
        }

        //the whole cardview whenever i click on it
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username ;
        public ImageView profile_pic , img1 ,img2 , img3 , img4 , platform;
        private TextView last_msg;

        public ViewHolder(View item){
            super(item);
            username = item.findViewById(R.id.username);
            profile_pic = item.findViewById(R.id.profile_image);
            platform = item.findViewById(R.id.member_platform_id);
            img1=item.findViewById(R.id.img1);
            img2=item.findViewById(R.id.img2);
            img3=item.findViewById(R.id.img3);
            img4=item.findViewById(R.id.img4);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

}
