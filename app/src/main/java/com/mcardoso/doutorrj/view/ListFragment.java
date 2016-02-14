package com.mcardoso.doutorrj.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.establishment.Establishment;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

/**
 * Created by mcardoso on 12/10/15.
 */
public class ListFragment extends NotifiableFragment {

    private static String TAG = "ListFragment";

    @Override
    protected boolean useLoadingScreen() {
        return true;
    }

    @Override
    protected Integer getTargetLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    public void draw() {
        final ListView listView = (ListView) this.view.findViewById(R.id.listView);
        listView.setAdapter(new CustomListAdapter(getContext(), R.layout.list_row, super.getCurrentList()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Testing");
            }
        });
    }

    class CustomListAdapter extends ArrayAdapter<Establishment> {

        private Context context;
        private List<Establishment> establishments;

        public CustomListAdapter(Context context, int resource, List<Establishment> objects) {
            super(context, resource, objects);
            this.context = context;
            this.establishments = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if ( convertView == null ) {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_row, parent, false);
            }
            Establishment establishment = this.establishments.get(position);

            String formattedName = WordUtils.capitalizeFully(establishment.getName());
            ((TextView) convertView.findViewById(R.id.row_title)).setText(formattedName);

            Integer typeTextId = establishment.isPrivateEstablishment() ? R.string.type_private : R.string.type_public;
            ((TextView) convertView.findViewById(R.id.row_type)).setText(typeTextId);

            Float distanceInMeters = LOCATION.distanceTo(establishment.getLocation());
            String formattedDistance;
            if (distanceInMeters > 1000f) {
                formattedDistance = String.format("%.2fkm", distanceInMeters/1000f);
            } else {
                formattedDistance = String.format("%.0fm", distanceInMeters);
            }
            ((TextView)convertView.findViewById(R.id.row_distance)).setText(formattedDistance);

            return convertView;
        }
    }
}