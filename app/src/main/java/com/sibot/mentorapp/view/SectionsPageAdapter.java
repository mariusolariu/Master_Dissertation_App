package com.sibot.mentorapp.view;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class SectionsPageAdapter extends FragmentPagerAdapter {

  private final List<Fragment> fragmentList = new ArrayList<>();
  private final List<String> fragmentsTitles = new ArrayList<>();

  SectionsPageAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int i) {
    return fragmentList.get(i);
  }

  @Override
  public int getCount() {
    return fragmentList.size();
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return fragmentsTitles.get(position);
  }

  void addFragment(Fragment fragment, String title){
    fragmentList.add(fragment);
    fragmentsTitles.add(title);
  }

}