package com.overdrain.doutorrj;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.mashape.unirest.http.Unirest;
import com.overdrain.doutorrj.utils.RestRequest;
import com.overdrain.doutorrj.view.fragment.MapFragment;
import com.overdrain.doutorrj.view.navigation.Navigation;


public class MainActivity extends AppCompatActivity {

    private static MainActivity INSTANCE;

    public MainActivity() {
        super();
        INSTANCE = this;
    }

    public static MainActivity getInstance() {
        return INSTANCE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new Navigation(savedInstanceState, toolbar);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_container, new MapFragment());
        transaction.commit();

        new RestRequest().execute();

    }


}