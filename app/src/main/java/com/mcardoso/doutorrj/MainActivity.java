package com.mcardoso.doutorrj;

import android.annotation.TargetApi;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.facebook.appevents.AppEventsLogger;
import com.mcardoso.doutorrj.helper.EstablishmentHelper;
import com.mcardoso.doutorrj.helper.LocationHelper;
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

    private CustomPageAdapter pageAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NewRelic.withApplicationToken(
                "AAf4e457dc725b55796e71d0bbd20869562d6358e1"
        ).start(this.getApplication());

        setContentView(R.layout.activity_main);

        LocationHelper.getInstance().setContext(this);

        new EstablishmentHelper(this);

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

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(this.viewPager);

        this.updateNavigationHeaderContent();
    }

    private void updateNavigationHeaderContent() {
        View headerView = ((NavigationView) this.findViewById(R.id.nav_view)).getHeaderView(0);
        BootstrapButton versionLabel = (BootstrapButton) headerView.findViewById(R.id.version_label);
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = "v" + info.versionName;
            versionLabel.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
            versionLabel.setVisibility(View.INVISIBLE);
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
//        getMenuInflater().inflate(R.menu.main, menu);
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
        EstablishmentType type = EstablishmentType.getTypeById(item.getItemId());
        NotifiableFragment.broadcastEstablishmentTypeChange(type);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationHelper.getInstance().onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return PackageManager.PERMISSION_GRANTED == this.checkSelfPermission(permission);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void askPermission(String permission) {
        this.requestPermissions(new String[]{permission}, 1);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if( hasPermission(permissions[0]) ) {
            LocationHelper.getInstance().setup();
        }
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
    }
}
