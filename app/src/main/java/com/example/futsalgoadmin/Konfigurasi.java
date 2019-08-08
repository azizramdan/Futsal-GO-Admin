package com.example.futsalgoadmin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Konfigurasi {
    static String HOST = "http://192.168.43.244/futsalgo/admin/";
    public static final String ADMIN = HOST + "admin.php";
    public static final String LAPANGAN = HOST + "lapangan.php";
    public static final String PESANAN = HOST + "pesanan.php";

    public static String parseDate(String time, String inputPattern, String outputPattern) {

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, new Locale("id", "ID"));

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

}
