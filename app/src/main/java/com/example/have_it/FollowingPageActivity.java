package com.example.have_it;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class FollowingPageActivity extends AppCompatActivity {

    /**
     * Reference to the bottom navigation menu, of class {@link BottomNavigationView}
     */
    BottomNavigationView bottomNavigationView;

    private FollowingSectionsPageAdapter sectionsPageAdapter;

    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_following_page);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        sectionsPageAdapter = new FollowingSectionsPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.following_container);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.following_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_outline_people_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_search_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_outline_contact_mail_24);

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
                        final Intent accountIntent = new Intent(FollowingPageActivity.this, AccountPageActivity.class);
                        startActivity(accountIntent);
                        break;
                }
                return false;
            }
        });
    }

    private void setupViewPager(ViewPager viewPager){
        FollowingSectionsPageAdapter adapter = new FollowingSectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new NowFollowingFragment());
        adapter.addFragment(new NewFollowingFragment());
        adapter.addFragment(new FollowingRequestFragment());
        viewPager.setAdapter(adapter);
    }


}
