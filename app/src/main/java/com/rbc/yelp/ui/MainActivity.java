package com.rbc.yelp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rbc.yelp.R;
import com.rbc.yelp.databinding.MainActivityBinding;
import com.rbc.yelp.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityBinding binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}