package com.DAO;

import com.model.Buku;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BukuDAO {
    private Connection connection;

    public BukuDAO(Connection connection) {
        this.connection = connection;
    }

    // Create
    public boolean insert(Buku buku) {
        String sql = "INSERT INTO buku (isbn, judul_buku, kategori, jumlah, penulis, tahun_terbit, " +
                "genre, penerbit, deskripsi, status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, buku.get_isbn());
            stmt.setString(2, buku.get_judul_buku());
            stmt.setString(3, buku.get_kategori());
            stmt.setInt(4, buku.get_jumlah());
            stmt.setString(5, buku.get_penulis());
            stmt.setInt(6, buku.get_tahun_terbit());
            stmt.setString(7, buku.get_genre());
            stmt.setString(8, buku.get_penerbit());
            stmt.setString(9, buku.get_deskripsi());

            stmt.setString(10, buku.get_status());
            stmt.setTimestamp(11, Timestamp.valueOf(buku.get_created_at()));
            stmt.setTimestamp(12, Timestamp.valueOf(buku.get_updated_at()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Read
    public Buku getByISBN(String isbn) {
        String sql = "SELECT * FROM buku WHERE isbn = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToBuku(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Buku> getAll() {
        List<Buku> bukuList = new ArrayList<>();
        String sql = "SELECT * FROM buku WHERE status != 'deleted'";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bukuList.add(mapResultSetToBuku(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bukuList;
    }

    // Update
    public boolean update(Buku buku) {
        String sql = "UPDATE buku SET judul_buku = ?, kategori = ?, jumlah = ?, " +
                "penulis = ?, tahun_terbit = ?, genre = ?, penerbit = ?, " +
                "deskripsi = ?, status = ?, updated_at = ? " +
                "WHERE isbn = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, buku.get_judul_buku());
            stmt.setString(2, buku.get_kategori());
            stmt.setInt(3, buku.get_jumlah());
            stmt.setString(4, buku.get_penulis());
            stmt.setInt(5, buku.get_tahun_terbit());
            stmt.setString(6, buku.get_genre());
            stmt.setString(7, buku.get_penerbit());
            stmt.setString(8, buku.get_deskripsi());

            stmt.setString(9, buku.get_status());
            stmt.setTimestamp(10, Timestamp.valueOf(buku.get_updated_at()));
            stmt.setString(11, buku.get_isbn());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete (Hard Delete)
    public boolean delete(String isbn) {
        String sql = "DELETE FROM buku WHERE isbn = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Search
    public List<Buku> search(String keyword) {
        List<Buku> bukuList = new ArrayList<>();
        String sql = "SELECT * FROM buku WHERE (judul_buku LIKE ? OR penulis LIKE ? OR isbn LIKE ?) " +
                "AND status != 'deleted'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bukuList.add(mapResultSetToBuku(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bukuList;
    }

    // Filter
    public List<Buku> filter(String genre, String kategori, Integer tahun) {
        List<Buku> bukuList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM buku WHERE status != 'deleted'");
        List<Object> params = new ArrayList<>();

        if (genre != null && !genre.isEmpty()) {
            sql.append(" AND genre = ?");
            params.add(genre);
        }
        if (kategori != null && !kategori.isEmpty()) {
            sql.append(" AND kategori = ?");
            params.add(kategori);
        }
        if (tahun != null) {
            sql.append(" AND tahun_terbit = ?");
            params.add(tahun);
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bukuList.add(mapResultSetToBuku(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bukuList;
    }

    // Helper method to map ResultSet to Buku object
    private Buku mapResultSetToBuku(ResultSet rs) throws SQLException {
        Buku buku = new Buku();
        buku.set_isbn(rs.getString("isbn"));
        buku.set_judul_buku(rs.getString("judul_buku"));
        buku.set_kategori(rs.getString("kategori"));
        buku.set_jumlah(rs.getInt("jumlah"));
        buku.set_penulis(rs.getString("penulis"));
        buku.set_tahun_terbit(rs.getInt("tahun_terbit"));
        buku.set_genre(rs.getString("genre"));
        buku.set_penerbit(rs.getString("penerbit"));
        buku.set_deskripsi(rs.getString("deskripsi"));

        buku.set_status(rs.getString("status"));
        buku.set_created_at(rs.getTimestamp("created_at").toLocalDateTime());
        buku.set_updated_at(rs.getTimestamp("updated_at").toLocalDateTime());
        return buku;
    }
}