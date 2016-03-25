package com.mcardoso.doutorrj;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.mcardoso.doutorrj.helper.AnalyticsHelper;
import com.mcardoso.doutorrj.helper.EstablishmentHelper;
import com.mcardoso.doutorrj.helper.LocationHelper;
import com.mcardoso.doutorrj.helper.PopUpHelper;
import com.mcardoso.doutorrj.model.establishment.Establishment;
import com.mcardoso.doutorrj.model.establishment.EstablishmentType;
import com.mcardoso.doutorrj.view.ListFragment;
import com.mcardoso.doutorrj.view.MapFragment;
import com.mcardoso.doutorrj.view.NotifiableFragment;
import com.newrelic.agent.android.NewRelic;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListFragment.EstablishmentListCallback {

    private static String TAG = "MainActivity";

    private static final int TOAST_DELAY = 2000;

    private CustomPageAdapter pageAdapter;
    private ViewPager viewPager;
    private LocationHelper locationHelper;
    private String version;
    private boolean userTouchedBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userTouchedBackButton = false;

        if ( savedInstanceState == null ) {
            NewRelic.withApplicationToken(
                    "AAf4e457dc725b55796e71d0bbd20869562d6358e1"
            ).start(this.getApplication());

            setContentView(R.layout.activity_main);

            new EstablishmentHelper(this);
            this.locationHelper = new LocationHelper(this);

            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            this.pageAdapter = new CustomPageAdapter(getSupportFragmentManager());
            this.viewPager = (ViewPager) findViewById(R.id.pager);
            this.viewPager.setAdapter(this.pageAdapter);
            this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    AnalyticsHelper.trackScreen(getBaseContext(), getViewPageTitle(position));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(this.viewPager);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if ( !this.userTouchedBackButton ) {
            this.userTouchedBackButton = true;
            Toast.makeText(this, R.string.app_close_message, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    userTouchedBackButton = false;
                }
            }, TOAST_DELAY);
        } else {
            this.finishAffinity();
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            PopUpHelper.show(this, PopUpHelper.PopUpBrand.ABOUT, true, this.getVersion());
        }

        return super.onOptionsItemSelected(item);
    }

    private String getVersion() {
        if (this.version == null) {
            try {
                PackageInfo info = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                this.version = info.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, e.getMessage(), e);
                this.version = "1.0.0";
            }
        }

        return this.version;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        EstablishmentType type = EstablishmentType.getTypeById(item.getItemId());
        NotifiableFragment.broadcastEstablishmentTypeChange(type);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.locationHelper.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.locationHelper.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.locationHelper.start();
    }

    @Override
    public void userSelected(Establishment establishment) {
        Integer mapFragmentPosition = 0;
        this.viewPager.setCurrentItem(mapFragmentPosition);
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + this.viewPager.getId() + ":" + this.pageAdapter.getItemId(mapFragmentPosition)
        );
        mapFragment.update(establishment);
    }

    public String getViewPageTitle(int position) {
        int resourceId;

        switch (position) {
            case 1:
                resourceId = R.string.list_title;
                break;
            default:
                resourceId = R.string.map_title;
                break;
        }
        return getResources().getString(resourceId);
    }

    class CustomPageAdapter extends FragmentPagerAdapter {

        public CustomPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;

            switch(position) {
                case 1:
                    fragment = new ListFragment();
                    break;
                default:
                    fragment = new MapFragment();
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            return getViewPageTitle(position);
        }
    }
}
