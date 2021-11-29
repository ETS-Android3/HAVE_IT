package com.example.have_it;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * ForgotPasswordActivity represent all the activities happened on password reset page.
 *  @author Jianbang Chen,Yuling Shen
 *  @see User
 *  @version 1.2
 */
public class ForgotPasswordActivity extends AppCompatActivity {
    /**
     *A reference to email input, of class {@link EditText}
     */
    private EditText emailEditText;
    /**
     *A reference to rest password confirmation button, of class {@link Button}
     */
    private Button resetPasswordButton;
    /**
     *A reference to show progress sign, of class {@link ProgressBar}
     */
    private ProgressBar progressBar;
    /**
     *A reference to firebase Authentication, of class {@link FirebaseAuth}
     */
    FirebaseAuth auth;

    /**
     * connect all the layout id with their own variables.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = (EditText) findViewById(R.id.email);
        resetPasswordButton = (Button) findViewById(R.id.resetPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    /**
     * The method store the user input reset email address.
     * Checking the validity of reset email address.
     * Send the reset email to the user email address.
     */
    private void resetPassword (){
        String email = emailEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide valid email");
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Reset email has been send!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ForgotPasswordActivity.this, "Try again! Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}