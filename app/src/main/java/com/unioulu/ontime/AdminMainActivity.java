package com.unioulu.ontime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.unioulu.ontime.fragment.AddPillScreenFragment;
import com.unioulu.ontime.fragment.EmergencyFragment;
import com.unioulu.ontime.fragment.OtherSettingsFragment;
import com.unioulu.ontime.fragment.SettingsFragment;
import com.unioulu.ontime.fragment.StatisticsScreenFragment;
import com.unioulu.ontime.fragment.TodayFragment;

public class AdminMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SettingsFragment.OnFragmentInteractionListener,
        EmergencyFragment.OnFragmentInteractionListener,
        AddPillScreenFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    // Admin user
    private final boolean ADMIN_USER = true;
    private final String TAG_EMERGENCY_FRAGMENT = "fragment_emergency";
    private final String TAG_OTHERSETTINGS_FRAGMENT = "fragment_other_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the sections.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.admin_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.admin_tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.admin_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_signOut) {
            Intent signOutTransition = new Intent(AdminMainActivity.this, MainActivity.class);
            signOutTransition.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(signOutTransition);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void settingsEmergencyAsAdmin() {
        EmergencyFragment emergencyFragment = EmergencyFragment.newInstance(3, ADMIN_USER);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim
                .enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.admin_container, emergencyFragment, TAG_EMERGENCY_FRAGMENT);
        transaction.addToBackStack(TAG_EMERGENCY_FRAGMENT);
        transaction.commit();
    }

    @Override
    public void otherSettingsAsAdmin() {
        // TODO : Design and place this fragment.
        Log.d("OtherSettings", "Other settings clicked !");
        OtherSettingsFragment otherSettingsFragment = OtherSettingsFragment.newInstance(5, ADMIN_USER);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim
                .enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.admin_container, otherSettingsFragment, TAG_OTHERSETTINGS_FRAGMENT);
        transaction.addToBackStack(TAG_OTHERSETTINGS_FRAGMENT);
        transaction.commit();
    }

    @Override
    public void makeCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        startActivity(callIntent);
    }

    @Override
    public void pillDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
        builder.setTitle(getResources().getString(R.string.pillDeleteDialogTitle));
        builder.setMessage(R.string.pillDeleteDialogMessage)
                .setPositiveButton(R.string.pillDeleteConfirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do positive things here..
                    }
                })
                .setNegativeButton(R.string.pillDeleteCancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog...
                    }
                });

        builder.create().show();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return TodayFragment.newInstance(position + 1);
            } else if(position == 1) {
                return AddPillScreenFragment.newInstance(position + 1);
            } else if(position == 2) {
                return StatisticsScreenFragment.newInstance(position + 1);
            } else if(position == 3) {
                return SettingsFragment.newInstance(position + 1);
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }
}
