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
import com.google.firebase.auth.FirebaseUser;

/**
 * UserLoginActivity represent all the activities happened at user login page.
 * @author Jianbang Chen,Yuling Shen
 * @see UserRegisterActivity ,ForgotPasswordActivity
 * @version 1.2
 *
 */
public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener, DatabaseUserReference{
    /**
     *Reference to the textview of register and forgot password , of class {@link TextView}
     */
    private TextView register,forgotPassword;
    /**
     *Reference to the email user input and password user input, of class {@link EditText}
     */
    private EditText editTextEmail, editTextPassword;
    /**
     *Reference to the sign in button, of class {@link Button}
     */
    private Button signIn;
    /**
     *A reference to firebase Authentication, of class {@link FirebaseAuth}
     */
    private FirebaseAuth mAuth;
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
        setContentView(R.layout.activity_user_login);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = (Button)findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }

    /**
     *This method direct the user to the register page or forgot password page when the text view is clicked.
     * Start sign in process when the button is clicked.
     * @param v
     */
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, UserRegisterActivity.class));
                break;
            case R.id.signIn:
                userLogin();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
        }
    }

    /**
     * This method sort the email address and password from user input.
     * Also check password and email validity, and notify the user error inputs.
     */
    private void userLogin() {
//        String email = editTextEmail.getText().toString().trim();
//        String password = editTextPassword.getText().toString().trim();
        String email = "2833395006a@gmail.com";
        String password = "123456";


        if(email.isEmpty()){
            editTextEmail.setError("Email address is required!");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("Minimum password length is 6 characters!");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //User Email verification
                    if(user.isEmailVerified()){
                        //redirect to user profile
                        logged.setUID(user.getUid());
                        startActivity(new Intent(UserLoginActivity.this,HabitPageActivity.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(UserLoginActivity.this, "check your email to verify your account!", Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(UserLoginActivity.this,"Failed to login! Please check your credentials!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
