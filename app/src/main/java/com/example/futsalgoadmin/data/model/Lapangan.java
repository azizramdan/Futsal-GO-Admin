package com.example.futsalgoadmin.data.model;

public class Lapangan {
    Integer id;
    String nama, harga, foto;

    public Lapangan(Integer id, String nama, String harga, String foto) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.foto = foto;
    }

    public Integer getId() {
        return id;
    }
    public String getNama() {
        return nama;
    }
    public String getHarga() {
        return harga;
    }
    public String getFoto() {
        return foto;
    }
}
