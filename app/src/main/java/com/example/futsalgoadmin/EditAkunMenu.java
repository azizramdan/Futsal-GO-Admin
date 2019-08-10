package com.example.futsalgoadmin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

public class EditAkunMenu extends Fragment {
    public EditAkunMenu(){}
    LinearLayout view;
    Integer id;
    EditText etTelp, etNamaRekening, etNoRekening, etJamBuka, etJamTutup, etPassword, etKonfirmasiPassword;
    String bank;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Edit Akun");

        view = (LinearLayout) inflater.inflate(R.layout.edit_akun_menu, container, false);
        etTelp = view.findViewById(R.id.telp);
        etNamaRekening = view.findViewById(R.id.nama_rekening);
        etNoRekening = view.findViewById(R.id.no_rekening);
        etJamBuka = view.findViewById(R.id.jam_buka);
        etJamTutup = view.findViewById(R.id.jam_tutup);
        etPassword = view.findViewById(R.id.password);
        etKonfirmasiPassword = view.findViewById(R.id.konfirmasi_password);

        SharedPreferences data = getActivity().getSharedPreferences("dataAdmin", Context.MODE_PRIVATE);
        id = data.getInt("id", 0);
        etTelp.setText(data.getString("telp", "0"));
        etNamaRekening.setText(data.getString("nama_rekening", "nama_rekening"));
        etNoRekening.setText(data.getString("no_rekening", "0"));
        etJamBuka.setText(data.getString("jam_buka", "jam_buka"));
        etJamTutup.setText(data.getString("jam_tutup", "jam_tutup"));

        Spinner spinner = view.findViewById(R.id.bank);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.bank, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter();
        int spinnerPosition = myAdap.getPosition(data.getString("bank", bank));
        spinner.setSelection(spinnerPosition);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                bank = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        etJamBuka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                Integer hourOfDay = c.get(Calendar.HOUR_OF_DAY);
                Integer minute = c.get(Calendar.MINUTE);
                TimePickerDialog tpd = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String date = hourOfDay + ":00" + ":00";
                        date = Konfigurasi.parseDate(date, "h:mm:ss", "hh:mm:ss");
                        etJamBuka.setText(date);
                    }
                }, hourOfDay, minute, DateFormat.is24HourFormat(getActivity()));
                tpd.show();
            }
        });
        etJamTutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                Integer hourOfDay = c.get(Calendar.HOUR_OF_DAY);
                Integer minute = c.get(Calendar.MINUTE);
                TimePickerDialog tpd = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String date = hourOfDay + ":00" + ":00";
                        date = Konfigurasi.parseDate(date, "h:mm:ss", "hh:mm:ss");
                        etJamBuka.setText(date);
                    }
                }, hourOfDay, minute, DateFormat.is24HourFormat(getActivity()));
                tpd.show();
            }
        });

        AndroidNetworking.initialize(getActivity());

        Button btnsimpan = view.findViewById(R.id.simpan);
        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPassword.getText().toString().length() != 0 && etPassword.getText().toString().length() < 5) {
                    Toast.makeText(getActivity(), "Password minimal 5 karakter!", Toast.LENGTH_LONG).show();
                } else {
                    if(!etPassword.getText().toString().equals(etKonfirmasiPassword.getText().toString())) {
                        Toast.makeText(getActivity(), "Konfirmasi password salah!", Toast.LENGTH_LONG).show();
                    } else {
                        simpan();
                    }
                }
            }
        });
        return view;
    }

    private void simpan() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        final String telp = etTelp.getText().toString();
        final String namaRekening = etNamaRekening.getText().toString();
        final String noRekening = etNoRekening.getText().toString();
        final String jamBuka = etJamBuka.getText().toString();
        final String jamTutup = etJamTutup.getText().toString();
        String password = etPassword.getText().toString();
        AndroidNetworking.post(Konfigurasi.ADMIN)
                .addBodyParameter("method", "update")
                .addBodyParameter("id", id.toString())
                .addBodyParameter("telp", telp)
                .addBodyParameter("bank", bank)
                .addBodyParameter("nama_rekening", namaRekening)
                .addBodyParameter("no_rekening", noRekening)
                .addBodyParameter("jam_buka", jamBuka)
                .addBodyParameter("jam_tutup", jamTutup)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("status")) {
                                SharedPreferences sharedpreferences = getActivity().getSharedPreferences("dataAdmin", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("telp", telp);
                                editor.putString("bank", bank);
                                editor.putString("nama_rekening", namaRekening);
                                editor.putString("no_rekening", noRekening);
                                editor.putString("jam_buka", jamBuka);
                                editor.putString("jam_tutup", jamTutup);
                                editor.commit();
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Data berhasil disimpan!", Toast.LENGTH_LONG).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Gagal menyimpan data!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Gagal menyimpan data!", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Gagal menyimpan data!", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
