package com.unioulu.ontime;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.unioulu.ontime.database_classes.AppDatabase;
import com.unioulu.ontime.database_classes.EmergencySettingsTable;
import com.unioulu.ontime.database_classes.Medicines;
import com.unioulu.ontime.database_classes.OtherSettingsTable;
import com.unioulu.ontime.database_classes.UsersTable;
import com.unioulu.ontime.fragment.EmergencyFragment;
import com.unioulu.ontime.fragment.TodayFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EmergencyFragment.OnFragmentInteractionListener {

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

    // Admin user and permission numbers.
    private final boolean ADMIN_USER = false;
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;


    // Variables used for application Database
    private static final String TAG_DB = "Database_TAG";
    private static final String DATABASE_NAME = "medicines_DB";
    private AppDatabase appDatabase; // Medicine database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization and built of Room database
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the sections.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // TODO: implement a function that initiate default values and settings to database if nothing is already written
        databaseDefaultInitialization();
    }

    void databaseDefaultInitialization() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // If there are users registered or the default user is present, do not add another default user
                int usersCount = appDatabase.usersTableInterface().usersCount();
                Log.d(TAG_DB, "Users count: " + usersCount);
                // If first time application ran !
                if (usersCount > 0)
                    return;

                Log.d(TAG_DB, "Default function called !");

                // Adding Default user first time application ran !
                UsersTable user = new UsersTable(
                        "Dafault",
                        "default.user@users.com",
                        "1234password"
                );

                appDatabase.usersTableInterface().createUser(user);

                Log.d(TAG_DB, "Added: " + user.toString());

                // Adding Emergency settings contacts
                EmergencySettingsTable emergencyContact = new EmergencySettingsTable(
                        "Emergency",
                        "112",
                        "NULL"
                );
                appDatabase.emergencySettingsInterface().insertEmergencyContact(emergencyContact);
                Log.d(TAG_DB, "Added: " + emergencyContact.toString());



                // Adding settings
                // ---------------------------------- Generating some fake settings time -----------------
                // Vars only for testing purposes
                String[] strings_time   ={" 8:00:00",
                                     "13:00:00",
                                     "19:30:00",
                                     "22:00:00"};

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

                Long[] long_time = new Long[4];
                try{
                    for (int i=0; i< strings_time.length; i++){
                        Date date = sdf.parse(strings_time[i]);
                        long_time[i] = date.getTime();
                    }


                }catch (ParseException e){
                    e.printStackTrace();
                }
                // --------------------------------- End of fake settings time generation --------------------------
                // Inserting new settings only if no previous settings are found in the table (Otherwise use update)
                OtherSettingsTable otherSettings = new OtherSettingsTable(
                        long_time[0],
                        long_time[1],
                        long_time[2],
                        long_time[3],
                        "10"
                );

                appDatabase.otherSettingsInterface().insertOtherSettings(otherSettings);
                Log.d(TAG_DB, "Added: " + otherSettings.toString());
            }
        }).start();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            Intent loginActivityTransition = new Intent(MainActivity.this, LoginRegisterActivity.class);
            startActivity(loginActivityTransition);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void makeCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));

            startActivity(callIntent);
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
                return TodayFragment.newInstance(position + 1);
            } else if(position == 1) {
                return EmergencyFragment.newInstance(position + 1, ADMIN_USER);
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
