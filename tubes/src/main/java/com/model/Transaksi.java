package com.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Transaksi {
    private String id_transaksi;
    private Buku buku;
    private Member member;
    private LocalDateTime tanggal_peminjaman;
    private LocalDateTime tanggal_pengembalian;
    private Status status_transaksi;

    public enum Status {
        PENDING_PEMINJAMAN, // Menunggu persetujuan admin untuk peminjaman
        ACCEPTED, // Disetujui admin, buku dipinjam
        REJECTED, // Ditolak admin
        PENDING_PENGEMBALIAN, // Menunggu verifikasi pengembalian oleh admin
        RETURNED, // Buku sudah dikembalikan
        OVERDUE // Terlambat pengembalian
    }

    // Constructor untuk transaksi baru (saat member request pinjam)
    public Transaksi(Buku buku, Member member) {
        this.id_transaksi = generateUUID("TRX-");
        this.buku = buku;
        this.member = member;
        this.tanggal_peminjaman = LocalDateTime.now();
        this.tanggal_pengembalian = null; // Belum dikembalikan
        this.status_transaksi = Status.PENDING_PEMINJAMAN;
    }

    // Constructor lengkap (misal untuk load dari database)
    public Transaksi(String id_transaksi, Buku buku, Member member, LocalDateTime tanggal_peminjaman,
            LocalDateTime tanggal_pengembalian, Status status_transaksi) {
        this.id_transaksi = id_transaksi;
        this.buku = buku;
        this.member = member;
        this.tanggal_peminjaman = tanggal_peminjaman;
        this.tanggal_pengembalian = tanggal_pengembalian;
        this.status_transaksi = status_transaksi;
    }

    // Getter & Setter
    public String getIdTransaksi() {
        return id_transaksi;
    }

    public Buku getBuku() {
        return buku;
    }

    public Member getMember() {
        return member;
    }

    public LocalDateTime getTanggalPeminjaman() {
        return tanggal_peminjaman;
    }

    public LocalDateTime getTanggalPengembalian() {
        return tanggal_pengembalian;
    }

    public Status getStatusTransaksi() {
        return status_transaksi;
    }

    public void setStatusTransaksi(Status status) {
        this.status_transaksi = status;
    }

    public void setTanggalPengembalian(LocalDateTime tanggal_pengembalian) {
        this.tanggal_pengembalian = tanggal_pengembalian;
    }

    public void setIdTransaksi(String id) {
        this.id_transaksi = id;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setTanggalPeminjaman(LocalDateTime tgl) {
        this.tanggal_peminjaman = tgl;
    }

    public void setBuku(Buku buku) {
        this.buku = buku;
    }

    // Utility
    public boolean isOverdue() {
        return status_transaksi == Status.ACCEPTED &&
                tanggal_pengembalian == null &&
                LocalDateTime.now().isAfter(tanggal_peminjaman.plusDays(3));
    }

    private static String generateUUID(String prefix) {
        return prefix + UUID.randomUUID().toString().replace("-", "");
    }

    public void laporan_bulanan(List<Transaksi> allTrx) {
        LocalDateTime now = LocalDateTime.now();
        int bulan = now.getMonthValue();
        int tahun = now.getYear();

        System.out.println("Laporan Transaksi Bulan " + bulan + " Tahun " + tahun);
        for (Transaksi trx : allTrx) {
            LocalDateTime tglpinjam = trx.getTanggalPeminjaman();
            LocalDateTime tglkembali = trx.getTanggalPengembalian();

            if ((tglpinjam.getMonthValue() == bulan && tglpinjam.getYear() == tahun)
                    || (tglkembali != null && tglkembali.getMonthValue() == bulan && tglkembali.getYear() == tahun)) {
                System.out.println("ID: " + trx.getIdTransaksi() +
                        ", Member: " + trx.getMember().getNamaLengkap() +
                        "Tanggal Peminjaman: " + trx.getTanggalPeminjaman() +
                        "Tanggal Pengembalian: " + trx.getTanggalPengembalian() +
                        ", Status: " + trx.getStatusTransaksi());
            }
        }
    }
}
