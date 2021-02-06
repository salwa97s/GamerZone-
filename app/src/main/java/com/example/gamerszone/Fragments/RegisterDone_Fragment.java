package com.example.gamerszone.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gamerszone.Activities.MainChatActivity;
import com.example.gamerszone.Models.User;
import com.example.gamerszone.R;
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


public class RegisterDone_Fragment extends Fragment {

    View v ;
    ArrayList<String> GamesList ;
    FirebaseUser fuser;
    DatabaseReference reference,reference1;
    LinearLayout next ;
    TextView username_txt;
    String s;

    public RegisterDone_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_register_done_, container, false);

     //   LoadData();

        username_txt=v.findViewById(R.id.txt_name);


    /*    fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("list", GamesList);
        reference.updateChildren(hashMap);*/

        next = v.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username_txt.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v ;
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
}
