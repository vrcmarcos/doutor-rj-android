package com.overdrain.doutorrj.view.navigation;

import android.app.Activity;
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
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.overdrain.doutorrj.R;
import com.overdrain.doutorrj.event.EventManager;
import com.overdrain.doutorrj.event.navigation.NavigationEvent;
import com.overdrain.doutorrj.event.navigation.handler.FacebookLoginHandler;
import com.overdrain.doutorrj.model.navigation.NavigationItem;
import com.squareup.picasso.Picasso;

/**
 * Created by mcardoso on 6/4/15.
 */
public class Navigation {

    public Navigation(Activity activity, Bundle savedInstanceState, Toolbar toolbar) {
        createImageLoader();
        createEventHandlers();

//        //Navigation Drawer Setup
//        final IProfile profile = new ProfileDrawerItem()
//                .withName("Marcos Cardoso")
//                .withEmail("vrcmarcos@gmail.com")
//                .withIcon(Uri.parse("https://graph.facebook.com/vrcmarcos/picture?width=9999"));

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .withSavedInstance(savedInstanceState)
                .build();

        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new ProfileSettingDrawerItem().withName("ProSett")
                )
                .addStickyDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.nav_fb_login)
                                .withIdentifier(NavigationItem.FACEBOOK_LOGIN.getId())
                                .withIcon(R.drawable.nav_ico_fb)
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
        new FacebookLoginHandler(NavigationEvent.FACEBOOK_LOGIN);
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
