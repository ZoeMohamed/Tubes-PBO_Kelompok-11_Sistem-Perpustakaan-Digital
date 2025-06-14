package com.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import com.tubes.App;
import com.Service.MemberService;
import com.Service.AdminService;
import com.model.Member;
import com.model.Admin; // Import Admin model

import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

public class LandingPageController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Button guestButton;
    @FXML
    private Label errorLabel;
    @FXML
    private VBox root;

    private MemberService memberService = new MemberService();
    private AdminService adminService = new AdminService(); // Initialize AdminService

    @FXML
    public void initialize() {
        // Create and start fade in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    @FXML
    private void handleLogin() {
        String identifier = emailField.getText();
        String password = passwordField.getText();

        if (identifier.isEmpty() || password.isEmpty()) {
            showError("Email/Username dan password harus diisi");
            return;
        }

        // Try admin login first
        Admin admin = adminService.loginAdmin(identifier, password);
        if (admin != null) {
            // Admin login successful
            App.setCurrentUser(String.valueOf(admin.getIdAdmin()));
            App.setCurrentUserRole("Admin");
            try {
                App.setRoot("AdminDashboard");
            } catch (IOException e) {
                showError("Gagal memuat halaman admin.");
                e.printStackTrace();
            }
            return;
        }

        // If admin login fails, try member login
        Member member = memberService.loginMember(identifier, password);
        if (member != null) {
            // Check if member is frozen
            if (member.getUserStatus().equals(Member.MemberStatus.FREEZE.name())) {
                showError("Akun Anda dibekukan karena keterlambatan pengembalian buku. Silakan hubungi admin.");
                return;
            }
            // Member login successful
            App.setCurrentUser(String.valueOf(member.getIdMember()));
            App.setCurrentUserRole("Member");
            try {
                App.setRoot("BookList");
            } catch (IOException e) {
                showError("Gagal memuat halaman member.");
                e.printStackTrace();
            }
        } else {
            showError("Email/Username atau password salah.");
        }
    }

    @FXML
    private void handleRegister() {
        try {
            App.setRoot("RegisterPage");
        } catch (Exception e) {
            showError("Terjadi kesalahan: " + e.getMessage());
        }
    }

    @FXML
    private void handleGuest() {
        try {
            App.setCurrentUser("Guest");
            App.setCurrentUserRole("Guest");
            App.setRoot("BookList"); // Navigate to BookList for guest
        } catch (Exception e) {
            showError("Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void showError(String message) {
        // Using Alert instead of Label for better visibility
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        // Keep the errorLabel logic for other messages if needed, or remove if only
        // using Alerts
        // errorLabel.setText(message);
        // errorLabel.setVisible(true);
        // // Hide error after 3 seconds
        // FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), errorLabel);
        // fadeOut.setFromValue(1.0);
        // fadeOut.setToValue(0.0);
        // fadeOut.setOnFinished(e -> errorLabel.setVisible(false));
        // fadeOut.play();
    }
}