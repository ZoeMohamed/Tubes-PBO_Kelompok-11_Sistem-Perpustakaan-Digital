package com.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;

import com.tubes.App;
import com.model.Buku;
import com.model.Transaksi;
import com.Service.TransaksiService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoryController {

    @FXML
    private Button backButton;
    @FXML
    private VBox historyList;
    @FXML
    private VBox emptyState;

    private TransaksiService transaksiService;

    @FXML
    public void initialize() {
        try {
            transaksiService = new TransaksiService();
            displayHistoryFromDB();
        } catch (Exception e) {
            System.err.println("Failed to initialize TransaksiService: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Initialization Error", "Failed to initialize transaction service.");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            App.setRoot("BookList");
        } catch (Exception e) {
            System.err.println("Failed to load BookList: " + e.getMessage());
        }
    }

    private void displayHistoryFromDB() {
        historyList.getChildren().clear();
        emptyState.setVisible(false);

        String currentUserId = App.getCurrentUser();
        if (currentUserId == null) {
            emptyState.setVisible(true);
            return;
        }

        List<Transaksi> allTransaksi = transaksiService.getSemuaTransaksi();
        List<Transaksi> userTransaksi = new ArrayList<>();
        for (Transaksi trx : allTransaksi) {
            if (trx.getMember() != null && currentUserId.equals(String.valueOf(trx.getMember().getIdMember()))) {
                userTransaksi.add(trx);
            }
        }

        if (userTransaksi.isEmpty()) {
            emptyState.setVisible(true);
        } else {
            int index = 1;
            for (Transaksi trx : userTransaksi) {
                historyList.getChildren().add(createHistoryEntry(index++, trx));
            }
        }
    }

    private HBox createHistoryEntry(int index, Transaksi trx) {
        HBox entry = new HBox(10);
        entry.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));
        entry.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 3; -fx-background-color: #ecf0f1;");
        entry.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label noLabel = new Label(String.valueOf(index));
        noLabel.setPrefWidth(50.0);

        Label titleLabel = new Label(
                trx.getBuku() != null ? trx.getBuku().get_judul_buku() : "N/A");
        titleLabel.setPrefWidth(250.0);
        titleLabel.setWrapText(true);

        Label tglPinjamLabel = new Label(
                trx.getTanggalPeminjaman() != null ? trx.getTanggalPeminjaman().toLocalDate().toString() : "N/A");
        tglPinjamLabel.setPrefWidth(120.0);

        Label tglKembaliLabel = new Label(
                trx.getTanggalPengembalian() != null ? trx.getTanggalPengembalian().toLocalDate().toString()
                        : "N/A");
        tglKembaliLabel.setPrefWidth(120.0);

        Label statusLabel = new Label(
                trx.getStatusTransaksi() != null ? trx.getStatusTransaksi().toString() : "N/A");
        statusLabel.setPrefWidth(150.0);

        Button returnButton = new Button("Return");
        returnButton.setStyle(
                "-fx-background-color: #28a745; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        returnButton.setVisible(false); // Initially hidden
        returnButton.setOnAction(event -> handleReturnBook(trx));

        // Show button only if status is ACCEPTED and not yet pending pengembalian or
        // returned
        if (trx.getStatusTransaksi() == Transaksi.Status.ACCEPTED) {
            returnButton.setVisible(true);
        }

        entry.getChildren().addAll(noLabel, titleLabel, tglPinjamLabel, tglKembaliLabel, statusLabel, returnButton);

        return entry;
    }

    private void handleReturnBook(Transaksi trx) {
        try {
            // Update transaction status to PENDING_PENGEMBALIAN
            trx.setStatusTransaksi(Transaksi.Status.PENDING_PENGEMBALIAN);
            transaksiService.updateTransaksi(trx);

            showAlert(AlertType.INFORMATION, "Success", "Book return request sent for admin approval!");
            displayHistoryFromDB(); // Refresh the list
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Failed to return book: " + e.getMessage());
            e.printStackTrace();
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