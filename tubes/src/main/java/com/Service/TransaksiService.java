package com.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.DAO.BukuDAO;
import com.DAO.MemberDAO;
import com.DAO.TransaksiDAO;
import com.model.Buku;
import com.model.Member;
import com.model.Transaksi;
import com.utilities.MysqlUtilities;

@SuppressWarnings("java:S1165")
public class TransaksiService {
    private TransaksiDAO transaksiDAO;
    private BukuDAO bukuDAO;
    private MemberDAO memberDAO;

    public TransaksiService(TransaksiDAO transaksiDAO, BukuDAO bukuDAO, MemberDAO memberDAO) {
        this.transaksiDAO = transaksiDAO;
        this.bukuDAO = bukuDAO;
        this.memberDAO = memberDAO;
    }

    public TransaksiService() throws SQLException {
        // buat koneksi dengan Dao
        Connection connection = MysqlUtilities.getConnection();
        // Inisialisasi Dao
        this.transaksiDAO = new TransaksiDAO(connection);
        this.bukuDAO = new BukuDAO(connection);
        this.memberDAO = new MemberDAO();
    }

    // Proses peminjaman buku
    public boolean pinjamBuku(Buku buku, Member member) {
        try {
            Buku b = bukuDAO.getByISBN(buku.get_isbn());
            if (b == null || b.get_jumlah() <= 0) {
                System.out.println("Stok habis.");
                return false;
            }

            Transaksi trx = new Transaksi(b, member);
            boolean suksesInsert = transaksiDAO.insertTransaksi(trx);
            b.set_jumlah(b.get_jumlah() - 1);
            boolean suksesKurangi = bukuDAO.update(b);

            return suksesInsert && suksesKurangi;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Proses pengembalian buku
    public boolean kembalikanBuku(String idTransaksi) {
        try {
            Transaksi trx = transaksiDAO.getById(idTransaksi);
            if (trx == null)
                return false;

            Transaksi.Status statusBaru = trx.isOverdue() ? Transaksi.Status.OVERDUE : Transaksi.Status.RETURNED;
            boolean updateStatus = transaksiDAO.updateStatus(idTransaksi, statusBaru.name());

            // If overdue, set member status to FREEZE
            if (statusBaru == Transaksi.Status.OVERDUE) {
                Member member = trx.getMember();
                if (member != null) {
                    member.setUserStatus(Member.MemberStatus.FREEZE);
                    memberDAO.updateMember(member);
                }
            }

            // Tambah stok kembali
            Buku buku = bukuDAO.getByISBN(trx.getBuku().get_isbn());
            buku.set_jumlah(buku.get_jumlah() + 1);
            boolean updateStok = bukuDAO.update(buku);

            return updateStatus && updateStok;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Ambil semua transaksi (admin view)
    public ArrayList<Transaksi> getSemuaTransaksi() {
        try {
            return transaksiDAO.getAllTransaksi();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Ambil transaksi pending (status = PENDING_PEMINJAMAN)
    public ArrayList<Transaksi> getPendingTransaksi() {
        try {
            return transaksiDAO.getTransaksiByStatus(Transaksi.Status.PENDING_PEMINJAMAN.name());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Ambil transaksi terlambat
    public ArrayList<Transaksi> getOverdueTransaksi() {
        try {
            return transaksiDAO.getOverdueTransaksi();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Ambil transaksi dalam rentang tanggal
    public ArrayList<Transaksi> getByTanggal(LocalDateTime from, LocalDateTime to) {
        try {
            return transaksiDAO.getTransaksiByTanggal(from, to);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Laporan per bulan
    public ArrayList<Transaksi> getLaporanBulanan(int bulan, int tahun) {
        try {
            return transaksiDAO.getLaporanBulanan(bulan, tahun);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Ambil 1 transaksi (untuk detail)
    public Transaksi getTransaksiById(String id) {
        try {
            return transaksiDAO.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update status transaksi (untuk validasi admin)
    public boolean updateStatus(String idTransaksi, String newStatus) {
        try {
            return transaksiDAO.updateStatus(idTransaksi, newStatus);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTransaksi(Transaksi trx) {
        try {
            return transaksiDAO.update(trx);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}