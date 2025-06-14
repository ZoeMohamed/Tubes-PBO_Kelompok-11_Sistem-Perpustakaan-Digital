package com.DAO;

import com.model.Buku;
import com.model.Member;
import com.model.Ulasan;
import com.utilities.MysqlUtilities;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UlasanDAO {
    private Connection koneksi;

    public UlasanDAO() {
        this.koneksi = MysqlUtilities.getConnection();
    }

    public boolean addUlasan(Ulasan ulasan) throws SQLException {
        String query = "INSERT INTO ulasan_buku (id_ulasan, isbn, id_user, ulasan, rating, tanggal_ulasan) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = koneksi.prepareStatement(query)) {
            ps.setString(1, ulasan.getIdUlasan());
            ps.setString(2, ulasan.getBuku().get_isbn());
            ps.setString(3, ulasan.getPengulas().getIdMember());
            ps.setString(4, ulasan.getUlasan());
            ps.setInt(5, ulasan.getRating());
            ps.setTimestamp(6, Timestamp.valueOf(ulasan.getTanggalUlasan()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Ulasan> getUlasanByISBN(String isbn) throws SQLException {
        String query = "SELECT * FROM ulasan_buku WHERE isbn = ? ORDER BY tanggal_ulasan DESC";
        List<Ulasan> list = new ArrayList<>();

        try (PreparedStatement ps = koneksi.prepareStatement(query)) {
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Buku buku = new Buku();
                buku.set_isbn(rs.getString("isbn"));

                Member pengulas = new Member();
                pengulas.setIdMember(rs.getString("id_user"));

                Ulasan u = new Ulasan(
                        rs.getString("id_ulasan"),
                        buku,
                        pengulas,
                        rs.getString("ulasan"),
                        rs.getInt("rating"));
                u.setTanggalUlasan(rs.getTimestamp("tanggal_ulasan").toLocalDateTime());
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return list;
    }

    public List<Ulasan> getAllUlasan() throws SQLException {
        String query = "SELECT * FROM ulasan_buku ORDER BY tanggal_ulasan DESC";
        List<Ulasan> list = new ArrayList<>();

        try (Statement st = koneksi.createStatement();
                ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Buku buku = new Buku();
                buku.set_isbn(rs.getString("isbn"));

                Member pengulas = new Member();
                pengulas.setIdMember(rs.getString("id_user"));

                Ulasan u = new Ulasan(
                        rs.getString("id_ulasan"),
                        buku,
                        pengulas,
                        rs.getString("ulasan"),
                        rs.getInt("rating"));
                u.setTanggalUlasan(rs.getTimestamp("tanggal_ulasan").toLocalDateTime());
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return list;
    }
}