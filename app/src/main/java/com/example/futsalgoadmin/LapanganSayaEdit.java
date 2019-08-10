package com.example.futsalgoadmin;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
        Button btnHapus = view.findViewById(R.id.hapus);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });
        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapus();
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
                            if(response.getBoolean("status")) simpanBerhasil();
                            else simpanGagal();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            simpanGagal();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        simpanGagal();
                    }
                });
    }

    private void simpanBerhasil() {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Edit lapangan berhasil!", Toast.LENGTH_LONG).show();
    }

    private void simpanGagal() {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Edit lapangan gagal!", Toast.LENGTH_LONG).show();
    }

    private void hapus() {
        new AlertDialog.Builder(getContext())
                .setTitle("Hapus Lapangan")
                .setMessage("Apakah Anda yakin ingin menghapus lapangan?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(
                        "Ya",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Loading...");
                                progressDialog.show();
                                AndroidNetworking.get(Konfigurasi.LAPANGAN)
                                        .addQueryParameter("method", "delete")
                                        .addQueryParameter("id", id.toString())
                                        .setPriority(Priority.LOW)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    if(response.getBoolean("status")) hapusBerhasil();
                                                    else hapusGagal();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    hapusGagal();
                                                }
                                            }
                                            @Override
                                            public void onError(ANError error) {
                                                simpanGagal();
                                            }
                                        });
                            }
                        })
                .setNegativeButton(
                        "Tidak",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //Do Something Here
                            }
                        }).show();
    }

    private void hapusBerhasil() {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Hapus lapangan berhasil!", Toast.LENGTH_LONG).show();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-2).getId(), fragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void hapusGagal() {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Hapus lapangan gagal!", Toast.LENGTH_LONG).show();
    }

}
