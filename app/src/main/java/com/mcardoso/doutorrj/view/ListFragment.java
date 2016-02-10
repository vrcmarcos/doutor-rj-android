package com.mcardoso.doutorrj.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.establishment.Establishment;

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
//        ListView listView = (ListView) this.view.findViewById(R.id.listView);
//        listView.setAdapter(
//                new CustomListAdapter(savedInstanceState, super.getCurrentList())
//        );
    }

    @Override
    protected void onCurrentListReceived(List<Establishment> currentList) {

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

//            Establishment establishment = this.establishments.get(position);
//
//            ((TextView)convertView.findViewById(R.id.row_title)).setText(establishment.getName());
//
//            Float distanceInMeters = LOCATION.distanceTo(establishment.getLocation());
//            String formattedDistance;
//            if (distanceInMeters > 1000f) {
//                formattedDistance = String.format("%.2fkm", distanceInMeters/1000f);
//            } else {
//                formattedDistance = String.format("%.0fm", distanceInMeters);
//            }
//            ((TextView)convertView.findViewById(R.id.row_distance)).setText(formattedDistance);

            return convertView;
        }
    }
}
