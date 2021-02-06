package com.example.gamerszone.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamerszone.Models.User;
import com.example.gamerszone.R;
import com.example.gamerszone.Adapters.RecviewadapterOffers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUserActivity extends AppCompatActivity {

    CircleImageView image_profile;
    TextView usernametxt , platformTxt , emailtxt , iscommon;
    ImageView platformIcon ;
    private RecyclerView MyRecView ,MyRecView2 ;
    DatabaseReference reference,reference2;
    FirebaseUser fuser,fuser2;
    String userid;
    Intent intent ;
    User userp ;
    private ArrayList<String> userGames , MyGames , SameGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lala);

        image_profile = findViewById(R.id.profile_image);
        usernametxt =findViewById(R.id.user_username_id);
        emailtxt = findViewById(R.id.user_email_id);
        platformTxt = findViewById(R.id.user_platform_id);
        platformIcon = findViewById(R.id.user_p_icon);
        MyRecView = findViewById(R.id.Rec_fav_id);
        MyRecView2 = findViewById(R.id.Rec_common_id);
        iscommon = findViewById(R.id.text_nocommongames);

        userGames = new ArrayList<>();
        SameGames = new ArrayList<>();
        MyGames  = new ArrayList<>();


        intent = getIntent();
        userid = intent.getStringExtra("userid");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userp = dataSnapshot.getValue(User.class);
                usernametxt.setText(userp.getUsername());
                platformTxt.setText(userp.getPlatform());
                emailtxt.setText(userp.getEmail());
                if (userp.getImageURL().equals("default")){
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(userp.getImageURL()).into(image_profile);
                }
                userGames = userp.getList();
                RecviewadapterOffers recadapter2 = new RecviewadapterOffers(ProfileUserActivity.this,userGames);
                MyRecView.setLayoutManager(new LinearLayoutManager(ProfileUserActivity.this,LinearLayoutManager.HORIZONTAL,false )) ;
                MyRecView.setAdapter(recadapter2);
                MyGamesFun();
                switch(userp.getPlatform()){
                    case "PS4":
                        platformIcon.setImageResource(R.drawable.ic_psfour);
                        break;
                    case "XBOX":
                        platformIcon.setImageResource(R.drawable.ic_xbox);
                        break;
                    case "PC":
                        platformIcon.setImageResource(R.drawable.ic_pc);
                        break;
                    case "MOBILE":
                        platformIcon.setImageResource(R.drawable.ic_mobile);
                        break;
                    case "NINTENDO":
                        platformIcon.setImageResource(R.drawable.ic_nin);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void MyGamesFun(){
        fuser2 = FirebaseAuth.getInstance().getCurrentUser();
        reference2 = FirebaseDatabase.getInstance().getReference("Users").child(fuser2.getUid());
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                MyGames = user.getList();
                if(user.getPlatform().equals(userp.getPlatform())){
                    for(String s : MyGames){
                        for(String a :userGames){
                            if(s.equals(a)){
                                SameGames.add(s);
                            }
                        }
                    }
                }

                if(SameGames.size()!=0){
                    MyRecView2.setVisibility(View.VISIBLE);
                    RecviewadapterOffers recadapter3 = new RecviewadapterOffers(ProfileUserActivity.this,SameGames);
                    MyRecView2.setLayoutManager(new LinearLayoutManager(ProfileUserActivity.this,LinearLayoutManager.HORIZONTAL,false )) ;
                    MyRecView2.setAdapter(recadapter3);
                }else {
                    iscommon.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
