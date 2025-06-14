package com.DAO;

import com.model.Member;
import com.utilities.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    private Connection connection;

    public MemberDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public Member findMemberById(String id) {
        String query = "SELECT * FROM members WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToMember(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Member findMemberByUsernameOrEmail(String usernameOrEmail) {
        String query = "SELECT * FROM members WHERE username = ? OR email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, usernameOrEmail);
            stmt.setString(2, usernameOrEmail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToMember(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public boolean insertMember(Member member) {
        String query = "INSERT INTO members (id_user, username, password, email, nama_lengkap, no_telepon, alamat, user_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, member.getIdMember());
            stmt.setString(2, member.getUsername());
            stmt.setString(3, member.getPassword());
            stmt.setString(4, member.getEmail());
            stmt.setString(5, member.getNamaLengkap());
            stmt.setString(6, member.getNoTelepon());
            stmt.setString(7, member.getAlamat());
            stmt.setString(8, member.getUserStatus().toString());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMember(Member member) {
        String query = "UPDATE members SET username = ?, password = ?, email = ?, nama_lengkap = ?, no_telepon = ?, alamat = ?, user_status = ? WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, member.getUsername());
            stmt.setString(2, member.getPassword());
            stmt.setString(3, member.getEmail());
            stmt.setString(4, member.getNamaLengkap());
            stmt.setString(5, member.getNoTelepon());
            stmt.setString(6, member.getAlamat());
            stmt.setString(7, member.getUserStatus().toString());
            stmt.setString(8, member.getIdMember());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMember(String id) {
        String query = "DELETE FROM members WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMemberStatus(String id, String status) {
        String query = "UPDATE members SET user_status = ? WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setString(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Member> getMembersBySearchAndStatus(String searchQuery, Member.MemberStatus status)
            throws SQLException {
        List<Member> members = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM members WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            queryBuilder.append(" AND (username LIKE ? OR nama_lengkap LIKE ? OR email LIKE ?)");
            String searchPattern = "%" + searchQuery.trim() + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        if (status != null) {
            queryBuilder.append(" AND user_status = ?");
            params.add(status.name());
        }

        queryBuilder.append(" ORDER BY username ASC");

        try (PreparedStatement stmt = connection.prepareStatement(queryBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        }
        return members;
    }

    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setIdMember(rs.getString("id_user"));
        member.setUsername(rs.getString("username"));
        member.setPassword(rs.getString("password"));
        member.setEmail(rs.getString("email"));
        member.setNamaLengkap(rs.getString("nama_lengkap"));
        member.setNoTelepon(rs.getString("no_telepon"));
        member.setAlamat(rs.getString("alamat"));
        member.setUserStatus(Member.MemberStatus.valueOf(rs.getString("user_status").toUpperCase()));
        member.setCreatedAt(rs.getTimestamp("created_at"));
        member.setUpdatedAt(rs.getTimestamp("updated_at"));
        return member;
    }
}