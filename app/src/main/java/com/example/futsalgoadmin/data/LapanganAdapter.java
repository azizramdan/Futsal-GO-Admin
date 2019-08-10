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

import com.example.futsalgoadmin.LapanganSayaDetail;
import com.example.futsalgoadmin.PesananDetail;
import com.example.futsalgoadmin.R;
import com.example.futsalgoadmin.data.model.Lapangan;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class LapanganAdapter extends RecyclerView.Adapter<LapanganAdapter.ViewHolder> {
    private List<Lapangan> list;
    private Fragment fragment = null;

    public LapanganAdapter(List<Lapangan> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lapangan_saya_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Lapangan data = list.get(position);

        holder.tvNamaLapangan.setText("Nama lapangan: " + data.getNama());
        holder.tvHarga.setText("Harga: " + NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(Double.parseDouble(data.getHarga())));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", data.getId());
                bundle.putString("nama", data.getNama());
                bundle.putString("harga", data.getHarga());
                bundle.putString("foto", data.getFoto());

                fragment = new LapanganSayaDetail();
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
        TextView tvNamaLapangan, tvHarga;
        CardView cardView;

        private ViewHolder(View itemView) {
            super(itemView);
            tvNamaLapangan = itemView.findViewById(R.id.nama_lapangan);
            tvHarga = itemView.findViewById(R.id.harga);
            cardView = itemView.findViewById(R.id.cardView);
        }

    }
}
