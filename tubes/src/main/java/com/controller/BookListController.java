package com.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

import com.tubes.App;
import com.model.Buku;
import com.DAO.BukuDAO;
import com.Service.BukuService;

public class BookListController {

    @FXML
    private VBox sidebar;
    @FXML
    private Label profilePic;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button profileButton;
    @FXML
    private Button historyButton;
    @FXML
    private Button usersButton;
    @FXML
    private Button transactionsButton;
    @FXML
    private Button loginGuestButton;
    @FXML
    private Button logoutButton;
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
    private javafx.scene.layout.FlowPane bookGrid;
    @FXML
    private Button addBookButton;
    @FXML
    private Label dashboardTitleLabel;

    private BukuService bukuService;

    @FXML
    public void initialize() {
        // Initialize genre and category filters
        genreFilter.getItems().addAll("Fiksi", "Non-Fiksi", "Fantasi", "Romance", "Misteri", "Sains");
        categoryFilter.getItems().addAll("Novel", "Komik", "Biografi", "Pendidikan", "Teknologi", "Sejarah");

        // Initialize BukuService
        try {
            // Get database connection from App
            Connection dbConnection = App.getDatabaseConnection();
            if (dbConnection != null) {
                bukuService = new BukuService(new BukuDAO(dbConnection));
            } else {
                System.err.println("Database connection not available. Cannot load books.");
                // Handle case where connection is not available (e.g., show error message)
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize BukuService: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            // Handle other potential errors during initialization
        }

        // Load initial books
        loadBooks();

        // Update UI based on user role
        updateUIForUserRole();
    }

    private void loadBooks() {
        // Use BukuService to get books
        List<Buku> books = new ArrayList<>();
        if (bukuService != null) {
            books = bukuService.getAllBuku();
        }
        // Check if the list is empty and handle the empty state visibility if you have
        // one in BookList.fxml
        // For now, just display whatever list is returned (empty or not)

        displayBooks(books);
    }

    private void updateUIForUserRole() {
        String currentUser = App.getCurrentUser();
        String currentUserRole = App.getCurrentUserRole();

        // Update dashboard title based on role
        if ("Admin".equals(currentUserRole)) {
            dashboardTitleLabel.setText("Dashboard Admin");
        } else if ("Member".equals(currentUserRole)) {
            dashboardTitleLabel.setText("Dashboard Member");
        } else if ("Guest".equals(currentUserRole)) { // Guest or no user
            dashboardTitleLabel.setText("Katalog Buku"); // Or suitable title for guests
        }

        if (currentUser != null) {
            usernameLabel.setText(currentUser);
            sidebar.setVisible(true);
            loginGuestButton.setVisible(false);
            logoutButton.setVisible(false);

            if ("Admin".equals(currentUserRole)) {
                usersButton.setVisible(false);
                transactionsButton.setVisible(true);
                addBookButton.setVisible(true);
                profileButton.setVisible(false);
                historyButton.setVisible(false);
            } else if ("Member".equals(currentUserRole)) {
                usersButton.setVisible(false);
                transactionsButton.setVisible(false);
                addBookButton.setVisible(false);
                profileButton.setVisible(true);
                historyButton.setVisible(true);
            } else if ("Guest".equals(currentUserRole)) {
                // No user logged in (Guest view)
                usernameLabel.setText("Tamu"); // Or empty
                sidebar.setVisible(true);
                loginGuestButton.setVisible(true); // Show Login button for guests
                logoutButton.setVisible(false);
                usersButton.setVisible(false);
                transactionsButton.setVisible(false);
                addBookButton.setVisible(false);
                profileButton.setVisible(false);
                historyButton.setVisible(false);
                // Ensure the back button is visible in guest view for navigation back to
                // LandingPage
                // It is already handled in FXML to be in the top HBox, make sure its action is
                // handleBack
            }
        }
        // Adjust padding for the main content VBox if needed to prevent cutoff
        // (already added padding in FXML, check if more is needed or if other elements
        // push it)
    }

    private VBox createBookItem(Buku buku) {
        VBox item = new VBox(8); // Reduced spacing for more compact card
        item.setAlignment(Pos.TOP_LEFT); // Align content to top-left for vertical card
        item.setPrefWidth(220); // Fixed width for consistent card size
        item.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 3); -fx-cursor: hand;");

        Label titleLabel = new Label(buku.get_judul_buku());
        titleLabel.setFont(Font.font("System Bold", 16));
        titleLabel.setWrapText(true);
        titleLabel.setTextAlignment(TextAlignment.LEFT);
        titleLabel.setPrefWidth(200); // Ensure title fits within card width

        Label authorLabel = new Label("Penulis: " + buku.get_penulis());
        authorLabel.setFont(Font.font("System Italic", 12));
        authorLabel.setTextFill(javafx.scene.paint.Color.web("#555555"));
        authorLabel.setWrapText(true);

        Label yearLabel = new Label("Tahun Terbit: " + buku.get_tahun_terbit());
        yearLabel.setFont(Font.font(12));
        yearLabel.setTextFill(javafx.scene.paint.Color.web("#7f8c8d"));

        Label genreLabel = new Label("Genre: " + buku.get_genre());
        genreLabel.setFont(Font.font(12));
        genreLabel.setTextFill(javafx.scene.paint.Color.web("#7f8c8d"));

        Label categoryLabel = new Label("Kategori: " + buku.get_kategori());
        categoryLabel.setFont(Font.font(12));
        categoryLabel.setTextFill(javafx.scene.paint.Color.web("#7f8c8d"));

        Label publisherLabel = new Label("Penerbit: " + buku.get_penerbit());
        publisherLabel.setFont(Font.font(12));
        publisherLabel.setTextFill(javafx.scene.paint.Color.web("#7f8c8d"));

        TextArea descriptionArea = new TextArea(buku.get_deskripsi());
        descriptionArea.setEditable(false);
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefHeight(80); // Fixed height for description
        descriptionArea
                .setStyle("-fx-control-inner-background: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5;");

        Label stockLabel = new Label("Stok: " + buku.get_jumlah());
        stockLabel.setFont(Font.font("System Bold", 14));
        stockLabel.setTextFill(buku.get_jumlah() > 0 ? javafx.scene.paint.Color.web("#27ae60")
                : javafx.scene.paint.Color.web("#e74c3c"));

        item.getChildren().addAll(titleLabel, authorLabel, yearLabel, genreLabel, categoryLabel, publisherLabel,
                descriptionArea, stockLabel);

        // Add click handler ONLY if the user is not an Admin
        String currentUserRole = App.getCurrentUserRole();
        if (!"Admin".equals(currentUserRole)) {
            item.setOnMouseClicked(event -> {
                try {
                    // Pass the Buku object to the BookDetailController
                    App.setRootWithData("BookDetail", buku);
                } catch (IOException e) {
                    System.err.println("Failed to load BookDetail: " + e.getMessage());
                    // Optionally show an error dialog
                }
            });
        }

        return item;
    }

