package com.example.practice2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    public TextInputEditText email;
    public TextInputEditText password;
    public MaterialButton signUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email=findViewById(R.id.signUpEmail);
        password=findViewById(R.id.signUpPassword);
        //signUpButton=findViewById(R.id.signUpButton);
    }

    public void signUp(View view){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(String.valueOf(email.getText()),String.valueOf(password.getText()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            sendVerificationEmail();
                            FirebaseAuth.getInstance().signOut();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendVerificationEmail() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUp.this,"Verification Email Sent",Toast.LENGTH_LONG).show();
                            Intent intent= new Intent(SignUp.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this,"Unable to send Verification Email",Toast.LENGTH_LONG).show();
            }
        });
    }
}