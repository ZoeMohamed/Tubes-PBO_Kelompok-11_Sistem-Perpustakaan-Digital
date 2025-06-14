package com.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Insets;

import com.tubes.App;
import com.model.Buku;
import com.model.Ulasan;
import com.model.Member;
import com.Service.UlasanService;
import com.Service.TransaksiService;
import java.sql.SQLException;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import com.Service.BukuService;
import com.DAO.BukuDAO;
import java.sql.Connection;

public class BookDetailController {
    @FXML
    private Button backButton;
    @FXML
    private Button borrowButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label isbnLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label stockLabel;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Button addReviewButton;
    @FXML
    private VBox reviewForm;
    @FXML
    private TextArea reviewTextArea;
    @FXML
    private Button submitReviewButton;
    @FXML
    private Button cancelReviewButton;
    @FXML
    private VBox reviewsList;
    @FXML
    private ComboBox<Integer> ratingComboBox;
    @FXML
    private Label averageRatingLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label yearLabel;
    @FXML
    private Label genreLabel;

    private Buku currentBook;
    private UlasanService ulasanService;
    private TransaksiService transaksiService;
    private BukuService bukuService;

    @FXML
    public void initialize() {
        try {
            ulasanService = new UlasanService();
            transaksiService = new TransaksiService();
            System.out.println("Services initialized in BookDetailController.");
            Connection dbConnection = App.getDatabaseConnection();
            if (dbConnection != null) {
                bukuService = new BukuService(new BukuDAO(dbConnection));
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize services: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Initialization Error", "Failed to initialize services.");
        }

        ratingComboBox.getItems().addAll(1, 2, 3, 4, 5);

        reviewForm.setVisible(false);
    }

    public void setBookDetails(Buku buku) {
        this.currentBook = buku;

        if (currentBook != null) {
            titleLabel.setText(currentBook.get_judul_buku());
            isbnLabel.setText(currentBook.get_isbn());
            authorLabel.setText(currentBook.get_penulis());
            yearLabel.setText(String.valueOf(currentBook.get_tahun_terbit()));
            genreLabel.setText(currentBook.get_genre());
            categoryLabel.setText(currentBook.get_kategori());
            stockLabel.setText(String.valueOf(currentBook.get_jumlah()));
            descriptionArea.setText(currentBook.get_deskripsi());

            loadAndDisplayReviews(currentBook.get_isbn());
        } else {
            System.err.println("Book details not available.");
            showAlert(AlertType.ERROR, "Error", "Could not load book details.");
        }
        updateButtonVisibility(App.getCurrentUserRole());
        reviewForm.setVisible(false);
    }

    private void loadAndDisplayReviews(String isbn) {
        reviewsList.getChildren().clear();
        double totalRating = 0;
        int numberOfReviews = 0;

        if (ulasanService != null) {
            List<Ulasan> reviews = ulasanService.getUlasanByISBN(isbn);
            if (reviews != null && !reviews.isEmpty()) {
                for (Ulasan ulasan : reviews) {
                    reviewsList.getChildren().add(createReviewItem(ulasan));
                    totalRating += ulasan.getRating();
                    numberOfReviews++;
                }

                double averageRating = totalRating / numberOfReviews;
                averageRatingLabel
                        .setText(String.format("(Rating: %.1f/5 dari %d ulasan)", averageRating, numberOfReviews));
            } else {
                averageRatingLabel.setText("(Belum ada rating)");
                Label noReviewLabel = new Label("Belum ada ulasan untuk buku ini.");
                noReviewLabel.setStyle("-fx-text-fill: #7f8c8d;");
                reviewsList.getChildren().add(noReviewLabel);
            }
        } else {
            averageRatingLabel.setText("(Review service not available)");
            Label errorReviewLabel = new Label("Could not load reviews due to service error.");
            errorReviewLabel.setStyle("-fx-text-fill: #e74c3c;");
            reviewsList.getChildren().add(errorReviewLabel);
        }
    }

    private VBox createReviewItem(Ulasan ulasan) {
        VBox reviewItem = new VBox(5);
        reviewItem.setStyle(
                "-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5; -fx-padding: 10;");

        String reviewerInfo = (ulasan.getPengulas() != null)
                ? "Review by Member (ID: " + ulasan.getPengulas().getIdMember() + ")"
                : "Review by Unknown Member";

        Label reviewerLabel = new Label(reviewerInfo);
        reviewerLabel.setStyle("-fx-font-weight: bold;");

        Label reviewTextLabel = new Label(ulasan.getUlasan());
        reviewTextLabel.setWrapText(true);

        Label ratingLabel = new Label("Rating: " + ulasan.getRating() + "/5");
        ratingLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #555555;");

        Label dateLabel = new Label("Tanggal: " + ulasan.getTanggalUlasan().toLocalDate().toString());
        dateLabel.setStyle("-fx-font-size: 10; -fx-text-fill: #7f8c8d;");

        reviewItem.getChildren().addAll(reviewerLabel, reviewTextLabel, ratingLabel, dateLabel);
        return reviewItem;
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            App.setRoot("BookList");
        } catch (IOException e) {
            System.err.println("Failed to load BookList: " + e.getMessage());
            showAlert(AlertType.ERROR, "Navigation Error", "Failed to go back to book list.");
        }
    }

