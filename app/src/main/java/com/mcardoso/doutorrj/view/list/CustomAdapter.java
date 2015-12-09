package com.mcardoso.doutorrj.view.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.Establishment;
import com.mcardoso.doutorrj.model.EstablishmentsList;

import java.util.List;

/**
 * Created by mcardoso on 12/7/15.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    private Context ctx;
    private List<Establishment> data;

    public CustomAdapter(Context context, List<Establishment> data) {
        super(context, R.layout.row);
        this.ctx = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.ctx);

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.populate(this.data.get(position));

        return convertView;
    }
}
