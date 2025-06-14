package com.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.SQLException;

import com.tubes.App;
import com.model.Buku;
import com.Service.BukuService;
import com.DAO.BukuDAO;

public class AddBookController {
    @FXML
    private Button backButton;

    @FXML
    private TextField titleField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField yearField;
    @FXML
    private ComboBox<String> genreComboBox;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextField stockField;
    @FXML
    private TextField publisherField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Label messageLabel;
    @FXML
    private Button confirmButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button cancelButton;

    private BukuService bukuService;

    @FXML
    public void initialize() {
        // Initialize genre and category options
        genreComboBox.getItems().addAll("Fiksi", "Non-Fiksi", "Fantasi", "Romance", "Misteri", "Sains");
        categoryComboBox.getItems().addAll("Novel", "Komik", "Biografi", "Pendidikan", "Teknologi", "Sejarah");

        // Initialize BukuService
        try {
            System.out.println("Attempting to get database connection..."); // Debug print
            // Get database connection from App
            Connection dbConnection = App.getDatabaseConnection();
            if (dbConnection != null) {
                System.out.println("Database connection obtained successfully."); // Debug print
                bukuService = new BukuService(new BukuDAO(dbConnection));
                System.out.println("BukuService initialized."); // Debug print
            } else {
                // This else block should ideally not be reached if MysqlUtilities throws
                // exception on failure
                System.err.println("Database connection not available. BukuService not initialized."); // Debug print
                // Handle case where connection is not available (e.g., show error message)
                showAlert(Alert.AlertType.ERROR, "Koneksi Database",
                        "Koneksi database tidak tersedia saat inisialisasi. Tidak dapat menambah buku.");
            }
        } catch (SQLException e) { // Catch SQLException specifically
            System.err.println("SQL Exception during BukuService initialization: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            showAlert(Alert.AlertType.ERROR, "Error Inisialisasi DB",
                    "Gagal menginisialisasi layanan buku karena masalah database: " + e.getMessage());
        } catch (Exception e) { // Catch any other exceptions
            System.err.println("Other Exception during BukuService initialization: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            showAlert(Alert.AlertType.ERROR, "Error Inisialisasi",
                    "Gagal menginisialisasi layanan buku: " + e.getMessage());
        }

        // Load initial books (This method is not in AddBookController, should be
        // removed or is leftover from copy-paste)
        // loadBooks(); // Remove this line

        // Update UI based on user role (This method is not in AddBookController, should
        // be removed or is leftover from copy-paste)
        // updateUIForUserRole(); // Remove this line
    }

    // loadBooks method is not part of AddBookController - It's from
    // BookListController/AdminDashboardController
    // Remove this method if it was mistakenly added here.
    // private void loadBooks() { ... }

    // updateUIForUserRole method is not part of AddBookController - It's from
    // BookListController/AdminDashboardController
    // Remove this method if it was mistakenly added here.
    // private void updateUIForUserRole() { ... }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            App.setRoot("AdminDashboard");
        } catch (IOException e) {
            showError("Gagal kembali ke dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        try {
            // Validate input
            if (!validateInput()) {
                return;
            }

            // Create new Buku object
            Buku buku = new Buku(
                    isbnField.getText(),
                    titleField.getText(),
                    categoryComboBox.getValue(),
                    Integer.parseInt(stockField.getText()),
                    authorField.getText(),
                    Integer.parseInt(yearField.getText()),
                    genreComboBox.getValue(),
                    publisherField.getText(),
                    descriptionArea.getText());

            System.out.println("Attempting to add book to database..."); // Debug print
            // Save to database
            if (bukuService != null) {
                if (bukuService.tambahBuku(buku)) {
                    showSuccess("Buku berhasil ditambahkan");
                    clearFields();
                    System.out.println("Book added successfully."); // Debug print
                } else {
                    // BukuService.tambahBuku can return false if validation fails within the
                    // service
                    System.err.println("BukuService.tambahBuku returned false."); // Debug print
                    showError("Gagal menambahkan buku: Validasi data atau masalah lain.");
                }
            } else {
                System.err.println("BukuService is null. Database connection failed previously."); // Debug print
                showError("Gagal menambahkan buku: Koneksi database tidak tersedia.");
            }

        } catch (IllegalArgumentException e) { // Catch specific validation exceptions from service
            System.err.println("Validation error during book add: " + e.getMessage());
            showError("Gagal menambahkan buku: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception during book add: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            showError("Terjadi kesalahan saat menambahkan buku: " + e.getMessage());
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            App.setRoot("AdminDashboard");
        } catch (IOException e) {
            showError("Gagal kembali ke dashboard: " + e.getMessage());
        }
    }

    private boolean validateInput() {
        if (titleField.getText().isEmpty()) {
            showError("Judul buku tidak boleh kosong");
            return false;
        }
        if (isbnField.getText().isEmpty()) {
            showError("ISBN tidak boleh kosong");
            return false;
        }
        if (authorField.getText().isEmpty()) {
            showError("Penulis tidak boleh kosong");
            return false;
        }
        if (yearField.getText().isEmpty()) {
            showError("Tahun terbit tidak boleh kosong");
            return false;
        }
        try {
            int year = Integer.parseInt(yearField.getText());
            if (year < 1800 || year > LocalDateTime.now().getYear()) {
                showError("Tahun terbit tidak valid");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Tahun terbit harus berupa angka");
            return false;
        }
        if (genreComboBox.getValue() == null) {
            showError("Genre harus dipilih");
            return false;
        }
        if (categoryComboBox.getValue() == null) {
            showError("Kategori harus dipilih");
            return false;
        }
        if (stockField.getText().isEmpty()) {
            showError("Jumlah buku tidak boleh kosong");
            return false;
        }
        try {
            int stock = Integer.parseInt(stockField.getText());
            if (stock < 0) {
                showError("Jumlah buku tidak boleh negatif");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Jumlah buku harus berupa angka");
            return false;
        }
        if (publisherField.getText().isEmpty()) {
            showError("Penerbit tidak boleh kosong");
            return false;
        }
        // Removed image path validation
        return true;
    }

    private void clearFields() {
        titleField.clear();
        isbnField.clear();
        authorField.clear();
        yearField.clear();
        genreComboBox.getSelectionModel().clearSelection();
        categoryComboBox.getSelectionModel().clearSelection();
        stockField.clear();
        publisherField.clear();
        descriptionArea.clear();
        // Removed image related clear actions
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #e74c3c;");
        messageLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #27ae60;");
        messageLabel.setVisible(true);
    }

    // Helper method for showing alerts - Make sure this is present
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}