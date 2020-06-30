package com.dnieln7.roadwatchman.ui.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dnieln7.roadwatchman.R;
import com.google.android.material.navigation.NavigationView;

public class AppActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavController controller = Navigation.findNavController(this, R.id.app_nav_controller);

        setSupportActionBar(toolbar);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.reports,
                R.id.profile,
                R.id.settings,
                R.id.about
        ).setOpenableLayout(drawer).build();

        NavigationUI.setupActionBarWithNavController(this, controller, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, controller);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.app_nav_controller);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
