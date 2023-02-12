package com.dimsen.tandingowner.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.dimsen.tandingowner.R;
import com.dimsen.tandingowner.fragment.AccountFragment;
import com.dimsen.tandingowner.fragment.HistoryFragment;
import com.dimsen.tandingowner.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    HistoryFragment historyFragment = new HistoryFragment();
    AccountFragment accountFragment = new AccountFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_home, homeFragment, null)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_home, homeFragment, null)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.menu_history:
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_home, historyFragment, null)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.menu_account:
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_home, accountFragment, null)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                        break;
                }

                return true;
            }
        });
    }
}