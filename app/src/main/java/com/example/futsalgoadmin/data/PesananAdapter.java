package com.example.futsalgoadmin.data;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.futsalgoadmin.PesananDetail;
import com.example.futsalgoadmin.R;
import com.example.futsalgoadmin.data.model.Pesanan;

import java.util.List;

public class PesananAdapter extends RecyclerView.Adapter<PesananAdapter.ViewHolder> {
    private List<Pesanan> list;
    private Fragment fragment = null;

    public PesananAdapter(List<Pesanan> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.beranda_pesanan_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Pesanan data = list.get(position);

        holder.tvIdPesanan.setText("ID pesanan: " + data.getId());
        holder.tvWaktuPilihTanggal.setText(data.getWaktuPilihTanggal());
        holder.tvWaktuPilihJam.setText(data.getWaktuPilihJam());
        holder.tvNamaPemesan.setText("Nama pemesan: " + data.getNamaPemesan());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", data.getId());
                bundle.putString("waktu_pilih_tanggal", data.getWaktuPilihTanggal());
                bundle.putString("waktu_pilih_jam", data.getWaktuPilihJam());
                bundle.putString("metode_bayar", data.getMetodeBayar());
                bundle.putString("status", data.getStatus());
                bundle.putString("nama_lapangan", data.getNamaLapangan());
                bundle.putString("nama_pemesan", data.getNamaPemesan());
                bundle.putString("telp", data.getTelp());

                fragment = new PesananDetail();
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdPesanan, tvWaktuPilihTanggal, tvWaktuPilihJam, tvNamaPemesan;
        CardView cardView;

        private ViewHolder(View itemView) {
            super(itemView);
            tvIdPesanan = itemView.findViewById(R.id.id_pesanan);
            tvWaktuPilihTanggal = itemView.findViewById(R.id.waktu_pilih_tanggal);
            tvWaktuPilihJam = itemView.findViewById(R.id.waktu_pilih_jam);
            tvNamaPemesan = itemView.findViewById(R.id.nama_pemesan);
            cardView = itemView.findViewById(R.id.cardView);
        }

    }
}
