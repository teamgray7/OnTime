package com.unioulu.ontime;

import android.Manifest;
import android.app.ActivityManager;
import android.arch.persistence.room.Room;
import android.content.Context;
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
import android.view.MenuItem;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.unioulu.ontime.database_classes.AppDatabase;
import com.unioulu.ontime.database_classes.DataHolder;
import com.unioulu.ontime.database_classes.EmergencySettingsTable;
import com.unioulu.ontime.database_classes.OtherSettingsTable;
import com.unioulu.ontime.database_classes.UsersTable;
import com.unioulu.ontime.fragment.EmergencyFragment;
import com.unioulu.ontime.fragment.TodayFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EmergencyFragment.OnFragmentInteractionListener,
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

    // Admin user and permission numbers.
    private final boolean ADMIN_USER = false;
    private final int MY_PERMISSIONS_REQUEST = 0;

    // Variables used for application Database
    private static final String TAG_DB = "Database_TAG";
    private static final String DATABASE_NAME = "medicines_DB";
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization and built of Room database
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        // Update the dataHolder (The Singleton)
        final DataHolder holder = DataHolder.getInstance();
        holder.setAppDatabase(appDatabase);

        // Start service
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        if (!AlarmService.isRunning(manager)) {
            Intent serviceIntent = new Intent(this, AlarmService.class);
            AlarmService service = new AlarmService();
            startService(serviceIntent);
        }

        // Toolbar on top of the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the sections.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Set up the tabs.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        // Set up the drawer on the left.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set up the navigation.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Default user initialization
        databaseDefaultInitialization(holder);

        if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) ||
        (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }
    }

    void databaseDefaultInitialization(final DataHolder holder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int active_userID;
                List<String> activeUsernames;

                // If there are users registered or the default user is present, do not add another default user
                int usersCount = appDatabase.usersTableInterface().usersCount();

                // If first time application ran.
                if (usersCount > 0) {
                    // Get active user and update DataHolder
                    activeUsernames = appDatabase.usersTableInterface().getActiveUsers(true);
                    active_userID = appDatabase.usersTableInterface().getUserIdByName(activeUsernames.get(activeUsernames.size() - 1));

                    // Settings the user_id of the shared DataHolder
                    holder.setUser_id(active_userID);
                    holder.setUsername(activeUsernames.get(activeUsernames.size()-1));
                } else {
                    // Adding Default user first time application ran.
                    UsersTable user = new UsersTable(
                            "Default",
                            "default.user@users.com",
                            "1234password",
                            true
                    );
                    appDatabase.usersTableInterface().createUser(user);

                    // Settings the user_id of the shared DataHolder
                    int default_user_id = appDatabase.usersTableInterface().getUserIdByName("Default");
                    holder.setUser_id(default_user_id);
                    holder.setUsername("Default");

                    // Adding Emergency settings contacts
                    EmergencySettingsTable emergencyContact = new EmergencySettingsTable(
                            default_user_id,
                            "Emergency",
                            "112",
                            "NULL"
                    );

                    try {
                        appDatabase.emergencySettingsInterface().insertEmergencyContact(emergencyContact);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    // ---------------------------------- Generating some fake settings time -----------------
                    // Vars only for testing purposes
                    String[] strings_time = {
                            "08:00",
                            "14:00",
                            "20:00"
                    };
                    // --------------------------------- End of fake settings time generation --------------------------

                    // Inserting new settings only if no previous settings are found in the table (Otherwise use update)
                    OtherSettingsTable otherSettings = new OtherSettingsTable(
                            default_user_id,
                            strings_time[0],
                            strings_time[1],
                            strings_time[2],
                            "10"
                    );

                    try {
                        appDatabase.otherSettingsInterface().insertOtherSettings(otherSettings);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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
                    MY_PERMISSIONS_REQUEST);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));

            startActivity(callIntent);
        }
    }

    @Override
    public void viewEditPill(String pillName, String pillImage, String pillAmount, int morning, int afternoon, int evening) {
        // This will not be implemented for normal user.
    }

    @Override
    public void emergencyContactSaved() {
        // This will not be implemented for normal user.
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
                return EmergencyFragment.newInstance(ADMIN_USER);
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
