package com.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Buku {
    /* ATRIBUT */
    private String isbn;
    private String judul_buku;
    private String kategori;
    private int jumlah;
    private String penulis;
    private int tahun_terbit;
    private String genre;
    private String penerbit;
    private String deskripsi;
    private String status;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private ArrayList<String> ulasan;
    private static ArrayList<Buku> buku_dihapus = new ArrayList<>();

    /* METHOD */
    public Buku() {
        this.ulasan = new ArrayList<>();
        this.status = "available";
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    public Buku(String isbn, String judul_buku, String kategori, int jumlah, String penulis,
            int tahun_terbit, String genre, String penerbit, String deskripsi) {
        this();
        this.isbn = isbn;
        this.judul_buku = judul_buku;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.penulis = penulis;
        this.tahun_terbit = tahun_terbit;
        this.genre = genre;
        this.penerbit = penerbit;
        this.deskripsi = deskripsi;
    }

    // Getter
    public String get_isbn() {
        return isbn;
    }

    public String get_judul_buku() {
        return judul_buku;
    }

    public String get_kategori() {
        return kategori;
    }

    public ArrayList<String> get_ulasan() {
        return ulasan;
    }

    public int get_jumlah() {
        return jumlah;
    }

    public String get_penulis() {
        return penulis;
    }

    public int get_tahun_terbit() {
        return tahun_terbit;
    }

    public String get_genre() {
        return genre;
    }

    public String get_penerbit() {
        return penerbit;
    }

    public String get_deskripsi() {
        return deskripsi;
    }

    public String get_status() {
        return status;
    }

    public LocalDateTime get_created_at() {
        return created_at;
    }

    public LocalDateTime get_updated_at() {
        return updated_at;
    }

  

    // Setter
    public void set_isbn(String isbn) {
        this.isbn = isbn;
        this.updated_at = LocalDateTime.now();
    }

    public void set_judul_buku(String judul_buku) {
        this.judul_buku = judul_buku;
        this.updated_at = LocalDateTime.now();
    }

    public void set_kategori(String kategori) {
        this.kategori = kategori;
        this.updated_at = LocalDateTime.now();
    }

    public void set_ulasan(ArrayList<String> ulasan) {
        this.ulasan = ulasan;
        this.updated_at = LocalDateTime.now();
    }

    public void set_jumlah(int jumlah) {
        this.jumlah = jumlah;
        this.updated_at = LocalDateTime.now();
    }

    public void set_penulis(String penulis) {
        this.penulis = penulis;
        this.updated_at = LocalDateTime.now();
    }

    public void set_tahun_terbit(int tahun_terbit) {
        this.tahun_terbit = tahun_terbit;
        this.updated_at = LocalDateTime.now();
    }

    public void set_genre(String genre) {
        this.genre = genre;
        this.updated_at = LocalDateTime.now();
    }

    public void set_penerbit(String penerbit) {
        this.penerbit = penerbit;
        this.updated_at = LocalDateTime.now();
    }

    public void set_deskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
        this.updated_at = LocalDateTime.now();
    }

    public void set_status(String status) {
        this.status = status;
        this.updated_at = LocalDateTime.now();
    }

    public void set_created_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public void set_updated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

 
    // Mencari buku berdasarkan judul
    public String cari_buku(String judul_buku) {
        if (this.judul_buku.equalsIgnoreCase(judul_buku)) {
            return "Buku ditemukan: " + this.judul_buku;
        } else {
            return "Buku tidak ditemukan.";
        }
    }

    // Menghapus buku dan menyimpannya dalam daftar buku yang telah dihapus
    public void hapus_buku() {
        buku_dihapus.add(this);
        this.isbn = null;
        this.judul_buku = null;
        this.kategori = null;
        this.ulasan = null;
        this.jumlah = 0;
        this.status = "deleted";
        this.updated_at = LocalDateTime.now();
    }

    // Mendapatkan daftar buku yang telah dihapus
    public static ArrayList<Buku> get_buku_dihapus() {
        return buku_dihapus;
    }

    // Update jumlah buku
    public int update_jumlah(int jumlah) {
        if (jumlah < 0) {
            System.out.println("Jumlah tidak boleh negatif.");
            return this.jumlah;
        } else {
            this.jumlah = jumlah;
            this.updated_at = LocalDateTime.now();
            return this.jumlah;
        }
    }

    // Update deskripsi buku
    public Buku update_deskripsi(String isbn, String judul_buku, String kategori, ArrayList<String> ulasan) {
        if (this.isbn.equals(isbn) || this.judul_buku.equalsIgnoreCase(judul_buku)) {
            this.judul_buku = judul_buku;
            this.kategori = kategori;
            this.ulasan = ulasan;
            this.updated_at = LocalDateTime.now();
            return this;
        } else {
            System.out.println("Buku tidak ditemukan.");
            return null;
        }
    }

    // Melakukan filter buku berdasarkan kategori
    public static ArrayList<Buku> filter_buku(ArrayList<Buku> daftar_buku, String kategori) {
        ArrayList<Buku> buku_terfilter = new ArrayList<>();
        for (Buku buku : daftar_buku) {
            if (buku.get_kategori().equalsIgnoreCase(kategori)) {
                buku_terfilter.add(buku);
            }
        }
        return buku_terfilter;
    }

    // Menampilkan semua ulasan
    public ArrayList<String> tampilkan_semua_ulasan() {
        ArrayList<String> ulasan_list = new ArrayList<>();
        if (this.ulasan != null && !this.ulasan.isEmpty()) {
            for (String ulasanItem : this.ulasan) {
                ulasan_list.add(ulasanItem);
            }
        } else {
            System.out.println("Tidak ada ulasan untuk buku ini.");
        }
        return ulasan_list;
    }

    public void add_ulasan(String ulasan) {
        if (this.ulasan == null) {
            this.ulasan = new ArrayList<>();
        }
        this.ulasan.add(ulasan);
        this.updated_at = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Buku{" +
                "isbn='" + isbn + '\'' +
                ", judul_buku='" + judul_buku + '\'' +
                ", kategori='" + kategori + '\'' +
                ", jumlah=" + jumlah +
                ", penulis='" + penulis + '\'' +
                ", tahun_terbit=" + tahun_terbit +
                ", genre='" + genre + '\'' +
                ", penerbit='" + penerbit + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", status='" + status + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
