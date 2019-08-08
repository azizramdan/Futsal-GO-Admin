package com.example.futsalgoadmin.ui.login;

import android.support.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    private boolean status;
    private String error, telp, email, bank, nama_rekening, no_rekening, jam_buka, jam_tutup;
    private Integer id;

    LoginResult(boolean status, String error) {
        this.status = status;
        this.error = error;
    }

    LoginResult(boolean status, String error, Integer id, String telp, String email, String bank, String nama_rekening, String no_rekening, String jam_buka, String jam_tutup) {
        this.status = status;
        this.error = error;
        this.id = id;
        this.telp = telp;
        this.email = email;
        this.bank = bank;
        this.nama_rekening = nama_rekening;
        this.no_rekening = no_rekening;
        this.jam_buka = jam_buka;
        this.jam_tutup = jam_tutup;
    }
    String getError(){
        return error;
    }
    Boolean getStatus() {
        return status;
    }
    Integer getId() {
        return  id;
    }
    String getTelp() {
        return  telp;
    }
    String getEmail() {
        return  email;
    }
    String getBank() {
        return  bank;
    }
    String getNamaRekening() {
        return  nama_rekening;
    }
    String getNoRekening() {
        return  no_rekening;
    }
    String getJamBuka() {
        return  jam_buka;
    }
    String getJamTutup() {
        return  jam_tutup;
    }

}
