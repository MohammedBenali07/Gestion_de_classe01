package com.example.gestiondeclasse;
public class Competence {
    private int id;
    private String name;
    private int progress;

    // Constructeur
    public Competence(int id, String name, int progress) {
        this.id = id;
        this.name = name;
        this.progress = progress;
    }

    // Getter et Setter pour id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter et Setter pour name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter et Setter pour progress
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
