package com.example.practice2;

import androidx.fragment.app.DialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class ResendVerification extends DialogFragment {
    EditText email;
    EditText password;
    MaterialButton send;

  @Override
    public void onStart(){
      super.onStart();
      getDialog().getWindow()
              .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
  }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_resend_verification, container, false);
        email=view.findViewById(R.id.email);
        password=view.findViewById(R.id.password);
        send=view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getContext(),"Hello",Toast.LENGTH_LONG)
                        .show();
                if(isEmpty(email.getText().toString(),password.getText().toString())){
                    Toast.makeText(getContext(),"Fill in the required field",Toast.LENGTH_LONG)
                            .show();
                }
                else{
                    signIn(email.getText().toString(),password.getText().toString());
                    getDialog().dismiss();
                }
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    private void signIn(String email, String password) {
        AuthCredential credential= EmailAuthProvider.getCredential(email,password);
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                sendVerification();
                FirebaseAuth.getInstance().signOut();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void sendVerification() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(),"Verification Email Sent",Toast.LENGTH_LONG)
                                .show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    public boolean isEmpty(String email, String password){
        return email.equals("")||password.equals("");
    }
}