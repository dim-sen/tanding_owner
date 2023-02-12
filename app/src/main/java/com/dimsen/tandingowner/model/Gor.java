package com.dimsen.tandingowner.model;

public class Gor {
    private String nama_gor;
    private String alamat;
    private String jam_buka;
    private String jam_tutup;
    private String tanggal_buka;
    private String gor_image;
    private String kontak;
    private String harga;

    private String key;

    public Gor() {
    }

    public Gor(String nama_gor, String alamat, String jam_buka, String jam_tutup, String tanggal_buka, String gor_image, String kontak, String harga) {
        this.nama_gor = nama_gor;
        this.alamat = alamat;
        this.jam_buka = jam_buka;
        this.jam_tutup = jam_tutup;
        this.tanggal_buka = tanggal_buka;
        this.gor_image = gor_image;
        this.kontak = kontak;
        this.harga = harga;
    }

    public String getNama_gor() {
        return nama_gor;
    }

    public void setNama_gor(String nama_gor) {
        this.nama_gor = nama_gor;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJam_buka() {
        return jam_buka;
    }

    public void setJam_buka(String jam_buka) {
        this.jam_buka = jam_buka;
    }

    public String getJam_tutup() {
        return jam_tutup;
    }

    public void setJam_tutup(String jam_tutup) {
        this.jam_tutup = jam_tutup;
    }

    public String getTanggal_buka() {
        return tanggal_buka;
    }

    public void setTanggal_buka(String tanggal_buka) {
        this.tanggal_buka = tanggal_buka;
    }

    public String getGor_image() {
        return gor_image;
    }

    public void setGor_image(String gor_image) {
        this.gor_image = gor_image;
    }

    public String getKontak() {
        return kontak;
    }

    public void setKontak(String kontak) {
        this.kontak = kontak;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
