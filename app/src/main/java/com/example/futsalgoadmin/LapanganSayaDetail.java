package com.example.futsalgoadmin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.text.NumberFormat;
import java.util.Locale;
import static android.support.constraint.Constraints.TAG;

public class LapanganSayaDetail extends Fragment {
    public LapanganSayaDetail() {}
    LinearLayout view;
    Integer idLapangan;
    private Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = (LinearLayout) inflater.inflate(R.layout.lapangan_saya_detail, container, false);
        getActivity().setTitle("Detail Pemesanan");

        ImageView ivFoto = view.findViewById(R.id.foto);
        TextView tvNamaLapangan = view.findViewById(R.id.nama_lapangan);
        TextView tvHarga = view.findViewById(R.id.harga);

        final Bundle data = this.getArguments();
        Log.d(TAG, "isi foto " + data.getString("foto"));
        idLapangan = data.getInt("id");
        Glide.with(this)
                .load(data.getString("foto"))
                .placeholder(R.drawable.picture)
                .into(ivFoto);
        tvNamaLapangan.setText(data.getString("nama"));
        tvHarga.setText("Harga per jam: " + NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(Double.parseDouble(data.getString("harga"))));

        Button btnEdit = view.findViewById(R.id.edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", data.getInt("id"));
                bundle.putString("nama", data.getString("nama"));
                bundle.putString("harga", data.getString("harga"));
                bundle.putString("foto", data.getString("foto"));

                fragment = new LapanganSayaEdit();
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}
