package com.example.gestiondeclasse;

public class Session {
    private String nomPrenom;
    private String matiere;
    private String date;
    private String type;

    // Constructor
    public Session(String nomPrenom, String matiere, String date, String type) {
        this.nomPrenom = nomPrenom;
        this.matiere = matiere;
        this.date = date;
        this.type = type;
    }

    // Getters
    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getMatiere() {
        return matiere;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
}
