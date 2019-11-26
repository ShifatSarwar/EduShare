package com.example.edushare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChooseLoginOrRegistrationActivity extends AppCompatActivity {
    private Button login;
    private TextView signup;
    private EditText emailID;
    private EditText password;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_or_registration);
        login=findViewById(R.id.loginButton);
        signup= findViewById(R.id.signupButton);
        emailID=findViewById(R.id.emailReader);
        password=findViewById(R.id.passwordReader);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser!=null) {
                    Intent intent=new Intent(ChooseLoginOrRegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
    }

    public void loginAction(View view) {
        final String email=emailID.getText().toString();
        final String passCode=password.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email, passCode).addOnCompleteListener(ChooseLoginOrRegistrationActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(ChooseLoginOrRegistrationActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signupAction(View view) {
        Intent intent =new Intent(ChooseLoginOrRegistrationActivity.this, RegistrationActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
