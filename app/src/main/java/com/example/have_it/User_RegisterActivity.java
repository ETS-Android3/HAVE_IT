package com.example.have_it;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * User_RegisterActivity represent all the activities happened on register page.
 * @author Jianbang Chen,Yuling Shen
 * @see User_RegisterActivity
 * @version 1.2
 */
public class User_RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     *
     */
    private FirebaseAuth mAuth;
    /**
     *
     */
    private TextView banner, registerUser;
    /**
     *
     */
    private EditText editTextFullName, editTextEmail, editTextPassword;
    /**
     *
     */
    private ProgressBar progressBar;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextEmail = (EditText) findViewById(R.id.email);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    /**
     * This class handle user clicking text view and register button.
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.banner:
                startActivity(new Intent(this, UserLoginActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;

        }
    }

    /**
     * This method storing the user email, password and fullName inputs.
     * Also, checking the validity of all the user input information with
     * notifications.
     *
     */
    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();

        if(fullName.isEmpty()) {
            editTextFullName.setError("Full name is required!");
            editTextFullName.requestFocus();
            return;
        }
        if(email.isEmpty()) {
            editTextEmail.setError("Email address is required!");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6) {
            editTextPassword.setError("Minimum password should be more than 6 characters!");
            editTextPassword.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
//                            User user = new User(fullName,email);
//                            FirebaseDatabase.getInstance().getReference("Users")
//                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//                                        Toast.makeText(User_RegisterActivity.this,"User has been registered successfully!",Toast.LENGTH_LONG).show();
//
//                                        //redirect to login layout
//                                        //startActivity(new Intent(User_RegisterActivity.this, UserLoginActivity.class));
//                                        finish();
//                                    }else{
//                                        Toast.makeText(User_RegisterActivity.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
//                                    }
//                                    progressBar.setVisibility(View.GONE);
//                                }
//                            });
                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                            Toast.makeText(User_RegisterActivity.this, "registered, check your email to verify your account!", Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(User_RegisterActivity.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}