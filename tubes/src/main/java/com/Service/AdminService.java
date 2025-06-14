package com.Service;
// tolong sesuain sama data yang di view yaak

import com.DAO.AdminDAO;
import com.model.Admin;
import com.utilities.DatabaseConnection;

import java.util.ArrayList;
import java.util.List;

public class AdminService {
    private AdminDAO adminDAO;

    public AdminService() {
        this.adminDAO = new AdminDAO();
    }

    // Placeholder for password verification utility (should use hashing)
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        // TODO: Implement actual password verification (e.g., using BCrypt)
        return plainPassword.equals(hashedPassword); // For now, simple string comparison
    }

    public Admin loginAdmin(String identifier, String password) {
        try {
            Admin admin = adminDAO.findAdminByUsernameOrEmail(identifier);
            if (admin != null && verifyPassword(password, admin.getPassword())) {
                return admin;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Admin getAdminById(String adminId) {
        try {
            return adminDAO.findAdminById(adminId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Admin> getAllAdmins() {
        try {
            return adminDAO.getAllAdmins();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean deleteAdmin(String adminId) {
        try {
            return adminDAO.deleteAdmin(adminId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAdmin(Admin admin) {
        try {
            return adminDAO.updateAdmin(admin);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addAdmin(Admin admin) {
        try {
            return adminDAO.insertAdmin(admin);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}