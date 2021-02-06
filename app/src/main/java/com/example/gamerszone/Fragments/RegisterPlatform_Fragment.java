package com.example.gamerszone.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gamerszone.R;
import com.example.gamerszone.Activities.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class RegisterPlatform_Fragment extends Fragment implements View.OnClickListener {

    View v ;
    ImageView ps4,xbox,ninto,pc,mobile ;

    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser fuser;

    public static final String SHARED_PREFS = "USERINFO" ;
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String PASS = "PASSWORD";
    private ArrayList<String> SelectedGames;


    public RegisterPlatform_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

     v = inflater.inflate(R.layout.fragment_register_platform_, container, false);

        ps4=(ImageView)v.findViewById(R.id.ps4_img_id);
        ps4.setOnClickListener(this);
        xbox=(ImageView)v.findViewById(R.id.xbox_img_id);
        xbox.setOnClickListener(this);
        ninto=(ImageView)v.findViewById(R.id.nin_img_id);
        ninto.setOnClickListener(this);
        pc=(ImageView)v.findViewById(R.id.pc_img_id);
        pc.setOnClickListener(this);
        mobile=(ImageView)v.findViewById(R.id.mobile_img_id);
        mobile.setOnClickListener(this);

        SelectedGames=new ArrayList<>();
        auth = FirebaseAuth.getInstance();

     return v ;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ps4_img_id :
                ReadData("PS4");
                break;
            case R.id.xbox_img_id:
                ReadData("XBOX");
                break;
            case R.id.nin_img_id:
                ReadData("NINTENDO");
                break;
            case R.id.pc_img_id:
                ReadData("PC");
                break;
            case R.id.mobile_img_id:
                ReadData("MOBILE");
                break;
        }
    }

    private void register(final String username, final String email, String password , final String platform){

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("platform", platform);
                            hashMap.put("email", email);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", username.toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                      //  LoadData();
                                        RegisterActivity mainActivity = (RegisterActivity) getActivity();
                                        mainActivity.Step3();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "You can't register woth this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void ReadData(String platform){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();

        LoadData();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("platform", platform);
        reference.updateChildren(hashMap);

        RegisterActivity mainActivity = (RegisterActivity) getActivity();
        mainActivity.Step3();
        pd.dismiss();

    }


    //just to clean the array before new use
    public void LoadData(){
        SharedPreferences sp2 = getActivity().getSharedPreferences("GAMES" , Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp2.getString("GamesArray" , null);
        Type typeT = new TypeToken<ArrayList<String>>() {}.getType();
        SelectedGames = gson.fromJson(json , typeT );
        if(SelectedGames == null){
            SelectedGames = new ArrayList<>();
        }
        SelectedGames.clear();
        SaveData();
    }

    public void SaveData(){
        SharedPreferences sp  = getActivity().getSharedPreferences("GAMES" ,Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(SelectedGames);
        editor.putString("GamesArray",json);
        editor.apply();
    }

}
