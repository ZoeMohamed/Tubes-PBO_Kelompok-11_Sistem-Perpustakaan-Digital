package com.DAO;

import com.model.Transaksi;
import com.model.Buku;
import com.model.Member;
import com.utilities.MysqlUtilities;
import com.DAO.BukuDAO;
import com.DAO.MemberDAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TransaksiDAO {
    private Connection koneksi;
    private BukuDAO bukuDAO = new BukuDAO(MysqlUtilities.getConnection());

    public TransaksiDAO(Connection koneksi) {
        this.koneksi = koneksi;
    }

    public TransaksiDAO() {
        this.koneksi = MysqlUtilities.getConnection();
    }

    // Tambah transaksi (pinjam)
    public boolean insertTransaksi(Transaksi trx) throws SQLException {
        String sql = "INSERT INTO transaksi (id_transaksi, id_user, isbn, tanggal_peminjaman, tanggal_pengembalian, status_transaksi) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, trx.getIdTransaksi());
            ps.setString(2, trx.getMember().getIdMember());
            ps.setString(3, trx.getBuku().get_isbn());
            ps.setTimestamp(4, Timestamp.valueOf(trx.getTanggalPeminjaman()));
            ps.setTimestamp(5,
                    trx.getTanggalPengembalian() != null ? Timestamp.valueOf(trx.getTanggalPengembalian()) : null);
            ps.setString(6, trx.getStatusTransaksi().name());
            return ps.executeUpdate() > 0;
        }
    }

    // Ambil semua transaksi
    public ArrayList<Transaksi> getAllTransaksi() throws SQLException {
        String query = "SELECT * FROM transaksi ORDER BY tanggal_peminjaman DESC";
        ArrayList<Transaksi> list = new ArrayList<>();

        try (Statement st = koneksi.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Transaksi trx = parseResult(rs);
                list.add(trx);
            }
        }

        return list;
    }

    // Filter overdue
    public ArrayList<Transaksi> getOverdueTransaksi() throws SQLException {
        String query = "SELECT * FROM transaksi WHERE tanggal_pengembalian < CURRENT_TIMESTAMP AND status_transaksi = '+Transaksi.Status.ACCEPTED.name()+'";
        ArrayList<Transaksi> list = new ArrayList<>();
        try (Statement st = koneksi.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                list.add(parseResult(rs));
            }
        }
        return list;
    }

    // Filter berdasarkan status
    public ArrayList<Transaksi> getTransaksiByStatus(String status) throws SQLException {
        String query = "SELECT * FROM transaksi WHERE status_transaksi = ?";
        ArrayList<Transaksi> list = new ArrayList<>();

        try (PreparedStatement ps = koneksi.prepareStatement(query)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(parseResult(rs));
            }
        }

        return list;
    }

    // Filter berdasarkan rentang tanggal
    public ArrayList<Transaksi> getTransaksiByTanggal(LocalDateTime from, LocalDateTime to) throws SQLException {
        String query = "SELECT * FROM transaksi WHERE tanggal_peminjaman BETWEEN ? AND ?";
        ArrayList<Transaksi> list = new ArrayList<>();
        try (PreparedStatement ps = koneksi.prepareStatement(query)) {
            ps.setTimestamp(1, Timestamp.valueOf(from));
            ps.setTimestamp(2, Timestamp.valueOf(to));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(parseResult(rs));
            }
        }
        return list;
    }

    // Laporan bulanan
    public ArrayList<Transaksi> getLaporanBulanan(int bulan, int tahun) throws SQLException {
        String query = "SELECT * FROM transaksi WHERE MONTH(tanggal_peminjaman) = ? AND YEAR(tanggal_peminjaman) = ?";
        ArrayList<Transaksi> list = new ArrayList<>();
        try (PreparedStatement ps = koneksi.prepareStatement(query)) {
            ps.setInt(1, bulan);
            ps.setInt(2, tahun);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(parseResult(rs));
            }
        }
        return list;
    }

    // Update status transaksi
    public boolean updateStatus(String idTransaksi, String newStatus) throws SQLException {
        String query = "UPDATE transaksi SET status_transaksi = ? WHERE id_transaksi = ?";
        try (PreparedStatement ps = koneksi.prepareStatement(query)) {
            ps.setString(1, newStatus);
            ps.setString(2, idTransaksi);
            return ps.executeUpdate() > 0;
        }
    }

    // Ambil transaksi berdasarkan ID
    public Transaksi getById(String idTransaksi) throws SQLException {
        String query = "SELECT * FROM transaksi WHERE id_transaksi = ?";
        try (PreparedStatement ps = koneksi.prepareStatement(query)) {
            ps.setString(1, idTransaksi);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return parseResult(rs);
                }
            }
        }
        return null;
    }

    // Parsing 1 baris ke objek Transaksi
    private Transaksi parseResult(ResultSet rs) throws SQLException {
        String id = rs.getString("id_transaksi");
        String isbn = rs.getString("isbn");
        String idUser = rs.getString("id_user");
        LocalDateTime tglPinjam = rs.getTimestamp("tanggal_peminjaman").toLocalDateTime();
        LocalDateTime tglKembali = rs.getTimestamp("tanggal_pengembalian") != null
                ? rs.getTimestamp("tanggal_pengembalian").toLocalDateTime()
                : null;
        String statusStr = rs.getString("status_transaksi");

        // Ambil buku lengkap
        Buku buku = bukuDAO.getByISBN(isbn);

        // Ambil member lengkap
        MemberDAO memberDAO = new MemberDAO();
        Member member = memberDAO.findMemberById(idUser);

        Transaksi trx = new Transaksi(id, buku, member, tglPinjam, tglKembali, Transaksi.Status.valueOf(statusStr));
        return trx;
    }

    public boolean update(Transaksi trx) throws SQLException {
        String query = "UPDATE transaksi SET id_user = ?, isbn = ?, tanggal_peminjaman = ?, tanggal_pengembalian = ?, status_transaksi = ? WHERE id_transaksi = ?";
        try (PreparedStatement ps = koneksi.prepareStatement(query)) {
            ps.setString(1, trx.getMember().getIdMember());
            ps.setString(2, trx.getBuku().get_isbn());
            ps.setTimestamp(3, Timestamp.valueOf(trx.getTanggalPeminjaman()));
            ps.setTimestamp(4,
                    trx.getTanggalPengembalian() != null ? Timestamp.valueOf(trx.getTanggalPengembalian()) : null);
            ps.setString(5, trx.getStatusTransaksi().name());
            ps.setString(6, trx.getIdTransaksi());
            return ps.executeUpdate() > 0;
        }
    }
}