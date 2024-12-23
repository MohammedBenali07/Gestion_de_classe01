package com.example.gestiondeclasse;

import java.util.Arrays;

public class User {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String status;
    private String career;
    private byte[] profileImage; // Colonne pour l'image de profil
    private String birthday;     // Nouvelle colonne pour la date d'anniversaire

    // Constructeur avec tous les paramètres
    public User(int id, String name, String surname, String email, String password, String status, String career, byte[] profileImage, String birthday) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.status = status;
        this.career = career;
        this.profileImage = profileImage;
        this.birthday = birthday;
    }

    // Constructeur sans l'image de profil (optionnel)
    public User(int id, String name, String surname, String email, String password, String status, String career, String birthday) {
        this(id, name, surname, email, password, status, career, null, birthday);
    }

    // Constructeur minimaliste
    public User(int id, String name, String surname, String email, String password) {
        this(id, name, surname, email, password, null, null, null, null);
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", career='" + career + '\'' +
                ", profileImage=" + Arrays.toString(profileImage) +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
