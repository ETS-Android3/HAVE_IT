package com.example.have_it;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * User_RegisterActivity represent all the activities happened on register page.
 * @author Jianbang Chen,Yuling Shen
 * @see UserRegisterActivity
 * @version 1.2
 */
public class UserRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     *A reference to firebase Authentication, of class {@link FirebaseAuth}
     */
    private FirebaseAuth mAuth;
    /**
     *A text view to show banner and register button, of class {@link TextView}
     */
    private TextView banner, registerUser;
    /**
     *Reference of user full name, email address, password inputs, of class {@link EditText}
     */
    private EditText editTextFullName, editTextEmail, editTextPassword;
    /**
     *A reference to show progress sign, of class {@link ProgressBar}
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
                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                            Toast.makeText(UserRegisterActivity.this, "registered, check your email to verify your account!", Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(UserRegisterActivity.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}