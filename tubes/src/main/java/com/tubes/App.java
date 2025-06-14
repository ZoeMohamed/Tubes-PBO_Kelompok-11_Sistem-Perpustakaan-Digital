package com.tubes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.controller.AdminDashboardController;
import com.controller.BookDetailController;
import com.model.Admin;
import com.model.Buku;
import com.tubes.utilities.MysqlUtilities;
import com.tubes.utilities.DatabaseInitializer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;
    private static String currentUser = null;
    private static String currentUserRole = null; // "Guest", "Member", "Admin"
    private static Connection databaseConnection;
    private static Buku currentBook;

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize database
        DatabaseInitializer.initializeDatabase();

        // --- DEBUGGING START: Skip login and directly load Admin Dashboard ---
        // DO NOT USE IN PRODUCTION
        // setCurrentUser("admin@example.com"); // Set your debug admin username/email
        // setCurrentUserRole("Admin"); // Set role to Admin

        // FXMLLoader fxmlLoader = new
        // FXMLLoader(App.class.getResource("/com/tubes/AdminDashboard.fxml"));
        // Parent root = fxmlLoader.load();
        // AdminDashboardController adminController = fxmlLoader.getController();
        // // You might need to pass admin data if your controller requires it for
        // // initialization
        // // For example, if AdminDashboardController has a setAdmin(Admin admin)
        // method
        // // Admin debugAdmin = new Admin(); // Create a dummy Admin object
        // // debugAdmin.setUsername("admin");
        // // debugAdmin.setPassword("admin123"); // NOTE: Pass hashed password if
        // // controller expects it
        // // adminController.setAdmin(debugAdmin);

        // scene = new Scene(root);
        // stage.setTitle("Admin Dashboard (Debug)");
        // stage.setScene(scene);
        // stage.show();
        // --- DEBUGGING END ---

        // Original code (commented out for debugging):
        Parent root = FXMLLoader.load(getClass().getResource("/com/tubes/LandingPage.fxml"));
        scene = new Scene(root);
        stage.setTitle("Perpustakaan Digital");
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private static FXMLLoader getFXMLLoader(String fxml) {
        return new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static void setRootWithData(String fxml, Object data) throws IOException {
        FXMLLoader fxmlLoader = getFXMLLoader(fxml);
        Parent root = fxmlLoader.load();
        Object controller = fxmlLoader.getController();
        if (controller instanceof BookDetailController && data instanceof Buku) {
            ((BookDetailController) controller).setBookDetails((Buku) data);
            ((BookDetailController) controller).updateButtonVisibility(currentUserRole);
        }
        scene.setRoot(root);
    }

    public static void setCurrentUser(String user) {
        currentUser = user;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUserRole(String role) {
        currentUserRole = role;
    }

    public static String getCurrentUserRole() {
        return currentUserRole;
    }

    public static void logout() throws IOException {
        currentUser = null;
        currentUserRole = null;
        setRoot("LandingPage");
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Connection getDatabaseConnection() throws SQLException {
        return MysqlUtilities.getConnection();
    }

    public static void setCurrentBook(Buku book) {
        currentBook = book;
    }

    public static Buku getCurrentBook() {
        return currentBook;
    }
}