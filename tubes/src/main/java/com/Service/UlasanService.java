package com.Service;

import com.DAO.UlasanDAO;
import com.model.Ulasan;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UlasanService {
    private UlasanDAO ulasanDAO;

    public UlasanService() {
        this.ulasanDAO = new UlasanDAO();
    }

    // Tambah ulasan (dari UI)
    public boolean tambahUlasan(Ulasan ulasan) {
        if (ulasan == null || ulasan.getBuku() == null || ulasan.getPengulas() == null || ulasan.getUlasan() == null
                || ulasan.getUlasan().trim().isEmpty()) {
            System.err.println("Invalid ulasan data provided.");
            return false;
        }

        try {

            return ulasanDAO.addUlasan(ulasan);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding ulasan to database: " + e.getMessage());
            return false;
        }
    }

    // Ambil ulasan berdasarkan ISBN - return List
    public List<Ulasan> getUlasanByISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            System.err.println("Invalid ISBN provided for getting ulasan.");
            return new ArrayList<>();
        }
        try {
            return ulasanDAO.getUlasanByISBN(isbn);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error getting ulasan by ISBN: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Ambil semua ulasan (untuk admin panel atau testing) - return List
    public List<Ulasan> getAllUlasan() {
        try {
            return ulasanDAO.getAllUlasan();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error getting all ulasan: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean sudahPernahMengulas(String isbn, String memberId) {
        if (isbn == null || isbn.trim().isEmpty() || memberId == null || memberId.trim().isEmpty()) {
            System.err.println("Invalid ISBN or Member ID provided for review check.");
            return false;
        }
        try {
            List<Ulasan> ulasanBuku = ulasanDAO.getUlasanByISBN(isbn);
            for (Ulasan u : ulasanBuku) {
                // Compare using Member ID which is set in DAO
                if (u.getPengulas() != null && u.getPengulas().getIdMember().equals(memberId)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking if user already reviewed: " + e.getMessage());
        }
        return false;
    }

}