package com.example.gamerszone.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gamerszone.Models.User;
import com.example.gamerszone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileInfoFragment extends Fragment {

    View v ;
    TextView Email , Platform , dec ;
    Button edit ;
    EditText description ;
    DatabaseReference reference;
    FirebaseUser fuser;

    public ProfileInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       v= inflater.inflate(R.layout.fragment_profile_info, container, false);

        Email = v.findViewById(R.id.email_txt_id);
        Platform = v.findViewById(R.id.platform_txt_id);
        edit = v.findViewById(R.id.profile_btn);
        dec = v.findViewById(R.id.des_txt_id);
        description = v.findViewById(R.id.des_edit_id);

        edit.setVisibility(View.VISIBLE);
        dec.setVisibility(View.VISIBLE);
        //description.setVisibility(View.VISIBLE);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Email.setText(user.getEmail());
                Platform.setText(user.getPlatform());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v ;
    }


}