    @FXML
    private void handleBorrow(ActionEvent event) {
        if (!"Member".equals(App.getCurrentUserRole())) {
            showAlert(AlertType.WARNING, "Akses Ditolak", "Hanya member yang dapat meminjam buku.");
            return;
        }
        if (currentBook == null) {
            showAlert(AlertType.ERROR, "Error", "Buku tidak ditemukan.");
            return;
        }
        if (currentBook.get_jumlah() <= 0) {
            showAlert(AlertType.WARNING, "Stok Habis", "Stok buku ini sudah habis.");
            return;
        }
        String userId = App.getCurrentUser();
        if (userId == null) {
            showAlert(AlertType.ERROR, "Error", "User tidak ditemukan.");
            return;
        }
        Member member = new Member();
        member.setIdMember(userId);
        boolean sukses = transaksiService.pinjamBuku(currentBook, member);
        if (sukses) {
            showAlert(AlertType.INFORMATION, "Sukses", "Peminjaman buku berhasil!");
            // Update stok di tampilan
            currentBook.set_jumlah(currentBook.get_jumlah() - 1);
            stockLabel.setText(String.valueOf(currentBook.get_jumlah()));
        } else {
            showAlert(AlertType.ERROR, "Gagal", "Peminjaman buku gagal. Stok habis atau terjadi error.");
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        if (currentBook == null) {
            showAlert(AlertType.ERROR, "Error", "Buku tidak ditemukan.");
            return;
        }
        showUpdateBookDialog(currentBook);
    }

    private void showUpdateBookDialog(Buku buku) {
        try {
            // Create the custom dialog.
            Dialog<Buku> dialog = new Dialog<>();
            dialog.setTitle("Update Buku");
            dialog.setHeaderText("Update Informasi Buku");

            // Set the button types.
            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            // Create the input fields and labels.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField titleField = new TextField();
            titleField.setPromptText("Judul Buku");
            titleField.setText(buku.get_judul_buku());

            TextField isbnField = new TextField();
            isbnField.setPromptText("ISBN");
            isbnField.setText(buku.get_isbn());
            isbnField.setDisable(true);

            TextField authorField = new TextField();
            authorField.setPromptText("Penulis");
            authorField.setText(buku.get_penulis());

            TextField yearField = new TextField();
            yearField.setPromptText("Tahun Terbit");
            yearField.setText(String.valueOf(buku.get_tahun_terbit()));

            ComboBox<String> genreComboBox = new ComboBox<>();
            genreComboBox.setPromptText("Genre");
            genreComboBox.getItems().addAll("Fiksi", "Non-Fiksi", "Fantasi", "Romance", "Misteri", "Sains");
            genreComboBox.setValue(buku.get_genre());

            ComboBox<String> categoryComboBox = new ComboBox<>();
            categoryComboBox.setPromptText("Kategori");
            categoryComboBox.getItems().addAll("Novel", "Komik", "Biografi", "Pendidikan", "Teknologi", "Sejarah");
            categoryComboBox.setValue(buku.get_kategori());

            TextField stockField = new TextField();
            stockField.setPromptText("Jumlah Buku");
            stockField.setText(String.valueOf(buku.get_jumlah()));

            TextField publisherField = new TextField();
            publisherField.setPromptText("Penerbit");
            publisherField.setText(buku.get_penerbit());

            TextArea descriptionArea = new TextArea();
            descriptionArea.setPromptText("Deskripsi");
            descriptionArea.setText(buku.get_deskripsi());
            descriptionArea.setWrapText(true);

            grid.add(new Label("Judul:"), 0, 0);
            grid.add(titleField, 1, 0);
            grid.add(new Label("ISBN:"), 0, 1);
            grid.add(isbnField, 1, 1);
            grid.add(new Label("Penulis:"), 0, 2);
            grid.add(authorField, 1, 2);
            grid.add(new Label("Tahun Terbit:"), 0, 3);
            grid.add(yearField, 1, 3);
            grid.add(new Label("Genre:"), 0, 4);
            grid.add(genreComboBox, 1, 4);
            grid.add(new Label("Kategori:"), 0, 5);
            grid.add(categoryComboBox, 1, 5);
            grid.add(new Label("Jumlah:"), 0, 6);
            grid.add(stockField, 1, 6);
            grid.add(new Label("Penerbit:"), 0, 7);
            grid.add(publisherField, 1, 7);
            grid.add(new Label("Deskripsi:"), 0, 8);
            grid.add(descriptionArea, 1, 8);

            dialog.getDialogPane().setContent(grid);

            // Convert the result to a Buku object when the update button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    try {
                        // Create a new Buku object with updated values
                        Buku updatedBuku = new Buku(
                                buku.get_isbn(),
                                titleField.getText(),
                                categoryComboBox.getValue(),
                                Integer.parseInt(stockField.getText()),
                                authorField.getText(),
                                Integer.parseInt(yearField.getText()),
                                genreComboBox.getValue(),
                                publisherField.getText(),
                                descriptionArea.getText());
                        return updatedBuku;
                    } catch (NumberFormatException e) {
                        showAlert(AlertType.ERROR, "Error Validasi",
                                "Jumlah buku dan tahun terbit harus berupa angka.");
                        return null;
                    }
                }
                return null;
            });

            System.out.println("Attempting to show dialog..."); // Debug print
            // Show the dialog and process the result.
            dialog.showAndWait().ifPresent(updatedBuku -> {
                if (updatedBuku != null) {
                    if (bukuService != null) {
                        try {
                            boolean success = bukuService.updateBuku(updatedBuku);
                            if (success) {
                                showAlert(AlertType.INFORMATION, "Success", "Buku berhasil diperbarui.");
                                // Update the current book and refresh the view
                                currentBook = updatedBuku;
                                setBookDetails(updatedBuku);
                            } else {
                                showAlert(AlertType.ERROR, "Error", "Gagal memperbarui buku.");
                            }
                        } catch (Exception e) {
                            showAlert(AlertType.ERROR, "Error",
                                    "Terjadi kesalahan saat memperbarui buku: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        showAlert(AlertType.ERROR, "Error", "Koneksi database tidak tersedia. Gagal memperbarui buku.");
                    }
                }
            });
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error Menampilkan Dialog",
                    "Terjadi kesalahan saat mencoba menampilkan dialog edit: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        System.out.println("Delete button clicked");
    }

    @FXML
    private void handleAddReview(ActionEvent event) {
        if ("Member".equals(App.getCurrentUserRole())) {
            reviewForm.setVisible(true);
            addReviewButton.setVisible(false);
            reviewTextArea.clear();
            ratingComboBox.getSelectionModel().clearSelection();
            System.out.println("Add Review button clicked");
        } else {
            showAlert(AlertType.WARNING, "Akses Ditolak", "Hanya member yang dapat menambahkan ulasan.");
        }
    }

    @FXML
    private void handleSubmitReview(ActionEvent event) {
        String reviewText = reviewTextArea.getText();
        Integer rating = ratingComboBox.getValue();

        if (reviewText == null || reviewText.trim().isEmpty()) {
            showAlert(AlertType.WARNING, "Input Kurang", "Ulasan tidak boleh kosong.");
            return;
        }
        if (rating == null) {
            showAlert(AlertType.WARNING, "Input Kurang", "Rating harus dipilih.");
            return;
        }

        String currentUserId = App.getCurrentUser();
        if (currentUserId == null || currentUserId.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "User not logged in.");
            return;
        }

        Member reviewer = new Member();
        reviewer.setIdMember(currentUserId);

        Ulasan newUlasan = new Ulasan(
                null,
                currentBook,
                reviewer,
                reviewText.trim(),
                rating);

        if (ulasanService != null) {
            boolean success = ulasanService.tambahUlasan(newUlasan);
            if (success) {
                showAlert(AlertType.INFORMATION, "Sukses", "Ulasan berhasil ditambahkan.");
                loadAndDisplayReviews(currentBook.get_isbn());
                reviewForm.setVisible(false);
                addReviewButton.setVisible(true);
                reviewTextArea.clear();
                ratingComboBox.getSelectionModel().clearSelection();
            } else {
                showAlert(AlertType.ERROR, "Gagal", "Gagal menambahkan ulasan.");
            }
        } else {
            showAlert(AlertType.ERROR, "Error", "Review service not available. Cannot submit review.");
        }
    }

    @FXML
    private void handleCancelReview(ActionEvent event) {
        reviewForm.setVisible(false);
        addReviewButton.setVisible(true);
        reviewTextArea.clear();
        ratingComboBox.getSelectionModel().clearSelection();
        System.out.println("Cancel Review button clicked");
    }

    public void updateButtonVisibility(String role) {
        if ("Member".equals(role)) {
            borrowButton.setVisible(true);
            editButton.setVisible(false);
            deleteButton.setVisible(false);
            addReviewButton.setVisible(true);
            reviewForm.setVisible(false);
        } else if ("Admin".equals(role)) {
            borrowButton.setVisible(false);
            editButton.setVisible(true);
            deleteButton.setVisible(true);
            addReviewButton.setVisible(false);
            reviewForm.setVisible(false);
        } else {
            borrowButton.setVisible(false);
            editButton.setVisible(false);
            deleteButton.setVisible(false);
            addReviewButton.setVisible(false);
            reviewForm.setVisible(false);
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}