package com.example.gamerszone.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ProfileGamesFragment extends Fragment {

    View v ;
    private RecyclerView MyRecView  ;
    private ArrayList<String> MyGames;
    FirebaseUser fuser3 , fusere;
    DatabaseReference reference3 , referencee;
    String PlatformType="9" ;

    public ProfileGamesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_profile_games, container, false);

        MyGames  = new ArrayList<>();
        MyGamesFun1();
        // whichPL1();

        //  Toast.makeText(getContext(), PlatformType ,
        //          Toast.LENGTH_SHORT).show();
        MyRecView=v.findViewById(R.id.rec_show_id);

        return v ;
    }

    public void MyGamesFun1(){
        fuser3 = FirebaseAuth.getInstance().getCurrentUser();
        reference3 = FirebaseDatabase.getInstance().getReference("Users");
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if (user.getId().equals(fuser3.getUid())){
                        MyGames = user.getList();
                    }
                }

                RecviewadapterOffers recadapter2 = new RecviewadapterOffers(getContext(),MyGames);
                MyRecView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false )) ;
                MyRecView.setAdapter(recadapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
