package com.mcardoso.doutorrj.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.Establishment;
import com.mcardoso.doutorrj.model.EstablishmentsList;

import java.util.List;

/**
 * Created by mcardoso on 12/10/15.
 */
public class ListFragment extends Fragment {

    private static String TAG = "ListFragment";
    private Bundle savedInstanceState;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        new RetrieveEstablishmentsTask().execute();
        Log.d(TAG, "Testing3");
        this.savedInstanceState = savedInstanceState;
        this.view = inflater.inflate(R.layout.fragment_list, container, false);
        return this.view;
    }

    private void updateList(EstablishmentsList establishmentList) {
        ListView listView = (ListView) this.view.findViewById(R.id.listView);
        listView.setAdapter(new CustomListAdapter(establishmentList.getResults()));
    }

    class RetrieveEstablishmentsTask extends AsyncTask<String, Void, EstablishmentsList> {

        private EstablishmentsList list;

        @Override
        protected EstablishmentsList doInBackground(String... params) {
            Log.d(TAG, "Testing");
            String url = getResources().getString(R.string.api_all_establishments);
            try {
                String result = Unirest.get(url).asString().getBody();
                this.list = new Gson().fromJson(result, EstablishmentsList.class);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            Log.d(TAG, "Testing2");

            return this.list;
        }

        @Override
        protected void onPostExecute(EstablishmentsList establishmentsList) {
            updateList(this.list);
        }
    }

    class CustomListAdapter extends BaseAdapter {

        private List<Establishment> establishments;

        public CustomListAdapter(List<Establishment> establishments) {
            super();
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
                LayoutInflater inflater = getLayoutInflater(savedInstanceState);
                convertView = inflater.inflate(R.layout.list_row, null);
            }

            TextView name = (TextView)convertView.findViewById(R.id.row_title);
            TextView summary = (TextView)convertView.findViewById(R.id.row_distance);

            name.setText(this.establishments.get(position).getName());
            summary.setText(this.establishments.get(position).getCnpj());

            return convertView;
        }
    }
}
