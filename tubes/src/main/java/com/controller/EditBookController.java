package com.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import java.io.File;
import java.time.LocalDateTime;

import com.tubes.App;
import com.model.Buku;
import com.DAO.BukuDAO;
import com.Service.BukuService;
import java.sql.Connection;
import java.sql.SQLException;

public class EditBookController {
    @FXML
    private Button backButton;
    @FXML
    private Label bookIdLabel;
    @FXML
    private VBox imagePreviewContainer;
    @FXML
    private Label imagePreviewLabel;
    @FXML
    private Button changeImageButton;
    @FXML
    private Button removeImageButton;
    @FXML
    private Label imagePathLabel;
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
    private Label borrowedLabel;
    @FXML
    private TextField publisherField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Label totalCopiesLabel;
    @FXML
    private Label availableCopiesLabel;
    @FXML
    private Label borrowedCopiesLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button cancelButton;
    @FXML
    private VBox deleteConfirmDialog;
    @FXML
    private Label deleteBookTitleLabel;
    @FXML
    private Button confirmDeleteButton;
    @FXML
    private Button cancelDeleteButton;

    private Buku currentBook;
    private BukuService bukuService;
    private String currentImagePath;

    @FXML
    public void initialize() {
        // Initialize genre and category options
        genreComboBox.getItems().addAll("Fiksi", "Non-Fiksi", "Fantasi", "Romance", "Misteri", "Sains");
        categoryComboBox.getItems().addAll("Novel", "Komik", "Biografi", "Pendidikan", "Teknologi", "Sejarah");

        // Initialize BukuService
        try {
            Connection dbConnection = App.getDatabaseConnection();
            if (dbConnection != null) {
                bukuService = new BukuService(new BukuDAO(dbConnection));
                System.out.println("BukuService initialized in EditBookController.");
            } else {
                System.err.println("Database connection not available. Cannot initialize BukuService in Edit Book view.");
                showAlert(AlertType.ERROR, "Koneksi Database", "Koneksi database tidak tersedia saat inisialisasi. Tidak dapat mengedit buku.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception during BukuService initialization in Edit Book view: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error Inisialisasi DB", "Gagal menginisialisasi layanan buku karena masalah database: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Other Exception during BukuService initialization in Edit Book view: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error Inisialisasi", "Gagal menginisialisasi layanan buku: " + e.getMessage());
        }

        // Load book data from App's current book
        loadBookData();
    }

    private void loadBookData() {
        // Get the current book from App
        currentBook = App.getCurrentBook();
        if (currentBook != null) {
            // Populate form fields
            bookIdLabel.setText("ISBN: " + currentBook.get_isbn());
            titleField.setText(currentBook.get_judul_buku());
            isbnField.setText(currentBook.get_isbn());
            authorField.setText(currentBook.get_penulis());
            yearField.setText(String.valueOf(currentBook.get_tahun_terbit()));
            genreComboBox.setValue(currentBook.get_genre());
            categoryComboBox.setValue(currentBook.get_kategori());
            stockField.setText(String.valueOf(currentBook.get_jumlah()));
            publisherField.setText(currentBook.get_penerbit());
            descriptionArea.setText(currentBook.get_deskripsi());

            // Update status labels
            updateStatusLabels();

          
        } else {
            showError("Tidak dapat memuat data buku");
        }
    }

    private void updateStatusLabels() {
        int total = currentBook.get_jumlah();
        int borrowed = 0; // TODO: Get actual borrowed count from database
        int available = total - borrowed;

        totalCopiesLabel.setText("Total: " + total + " eksemplar");
        availableCopiesLabel.setText("Tersedia: " + available + " eksemplar");
        borrowedCopiesLabel.setText("Dipinjam: " + borrowed + " eksemplar");
        borrowedLabel.setText("(" + borrowed + " sedang dipinjam)");
    }



    @FXML
    private void handleBack(ActionEvent event) {
        try {
            App.setRoot("AdminDashboard");
        } catch (IOException e) {
            showError("Gagal kembali ke dashboard: " + e.getMessage());
        }
    }


    @FXML
    private void handleRemoveImage(ActionEvent event) {
        imagePreviewContainer.getChildren().clear();
        imagePreviewContainer.getChildren().add(imagePreviewLabel);
        currentImagePath = null;
        imagePathLabel.setText("");
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        try {
            // Create updated book object
            Buku updatedBook = new Buku(
                isbnField.getText(),
                titleField.getText(),
                categoryComboBox.getValue(),
                Integer.parseInt(stockField.getText()),
                authorField.getText(),
                Integer.parseInt(yearField.getText()),
                genreComboBox.getValue(),
                publisherField.getText(),
                descriptionArea.getText()
            );

       
            if (bukuService != null) {
                boolean success = bukuService.updateBuku(updatedBook);
                if (success) {
                    showSuccess("Buku berhasil diperbarui");
                    try {
                        App.setRoot("AdminDashboard");
                    } catch (IOException e) {
                        showError("Gagal kembali ke dashboard: " + e.getMessage());
                    }
                } else {
                    showError("Gagal memperbarui buku");
                }
            } else {
                showError("Koneksi database tidak tersedia");
            }
        } catch (Exception e) {
            showError("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReset(ActionEvent event) {
        loadBookData();
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        deleteBookTitleLabel.setText("Judul: " + currentBook.get_judul_buku());
        deleteConfirmDialog.setVisible(true);
    }

    @FXML
    private void handleConfirmDelete(ActionEvent event) {
        if (bukuService != null) {
            boolean deleted = bukuService.hapusBuku(currentBook.get_isbn());
            if (deleted) {
                showSuccess("Buku berhasil dihapus");
                try {
                    App.setRoot("AdminDashboard");
                } catch (IOException e) {
                    showError("Gagal kembali ke dashboard: " + e.getMessage());
                }
            } else {
                showError("Gagal menghapus buku");
            }
        } else {
            showError("Koneksi database tidak tersedia");
        }
        deleteConfirmDialog.setVisible(false);
    }

    @FXML
    private void handleCancelDelete(ActionEvent event) {
        deleteConfirmDialog.setVisible(false);
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
        return true;
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

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 