    // Method to display books in the GridPane
    private void displayBooks(List<Buku> books) {
        bookGrid.getChildren().clear(); // Clear existing book items
        for (Buku buku : books) {
            bookGrid.getChildren().add(createBookItem(buku));
        }
    }

    @FXML
    private void handleProfile(ActionEvent event) {
        try {
            com.tubes.App.setRoot("Profile");
        } catch (Exception e) {
            System.err.println("Gagal membuka halaman Profile: " + e.getMessage());
        }
    }

    @FXML
    private void handleHistory(ActionEvent event) {
        try {
            App.setRoot("History");
        } catch (Exception e) {
            System.err.println("Failed to load History page: " + e.getMessage());
        }
    }

    @FXML
    private void handleUsers(ActionEvent event) {
        // TODO: Implement navigation to Manage Users view (Admin only)
        System.out.println("Manage Users button clicked");
    }

    @FXML
    private void handleTransactions(ActionEvent event) {
        // TODO: Implement navigation to Transactions view (Admin only)
        System.out.println("Transactions button clicked");
    }

    @FXML
    private void handleLoginGuest(ActionEvent event) {
        // This button should only be visible for guests, clicking it should probably go
        // to LandingPage
        try {
            App.setRoot("LandingPage");
        } catch (Exception e) {
            System.err.println("Failed to load LandingPage: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            App.logout(); // Use the logout method in App.java
        } catch (Exception e) {
            System.err.println("Failed to logout: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchText = searchField.getText();
        // Implement book search for member/guest
        List<Buku> searchResults = new ArrayList<>();
        if (bukuService != null) {
            try {
                searchResults = bukuService.cariBuku(searchText);
                displayBooks(searchResults);
            } catch (Exception e) {
                System.err.println("Gagal mencari buku: " + e.getMessage());
                // Optionally show an error dialog
            }
        } else {
            System.err.println("Koneksi database tidak tersedia. Gagal mencari buku.");
            // Optionally show an error dialog
        }
    }

    @FXML
    private void handleFilter(ActionEvent event) {
        // Toggle filter panel visibility
        filterPanel.setVisible(!filterPanel.isVisible());
        // Filter options for member/guest are already implemented in handleApplyFilter
    }

    @FXML
    private void handleApplyFilter(ActionEvent event) {
        // Implement filter logic for member/guest
        String genre = genreFilter.getValue();
        String kategori = categoryFilter.getValue();
        Integer tahun = null;
        try {
            if (!yearFilter.getText().isEmpty()) {
                tahun = Integer.parseInt(yearFilter.getText());
            }
        } catch (NumberFormatException e) {
            System.err.println("Tahun terbit harus berupa angka.");
            // Optionally show an error dialog
            return;
        }

        List<Buku> filterResults = new ArrayList<>();
        if (bukuService != null) {
            try {
                filterResults = bukuService.filterBuku(genre, kategori, tahun);
                displayBooks(filterResults);
            } catch (Exception e) {
                System.err.println("Gagal memfilter buku: " + e.getMessage());
                // Optionally show an error dialog
            }
        } else {
            System.err.println("Koneksi database tidak tersedia. Gagal memfilter buku.");
            // Optionally show an error dialog
        }
    }

    @FXML
    private void handleClearFilter(ActionEvent event) {
        // Implement clear filter logic for member/guest
        searchField.clear();
        yearFilter.clear();
        genreFilter.getSelectionModel().clearSelection();
        categoryFilter.getSelectionModel().clearSelection();
        loadBooks(); // Reload all books
    }

    @FXML
    private void handleAddBook(ActionEvent event) {
        // This button should be hidden for members/guests
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            App.setRoot("LandingPage");
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally show an error dialog
        }
    }
}