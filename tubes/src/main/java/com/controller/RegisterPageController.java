package com.controller;

import com.model.Member;
import com.Service.MemberService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.util.UUID;
import com.tubes.App;

public class RegisterPageController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private CheckBox termsCheckbox;
    @FXML
    private Button registerButton;
    @FXML
    private Button backButton;
    @FXML
    private Label messageLabel;

    private MemberService memberService = new MemberService();

    @FXML
    private void handleRegister() {
        // Basic client-side validation (can add more sophisticated validation)
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                emailField.getText().isEmpty() || firstNameField.getText().isEmpty() ||
                lastNameField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                addressField.getText().isEmpty()) {
            showAlert(AlertType.WARNING, "Form Error!", "Please fill in all fields.");
            return;
        }

        // Create a new Member object
        String newId = UUID.randomUUID().toString(); // Generate UUID for id_user
        Member newMember = new Member(
                newId, // Generated ID
                usernameField.getText(),
                passwordField.getText(), // Service will hash this
                emailField.getText(),
                firstNameField.getText() + " " + lastNameField.getText(),
                phoneField.getText(),
                addressField.getText(), // Add address
                Member.MemberStatus.ACTIVE // Default status
        );

        // Call the MemberService to register the member
        boolean success = memberService.registerMember(newMember);

        if (success) {
            showAlert(AlertType.INFORMATION, "Registration Successful", "Your account has been created.");
            // Optionally navigate to login page
            handleBack(); // Go back to landing page after successful registration
        } else {
            // Service layer should handle specific error logging (e.g., duplicate
            // user/email)
            showAlert(AlertType.ERROR, "Registration Failed",
                    "Could not register account. Username or email might be taken.");
        }
    }

    @FXML
    private void handleBack() {
        try {
            App.setRoot("LandingPage");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", "Could not load the landing page.");
        }
    }

    // Helper method to show alerts
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}