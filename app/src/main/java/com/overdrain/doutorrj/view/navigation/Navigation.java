package com.overdrain.doutorrj.view.navigation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.overdrain.doutorrj.MainActivity;
import com.overdrain.doutorrj.R;
import com.overdrain.doutorrj.event.EventManager;
import com.overdrain.doutorrj.event.navigation.NavigationEvent;
import com.overdrain.doutorrj.event.navigation.handler.AboutUsHandler;
import com.overdrain.doutorrj.model.navigation.NavigationItem;
import com.squareup.picasso.Picasso;

/**
 * Created by mcardoso on 6/4/15.
 */
public class Navigation {

    public Navigation(Bundle savedInstanceState, Toolbar toolbar) {
        createImageLoader();
        createEventHandlers();

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(MainActivity.getInstance())
                .withHeaderBackground(R.drawable.header)
                .withSavedInstance(savedInstanceState)
                .build();

        new DrawerBuilder()
                .withActivity(MainActivity.getInstance())
                .withToolbar(toolbar)
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .withAccountHeader(headerResult)
                .addStickyDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.nav_about_us)
                                .withIdentifier(NavigationItem.ABOUT_US.getId())
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    @Override
                    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem drawerItem) {

                        NavigationEvent event = NavigationItem.findById(drawerItem.getIdentifier()).getEvent();
                        EventManager.getInstance().dispatch(event);
                        return false;
                    }
                })
                .build();
    }

    private void createEventHandlers() {
        new AboutUsHandler(NavigationEvent.ABOUT_US);
    }

    private void createImageLoader() {
        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return null;
            }
        });
    }
}
