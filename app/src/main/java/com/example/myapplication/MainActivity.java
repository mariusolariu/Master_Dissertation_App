package com.example.myapplication;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String MAIN_TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);

        Toolbar toolbar =  findViewById(R.id.toolbar);


//        setupDrawer();

        drawerLayout =  findViewById(R.id.nav_drawer);
        navigationView =  findViewById(R.id.nv);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //set view pager
        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        TabLayout tabLay = findViewById(R.id.tabLayout);
        tabLay.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
    }

//    @Override
//    public void onPostCreate(@androidx.annotation.Nullable Bundle savedInstanceState, @androidx.annotation.Nullable PersistableBundle persistentState) {
//        super.onPostCreate(savedInstanceState, persistentState);
//    }

    //    private void setupDrawer() {
//        drawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
//        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
//
//        drawerLayout.addDrawerListener(drawerToggle);
//
//        drawerToggle.syncState();
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        navigationView = (NavigationView) findViewById(R.id.nv);
//
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                int itemId = menuItem.getItemId();
//
//                switch (itemId) {
//                    case R.id.sosButton:
//                        Toast.makeText(MainActivity.this, "Please help", Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case R.id.resetPassB:
//                        Toast.makeText(MainActivity.this, "Reset Pass", Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case R.id.signOutB:
//                        Toast.makeText(MainActivity.this, "Sign out", Toast.LENGTH_SHORT).show();
//                        break;
//
//                    default:
//                        return true;
//                }
//
//                return true;
//            }
//
//        });
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        sectionsPageAdapter.addFragment(new ProgressFragment(), "Progress");
        sectionsPageAdapter.addFragment(new UpcomingFragment(), "Upcoming");
        sectionsPageAdapter.addFragment(new FeedbackFragment(), "Feedback");
        sectionsPageAdapter.addFragment(new PastFragment(), "Past");

        viewPager.setAdapter(sectionsPageAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        switch (itemId) {
            case R.id.sosButton:
                Toast.makeText(MainActivity.this, "Please help", Toast.LENGTH_SHORT).show();
                break;

            case R.id.resetPassB:
                Toast.makeText(MainActivity.this, "Reset Pass", Toast.LENGTH_SHORT).show();
                break;

            case R.id.signOutB:
                Toast.makeText(MainActivity.this, "Sign out", Toast.LENGTH_SHORT).show();
                break;

            default:
                return true;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
