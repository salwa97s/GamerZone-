package com.example.gamerszone.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;


public class RegisterInfo_Fragment extends Fragment {

    View v ;
    EditText username, email, password;
    Button btn_register;
    boolean flag = true;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;


    public static final String SHARED_PREFS = "USERINFO" ;
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String PASS = "PASSWORD";


    public RegisterInfo_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_register_info_, container, false);

        username=(EditText)v.findViewById(R.id.username_id);
        email = (EditText)v.findViewById(R.id.email_id);
        password = (EditText)v.findViewById(R.id.Pass_id);
        btn_register = (Button)v.findViewById(R.id.btnNext_id);

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd = new ProgressDialog(getContext());
                pd.setMessage("Loading...");
                pd.show();

                String u = username.getText().toString();
                String e = email.getText().toString();
                String p = password.getText().toString() ;

                if (TextUtils.isEmpty(u) || TextUtils.isEmpty(e) || TextUtils.isEmpty(p)){
                    Toast.makeText(getContext(), "All fileds are required", Toast.LENGTH_SHORT).show();
                } else if (p.length() < 6 ){
                    Toast.makeText(getContext(), "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                }else if (!(e.contains("@"))&&!(e.contains("com"))) {
                    Toast.makeText(getContext(), "wrong email", Toast.LENGTH_SHORT).show();
                }else {
                    register(u,e,p);
                }


            }
        });
        return v ;
    }


    private void register(final String username, final String email, String password ){

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
                            hashMap.put("email", email);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", username.toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        pd.dismiss();
                                        RegisterActivity mainActivity = (RegisterActivity) getActivity();
                                        mainActivity.Step2();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "You can't register woth this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //just to move it to the other fragment
    private void TransformData(){
        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREFS , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit() ;

        //saving data
        editor.putString(NAME , username.getText().toString());
        editor.putString(EMAIL ,email.getText().toString());
        editor.putString(PASS , password.getText().toString());

        editor.apply();
    }
}

