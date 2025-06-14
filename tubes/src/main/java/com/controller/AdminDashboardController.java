package com.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.util.Pair;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;

import com.tubes.App;
import com.model.Admin;
import com.model.Buku;
import com.Service.BukuService;
import com.DAO.BukuDAO;
import javafx.application.Platform;
import javafx.stage.Stage;

public class AdminDashboardController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private VBox sidebar;
    @FXML
    private Label profilePic;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button usersButton;
    @FXML
    private Button transactionsButton;
    @FXML
    private Button backButton;
    @FXML
    private Label dashboardTitleLabel;
    @FXML
    private Button loginGuestButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private Button filterButton;
    @FXML
    private HBox filterPanel;
    @FXML
    private TextField yearFilter;
    @FXML
    private ComboBox<String> genreFilter;
    @FXML
    private ComboBox<String> categoryFilter;
    @FXML
    private Button applyFilterButton;
    @FXML
    private Button clearFilterButton;
    @FXML
    private VBox bookListContainer;
    @FXML
    private Button addBookButton;

    private Admin currentAdmin;
    private BukuService bukuService;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome, Admin");
        sidebar.setVisible(true);
        sidebar.setManaged(true);

        usersButton.setVisible(true);
        usersButton.setManaged(true);
        transactionsButton.setVisible(true);
        transactionsButton.setManaged(true);

        dashboardTitleLabel.setText("Admin Dashboard");
        backButton.setVisible(false);
        backButton.setManaged(false);
        loginGuestButton.setVisible(false);
        loginGuestButton.setManaged(false);
        logoutButton.setVisible(true);
        logoutButton.setManaged(true);

        addBookButton.setVisible(true);
        addBookButton.setManaged(true);

        genreFilter.getItems().addAll("Fiksi", "Non-Fiksi", "Fantasi", "Romance", "Misteri", "Sains");
        categoryFilter.getItems().addAll("Novel", "Komik", "Biografi", "Pendidikan", "Teknologi", "Sejarah");

        try {
            Connection connection = App.getDatabaseConnection();
            if (connection != null) {
                bukuService = new BukuService(new BukuDAO(connection));
                loadAdminData();
                loadBooks();
            } else {
                showAlert(AlertType.ERROR, "Database Error", "Failed to get database connection");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Failed to initialize database connection: " + e.getMessage());
        }
    }

    private void loadAdminData() {
        currentAdmin = new Admin();
        currentAdmin.setUsername("Admin");
        currentAdmin.setEmail("admin@example.com");
        usernameLabel.setText(currentAdmin.getUsername());
    }

    private void loadBooks() {
        List<Buku> books = new ArrayList<>();
        try {
            books = bukuService.getAllBuku();
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Data Load Error", "Gagal memuat data buku: " + e.getMessage());
        }
        displayBooks(books);
    }

    private void displayBooks(List<Buku> books) {
        if (bookListContainer == null) {
            return;
        }
        bookListContainer.getChildren().clear();

        if (books != null && !books.isEmpty()) {
            HBox header = createBookListItemHeader();
            bookListContainer.getChildren().add(header);

            for (Buku buku : books) {
                HBox bookItem = createBookListItem(buku);
                bookListContainer.getChildren().add(bookItem);
            }
        } else {
            Label emptyLabel = new Label("Tidak ada buku ditemukan.");
            emptyLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 16;");
            bookListContainer.setAlignment(Pos.CENTER);
            bookListContainer.getChildren().add(emptyLabel);
        }
    }

    private HBox createBookListItemHeader() {
        HBox header = new HBox(10);
        header.setPadding(new Insets(10, 15, 10, 15));
        header.setStyle(
                "-fx-background-color: #4fd1c5; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 0);");
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleHeader = new Label("Judul");
        titleHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px;");
        titleHeader.setMinWidth(200);
        titleHeader.setPrefWidth(200);

        Label authorHeader = new Label("Penulis");
        authorHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px;");
        authorHeader.setMinWidth(150);
        authorHeader.setPrefWidth(150);

        Label isbnHeader = new Label("ISBN");
        isbnHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px;");
        isbnHeader.setMinWidth(120);
        isbnHeader.setPrefWidth(120);

        Label stockHeader = new Label("Stok");
        stockHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px;");
        stockHeader.setMinWidth(60);
        stockHeader.setPrefWidth(60);

        Label actionHeader = new Label("Aksi");
        actionHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px;");
        actionHeader.setMinWidth(150);
        actionHeader.setPrefWidth(150);

        header.getChildren().addAll(titleHeader, authorHeader, isbnHeader, stockHeader, actionHeader);
        return header;
    }

    private HBox createBookListItem(Buku buku) {
        HBox item = new HBox(10);
        item.setPadding(new Insets(5, 10, 5, 10));
        item.setStyle("-fx-border-color: #dcdcdc; -fx-border-radius: 3; -fx-background-color: #f4f4f4;");
        item.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(buku.get_judul_buku());
        titleLabel.setPrefWidth(300.0);
        titleLabel.setWrapText(true);

        Label isbnLabel = new Label(buku.get_isbn());
        isbnLabel.setPrefWidth(150.0);

        Label authorLabel = new Label(buku.get_penulis());
        authorLabel.setPrefWidth(150.0);

        Label stockLabel = new Label(String.valueOf(buku.get_jumlah()));
        stockLabel.setPrefWidth(50.0);

        // Action Buttons
        Button updateButton = new Button("Update");
        updateButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-background-radius: 3;");
        updateButton.setOnAction(event -> {
            System.out.println("Update button for book " + buku.get_judul_buku() + " clicked in createBookListItem.");
            handleUpdateBook(buku);
        }); // Pass the buku object

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 3;");
        deleteButton.setOnAction(event -> handleDeleteBook(buku)); // Pass the buku object

        HBox actionButtons = new HBox(5, updateButton, deleteButton);
        actionButtons.setPrefWidth(150.0); // Match header width

        item.getChildren().addAll(titleLabel, isbnLabel, authorLabel, stockLabel, actionButtons);

        return item;
    }

    private void handleEditBook(Buku buku) {
        try {
            App.setCurrentBook(buku);
            App.setRoot("EditBook");
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to open edit book form: " + e.getMessage());
        }
    }

    private void handleDeleteBook(Buku buku) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Book");
        alert.setContentText("Are you sure you want to delete " + buku.get_judul_buku() + "?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                bukuService.hapusBuku(buku.get_isbn());
                loadBooks();
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error", "Failed to delete book: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            App.setRoot("LandingPage");
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to logout: " + e.getMessage());
        }
    }

    @FXML
    private void handleUsers(ActionEvent event) {
        try {
            App.setRoot("AdminUserManagement");
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to open user management: " + e.getMessage());
        }
    }

    @FXML
    private void handleTransactions(ActionEvent event) {
        try {
            App.setRoot("AdminTransactions");
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to open transactions: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();
        List<Buku> filteredBooks = new ArrayList<>();
        try {
            List<Buku> allBooks = bukuService.getAllBuku();
            for (Buku buku : allBooks) {
                if (buku.get_judul_buku().toLowerCase().contains(searchText) ||
                        buku.get_penulis().toLowerCase().contains(searchText) ||
                        buku.get_isbn().toLowerCase().contains(searchText)) {
                    filteredBooks.add(buku);
                }
            }
            displayBooks(filteredBooks);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Failed to search books: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddBook(ActionEvent event) {
        try {
            App.setRoot("AddBook");
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to open add book form: " + e.getMessage());
        }
    }

    @FXML
    private void handleLoginGuest(ActionEvent event) {
        try {
            App.setCurrentUser("Guest");
            App.setCurrentUserRole("Guest");
            App.setRoot("BookList");
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Navigation Error", "Failed to load book list page.");
        }
    }

    @FXML
    private void handleFilter(ActionEvent event) {
        filterPanel.setVisible(!filterPanel.isVisible());
    }

    @FXML
    private void handleApplyFilter(ActionEvent event) {
        String year = yearFilter.getText();
        String genre = genreFilter.getValue();
        String category = categoryFilter.getValue();

        List<Buku> filteredBooks = new ArrayList<>();
        try {
            List<Buku> allBooks = bukuService.getAllBuku();
            for (Buku buku : allBooks) {
                boolean matches = true;
                if (!year.isEmpty() && !String.valueOf(buku.get_tahun_terbit()).equals(year)) {
                    matches = false;
                }
                if (genre != null && !genre.equals(buku.get_genre())) {
                    matches = false;
                }
                if (category != null && !category.equals(buku.get_kategori())) {
                    matches = false;
                }
                if (matches) {
                    filteredBooks.add(buku);
                }
            }
            displayBooks(filteredBooks);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Failed to apply filters: " + e.getMessage());
        }
    }

    @FXML
    private void handleClearFilter(ActionEvent event) {
        yearFilter.clear();
        genreFilter.getSelectionModel().clearSelection();
        categoryFilter.getSelectionModel().clearSelection();
        try {
            loadBooks();
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Failed to clear filters: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            App.setRoot("LandingPage");
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Navigation Error", "Failed to go back to landing page.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Handler for Update button
    private void handleUpdateBook(Buku buku) {
        System.out.println("Entering handleUpdateBook for book: " + buku.get_judul_buku());
        showUpdateBookDialog(buku);
    }

    // Method to show update book dialog (programmatically)
    private void showUpdateBookDialog(Buku buku) {
        System.out.println("Attempting to show update book dialog for: " + buku.get_judul_buku());
        try {
            Platform.runLater(() -> {
                // Create the custom dialog.
                Dialog<Buku> dialog = new Dialog<>();
                dialog.setTitle("Update Buku");
                dialog.setHeaderText("Update Informasi Buku");

                // Set the owner of the dialog to the current stage
                Stage stage = (Stage) welcomeLabel.getScene().getWindow();
                dialog.initOwner(stage);

                // Set the button types.
                ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

                // Set preferred size for the dialog pane
                dialog.getDialogPane().setPrefSize(600, 500);

                // --- ORIGINAL CONTENT: Reverting to full form --- START
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField titleField = new TextField();
                titleField.setPromptText("Judul Buku");
                titleField.setText(buku.get_judul_buku()); // Set current value

                TextField isbnField = new TextField();
                isbnField.setPromptText("ISBN");
                isbnField.setText(buku.get_isbn()); // Set current value
                isbnField.setDisable(true); // ISBN usually shouldn't be changed

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
                // --- ORIGINAL CONTENT --- END

                // Convert the result to a Buku object when the update button is clicked.
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == updateButtonType) {
                        try {
                            // Create a new Buku object with updated values
                            Buku updatedBuku = new Buku(
                                    buku.get_isbn(), // Use original ISBN
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

                // Show the dialog and process the result.
                dialog.showAndWait().ifPresent(updatedBuku -> {
                    if (updatedBuku != null) {
                        System.out.println("Attempting to update book: " + updatedBuku.get_judul_buku());
                        if (bukuService != null) {
                            try {
                                boolean success = bukuService.updateBuku(updatedBuku);
                                if (success) {
                                    showAlert(AlertType.INFORMATION, "Success", "Buku berhasil diperbarui.");
                                    loadBooks(); // Refresh the list
                                } else {
                                    showAlert(AlertType.ERROR, "Error", "Gagal memperbarui buku.");
                                }
                            } catch (IllegalArgumentException e) {
                                showAlert(AlertType.ERROR, "Error Validasi",
                                        "Gagal memperbarui buku: " + e.getMessage());
                            } catch (Exception e) {
                                showAlert(AlertType.ERROR, "Error",
                                        "Terjadi kesalahan saat memperbarui buku: " + e.getMessage());
                                e.printStackTrace();
                            }
                        } else {
                            showAlert(AlertType.ERROR, "Error",
                                    "Koneksi database tidak tersedia. Gagal memperbarui buku.");
                        }
                    }
                });
                System.out.println("After dialog.showAndWait() for: " + buku.get_judul_buku());

            });
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error Menampilkan Dialog",
                    "Terjadi kesalahan saat mencoba menampilkan dialog edit: " + e.getMessage());
            e.printStackTrace();
        }
    }
}