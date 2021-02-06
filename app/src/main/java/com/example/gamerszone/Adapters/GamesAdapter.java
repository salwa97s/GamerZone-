
package com.example.gamerszone.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamerszone.Models.Games;
import com.example.gamerszone.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.MyViewHolderS> {

    public interface ChnageStatusListener{
        void onItemChangeListener(int position, Games model);
    }

    Context mContext ;
    ArrayList<Games> mData;
    boolean f=false ;
    ChnageStatusListener chnageStatusListener;
    ArrayList<String> SelectedGames ;

    public GamesAdapter(Context mContext, ArrayList<Games>  mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolderS onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v ;
        v= LayoutInflater.from(mContext).inflate(R.layout.cardview_games,parent,false);
        MyViewHolderS VHolder = new MyViewHolderS(v);



        return VHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderS viewHolder,final int i) {
        Games model=mData.get(i);
        if(model!=null){
            viewHolder.position=i;
            viewHolder.pic.setImageResource(model.getImage());
            if(model.isSelected()){
                viewHolder.pic.setBackgroundColor(mContext.getResources().getColor(R.color.selected));
            }
            else{
                viewHolder.pic.setBackgroundColor(mContext.getResources().getColor(R.color.notselected));
            }
        }
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Games model1=mData.get(i);
                if(model1.isSelected()){
                    model1.setSelected(false);
                    //Toast.makeText(mContext, "i dont want "+model1.getName(),Toast.LENGTH_LONG).show();
                    LoadData();
                    removeitem(model1.getCode());
                    SaveData();

                }else{
                    model1.setSelected(true);
                   // Toast.makeText(mContext, "i took "+model1.getName(), Toast.LENGTH_SHORT).show();
                    LoadData();
                    additem(model1.getCode());
                    SaveData();
                }
                mData.set(viewHolder.position,model1);
                if(chnageStatusListener!=null){
                    chnageStatusListener.onItemChangeListener(viewHolder.position,model1);
                }
                notifyItemChanged(viewHolder.position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolderS extends RecyclerView.ViewHolder{

        ImageView pic ;
        private RelativeLayout CategoryCV ;
        public int position;
        public View view;


        public MyViewHolderS(@NonNull View itemView) {
            super(itemView);
            view = itemView ;
            pic = (ImageView)itemView.findViewById(R.id.img_game);
            CategoryCV = (RelativeLayout) itemView.findViewById(R.id.CVGame);
        }
    }

    public void SaveData(){
        SharedPreferences sp  = mContext.getSharedPreferences("GAMES" ,Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(SelectedGames);
        editor.putString("GamesArray",json);
        editor.apply();
    }

    public void LoadData(){
        SharedPreferences sp2 = mContext.getSharedPreferences("GAMES" , Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp2.getString("GamesArray" , null);
        Type typeT = new TypeToken<ArrayList<String>>() {}.getType();
        SelectedGames = gson.fromJson(json , typeT );
        if(SelectedGames == null){
            SelectedGames = new ArrayList<>();
        }

    }

    public void additem(String s){
        SelectedGames.add(s);
    }

    public void removeitem(String s){
        SelectedGames.remove(s);
    }
}

