package com.controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class GuestController {
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            com.tubes.App.setRoot("LandingPage");
        } catch (Exception e) {
            System.err.println("Gagal kembali ke halaman login: " + e.getMessage());
        }
    }
} 