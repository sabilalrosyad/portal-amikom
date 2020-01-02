package com.teamtf.portalamikom.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamtf.portalamikom.MainActivity;
import com.teamtf.portalamikom.R;
import com.teamtf.portalamikom.adapter.ViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private SharedPreferences prefs;
    private MainActivity main;
    private ActionBar actionBar;
    private ViewPager vpMain;
    private BottomNavigationView bnvMain;
    private ViewPagerAdapter adapter;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(){
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        prefs = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        main = (MainActivity) getActivity();
        assert main != null;
        actionBar = main.getSupportActionBar();

        vpMain = v.findViewById(R.id.vp_main);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        setUpViewPager();

        bnvMain = v.findViewById(R.id.bnv_main);
        bnvMain.setOnNavigationItemSelectedListener(this);
        bnvMain.setSelectedItemId(R.id.mi_news);

        return v;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.mi_events:
                vpMain.setCurrentItem(0);
                Log.d("MENU ITEM", "onNavigationItemSelected: 0 " + bnvMain.getMenu());
                return true;
            case R.id.mi_account:
                if (!prefs.getBoolean("isLogin", false)) {
                    Log.d("MENU ITEM", "onNavigationItemSelected: 2 1 " + bnvMain.getMenu());
                    showAuth();
                    return false;
                } else {
                    vpMain.setCurrentItem(2);
                    Log.d("MENU ITEM", "onNavigationItemSelected: 2 2 " + bnvMain.getMenu());
                    return true;
                }
            default:
                vpMain.setCurrentItem(1);
                Log.d("MENU ITEM", "onNavigationItemSelected: 1 " + bnvMain.getMenu());
                return true;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bnvMain.getMenu().getItem(position).setChecked(true);
        Log.d("ITEM POSITION", "onPageSelected: " + bnvMain.getMenu().getItem(position));

        Fragment fragment = adapter.getFragment(position);
        if (fragment != null) {
            fragment.onResume();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setUpViewPager() {

        if (!prefs.getBoolean("isLogin", false)) {
            adapter.addFragment(EventsFragment.newInstance(), getString(R.string.event));
            adapter.addFragment(NewsFragment.newInstance(), getString(R.string.news));
        } else {
            adapter.addFragment(EventsFragment.newInstance(), getString(R.string.event));
            adapter.addFragment(NewsFragment.newInstance(), getString(R.string.news));
            adapter.addFragment(AccountFragment.newInstance(), getString(R.string.account));
        }

        vpMain.setOffscreenPageLimit(3);
        vpMain.setAdapter(adapter);
    }

    private void showAuth() {
        main.replaceFragment(AuthFragment.newInstance(), getString(R.string.tag_auth_fragment));

        actionBar.setTitle(R.string.login);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }
}
