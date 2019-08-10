package com.example.futsalgoadmin.data.model;

public class Pesanan {
    Integer id;
    String waktu_pilih_tanggal, waktu_pilih_jam, metode_bayar, status, nama_lapangan, nama_pemesan, telp;

    public Pesanan(Integer id, String waktu_pilih_tanggal, String waktu_pilih_jam, String metode_bayar, String status, String nama_lapangan, String nama_pemesan, String telp) {
        this.id = id;
        this.waktu_pilih_tanggal = waktu_pilih_tanggal;
        this.waktu_pilih_jam = waktu_pilih_jam;
        this.metode_bayar = metode_bayar;
        this.status = status;
        this.nama_lapangan = nama_lapangan;
        this.nama_pemesan = nama_pemesan;
        this.telp = telp;
    }

    public Integer getId() {
        return id;
    }
    public String getWaktuPilihTanggal() {
        return waktu_pilih_tanggal;
    }
    public String getWaktuPilihJam() {
        return waktu_pilih_jam;
    }
    public String getMetodeBayar() {
        return metode_bayar;
    }
    public String getStatus() {
        return status;
    }
    public String getNamaLapangan() {
        return nama_lapangan;
    }
    public String getNamaPemesan() {
        return nama_pemesan;
    }
    public String getTelp() {
        return telp;
    }
}
