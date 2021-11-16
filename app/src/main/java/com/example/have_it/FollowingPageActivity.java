package com.example.have_it;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class FollowingPageActivity extends AppCompatActivity {

    /**
     * Reference to the bottom navigation menu, of class {@link BottomNavigationView}
     */
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_following_page);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.following_menu);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.habit_menu:
                        final Intent habitIntent = new Intent(FollowingPageActivity.this, HabitPageActivity.class);
                        startActivity(habitIntent);
                        break;

                    case R.id.following_menu:
                        break;

                    case R.id.account_menu:
                        break;
                }
                return false;
            }
        });
    }
}