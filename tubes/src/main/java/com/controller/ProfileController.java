package com.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import java.io.IOException;

import com.tubes.App;
import com.Service.MemberService;
import com.Service.AdminService;
import com.model.Member;
import com.model.Admin;
import com.model.User;
import javafx.scene.control.Alert.AlertType;

public class ProfileController {
    @FXML
    private Button backButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextArea addressField;
    @FXML
    private TextField joinDateField; // This might need adjustment based on Member model
    @FXML
    private Button updateButton;
    @FXML
    private Button logoutButton;
    @FXML
    private HBox editButtons;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label messageLabel;

    private MemberService memberService = new MemberService();
    private AdminService adminService = new AdminService();
    private Object currentUserProfile;

    @FXML
    public void initialize() {
        loadUserProfile();
    }

    private void loadUserProfile() {
        String userId = App.getCurrentUser();
        String userRole = App.getCurrentUserRole();

        usernameLabel.setText(userId); // Displaying ID for now, can change to username if retrieved
        roleLabel.setText(userRole);

        // Load data based on role
        if ("Member".equals(userRole)) {
            Member member = memberService.getMemberById(userId);
            currentUserProfile = member;
            if (member != null) {
                nameField.setText(member.getNamaLengkap());
                emailField.setText(member.getEmail());
                phoneField.setText(member.getNoTelepon());
                if (member.getCreatedAt() != null) {
                    joinDateField.setText(member.getCreatedAt().toString());
                } else {
                    joinDateField.setText("N/A");
                }

                setFieldsEditable(true);
                editButtons.setVisible(true);
                updateButton.setVisible(false);

            } else {
                showAlert(AlertType.ERROR, "Error", "Gagal memuat data profil member.");
                setFieldsEditable(false);
                editButtons.setVisible(false);
                updateButton.setVisible(false);
            }
        } else if ("Admin".equals(userRole)) {
            Admin admin = adminService.getAdminById(userId);
            currentUserProfile = admin;
            if (admin != null) {
                nameField.setText(admin.getNamaLengkap());
                emailField.setText(admin.getEmail());
                phoneField.setText(admin.getNoTelepon());
                if (admin.getCreatedAt() != null) {
                    joinDateField.setText(admin.getCreatedAt().toString());
                } else {
                    joinDateField.setText("N/A");
                }

                setFieldsEditable(true);
                editButtons.setVisible(true);
                updateButton.setVisible(false);

            } else {
                showAlert(AlertType.ERROR, "Error", "Gagal memuat data profil admin.");
                setFieldsEditable(false);
                editButtons.setVisible(false);
                updateButton.setVisible(false);
            }
        } else { // Guest or other roles
            showAlert(AlertType.INFORMATION, "Info", "Guest profile view (not applicable).");
            setFieldsEditable(false);
            editButtons.setVisible(false);
            updateButton.setVisible(false);
        }
    }

    private void setFieldsEditable(boolean editable) {
        nameField.setEditable(editable);
        emailField.setEditable(editable);
        phoneField.setEditable(editable);
        addressField.setEditable(editable);
        // joinDateField should probably not be editable
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            if ("Member".equals(App.getCurrentUserRole())) {
                com.tubes.App.setRoot("BookList");
            } else if ("Admin".equals(App.getCurrentUserRole())) {
                com.tubes.App.setRoot("AdminDashboard");
            } else { // Guest
                com.tubes.App.setRoot("BookList");
            }
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Navigation Error", "Gagal kembali ke halaman sebelumnya.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        setFieldsEditable(true);
        editButtons.setVisible(true);
        updateButton.setVisible(false);
        messageLabel.setVisible(false);
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (currentUserProfile != null) {
            boolean success = false;
            if (currentUserProfile instanceof Member) {
                Member member = (Member) currentUserProfile;
                member.setNamaLengkap(nameField.getText());
                member.setEmail(emailField.getText());
                member.setNoTelepon(phoneField.getText());
                success = memberService.updateMemberProfile(member);
            } else if (currentUserProfile instanceof Admin) {
                showAlert(AlertType.INFORMATION, "Info", "Update Admin profile is not implemented yet.");
                return;
            }

            if (success) {
                showAlert(AlertType.INFORMATION, "Sukses", "Perubahan profil berhasil disimpan.");
                setFieldsEditable(false);
                editButtons.setVisible(false);
                updateButton.setVisible(true);
            } else {
                showAlert(AlertType.ERROR, "Gagal", "Gagal menyimpan perubahan profil.");
            }
        } else {
            showAlert(AlertType.ERROR, "Error", "Tidak ada data profil untuk disimpan.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        loadUserProfile(); // Reload original data
        setFieldsEditable(false);
        editButtons.setVisible(false);
        updateButton.setVisible(true);
        messageLabel.setText("Edit dibatalkan.");
        messageLabel.setStyle("-fx-text-fill: #e67e22;");
        messageLabel.setVisible(true);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            App.logout();
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Logout Error", "Gagal logout.");
            e.printStackTrace();
        }
    }

    // Helper method to show alerts
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}