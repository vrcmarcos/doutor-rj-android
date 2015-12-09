package com.mcardoso.doutorrj.view.list;

import android.view.View;
import android.widget.TextView;

import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.Establishment;

/**
 * Created by mcardoso on 12/8/15.
 */
public class ViewHolder {

    private View row = null;

    public ViewHolder(View row) {
        this.row = row;
    }

    public void populate(Establishment establishment) {
        TextView rowTitle = (TextView) this.row.findViewById(R.id.row_title);
        rowTitle.setText(establishment.getName());
    }
}
