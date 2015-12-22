package com.mcardoso.doutorrj.view;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.EstablishmentsList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcardoso on 12/17/15.
 */
public abstract class NotifiableFragment extends Fragment {

    public static List<NotifiableFragment> NOTIFIABLE_FRAGMENTS = new ArrayList<>();

    protected View view;
    protected Bundle savedInstanceState;

    protected abstract Integer getTargetLayoutId();
    public abstract void handleNotification(EstablishmentsList establishmentsList);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NOTIFIABLE_FRAGMENTS.add(this);
        this.view = inflater.inflate(R.layout.fragment_load, container, false);
        this.savedInstanceState = savedInstanceState;
        return this.view;
    }

    protected void changeLayout() {
        RelativeLayout layout = (RelativeLayout) this.view.findViewById(R.id.fragment_load);
        layout.removeAllViews();
        layout.addView(View.inflate(this.view.getContext(), this.getTargetLayoutId(), null));
    }

    public static void broadcastLocation(Location location) {

    }
}
