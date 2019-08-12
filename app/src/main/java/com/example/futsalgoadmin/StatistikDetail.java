package com.example.futsalgoadmin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.example.futsalgoadmin.data.model.Lapangan;
import com.example.futsalgoadmin.data.model.StatistikAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;

public class StatistikDetail extends Fragment {
    public StatistikDetail() {}
    LinearLayout view;
    EditText etPeriode;
    TextView tvSelesai, tvBatal, tvKadaluarsa;
    Integer  id, bulan, tahun;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = (LinearLayout) inflater.inflate(R.layout.statistik_detail, container, false);
        getActivity().setTitle("Statistik Lapangan");

        etPeriode = view.findViewById(R.id.periode);
        tvSelesai = view.findViewById(R.id.selesai);
        tvBatal = view.findViewById(R.id.batal);
        tvKadaluarsa = view.findViewById(R.id.kadaluarsa);

        final Bundle data = this.getArguments();
        id = data.getInt("id");
        etPeriode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPeriode();
            }
        });
//        getStatistik(data.getInt("id"));

        return view;
    }

    private void setPeriode() {
        final Calendar c = Calendar.getInstance();
        Integer mYear = c.get(Calendar.YEAR);
        Integer mMonth = c.get(Calendar.MONTH);
        Integer mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                bulan = monthOfYear + 1;
                tahun = year;

                String date = Konfigurasi.parseDate(tahun + "-" + bulan, "yyyy-M", "MMMM yyyy");
                etPeriode.setText(date);

                tvSelesai.setVisibility(View.VISIBLE);
                tvBatal.setVisibility(View.VISIBLE);
                tvKadaluarsa.setVisibility(View.VISIBLE);
                getStatistik();

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void getStatistik() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.get(Konfigurasi.PESANAN)
                .addQueryParameter("method", "statistik")
                .addQueryParameter("id", id.toString())
                .addQueryParameter("bulan", bulan.toString())
                .addQueryParameter("tahun", tahun.toString())
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");

                            tvSelesai.setText("Selesai: " + data.optString("selesai"));
                            tvBatal.setText("Dibatalkan: " + data.optString("batal"));
                            tvKadaluarsa.setText("Kadaluarsa: " + data.optString("kadaluarsa"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                    }
                });
    }
}
