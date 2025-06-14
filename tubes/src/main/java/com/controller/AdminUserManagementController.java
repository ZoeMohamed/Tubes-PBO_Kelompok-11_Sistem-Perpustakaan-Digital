package com.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.beans.property.SimpleStringProperty;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javafx.stage.Stage;

import com.tubes.App;
import com.Service.MemberService;
import com.model.Member;

public class AdminUserManagementController {
    @FXML
    private TableView<Member> userTable;
    @FXML
    private TableColumn<Member, String> idColumn;
    @FXML
    private TableColumn<Member, String> usernameColumn;
    @FXML
    private TableColumn<Member, String> nameColumn;
    @FXML
    private TableColumn<Member, String> emailColumn;
    @FXML
    private TableColumn<Member, String> phoneColumn;
    @FXML
    private TableColumn<Member, String> statusColumn;
    @FXML
    private TableColumn<Member, String> actionsColumn;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> statusFilter;
    @FXML
    private Button addUserButton;

    private MemberService memberService;
    private ObservableList<Member> memberList;

    @FXML
    public void initialize() {
        memberService = new MemberService();
        memberList = FXCollections.observableArrayList();

        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idMember"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("namaLengkap"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("noTelepon"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("userStatus"));

        // Add action buttons to the actions column
        actionsColumn.setCellFactory(col -> new TableCell<Member, String>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttons = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Member member = getTableView().getItems().get(getIndex());
                    handleEditMember(member);
                });
                deleteButton.setOnAction(event -> {
                    Member member = getTableView().getItems().get(getIndex());
                    handleDeleteMember(member);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        // Initialize status filter
        statusFilter.getItems().addAll("All", "Active", "Frozen");
        statusFilter.setValue("All");
        statusFilter.setOnAction(e -> filterMembers());

        // Load initial data
        loadMembers();
    }

    private void loadMembers() {
        try {
            memberList.clear();
            String searchQuery = searchField.getText();
            String selectedStatus = statusFilter.getValue();
            List<Member> members = memberService.getFilteredAndSearchedMembers(searchQuery, selectedStatus);
            memberList.addAll(members);
            userTable.setItems(memberList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load members: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        loadMembers();
    }

    private void filterMembers() {
        loadMembers();
    }

    @FXML
    private void handleAddMember(ActionEvent event) {
        Dialog<Member> dialog = new Dialog<>();
        dialog.setTitle("Tambah Member Baru");
        dialog.setHeaderText("Isi detail member baru.");

        ButtonType addButtonType = new ButtonType("Tambah", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setStyle(
                "-fx-background-color: #f0f2f5; -fx-padding: 20px; -fx-border-radius: 10; -fx-background-radius: 10;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 20, 20, 20));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle(
                "-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8px;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle(
                "-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8px;");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle(
                "-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8px;");
        TextField namaLengkapField = new TextField();
        namaLengkapField.setPromptText("Nama Lengkap");
        namaLengkapField.setStyle(
                "-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8px;");
        TextField noTeleponField = new TextField();
        noTeleponField.setPromptText("Nomor Telepon");
        noTeleponField.setStyle(
                "-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8px;");
        TextArea alamatArea = new TextArea();
        alamatArea.setPromptText("Alamat");
        alamatArea.setStyle(
                "-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8px;");

        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");
        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");
        Label namaLengkapLabel = new Label("Nama Lengkap:");
        namaLengkapLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");
        Label noTeleponLabel = new Label("No. Telepon:");
        noTeleponLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");
        Label alamatLabel = new Label("Alamat:");
        alamatLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(emailLabel, 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(namaLengkapLabel, 0, 3);
        grid.add(namaLengkapField, 1, 3);
        grid.add(noTeleponLabel, 0, 4);
        grid.add(noTeleponField, 1, 4);
        grid.add(alamatLabel, 0, 5);
        grid.add(alamatArea, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Apply styling to buttons after they are added to the dialog pane
        dialog.getDialogPane().lookupButton(addButtonType).setStyle(
                "-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setStyle(
                "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String newId = UUID.randomUUID().toString(); // Generate UUID for id_user
                return new Member(
                        newId,
                        usernameField.getText(),
                        passwordField.getText(),
                        emailField.getText(),
                        namaLengkapField.getText(),
                        noTeleponField.getText(),
                        alamatArea.getText(), // Add alamat
                        Member.MemberStatus.ACTIVE // Default status for new member
                );
            }
            return null;
        });

        Optional<Member> result = dialog.showAndWait();
        result.ifPresent(newMember -> {
            try {
                // Pastikan password di-hash sebelum disimpan
                // Di sini saya asumsikan memberService akan menghash password
                boolean success = memberService.registerMember(newMember);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Sukses", "Member berhasil ditambahkan.");
                    loadMembers(); // Reload the table to show the new member
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error",
                            "Gagal menambahkan member. Pastikan semua kolom terisi dan unik.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menambahkan member: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void handleEditMember(Member member) {
        System.out.println("Edit button clicked for member: " + member.getUsername());
        showEditMemberDialog(member);
    }

    private void showEditMemberDialog(Member member) {
        Dialog<Member> dialog = new Dialog<>();
        dialog.setTitle("Edit Member");
        dialog.setHeaderText("Edit Informasi Member");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Get the current stage to set as owner
        Stage stage = (Stage) userTable.getScene().getWindow();
        dialog.initOwner(stage);

        TextField idMemberField = new TextField(member.getIdMember());
        idMemberField.setDisable(true); // ID should not be editable
        TextField usernameField = new TextField(member.getUsername());
        TextField emailField = new TextField(member.getEmail());
        TextField namaLengkapField = new TextField(member.getNamaLengkap());
        TextField noTeleponField = new TextField(member.getNoTelepon());
        TextArea alamatArea = new TextArea(member.getAlamat());
        alamatArea.setWrapText(true);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Biarkan kosong jika tidak ingin mengubah password");

        ComboBox<String> userStatusComboBox = new ComboBox<>();
        userStatusComboBox.getItems().addAll("Active", "Freeze");
        userStatusComboBox.setValue(member.getUserStatus());

        grid.add(new Label("ID Member:"), 0, 0);
        grid.add(idMemberField, 1, 0);
        grid.add(new Label("Username:"), 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(new Label("Password Baru:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(new Label("Nama Lengkap:"), 0, 4);
        grid.add(namaLengkapField, 1, 4);
        grid.add(new Label("No. Telepon:"), 0, 5);
        grid.add(noTeleponField, 1, 5);
        grid.add(new Label("Alamat:"), 0, 6);
        grid.add(alamatArea, 1, 6);
        grid.add(new Label("Status User:"), 0, 7);
        grid.add(userStatusComboBox, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                // Basic validation
                if (usernameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                        namaLengkapField.getText().isEmpty() || noTeleponField.getText().isEmpty() ||
                        alamatArea.getText().isEmpty() || userStatusComboBox.getValue() == null) {
                    showAlert(Alert.AlertType.ERROR, "Error Validasi",
                            "Semua field tidak boleh kosong (kecuali password).");
                    return null;
                }

                Member updatedMember = new Member(
                        member.getIdMember(), // Use original ID
                        usernameField.getText(),
                        // Use new password if provided, otherwise keep old (not ideal, should hash)
                        passwordField.getText().isEmpty() ? member.getPassword() : passwordField.getText(),
                        emailField.getText(),
                        namaLengkapField.getText(),
                        noTeleponField.getText(),
                        alamatArea.getText(),
                        Member.MemberStatus.valueOf(userStatusComboBox.getValue().toUpperCase()));
                return updatedMember;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedMember -> {
            if (updatedMember != null) {
                try {
                    boolean success = memberService.updateMemberProfile(updatedMember);
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Member berhasil diperbarui.");
                        loadMembers(); // Refresh the table
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Gagal memperbarui member.");
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error",
                            "Terjadi kesalahan saat memperbarui member: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleDeleteMember(Member member) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Member");
        alert.setContentText("Are you sure you want to delete member " + member.getUsername() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean success = memberService.deleteMember(String.valueOf(member.getIdMember()));
                if (success) {
                    memberList.remove(member);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Member deleted successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete member.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete member: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            App.setRoot("AdminDashboard");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to return to dashboard: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}