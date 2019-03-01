package com.unioulu.ontime;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


        // Floating Action Button !

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // TODO : Seperate fragments from main activity.
    // TODO : Find a way for user/admin having duplicate tabs.
    public static class StatisticsScreenFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public StatisticsScreenFragment() {
            // Empty constructor
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static StatisticsScreenFragment newInstance(int sectionNumber) {
            StatisticsScreenFragment fragment = new StatisticsScreenFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
            return rootView;
        }
    }

    public static class AddPillScreenFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public AddPillScreenFragment() {
            // Empty constructor
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static AddPillScreenFragment newInstance(int sectionNumber) {
            AddPillScreenFragment fragment = new AddPillScreenFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pill, container, false);
            return rootView;
        }
    }

    public static class TodayFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        // Today's fragment vars
        private static final String ARG_SECTION_NUMBER = "section_number";

        private final String TAG = "Today fragment";
        private TextView textView;
        private ArrayList<String> mNames = new ArrayList<>();
        private ArrayList<String> mImageUrls= new ArrayList<>();


        public TodayFragment() {
            // Empty constructor
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static TodayFragment newInstance(int sectionNumber) {
            TodayFragment fragment = new TodayFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }



        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_today, container, false);


            // Initialization of the Recycler views
            Log.d(TAG, "initRecyclerView: recyclerview initialization");

            RecyclerView nextPillsRV = rootView.findViewById(R.id.NextPillRecyclerView);
            RecyclerView alreadyTakenPillsRV = rootView.findViewById(R.id.AlreadyTakenRecyclerView);

            initImageBitmaps();

            RecyclerViewAdapter nextPillsAdapter = new RecyclerViewAdapter(getContext(), mNames, mImageUrls);
            RecyclerViewAdapter alreadyTakenPillsAdapter = new RecyclerViewAdapter(getContext(), mNames, mImageUrls);

            nextPillsRV.setAdapter(nextPillsAdapter);
            alreadyTakenPillsRV.setAdapter(alreadyTakenPillsAdapter);

            nextPillsRV.setLayoutManager(new LinearLayoutManager(getContext()));
            alreadyTakenPillsRV.setLayoutManager(new LinearLayoutManager(getContext()));

            return rootView;
        }

        private void initImageBitmaps(){
            Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

            mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
            mNames.add("Havasu Falls");

            mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
            mNames.add("Trondheim");

            mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
            mNames.add("Portugal");

            mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
            mNames.add("Rocky Mountain National Park");


            mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
            mNames.add("Mahahual");

            mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
            mNames.add("Frozen Lake");


            mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
            mNames.add("White Sands Desert");

            mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
            mNames.add("Austrailia");

            mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
            mNames.add("Washington");

        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if(position == 0) {
                return TodayFragment.newInstance(position + 1);
            } else if(position == 1) {
                return AddPillScreenFragment.newInstance(position + 1);
            } else {
                return StatisticsScreenFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }




}
