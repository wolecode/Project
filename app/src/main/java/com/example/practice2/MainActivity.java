package com.example.practice2;


import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    public TextView signUp;
    public TextView signIn;
    public TextView resendVerification;
    public TextInputEditText email;
    public TextInputEditText password;
    public FirebaseAuth.AuthStateListener mListener;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authenticationSetup();

        signUp=findViewById(R.id.signUp);
        signIn=findViewById(R.id.signIn);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        resendVerification=findViewById(R.id.resendVerification);

        signUp.setOnClickListener(v->signUp());
        signIn.setOnClickListener(v->signIn());
         resendVerification.setOnClickListener(v->resend());
    }
    public void resend(){
        ResendVerification resendDialog=new ResendVerification();
        //FragmentTransaction t= getSupportFragmentManager().beginTransaction();
       // resendDialog.show(t,"resend_email_verification");

        resendDialog.setStyle(DialogFragment.STYLE_NORMAL,0);
        resendDialog.show(getSupportFragmentManager(),"resend_email_verification");
     }
    public void signUp(){
        Intent intent=new Intent(this,SignUp.class);
        startActivity(intent);
    }
    public void signIn(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(String.valueOf(email.getText()),String.valueOf(password.getText()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void authenticationSetup(){
        mListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    if(user.isEmailVerified()){
                    Toast.makeText(MainActivity.this,"You are verified",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this,"Verify your email",Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                    }
                }

            }
        };
    }
    @Override
    public void onStart(){
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mListener);
    }
    @Override
    public void onStop(){
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(mListener);
    }
}
