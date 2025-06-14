package com.model;

import java.sql.Timestamp;

public class Member {
    private String idMember;
    private String username;
    private String password;
    private String email;
    private String namaLengkap;
    private String noTelepon;
    private MemberStatus userStatus;
    private String alamat;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public enum MemberStatus {
        ACTIVE,
        FREEZE
    }

    public Member() {
    }

    public Member(String idMember, String username, String password, String email, String namaLengkap, String noTelepon,
            String alamat, MemberStatus userStatus) {
        this.idMember = idMember;
        this.username = username;
        this.password = password;
        this.email = email;
        this.namaLengkap = namaLengkap;
        this.noTelepon = noTelepon;
        this.alamat = alamat;
        this.userStatus = userStatus;
    }

    public String getIdMember() {
        return idMember;
    }

    public void setIdMember(String idMember) {
        this.idMember = idMember;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getUserStatus() {
        return userStatus.name();
    }

    public void setUserStatus(MemberStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}