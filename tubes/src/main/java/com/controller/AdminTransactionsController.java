package com.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import com.model.Transaksi;
import com.model.Buku;
import com.model.Member;
import com.Service.TransaksiService;
import com.DAO.BukuDAO;
import com.utilities.MysqlUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Callback;
import com.DAO.MemberDAO;
import com.DAO.TransaksiDAO;
import com.tubes.App;

import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.io.IOException;
import java.util.ArrayList;

public class AdminTransactionsController {
    @FXML
    private VBox transactionsList;
    @FXML
    private VBox emptyState;
    @FXML
    private Label totalTransactionsLabel;

    // Report section FXML elements
    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private ComboBox<String> yearComboBox;

    private TransaksiService transaksiService;
    private BukuDAO bukuDAO;
    private MemberDAO memberDAO;
    private Transaksi selectedTransaksi;

    @FXML
    public void initialize() {
        // Initialize DAOs and Service with connection
        Connection connection = MysqlUtilities.getConnection();
        bukuDAO = new BukuDAO(connection);
        memberDAO = new MemberDAO();
        transaksiService = new TransaksiService(new TransaksiDAO(connection), bukuDAO, memberDAO);

        // Initialize report year and month comboboxes (basic example)
        for (int i = 2020; i <= LocalDateTime.now().getYear(); i++) {
            yearComboBox.getItems().add(String.valueOf(i));
        }
        monthComboBox.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");

        // Load initial data
        loadTransactions();
    }

    @FXML
    private void handleShowAll(ActionEvent e) {
        // Implementation needed
    }

    @FXML
    private void handleShowPending(ActionEvent e) {
        // Implementation needed
    }

