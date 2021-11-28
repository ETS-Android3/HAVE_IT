package com.example.have_it;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * This is the activity for the account page (only function is logging out)
 * @author yulingshen
 */
public class AccountPageActivity extends AppCompatActivity implements DatabaseUserReference {

    /**
     * Reference to the bottom navigation menu, of class {@link BottomNavigationView}
     */
    BottomNavigationView bottomNavigationView;

    /**
     * Reference to the logout button of class {@link Button}
     */
    Button logoutButton;

    /**
     *This is the method invoked when the activity starts
     * @param savedInstanceState {@link Bundle} used for its super class
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account_page);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.account_menu);
        logoutButton = (Button)findViewById(R.id.logout);

        TextView textViewName = (TextView) findViewById(R.id.textviewname);
        DocumentReference docRef = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.i("LOGGER","Get name "+document.getString("Name"));
                        textViewName.setText(document.getString("Name"));
                    } else {
                        Log.d("LOGGER", "No such name");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.habit_menu:
                        final Intent habitIntent = new Intent(AccountPageActivity.this, HabitPageActivity.class);
                        startActivity(habitIntent);
                        break;

                    case R.id.following_menu:
                        final Intent followingIntent = new Intent(AccountPageActivity.this, FollowingPageActivity.class);
                        startActivity(followingIntent);
                        break;

                    case R.id.account_menu:
                        break;
                }
                return false;
            }
        });
        final Intent UserLoginActivity = new Intent(this, UserLoginActivity.class);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logged.setUID("");
                startActivity(UserLoginActivity);
            }
        });
    }
}
