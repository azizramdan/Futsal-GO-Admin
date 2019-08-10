package com.example.futsalgoadmin;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class LapanganSayaEdit extends Fragment {
    public LapanganSayaEdit(){}
    LinearLayout view;
    Integer id;
    EditText etNama, etHarga, etFoto;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Edit Lapangan");

        view = (LinearLayout) inflater.inflate(R.layout.lapangan_saya_edit, container, false);
        etNama = view.findViewById(R.id.nama);
        etHarga = view.findViewById(R.id.harga);
        etFoto = view.findViewById(R.id.foto);

        Bundle bundle = this.getArguments();
        id = bundle.getInt("id");
        etNama.setText(bundle.getString("nama"));
        etHarga.setText(bundle.getString("harga"));
        etFoto.setText(bundle.getString("foto"));

        AndroidNetworking.initialize(getActivity());

        Button btnSimpan = view.findViewById(R.id.simpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });
        return view;
    }

    private void simpan() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.post(Konfigurasi.LAPANGAN)
                .addBodyParameter("method", "update")
                .addBodyParameter("id", id.toString())
                .addBodyParameter("nama", etNama.getText().toString())
                .addBodyParameter("harga", etHarga.getText().toString())
                .addBodyParameter("foto", etFoto.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("status")) berhasil();
                            else gagal();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            gagal();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        gagal();
                    }
                });
    }

    private void berhasil() {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Edit lapangan berhasil!", Toast.LENGTH_LONG).show();
    }

    private void gagal() {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Edit lapangan gagal!", Toast.LENGTH_LONG).show();
    }

}
