package com.mcardoso.doutorrj.controller;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mcardoso.doutorrj.MainActivity;
import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.Establishment;
import com.mcardoso.doutorrj.model.EstablishmentsList;
import com.mcardoso.doutorrj.util.RestRequest;
import com.mcardoso.doutorrj.view.list.CustomAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mcardoso on 12/7/15.
 */
public class EstablishmentsController {

    private Context ctx;
    private ListView listView;
    private EstablishmentsList establishmentsList;
    private Map<String, Establishment> data;

    public EstablishmentsController(Context ctx, ListView listView) {
        this.ctx = ctx;
        this.listView = listView;
        this.data = new HashMap<String, Establishment>();
        Resources res = this.ctx.getResources();
        String url = res.getString(R.string.api_base_url) + res.getString(R.string.api_all_establishments);
        fetchData(url);
    }

    public void setEstablishmentsList(EstablishmentsList establishmentsList) {
        this.establishmentsList = establishmentsList;
        CustomAdapter adapter = new CustomAdapter(this.ctx, this.establishmentsList.getResults());
        List<String> data = new ArrayList<String>();
        for (Establishment establishment:this.establishmentsList.getResults()) {
            data.add(establishment.getName());
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.ctx, android.R.layout.simple_list_item_1, data);
        this.listView.setAdapter(adapter2);
    }

    private void fetchData(String url) {
        new RestRequest(url, EstablishmentsList.class, this, "setEstablishmentsList").execute();
    }
}
