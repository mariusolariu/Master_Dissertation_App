package com.example.myapplication;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private static final String MAIN_TAG = "MainActivity";

  private SectionsPageAdapter sectionsPageAdapter;

  private ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

    viewPager = findViewById(R.id.viewPager);
    setupViewPager(viewPager);

    TabLayout tabLay = findViewById(R.id.tabLayout);
    tabLay.setupWithViewPager(viewPager);
    setupViewPager(viewPager);
  }

  private void setupViewPager(ViewPager viewPager) {
    SectionsPageAdapter sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

    sectionsPageAdapter.addFragment(new ProgressFragment(), "Progress");
    sectionsPageAdapter.addFragment(new UpcomingFragment(), "Upcoming");
    sectionsPageAdapter.addFragment(new FeedbackFragment(), "Feedback");
    sectionsPageAdapter.addFragment(new PastFragment(), "Past");

    viewPager.setAdapter(sectionsPageAdapter );
  }
}
