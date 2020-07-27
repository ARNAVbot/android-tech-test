package com.bridge.androidtechnicaltest.ui;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.bridge.androidtechnicaltest.InternetBroadCastReceiver;
import com.bridge.androidtechnicaltest.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bridge.androidtechnicaltest.App;
import com.bridge.androidtechnicaltest.R;
import com.bridge.androidtechnicaltest.db.PupilDao;
import com.bridge.androidtechnicaltest.network.PupilService;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private ActivityMainBinding binding;

    private static final String TAG = MainActivity.class.getCanonicalName();
    private InternetBroadCastReceiver broadCastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        broadCastReceiver = new InternetBroadCastReceiver();
        registerReceiver(broadCastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        navController = Navigation.findNavController(this, R.id.navigationHostFragment);
        NavInflater navInflater = navController.getNavInflater();
        NavGraph navGraph = navInflater.inflate(R.navigation.nav_graph);
        navController.setGraph(navGraph);
        NavigationUI.setupActionBarWithNavController(
                this, navController);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCastReceiver);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }
}