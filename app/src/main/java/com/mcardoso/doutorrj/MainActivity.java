package com.mcardoso.doutorrj;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mcardoso.doutorrj.model.EstablishmentsList;
import com.mcardoso.doutorrj.util.LocationTracker;
import com.mcardoso.doutorrj.view.CustomPageAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = "MainActivity";
    private static int UPDATE_LIST_DELAY = 2 * 1000;
    private static int MAX_RETRIES = 5;

    private CustomPageAdapter pageAdapter;
    private ViewPager viewPager;
    private int requestRetries = 0;

    public static EstablishmentsList ESTABLISHMENTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationTracker.getInstance().setup(this);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.pageAdapter = new CustomPageAdapter(getSupportFragmentManager(), this);
        this.viewPager = (ViewPager) findViewById(R.id.pager);
        this.viewPager.setAdapter(this.pageAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(this.viewPager);

        ESTABLISHMENTS = (EstablishmentsList) EstablishmentsList.load(this, EstablishmentsList.class);
        new RetrieveEstablishmentsTask().execute();
    }

    class RetrieveEstablishmentsTask extends AsyncTask<String, Void, EstablishmentsList> {

        @Override
        protected EstablishmentsList doInBackground(String... params) {
            EstablishmentsList list = null;
            String url = getResources().getString(R.string.api_all_establishments);
            try {
                String result = Unirest.get(url).asString().getBody();
                list = new Gson().fromJson(result, EstablishmentsList.class);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return list;
        }

        @Override
        protected void onPostExecute(EstablishmentsList establishmentsList) {
            onRequestFinish(establishmentsList);
        }
    }

    private void onRequestFinish(EstablishmentsList establishmentsList) {
        if (establishmentsList == null) {
            requestRetries += 1;
            if ( requestRetries <= MAX_RETRIES ) {
                Log.d(TAG, "Trying to reach the servers...");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new RetrieveEstablishmentsTask().execute();
                    }
                }, UPDATE_LIST_DELAY);
                new RetrieveEstablishmentsTask().execute();
            } else {
                Log.e(TAG, "Could NOT reach servers!");
            }
        } else {
            establishmentsList.save(getApplicationContext());
            ESTABLISHMENTS = establishmentsList;
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

        if (id == R.id.nav_camara) {
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
}
