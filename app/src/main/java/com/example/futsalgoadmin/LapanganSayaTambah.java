package com.example.futsalgoadmin;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

public class LapanganSayaTambah extends Fragment {
    public LapanganSayaTambah(){}
    LinearLayout view;
    Integer idAdmin;
    EditText etNama, etHarga, etFoto;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Tambah Lapangan");

        view = (LinearLayout) inflater.inflate(R.layout.lapangan_saya_tambah, container, false);
        etNama = view.findViewById(R.id.nama);
        etHarga = view.findViewById(R.id.harga);
        etFoto = view.findViewById(R.id.foto);

        AndroidNetworking.initialize(getActivity());

        SharedPreferences data = getActivity().getSharedPreferences("dataAdmin", Context.MODE_PRIVATE);
        idAdmin = data.getInt("id", 0);

        Button btnTambah = view.findViewById(R.id.tambah);
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambah();
            }
        });
        return view;
    }

    private void tambah() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.post(Konfigurasi.LAPANGAN)
                .addBodyParameter("method", "store")
                .addBodyParameter("id_admin", idAdmin.toString())
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
        Toast.makeText(getActivity(), "Tambah lapangan berhasil!", Toast.LENGTH_LONG).show();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void gagal() {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Tambah lapangan gagal!", Toast.LENGTH_LONG).show();
    }
}
