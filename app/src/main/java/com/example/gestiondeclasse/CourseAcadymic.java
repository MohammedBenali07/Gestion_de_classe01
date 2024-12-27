package com.example.gestiondeclasse;

import java.util.ArrayList;

public class CourseAcadymic {
    private String courseName;
    private String professorName;
    private String driveLink;
    private ArrayList<String> skills;

    // Constructeur
    public CourseAcadymic(String courseName, String professorName, String driveLink, ArrayList<String> skills) {
        this.courseName = courseName;
        this.professorName = professorName;
        this.driveLink = driveLink;
        this.skills = skills;
    }

    // Getters
    public String getCourseName() {
        return courseName;
    }

    public String getProfessorName() {
        return professorName;
    }

    public String getDriveLink() {
        return driveLink;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }
}
