package com.example.lexis.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.lexis.R;
import com.example.lexis.databinding.ActivityMainBinding;
import com.example.lexis.fragments.FeedFragment;
import com.example.lexis.fragments.PracticeFragment;
import com.example.lexis.fragments.ProfileFragment;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switchToSelectedFragment(item);
            return true;
        });
        binding.bottomNavigation.setSelectedItemId(R.id.action_home); // default tab is home
    }

    private void switchToSelectedFragment(MenuItem item) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment feedFragment = new FeedFragment();
        final Fragment practiceFragment = new PracticeFragment();
        //final Fragment profileFragment = ProfileFragment.newInstance(ParseUser.getCurrentUser());
        final Fragment profileFragment = new ProfileFragment();

        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.action_home:
                fragment = feedFragment;
                break;
            case R.id.action_practice:
                fragment = practiceFragment;
                break;
            case R.id.action_profile:
            default:
                fragment = profileFragment;
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }
}