    @FXML
    private void handleShowOverdue(ActionEvent e) {
        // Implementation needed
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            App.setRoot("AdminDashboard"); // Assuming AdminDashboard FXML is named "AdminDashboard.fxml"
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to go back to Admin Dashboard.");
        }
    }

    @FXML
    private void handleFilter(ActionEvent e) {
        // Implementation needed
    }

    @FXML
    private void handleDownloadReport(ActionEvent e) {
        String selectedMonth = monthComboBox.getValue();
        String selectedYear = yearComboBox.getValue();
        if (selectedMonth == null || selectedYear == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih bulan dan tahun untuk laporan.");
            return;
        }
        // Call service to get data for the report
        // TransaksiService.getLaporanBulanan(Integer.parseInt(selectedMonth),
        // Integer.parseInt(selectedYear));
        showAlert(Alert.AlertType.INFORMATION, "Info", "Laporan berhasil di download!");
    }

    private void loadTransactions() {
        if (transaksiService != null) {
            List<Transaksi> transactions = transaksiService.getSemuaTransaksi();
            System.out.println("DEBUG: Jumlah transaksi yang dimuat: " + transactions.size());

            transactionsList.getChildren().clear(); // Clear previous entries

            if (transactions.isEmpty()) {
                emptyState.setVisible(true);
                transactionsList.setVisible(false); // Hide the VBox if empty
            } else {
                emptyState.setVisible(false);
                transactionsList.setVisible(true); // Show the VBox if not empty

                for (Transaksi trx : transactions) {
                    transactionsList.getChildren().add(createTransactionRow(trx));
                }
            }

            totalTransactionsLabel.setText("Total: " + transactions.size() + " transaksi");

        } else {
            System.err.println("ERROR: TransaksiService is null");
            showAlert(Alert.AlertType.ERROR, "Data Load Error", "Transaction service not available.");
            emptyState.setVisible(true);
            transactionsList.setVisible(false);
        }
    }

    private HBox createTransactionRow(Transaksi trx) {
        HBox row = new HBox(8); // Sesuaikan spacing dengan header
        row.setPadding(new javafx.geometry.Insets(8, 10, 8, 10)); // Sesuaikan padding
        row.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-radius: 3;");
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Create labels for each column, match preferred widths from FXML header
        Label idLabel = new Label(trx.getIdTransaksi());
        idLabel.setPrefWidth(50.0); // Match header width

        Label memberLabel = new Label(trx.getMember() != null ? trx.getMember().getNamaLengkap() : "-");
        memberLabel.setPrefWidth(150.0); // Match header width
        memberLabel.setWrapText(true);

        Label bookLabel = new Label(trx.getBuku() != null ? trx.getBuku().get_judul_buku() : "-");
        bookLabel.setPrefWidth(200.0); // Match header width
        bookLabel.setWrapText(true);

        Label borrowDateLabel = new Label(
                trx.getTanggalPeminjaman() != null ? trx.getTanggalPeminjaman().toLocalDate().toString() : "-");
        borrowDateLabel.setPrefWidth(100.0); // Match header width

        Label dueDateLabel = new Label(
                trx.getTanggalPeminjaman() != null ? trx.getTanggalPeminjaman().plusDays(3).toLocalDate().toString()
                        : "-"); // Assuming 3 days loan
        dueDateLabel.setPrefWidth(100.0); // Match header width

        Label statusLabel = new Label(trx.getStatusTransaksi() != null ? trx.getStatusTransaksi().toString() : "-");
        statusLabel.setPrefWidth(180.0); // Increased width to prevent truncation

        HBox actionBox = new HBox(5);
        actionBox.setPrefWidth(200.0); // Further increased width to accommodate buttons and align header
        actionBox.setAlignment(javafx.geometry.Pos.CENTER); // Align content to center

        Button actionButton = new Button();
        Button rejectButton = new Button("Tolak");

        actionButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-background-radius: 3;");
        rejectButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 3;");

        if (trx.getStatusTransaksi() == Transaksi.Status.PENDING_PEMINJAMAN) {
            actionButton.setText("Setujui");
            actionButton.setOnAction(e -> handleApprove(trx));
            rejectButton.setOnAction(e -> handleReject(trx));
            actionBox.getChildren().addAll(actionButton, rejectButton);
        } else if (trx.getStatusTransaksi() == Transaksi.Status.PENDING_PENGEMBALIAN) {
            actionButton.setText("Setujui Pengembalian");
            actionButton.setOnAction(e -> handleApproveReturn(trx));
            rejectButton.setText("Tolak Pengembalian");
            rejectButton.setOnAction(e -> handleRejectReturn(trx));
            actionBox.getChildren().addAll(actionButton, rejectButton);
        }

        row.getChildren().addAll(idLabel, memberLabel, bookLabel, borrowDateLabel, dueDateLabel, statusLabel,
                actionBox);

        return row;
    }

    // Handler for Approve button (inside action column)
    private void handleApprove(Transaksi trx) {
        if (transaksiService != null) {
            boolean success = transaksiService.updateStatus(trx.getIdTransaksi(), Transaksi.Status.ACCEPTED.name());
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction approved.");
                loadTransactions(); // Refresh list
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to approve transaction.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Transaction service not available.");
        }
    }

    // Handler for Reject button (inside action column)
    private void handleReject(Transaksi trx) {
        if (transaksiService != null) {
            // TransaksiService already handles stock return on rejection
            boolean success = transaksiService.updateStatus(trx.getIdTransaksi(), Transaksi.Status.REJECTED.name());
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction rejected. Book stock restored.");
                loadTransactions(); // Refresh list
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to reject transaction.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Transaction service not available.");
        }
    }

    // Handler for Approve Return button
    private void handleApproveReturn(Transaksi trx) {
        if (transaksiService != null) {
            // Update status to RETURNED and increase book stock
            trx.setStatusTransaksi(Transaksi.Status.RETURNED);
            trx.setTanggalPengembalian(LocalDateTime.now());
            boolean success = transaksiService.updateTransaksi(trx);

            if (success) {
                Buku buku = trx.getBuku();
                if (buku != null) {
                    buku.set_jumlah(buku.get_jumlah() + 1);
                    bukuDAO.update(buku);
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book return approved. Stock updated.");
                loadTransactions(); // Refresh list
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to approve return.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Transaction service not available.");
        }
    }

    // Handler for Reject Return button
    private void handleRejectReturn(Transaksi trx) {
        if (transaksiService != null) {
            // Change status back to REJECTED
            boolean success = transaksiService.updateStatus(trx.getIdTransaksi(), Transaksi.Status.REJECTED.name());
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book return rejected. Status set to rejected.");
                loadTransactions(); // Refresh list
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to reject return.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Transaction service not available.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}