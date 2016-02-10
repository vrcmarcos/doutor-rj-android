package com.mcardoso.doutorrj.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.establishment.Establishment;
import com.mcardoso.doutorrj.util.NotifiableData;

import java.util.List;

/**
 * Created by mcardoso on 12/17/15.
 */
public abstract class NotifiableFragment extends Fragment {

    protected View view;
    protected Bundle savedInstanceState;

    public abstract void draw();

    protected abstract boolean useLoadingScreen();
    protected abstract Integer getTargetLayoutId();
    protected abstract void onCurrentListReceived(List<Establishment> currentList);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NotifiableData.getInstance().registerFragment(this);
        int layoutId = this.useLoadingScreen() ? R.layout.fragment_load : this.getTargetLayoutId();
        this.view = inflater.inflate(layoutId, container, false);
        this.savedInstanceState = savedInstanceState;
        return this.view;
    }


    protected void changeLayout() {
        if ( this.useLoadingScreen() ) {
            RelativeLayout layout = (RelativeLayout) this.view.findViewById(R.id.fragment_load);
            layout.removeAllViews();
            layout.addView(View.inflate(this.view.getContext(), this.getTargetLayoutId(), null));
        }
    }
}