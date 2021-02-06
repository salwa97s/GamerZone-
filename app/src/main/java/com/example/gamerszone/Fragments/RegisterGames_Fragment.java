package com.example.gamerszone.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamerszone.Adapters.GamesAdapter;
import com.example.gamerszone.Models.Games;
import com.example.gamerszone.Models.User;
import com.example.gamerszone.R;
import com.example.gamerszone.Activities.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class RegisterGames_Fragment extends Fragment {

    ArrayList<com.example.gamerszone.Models.Games> Games ;
    private RecyclerView MyRecViewEvents;

    FirebaseUser fuser;
    DatabaseReference reference;
    String PlatformType ;

    Button Done_btn ;
    ArrayList<String> GamesList ;


    public static final String SHARED_PREFS = "PLATFORM" ;
    public static final String NAME = "PL";

    View v ;

    public RegisterGames_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_register_games_, container, false);

        Done_btn = v.findViewById(R.id.button_done_id);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                PlatformType = user.getPlatform();
                Switcher(PlatformType);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        GamesList=new ArrayList<>();

        Done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // im tiredddddd
                ok();
                saveplatform(PlatformType);
                RegisterActivity mainActivity = (RegisterActivity) getActivity();
                mainActivity.Step4();
            }
        });


        return v ;
    }

    private void ok() {

        LoadData();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("list", GamesList);
        reference.updateChildren(hashMap);
    }
    public void LoadData(){
        SharedPreferences sp2 = getActivity().getSharedPreferences("GAMES" , Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp2.getString("GamesArray" , null);
        Type typeT = new TypeToken<ArrayList<String>>() {}.getType();
        GamesList = gson.fromJson(json , typeT );
        if(GamesList == null){
            GamesList = new ArrayList<>();
        }

    }

    private void Switcher(String PlatformT){

        switch (PlatformT){
            case "PC": {
                GamesList2("PC");
                bla();}
                break ;
            case"PS4":
            {
                GamesList2("PS4");
                bla();
            }
            break;
            case"XBOX":
            {
                GamesList2("XBOX");
                bla();
            }
            break;
            case"NINTENDO":
            {
                GamesList2("NINTENDO");
                bla();
            }
            break;
            case"MOBILE":
            {
                GamesList2("MOBILE");
                bla();
            }
            break;
        }

    }

    public void bla(){
        MyRecViewEvents= v.findViewById(R.id.rec_games_id);
        GamesAdapter recadapter = new GamesAdapter(getContext(),Games);
        MyRecViewEvents.setLayoutManager(new GridLayoutManager(getContext() , 2));
        MyRecViewEvents.setAdapter(recadapter);
    }

    public void saveplatform(String s){
        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREFS , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit() ;
        //saving data
        editor.putString(NAME , s);
        editor.apply();
    }


    public void GamesList2(String s){
        Games = new ArrayList<>();
        if(s=="PS4"||s=="XBOX"){
            Games.add(new Games("FORTNITE" , R.drawable.fortnite , false ,"fortnite"));
            Games.add(new Games("ROCKET" , R.drawable.rocket , false,"rocket"));
            Games.add(new Games("RAINBOW 6" , R.drawable.rainbow , false,"rainbow"));
            Games.add(new Games("APEX LEGENDS" , R.drawable.apex , false,"apex"));
            Games.add(new Games("CALL OF DUTY" , R.drawable.callof , false,"callof"));
            Games.add(new Games("DESTINY 2" , R.drawable.destiny , false,"destiny"));
            Games.add(new Games("GTA V" , R.drawable.gta , false,"gta"));
            Games.add(new Games("OVERWATCH" , R.drawable.overwatch , false,"overwatch"));
            Games.add(new Games("ARK:SURVIVAL EVOLVED" , R.drawable.ark , false,"ark"));
            Games.add(new Games("PUBG" , R.drawable.pubg , false,"pubg"));
            Games.add(new Games("MINECRAFT" , R.drawable.minecraft , false,"minecraft"));
            Games.add(new Games("BATTLEFIELED V" , R.drawable.battlefield , false,"battlefield"));
            Games.add(new Games("DEAD BY DAYLIGHT" , R.drawable.deadbd , false,"deadbd"));
            Games.add(new Games("FIFA" , R.drawable.fifa , false,"fifa"));
        }
        if(s=="NINTENDO"){
            Games.add(new Games("ZELDA" , R.drawable.zelda , false,"zelda"));
            Games.add(new Games("SPLATOON 2" , R.drawable.splatoon , false,"splatoon"));
            Games.add(new Games("MARIO KART 8" , R.drawable.mariokart , false,"mariokart"));
            Games.add(new Games("POKEMON LET'S GO" , R.drawable.pokemon , false,"pokemon"));
            Games.add(new Games("WARFRAME" , R.drawable.warframe , false,"warframe"));
            Games.add(new Games("FORTNITE" , R.drawable.fortnite , false,"fortnite"));
            Games.add(new Games("ROCKET OF LEAGUE" , R.drawable.rocket , false,"rocket"));
            Games.add(new Games("MINECRAFT" , R.drawable.ninmc , false,"ninmc"));
            Games.add(new Games("ARK:SURVIVAL EVOLVED" , R.drawable.ark , false,"ark"));
            Games.add(new Games("PALADINS" , R.drawable.paladins , false,"paladins"));
        }
        if(s=="PC"){
            Games.add(new Games("FORTNITE" , R.drawable.fortnite , false,"fortnite"));
            Games.add(new Games("PUBG" , R.drawable.pubg , false,"pubg"));
            Games.add(new Games("RAINBOW 6" , R.drawable.rainbow , false,"rainbow"));
            Games.add(new Games("CALL OF DUTY" , R.drawable.callof , false,"callof"));
            Games.add(new Games("CALL OF DUTY" , R.drawable.callofwarzone , false,"callofwarzone"));
            Games.add(new Games("CALL OF DUTY" , R.drawable.callofblack , false,"callofblack"));
            Games.add(new Games("LEAGUE OF LEGENDS" , R.drawable.lol , false,"lol"));
            Games.add(new Games("DESTINY 2" , R.drawable.destiny , false,"destiny"));
            Games.add(new Games("GTA V" , R.drawable.gta , false,"gta"));
            Games.add(new Games("ROCKET" , R.drawable.rocket , false,"rocket"));
            Games.add(new Games("APEX LEGENDS" , R.drawable.apex , false,"apex"));
            Games.add(new Games("OVERWATCH" , R.drawable.overwatch , false,"overwatch"));
            Games.add(new Games("ARK:SURVIVAL EVOLVED" , R.drawable.ark , false,"ark"));
            Games.add(new Games("MINECRAFT" , R.drawable.minecraft , false,"minecraft"));
            Games.add(new Games("BATTLEFIELED V" , R.drawable.battlefield , false,"battlefield"));
            Games.add(new Games("DEAD BY DAYLIGHT" , R.drawable.deadbd , false,"deadbd"));
            Games.add(new Games("RUST" , R.drawable.rust , false,"rust"));
        }
        if(s=="MOBILE"){
            Games.add(new Games("FORTNITE" , R.drawable.fortnite , false,"fortnite"));
            Games.add(new Games("PUBG" , R.drawable.pubg , false,"pubg"));
            Games.add(new Games("CALL OF DUTY" , R.drawable.callofmobile , false,"callofmobile"));
            Games.add(new Games("MINECRAFT" , R.drawable.minecraft , false,"minecraft"));
            Games.add(new Games("CLASH OF CLANS" , R.drawable.clashofclans , false,"clashofclans"));
            Games.add(new Games("CLASH ROYALE" , R.drawable.clashroyale , false,"clashroyale"));
            Games.add(new Games("ROBLOX" , R.drawable.roblox , false,"roblox"));
            Games.add(new Games("POKEMON GO" , R.drawable.pokemongo , false,"pokemongo"));
            Games.add(new Games("ARENA OF VALOR" , R.drawable.arenaofvalor , false,"arenaofvalor"));
            Games.add(new Games("ARK:SURVIVAL EVOLVED" , R.drawable.ark , false,"ark"));
            Games.add(new Games("CRITICAL OPS" , R.drawable.cops , false,"cops"));

        }

    }
}
