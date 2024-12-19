package com.example.gestiondeclasse;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nom du fichier de la base de données
    private static final String DATABASE_NAME = "gestion_classe.db";
    private static final int DATABASE_VERSION = 1;

    // Instance unique pour la classe DatabaseHelper
    private static DatabaseHelper instance;

    // Constructeur privé pour éviter l'instanciation directe
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Méthode pour obtenir l'instance unique de DatabaseHelper
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Créer les tables de la base de données
        db.execSQL("CREATE TABLE niveaux (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom TEXT NOT NULL" +
                ");");

        db.execSQL("CREATE TABLE modules (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom TEXT NOT NULL," +
                "semestre INTEGER NOT NULL CHECK(semestre IN (1, 2))," +
                "id_niveau INTEGER NOT NULL," +
                "link TEXT NOT NULL," +
                "FOREIGN KEY (id_niveau) REFERENCES niveaux(id)" +
                ");");

        db.execSQL("CREATE TABLE etudiants (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom TEXT NOT NULL," +
                "prenom TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "id_niveau INTEGER NOT NULL," +
                "FOREIGN KEY (id_niveau) REFERENCES niveaux(id)" +
                ");");

        db.execSQL("CREATE TABLE etudiant_competences (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_etudiant INTEGER NOT NULL," +
                "nom_competence TEXT NOT NULL," +
                "niveau_progression INTEGER NOT NULL CHECK(niveau_progression BETWEEN 1 AND 100)," +
                "FOREIGN KEY (id_etudiant) REFERENCES etudiants(id)" +
                ");");

        db.execSQL("CREATE TABLE formations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom TEXT NOT NULL," +
                "nom_competence TEXT NOT NULL," +
                "link TEXT NOT NULL," +
                "image TEXT" +
                ");");

        db.execSQL("CREATE TABLE absences (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_etudiant INTEGER NOT NULL," +
                "id_module INTEGER NOT NULL," +
                "date_absence DATE NOT NULL," +
                "FOREIGN KEY (id_etudiant) REFERENCES etudiants(id)," +
                "FOREIGN KEY (id_module) REFERENCES modules(id)" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Supprimer les anciennes tables si elles existent (optionnel)
        db.execSQL("DROP TABLE IF EXISTS absences");
        db.execSQL("DROP TABLE IF EXISTS formations");
        db.execSQL("DROP TABLE IF EXISTS etudiant_competences");
        db.execSQL("DROP TABLE IF EXISTS etudiants");
        db.execSQL("DROP TABLE IF EXISTS modules");
        db.execSQL("DROP TABLE IF EXISTS niveaux");

        // Recréer les tables
        onCreate(db);
    }

    // Méthode pour obtenir une instance de la base de données en mode en écriture
    public SQLiteDatabase getWritableDb() {
        return instance.getWritableDatabase();
    }

    // Méthode pour obtenir une instance de la base de données en mode en lecture
    public SQLiteDatabase getReadableDb() {
        return instance.getReadableDatabase();
    }

    public long addStudent(String name, String surname, String email, String status, String career) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insérer un nouvel étudiant dans la table `etudiants`
        ContentValues values = new ContentValues();
        values.put("nom", name);
        values.put("prenom", surname);
        values.put("email", email);
        values.put("id_niveau", getLevelIdByStatus(status));  // Exemple : vous pouvez utiliser status pour définir id_niveau
        values.put("career", career);

        long studentId = db.insert("etudiants", null, values);
        db.close();

        return studentId;  // Retourne l'id de l'étudiant ajouté
    }

    private int getLevelIdByStatus(String status) {
        // Logique pour obtenir l'ID du niveau selon le statut (exemple)
        switch (status) {
            case "Débutant":
                return 1;
            case "Intermédiaire":
                return 2;
            case "Avancé":
                return 3;
            default:
                return 1;  // Valeur par défaut
        }
    }

}
