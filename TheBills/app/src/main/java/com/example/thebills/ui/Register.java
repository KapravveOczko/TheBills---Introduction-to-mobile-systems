package com.example.thebills.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.thebills.R;
import com.example.thebills.UserManager;
import com.google.android.material.textfield.TextInputEditText;
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
        mAuth = FirebaseAuth.getInstance();
        userManager = new UserManager();

        // Find views by their IDs
        editTextEmail = findViewById(R.id.textFieldEmail);
        editTextPassword = findViewById(R.id.textFieldPassword);
        buttonReg = findViewById(R.id.buttonRegister);
        textView = findViewById(R.id.textRegister);
        usernameInput = findViewById(R.id.textFieldUsername);

        // Set onClickListener for TextView to navigate to LoginActivity
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });

        // Set onClickListener for Register Button
        buttonReg.setOnClickListener(v -> {
            String email = String.valueOf(editTextEmail.getText());
            String password = String.valueOf(editTextPassword.getText());
            String username = String.valueOf(usernameInput.getText());

            // Validate input fields
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
                Toast.makeText(Register.this, "Please enter all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Notify the user that the registration process is ongoing
            Toast.makeText(Register.this, "Working on your account!", Toast.LENGTH_SHORT).show();

            // Check username availability
            userManager.checkAvailability(new UserManager.GetUsernameListCallback() {

                @Override
                public void onUsernameListReceived(Map<String, Boolean> usernameList) {
                    // Retrieve the list of usernames from the callback
                    List<String> listOfUsernames = new ArrayList<>(usernameList.keySet());
                    Log.d("TheBills: Register activity", "current usernames: " + listOfUsernames);

                    // Check if the desired username is not already in use
                    if (!listOfUsernames.contains(username)) {
                        // If the username is available, proceed with creating the user account
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(task -> {
                                    // Check if user account creation was successful
                                    if (task.isSuccessful()) {
                                        userManager.addUsername(mAuth.getCurrentUser().getUid(), username);
                                        Toast.makeText(Register.this, "Account created!", Toast.LENGTH_SHORT).show();

                                        // Redirect the user to the main activity
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If user account creation failed, notify the user
                                        Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Register.this, "Username already exists.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(String error) {
                    Toast.makeText(Register.this, "Something went wrong, please try again later", Toast.LENGTH_LONG).show();
                    Log.d("TheBills: Register activity", "something went wrong: " + error);
                }
            });
        });
    }
}
