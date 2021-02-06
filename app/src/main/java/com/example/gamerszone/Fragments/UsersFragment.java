package com.example.gamerszone.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamerszone.Adapters.MembersAdapter;
import com.example.gamerszone.Models.User;
import com.example.gamerszone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    private RecyclerView recyclerView;
    private MembersAdapter membersAdapter;
    private List<User> mUsers;
    private ArrayList<String> MyGames  , UserGame;
    private String PlatformType ;
    private Button Show_Menu_Btn;
    private TextView FilterStatus  , EmptyMsg;
    private ImageView EmptyImg;

    FirebaseUser fuser,fuser1;
    DatabaseReference reference,reference1;
    EditText search_users;

    View v ;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v= inflater.inflate(R.layout.fragment_users_, container, false);


        FilterStatus = v.findViewById(R.id.Filter_Status_id);
        EmptyMsg = v.findViewById(R.id.txt_empty_id);
        EmptyImg = v.findViewById(R.id.img_empty_id);

        whichPL();

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
        UserGame = new ArrayList<>();
        MyGames  = new ArrayList<>();

        MyGamesFun();
        readUsers();

        //
        Show_Menu_Btn = v.findViewById(R.id.Show_Menu_Btn);
        Show_Menu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                onclickpop(v);
            }
        });

        search_users = v.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
                FilterStatus.setText("Looking For :");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v ;
    }

    private void searchUsers(String s) {

       // Toast.makeText(getContext(), "im here"+ s , Toast.LENGTH_SHORT).show();
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert fuser != null;
                    if (!user.getId().equals(fuser.getUid())){
                        mUsers.add(user);
                    }
                }

                membersAdapter = new MembersAdapter(getContext(), mUsers);
                recyclerView.setAdapter(membersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




    public void whichPL(){
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    try{
                        if (user.getId().equals(fuser.getUid())){
                            PlatformType = user.getPlatform();
                        }
                    }catch(NullPointerException ignore){}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void MyGamesFun(){
        fuser1 = FirebaseAuth.getInstance().getCurrentUser();
        reference1 = FirebaseDatabase.getInstance().getReference("Users");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    try{
                        if (user.getId().equals(fuser1.getUid())){
                            MyGames = user.getList();
                        }
                    }catch(NullPointerException ignore){}

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //show users with same platform and games
    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                int counter = 0 ;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user =snapshot.getValue(User.class);
                    assert  user != null;
                    assert firebaseUser != null ;
                        try{
                            if((!user.getId().equals(firebaseUser.getUid()))) {
                                if (user.getPlatform().equals(PlatformType)) {
                                    for (String s : MyGames) {
                                        for (String a : user.getList()) {
                                            if (s.equals(a)) {
                                                UserGame.add(s);
                                                counter++;
                                                if (counter == 1) {
                                                    mUsers.add(user);
                                                }
                                            }
                                        }
                                    }
                                    counter = 0;
                                }
                            }
                        }catch(NullPointerException ignore){
                    }
                }

                //if list is empty show msg
                if(mUsers.size()==0){
                    EmptyImg.setVisibility(View.VISIBLE);
                    EmptyMsg.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else{
                    EmptyImg.setVisibility(View.GONE);
                    EmptyMsg.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                membersAdapter = new MembersAdapter(getContext(),mUsers);
                recyclerView.setAdapter(membersAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //shows users with same platform only !
    private void readusersplatform() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user =snapshot.getValue(User.class);

                    assert  user != null;
                    assert firebaseUser != null ;
                    if((!user.getId().equals(firebaseUser.getUid()))){
                        try{
                            if(user.getPlatform().equals(PlatformType)){
                                mUsers.add(user);
                            }
                        }catch(NullPointerException ignore){}
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
                membersAdapter = new MembersAdapter(getContext(),mUsers);
                recyclerView.setAdapter(membersAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //shows users  !
    private void readusersall() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user =snapshot.getValue(User.class);

                    assert  user != null;
                    assert firebaseUser != null ;
                    if((!user.getId().equals(firebaseUser.getUid()))){
                        mUsers.add(user);
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
                membersAdapter = new MembersAdapter(getContext(),mUsers);
                recyclerView.setAdapter(membersAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void onclickpop(View v ){
        PopupMenu popupMenu = new PopupMenu(getContext() , v) ;
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ALL:
                readusersall();
                FilterStatus.setText("All The Users :");
                break;
            case R.id.Platform:
                readusersplatform();
                FilterStatus.setText("Users With Same Platform : ");
                break;
            case R.id.Games:
                readUsers();
                FilterStatus.setText("Users Shared Same Games : ");
                break;
        }
        return false ;
    }
}
