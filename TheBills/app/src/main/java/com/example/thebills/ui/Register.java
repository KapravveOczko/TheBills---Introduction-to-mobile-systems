package com.example.thebills.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thebills.R;
import com.example.thebills.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    Button buttonReg;
    FirebaseAuth mAuth;
    TextView textView;
    TextInputEditText usernameInput;

    UserManager userManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userManager = new UserManager();

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.textFieldEmail);
        editTextPassword = findViewById(R.id.textFieldPassword);
        buttonReg = findViewById(R.id.buttonRegister);
        textView = findViewById(R.id.textRegister);
        usernameInput = findViewById(R.id.textFieldUsername);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


//        buttonReg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email, password, username;
//                email = String.valueOf(editTextEmail.getText());
//                password = String.valueOf(editTextPassword.getText());
//                username = String.valueOf(usernameInput.getText());
//
//                if (TextUtils.isEmpty(email)){
//                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (TextUtils.isEmpty(password)){
//                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (TextUtils.isEmpty(username)){
//                    Toast.makeText(Register.this, "Enter username", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (userManager.checkAvailability(username, new UserManager.GetUsernameListCallback() {
//
//                    @Override
//                    public boolean onUsernameListReceived(Map<String, Boolean> usernameList) {
//                        List<String> listOfUsernames = (List<String>) usernameList.keySet();
//                        return !listOfUsernames.contains(username);
//                    }
//
//                    @Override
//                    public boolean onCancelled(String error) {
//                        // LONG or SHORT ???
//                        Toast.makeText(Register.this, "Something went wrong, please try again later", Toast.LENGTH_LONG).show();
//                        Log.d("database","something went wrong: " + error);
//                        return false;
//                    }
//
//
//                })){
//                    return;
//                }
//
//
//                mAuth.createUserWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    userManager.addUsername(mAuth.getCurrentUser().getUid(), username);
//
//                                    Toast.makeText(Register.this, "Account created!",
//                                            Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(getApplicationContext(), com.example.thebills.ui.MainActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                } else {
//                                    Toast.makeText(Register.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//
//            }
//        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, username;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                username = String.valueOf(usernameInput.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(Register.this, "Enter username", Toast.LENGTH_SHORT).show();
                    return;
                }

                userManager.checkAvailability(username, new UserManager.GetUsernameListCallback() {
                    @Override
                    public void onUsernameListReceived(Map<String, Boolean> usernameList) {
                        List<String> listOfUsernames = new ArrayList<>(usernameList.keySet());
                        Log.d("current usernames:" , listOfUsernames.toString());
                        if (!listOfUsernames.contains(username)) {
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                userManager.addUsername(mAuth.getCurrentUser().getUid(), username);
                                                Toast.makeText(Register.this, "Account created!",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), com.example.thebills.ui.MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(Register.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(Register.this, "Username already exists.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(String error) {
                        Toast.makeText(Register.this, "Something went wrong, please try again later", Toast.LENGTH_LONG).show();
                        Log.d("database","something went wrong: " + error);
                    }
                });
            }
        });


    }
}