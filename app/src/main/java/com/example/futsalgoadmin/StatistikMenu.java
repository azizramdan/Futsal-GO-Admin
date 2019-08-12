package com.example.futsalgoadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.futsalgoadmin.data.LapanganAdapter;
import com.example.futsalgoadmin.data.model.Lapangan;
import com.example.futsalgoadmin.data.model.StatistikAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StatistikMenu extends Fragment {
    public StatistikMenu(){}
    RelativeLayout view;
    private List<Lapangan> dataList;
    private RecyclerView recyclerView;
    Integer id_admin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Statistik Lapangan");

        view = (RelativeLayout) inflater.inflate(R.layout.statistik_menu, container, false);
        recyclerView = view.findViewById(R.id.main_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataList = new ArrayList<>();

        SharedPreferences user = getActivity().getSharedPreferences("dataAdmin", Context.MODE_PRIVATE);
        id_admin = user.getInt("id", 0);

        AndroidNetworking.initialize(getActivity());

        getLapangan();
        return view;
    }

    public void getLapangan() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.get(Konfigurasi.LAPANGAN)
                .addQueryParameter("method", "index")
                .addQueryParameter("id_admin", id_admin.toString())
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject dataObj = data.getJSONObject(i);

                                dataList.add(new Lapangan(
                                        dataObj.getInt("id"),
                                        dataObj.getString("nama"),
                                        dataObj.getString("harga"),
                                        dataObj.getString("foto")
                                ));
                            }
                            StatistikAdapter adapter = new StatistikAdapter(dataList);
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                    }
                });
    }
}
