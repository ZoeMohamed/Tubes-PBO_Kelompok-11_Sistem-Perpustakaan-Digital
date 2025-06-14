package com.Service;

import com.DAO.BukuDAO;
import com.model.Buku;
import java.util.List;

public class BukuService {
    private BukuDAO bukuDAO;

    public BukuService(BukuDAO bukuDAO) {
        this.bukuDAO = bukuDAO;
    }

    // Create
    public boolean tambahBuku(Buku buku) {
        // Validasi input
        if (!validateBuku(buku)) {
            return false;
        }

        // Cek apakah ISBN sudah ada
        if (bukuDAO.getByISBN(buku.get_isbn()) != null) {
            throw new IllegalArgumentException("ISBN sudah terdaftar");
        }

        return bukuDAO.insert(buku);
    }

    // Read
    public Buku getBukuByISBN(String isbn) {
        return bukuDAO.getByISBN(isbn);
    }

    public List<Buku> getAllBuku() {
        return bukuDAO.getAll();
    }

    // Update
    public boolean updateBuku(Buku buku) {
        // Validasi input
        if (!validateBuku(buku)) {
            return false;
        }

        // Cek apakah buku ada
        Buku existingBuku = bukuDAO.getByISBN(buku.get_isbn());
        if (existingBuku == null) {
            throw new IllegalArgumentException("Buku tidak ditemukan");
        }

        return bukuDAO.update(buku);
    }

    // Delete
    public boolean hapusBuku(String isbn) {
        // Cek apakah buku ada
        Buku buku = bukuDAO.getByISBN(isbn);
        if (buku == null) {
            throw new IllegalArgumentException("Buku tidak ditemukan");
        }

        return bukuDAO.delete(isbn);
    }

    // Search
    public List<Buku> cariBuku(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBuku();
        }
        return bukuDAO.search(keyword.trim());
    }

    // Filter
    public List<Buku> filterBuku(String genre, String kategori, Integer tahun) {
        return bukuDAO.filter(genre, kategori, tahun);
    }

    // Validasi
    private boolean validateBuku(Buku buku) {
        if (buku == null) {
            throw new IllegalArgumentException("Data buku tidak boleh kosong");
        }

        if (buku.get_isbn() == null || buku.get_isbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN tidak boleh kosong");
        }

        if (buku.get_judul_buku() == null || buku.get_judul_buku().trim().isEmpty()) {
            throw new IllegalArgumentException("Judul buku tidak boleh kosong");
        }

        if (buku.get_jumlah() < 0) {
            throw new IllegalArgumentException("Jumlah buku tidak boleh negatif");
        }

        if (buku.get_tahun_terbit() < 1800 || buku.get_tahun_terbit() > java.time.LocalDateTime.now().getYear()) {
            throw new IllegalArgumentException("Tahun terbit tidak valid");
        }

        return true;
    }

    // Update stok
    public boolean updateStok(String isbn, int jumlah) {
        Buku buku = bukuDAO.getByISBN(isbn);
        if (buku == null) {
            throw new IllegalArgumentException("Buku tidak ditemukan");
        }

        if (jumlah < 0) {
            throw new IllegalArgumentException("Jumlah tidak boleh negatif");
        }

        buku.set_jumlah(jumlah);
        return bukuDAO.update(buku);
    }

    // Update status
    public boolean updateStatus(String isbn, String status) {
        Buku buku = bukuDAO.getByISBN(isbn);
        if (buku == null) {
            throw new IllegalArgumentException("Buku tidak ditemukan");
        }

        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Status tidak valid");
        }

        buku.set_status(status);
        return bukuDAO.update(buku);
    }

    private boolean isValidStatus(String status) {
        return status != null &&
                (status.equals("available") ||
                        status.equals("borrowed") ||
                        status.equals("maintenance") ||
                        status.equals("deleted"));
    }
}