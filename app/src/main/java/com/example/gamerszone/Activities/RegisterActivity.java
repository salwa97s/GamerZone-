package com.example.gamerszone.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gamerszone.Fragments.RegisterDone_Fragment;
import com.example.gamerszone.Fragments.RegisterGames_Fragment;
import com.example.gamerszone.Fragments.RegisterInfo_Fragment;
import com.example.gamerszone.Fragments.RegisterPlatform_Fragment;
import com.example.gamerszone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    ImageView StepViewimg;

    FirebaseAuth auth;
    DatabaseReference reference;

    private FrameLayout mMainFrame ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        StepViewimg=(ImageView)findViewById(R.id.StepView_img);

        mMainFrame = (FrameLayout)findViewById(R.id.Fragment_container_id);
        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container_id,new RegisterInfo_Fragment()).commit();

    }


    public void Step2(){
        StepViewimg.setImageResource(R.drawable.sthree);
        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container_id,new RegisterPlatform_Fragment()).commit();
    }


    public void Step3(){
        StepViewimg.setImageResource(R.drawable.sfour);
        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container_id,new RegisterGames_Fragment()).commit();
    }

    public void Step4(){
        StepViewimg.setImageResource(R.drawable.sfive);
        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container_id,new RegisterDone_Fragment()).commit();
    }

    private void register(final String username, String email, String password){

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
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", username.toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "You can't register woth this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}

