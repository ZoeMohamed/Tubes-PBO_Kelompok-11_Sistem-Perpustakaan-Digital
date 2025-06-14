package com.model;

import java.time.LocalDateTime;
import java.util.*;

public class User {
    protected String id_user; // Common ID for all users
    protected String username;
    protected String password;
    protected String nama_lengkap;
    protected String email;
    protected String no_telepon; // Corrected from no_telp
    protected String alamat;
    protected LocalDateTime created_at; // Add created_at field
    protected LocalDateTime updated_at; // Add updated_at field

    // Default constructor
    public User() {
    }

    // Constructor with common fields
    public User(String id_user, String username, String password, String nama_lengkap, String email, String no_telepon,
            String alamat) {
        this.id_user = id_user;
        this.username = username;
        this.password = password;
        this.nama_lengkap = nama_lengkap;
        this.email = email;
        this.no_telepon = no_telepon;
        this.alamat = alamat;
        this.created_at = LocalDateTime.now(); // Set created_at on creation
        this.updated_at = LocalDateTime.now(); // Set updated_at on creation
    }

    public User(String id_user, String username, String password, String nama_lengkap, String email, String no_telepon,
            String alamat, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id_user = id_user;
        this.username = username;
        this.password = password;
        this.nama_lengkap = nama_lengkap;
        this.email = email;
        this.no_telepon = no_telepon;
        this.alamat = alamat;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Getters (snake_case)
    public String get_id_user() {
        return id_user;
    }

    public String get_username() {
        return username;
    }

    public String get_password() {
        return password;
    }

    public String get_nama_lengkap() {
        return nama_lengkap;
    }

    public String get_email() {
        return email;
    }

    public String get_no_telepon() {
        return no_telepon;
    }

    public String get_alamat() {
        return alamat;
    }

    public LocalDateTime get_created_at() {
        return created_at;
    }

    public LocalDateTime get_updated_at() {
        return updated_at;
    }

    // Setters (snake_case)
    public void set_id_user(String id_user) {
        this.id_user = id_user;
    }

    public void set_username(String username) {
        this.username = username;
    }

    public void set_password(String password) {
        this.password = password;
    }

    public void set_nama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public void set_email(String email) {
        this.email = email;
    }

    public void set_no_telepon(String no_telepon) {
        this.no_telepon = no_telepon;
    }

    public void set_alamat(String alamat) {
        this.alamat = alamat;
    }

    public void set_created_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public void set_updated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    // Removed member-specific attributes (user_status, freeze_until)
    // Removed member-specific methods (hasOverdue, freeze_Acc, mengulas)
    // Removed generateUUID method (will handle ID generation in subclasses or
    // service)
    // Kept equals and hashCode as they are based on id_user

    @Override
    public int hashCode() {
        return Objects.hash(id_user);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        User user = (User) obj;
        return Objects.equals(id_user, user.id_user);
    }
}
