package com.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ulasan {
    private String id_ulasan;
    private Buku buku;
    private Member pengulas;
    private String ulasan;
    private LocalDateTime tanggal_ulasan;
    private int rating;

    public Ulasan(String id, Buku buku, Member pengulas, String Ulasan, int rating){
        this.id_ulasan = generateUUID("UL-");
        this.buku = buku;
        this.pengulas = pengulas;
        this.ulasan = Ulasan;
        this.tanggal_ulasan = LocalDateTime.now();
        this.rating = rating;


    }

    // Getter
    public String getIdUlasan() {
        return id_ulasan;
    }

    public Buku getBuku() {
        return buku;
    }

    public Member getPengulas() {
        return pengulas;
    }

    public String getUlasan() {
        return ulasan;
    }

    public LocalDateTime getTanggalUlasan() {
        return tanggal_ulasan;
    }

    public int getRating(){
        return this.rating;
    }

    // Setter
    public void setIdUlasan(String id_ulasan) {
        this.id_ulasan = id_ulasan;
    }

    public void setBuku(Buku buku) {
        this.buku = buku;
    }

    public void setPengulas(Member pengulas) {
        this.pengulas = pengulas;
    }

    public void setUlasan(String ulasan) {
        this.ulasan = ulasan;
    }

    public void setTanggalUlasan(LocalDateTime tanggal_ulasan) {
        this.tanggal_ulasan = tanggal_ulasan;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    private static String generateUUID(String prefix) {
        return prefix + UUID.randomUUID().toString().replace("-", "");
    }

} 