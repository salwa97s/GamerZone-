package com.example.gamerszone.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamerszone.Adapters.UserAdapter;
import com.example.gamerszone.Models.Chatlist;
import com.example.gamerszone.Models.User;
import com.example.gamerszone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    View v ;
    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> mUsers;
    private TextView EmptyMsg ;
    private ImageView EmptyImg ;

    FirebaseUser fuser;
    DatabaseReference reference,reference1;

    private List<Chatlist> usersList;

    int counter=0;
    public ChatsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v= inflater.inflate(R.layout.fragment_chats_, container, false);

        EmptyImg = v.findViewById(R.id.img_empty_id);
        EmptyMsg = v.findViewById(R.id.txt_empty_id);

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        //edit it ti diff child like it shows even inside
        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }

               next();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }

   /* private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
       // Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }*/

   public void next(){
       reference1 = FirebaseDatabase.getInstance().getReference("Chatlist");
       reference1.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                   for(DataSnapshot staptwo : snapshot.getChildren()){
                       if((staptwo.getKey()).equals(fuser.getUid())){
                           for(Chatlist s : usersList){
                               if(s.getId().equals(snapshot.getKey())){
                                   //nothing
                               }else { counter++;}
                           }
                           if(usersList.size()==counter){
                               Chatlist chatlist = new Chatlist();
                               chatlist.setId(snapshot.getKey());
                               usersList.add(chatlist);
                           }
                           counter=0;
                       }
                   }
               }
               chatList();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
   }

    private void chatList() {

        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for (Chatlist chatlist : usersList){
                        if (user.getId().equals(chatlist.getId())){
                            mUsers.add(user);
                        }
                    }
                }
                if(mUsers.size()==0){
                    EmptyImg.setVisibility(View.VISIBLE);
                    EmptyMsg.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else{
                    EmptyImg.setVisibility(View.GONE);
                    EmptyMsg.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                userAdapter = new UserAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
