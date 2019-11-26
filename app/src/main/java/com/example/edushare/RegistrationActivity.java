package com.example.edushare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private Button register;
    private EditText emailID;
    private EditText userName;
    private RadioGroup gender;
    private EditText password;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser!=null) {
                    Intent intent=new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        register=findViewById(R.id.registerButton);
        emailID=findViewById(R.id.emailReader);
        password=findViewById(R.id.passwordReader);
        userName=findViewById(R.id.nameReader);
        gender=findViewById(R.id.genderIdentification);
    }

    public void registerUser(View view) {
        int selectID=gender.getCheckedRadioButtonId();
        final RadioButton radioButton=findViewById(selectID);
        if(radioButton.getText()==null) {
            return;
        }
        final String email=emailID.getText().toString();
        final String passCode=password.getText().toString();
        final String uName=userName.getText().toString();
        final String uGender=radioButton.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(email, passCode).addOnCompleteListener(RegistrationActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                        } else {
                            String userID=firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDB= FirebaseDatabase.getInstance().getReference()
                                    .child("users").child("userid").child("name");
                            currentUserDB.setValue(uName);
                            currentUserDB=FirebaseDatabase.getInstance().getReference()
                                    .child("users").child("userid").child("gender");
                            currentUserDB.setValue(uGender);
                        }
                    }
                });
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
