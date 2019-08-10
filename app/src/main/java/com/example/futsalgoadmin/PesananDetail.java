package com.example.futsalgoadmin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Boolean.FALSE;

public class PesananDetail extends Fragment {
    public PesananDetail() {}
    LinearLayout view;
    Integer idPesanan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = (LinearLayout) inflater.inflate(R.layout.pesanan_detail, container, false);
        getActivity().setTitle("Detail Pemesanan");

        TextView tvIdPesanan = view.findViewById(R.id.id_pesanan);
        TextView tvNamaLapangan = view.findViewById(R.id.nama_lapangan);
        TextView tvWaktuPilihTanggal = view.findViewById(R.id.waktu_pilih_tanggal);
        TextView tvWaktuPilihJam = view.findViewById(R.id.waktu_pilih_jam);
        TextView tvNamaPemesan = view.findViewById(R.id.nama_pemesan);
        TextView tvTelp = view.findViewById(R.id.telp);
        TextView tvStatus = view.findViewById(R.id.status);
        Button btnSudahBayar = view.findViewById(R.id.sudah_bayar);
        Button btnBatalkan = view.findViewById(R.id.batal);

        Bundle bundle = this.getArguments();
        idPesanan = bundle.getInt("id");
        tvIdPesanan.setText("ID pesanan " + idPesanan.toString());
        tvNamaLapangan.setText("Lapangan: " + bundle.getString("nama_lapangan"));
        tvWaktuPilihTanggal.setText(bundle.getString("waktu_pilih_tanggal"));
        tvWaktuPilihJam.setText(bundle.getString("waktu_pilih_jam"));
        tvNamaPemesan.setText("Nama pemesan: " + bundle.getString("nama_pemesan"));
        tvTelp.setText("No telp: " + bundle.getString("telp"));
        tvStatus.setText("Status pesanan: " + bundle.getString("status"));

        if(!bundle.getString("status").equals("Belum bayar")) {
            btnSudahBayar.setEnabled(FALSE);
            btnBatalkan.setEnabled(FALSE);
        } else {
            btnSudahBayar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String status = "sudah";
                    String title = "Konfirmasi sudah bayar";
                    String msg = "Apakah Anda yakin bahwa pemesan sudah membayar?";
                    Integer icon = android.R.drawable.ic_dialog_alert;
                    konfirmasi(title, msg, icon, status);
                }
            });
            btnBatalkan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String status = "batal";
                    String title = "Pembatalan Pesanan";
                    String msg = "Apakah Anda yakin ingin membatalkan pesanan?";
                    Integer icon = android.R.drawable.ic_dialog_alert;
                    konfirmasi(title, msg, icon, status);
                }
            });
        }

        return view;
    }
    private void konfirmasi(String title, String msg, Integer icon, final String status) {
        final View v = view;
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(msg)
                .setIcon(icon)
                .setPositiveButton(
                        "Ya",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Sending...");
                                progressDialog.show();
                                AndroidNetworking.post(Konfigurasi.PESANAN)
                                        .addBodyParameter("method", "konfirmasi")
                                        .addBodyParameter("id", idPesanan.toString())
                                        .addBodyParameter("status", status)
                                        .setPriority(Priority.LOW)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    progressDialog.dismiss();
                                                    if(response.getBoolean("status")) {
                                                        if(status.equals("sudah")) {
                                                            Toast.makeText(getActivity(), "Pesanan berhasil dibayar!", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            Toast.makeText(getActivity(), "Pesanan berhasil dibatalkan!", Toast.LENGTH_LONG).show();
                                                        }
                                                        Fragment fragment = new BerandaMenu();
                                                        AppCompatActivity activity = (AppCompatActivity) v.getContext();

                                                        activity.getSupportFragmentManager()
                                                                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                                        activity.getSupportFragmentManager()
                                                                .beginTransaction()
                                                                .replace(R.id.frame_container, fragment)
                                                                .commit();
                                                    } else {
                                                        if(status.equals("sudah")) {
                                                            Toast.makeText(getActivity(), "Pesanan gagal dibayar!", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            Toast.makeText(getActivity(), "Pesanan gagal dibatalkan!", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    progressDialog.dismiss();
                                                    if(status.equals("sudah")) {
                                                        Toast.makeText(getActivity(), "Pesanan gagal dibayar!", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(getActivity(), "Pesanan gagal dibatalkan!", Toast.LENGTH_LONG).show();
                                                    }                                                }
                                            }
                                            @Override
                                            public void onError(ANError error) {
                                                progressDialog.dismiss();
                                                if(status.equals("sudah")) {
                                                    Toast.makeText(getActivity(), "Pesanan gagal dibayar!", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "Pesanan gagal dibatalkan!", Toast.LENGTH_LONG).show();
                                                }                                            }
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
}
