package com.example.gestiondeclasse;

import android.net.Uri;
import java.util.List;

public class Competence {
    private String name;
    private int progress;

    // Constructeur principal
    public Competence(String name, int progress) {
        this.name = name;
        this.progress = progress;
    }

    // Constructeur secondaire (progress par défaut à 0)
    public Competence(String name) {
        this(name, 0); // progression par défaut à 0
    }

    // Getters
    public String getName() {
        return name;
    }



    public int getProgress() {
        return progress;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }



    public void setProgress(int progress) {
        // Assurez-vous que la progression reste dans la plage [0, 100]
        if (progress < 0) {
            this.progress = 0;
        } else if (progress > 100) {
            this.progress = 100;
        } else {
            this.progress = progress;
        }
    }

    @Override
    public String toString() {
        return "Competence{" +
                "name='" + name + '\'' +
                ", progress=" + progress +
                '}';
    }
}
