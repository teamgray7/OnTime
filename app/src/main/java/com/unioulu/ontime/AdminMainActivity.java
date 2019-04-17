package com.unioulu.ontime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

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
        AddPillScreenFragment.OnFragmentInteractionListener,
        OtherSettingsFragment.OnFragmentInteractionListener,
        TodayFragment.OnFragmentInteractionListener {

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

        // Hiding the emergency settings and other settings tab from tabLayout
        ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(4).setVisibility(View.GONE);
        ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(5).setVisibility(View.GONE);

        // Set the first tab as selected tab
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        if(tab != null) {
            tab.select();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            TabLayout tabLayout = (TabLayout) findViewById(R.id.admin_tabs);
            TabLayout.Tab tabSettings = tabLayout.getTabAt(3);

            // If back button is pressed,
            //   - If it is pressed on other/emergency settings, go back to settings.
            //   - Else if it is one of the visible tabs, do as super.onBackPressed()
            if(tabLayout.getSelectedTabPosition() > 3) {
                if (tabSettings != null) {
                    tabSettings.select();
                }
            } else {
                super.onBackPressed();
            }
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
    public void settingsAsAdmin() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.admin_tabs);
        TabLayout.Tab tabSettings = tabLayout.getTabAt(3);

        if(tabSettings != null) {
            tabSettings.select();
        }
    }

    @Override
    public void settingsEmergencyAsAdmin() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.admin_tabs);
        TabLayout.Tab tabEmergencySettings = tabLayout.getTabAt(4);

        if(tabEmergencySettings != null) {
            tabEmergencySettings.select();
        }
    }

    @Override
    public void otherSettingsAsAdmin() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.admin_tabs);
        TabLayout.Tab tabOtherSettings = tabLayout.getTabAt(5);

        if(tabOtherSettings != null) {
            tabOtherSettings.select();
        }
    }

    @Override
    public void makeCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        startActivity(callIntent);
    }

    @Override
    public void pillCancel() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.admin_tabs);
        TabLayout.Tab tabOtherSettings = tabLayout.getTabAt(0);

        if(tabOtherSettings != null) {
            tabOtherSettings.select();
        }
    }

    @Override
    public void pillSaved() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.admin_tabs);
        TabLayout.Tab tabOtherSettings = tabLayout.getTabAt(0);

        if(tabOtherSettings != null) {
            tabOtherSettings.select();
        }
    }

    /*
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
    } */

    @Override
    public void viewEditPill(String pillName, String pillImage) {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.admin_tabs);
        TabLayout.Tab tabSettings = tabLayout.getTabAt(1);

        if(tabSettings != null) {
            tabSettings.select();

            AddPillScreenFragment fragment = (AddPillScreenFragment) getSupportFragmentManager()
                    .findFragmentByTag("android:switcher:" + mViewPager.getId() + ":" + mViewPager.getCurrentItem());

            fragment.setFragmentDetails(pillName, pillImage);
        }
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
                return TodayFragment.newInstance();
            } else if(position == 1) {
                return AddPillScreenFragment.newInstance();
            } else if(position == 2) {
                return StatisticsScreenFragment.newInstance(position + 1);
            } else if(position == 3) {
                return SettingsFragment.newInstance(position + 1);
            } else if(position == 4) {
                return EmergencyFragment.newInstance(position + 1, true);
            } else if(position == 5) {
                return OtherSettingsFragment.newInstance();
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            // Show 6 total pages. (2 of them are hidden...)
            return 6;
        }
    }
}
