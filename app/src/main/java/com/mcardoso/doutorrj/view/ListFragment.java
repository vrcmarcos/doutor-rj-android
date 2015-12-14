package com.mcardoso.doutorrj.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mcardoso.doutorrj.MainActivity;
import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.Establishment;
import com.mcardoso.doutorrj.util.LocationTracker;

import java.util.List;

/**
 * Created by mcardoso on 12/10/15.
 */
public class ListFragment extends Fragment {

    private static String TAG = "ListFragment";
    private static int UPDATE_LIST_DELAY = 2 * 1000;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_list, container, false);
        this.populate(savedInstanceState);
        return this.view;
    }

    private void populate(final Bundle savedInstanceState) {
        Log.d(TAG, "Tentando popular...");
        if (MainActivity.ESTABLISHMENTS != null) {
            ListView listView = (ListView) this.view.findViewById(R.id.listView);
            listView.setAdapter(
                    new CustomListAdapter(savedInstanceState, MainActivity.ESTABLISHMENTS.getResults())
            );
            this.view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        } else {
            Log.d(TAG, "Agendando popular...");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    populate(savedInstanceState);
                }
            }, UPDATE_LIST_DELAY);
        }
    }

    class CustomListAdapter extends BaseAdapter {

        private List<Establishment> establishments;
        private Bundle savedInstanceState;

        public CustomListAdapter(Bundle savedInstanceState, List<Establishment> establishments) {
            super();
            this.savedInstanceState = savedInstanceState;
            this.establishments = establishments;
        }

        @Override
        public int getCount() {
            return this.establishments.size();
        }

        @Override
        public Object getItem(int position) {
            return this.establishments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater(this.savedInstanceState);
                convertView = inflater.inflate(R.layout.list_row, null);
            }

            TextView name = (TextView)convertView.findViewById(R.id.row_title);
            TextView distance = (TextView)convertView.findViewById(R.id.row_distance);

            Establishment establishment = this.establishments.get(position);
            Float distanceInMeters = LocationTracker.getInstance().getCurrentLocation().distanceTo(
                    establishment.getLocation());

            name.setText(establishment.getName());
            distance.setText(distanceInMeters+"m");

            return convertView;
        }
    }
}
