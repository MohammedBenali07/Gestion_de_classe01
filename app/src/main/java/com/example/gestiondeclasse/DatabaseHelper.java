package com.example.gestiondeclasse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    private static final String DATABASE_NAME = "studentDatabase";
    private static final int DATABASE_VERSION = 5; // Mise à jour de la version pour l'ajout de la colonne birthday

    // Tables et colonnes de la base de données
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SURNAME = "surname";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_CAREER = "career";
    private static final String COLUMN_PROFILE_IMAGE = "Profile_Image"; // Nouvelle colonne
    private static final String COLUMN_BIRTHDAY = "birthday"; // Colonne pour la date d'anniversaire

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Création de la table USERS avec les nouvelles colonnes
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_SURNAME + " TEXT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_PASSWORD + " TEXT," +
                COLUMN_STATUS + " TEXT," +
                COLUMN_CAREER + " TEXT," +
                COLUMN_PROFILE_IMAGE + " BLOB," +
                COLUMN_BIRTHDAY + " TEXT" + ")"; // Ajout de la colonne birthday
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN new_column_name TEXT");
        }
        if (oldVersion < 4) { // Migration pour ajouter Profile_Image
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_PROFILE_IMAGE + " BLOB");
        }
        if (oldVersion < 5) { // Migration pour ajouter birthday
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_BIRTHDAY + " TEXT");
        }
    }

    // Méthode pour vérifier si l'utilisateur existe déjà
    public boolean isUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_EMAIL + " = ?", new String[]{email},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Méthode pour ajouter un utilisateur avec une image de profil et une date d'anniversaire
    public long addUser(String name, String surname, String email, String password, String status, String career, String birthday,byte[] profileImage) {
        if (isUserExists(email)) {
            return -1; // Retourne -1 si l'utilisateur existe déjà
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_SURNAME, surname);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_CAREER, career);
        values.put(COLUMN_PROFILE_IMAGE, profileImage); // Ajout de l'image
        values.put(COLUMN_BIRTHDAY, birthday); // Ajout de la date d'anniversaire

        long result = -1;
        try {
            result = db.insert(TABLE_USERS, null, values);
        } catch (SQLException e) {
            Log.e("DatabaseError", "Erreur lors de l'ajout de l'utilisateur: " + e.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    // Méthode pour vérifier la validité d'un utilisateur par email et mot de passe
    public boolean isUserValid(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        boolean isValid = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return isValid;
    }

    // Méthode pour récupérer un utilisateur par son email, incluant l'image de profil et la date d'anniversaire
    public User getUserByEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        User user = null;

        try {
            cursor = db.query(TABLE_USERS, null, COLUMN_EMAIL + "=?",
                    new String[]{email}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SURNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CAREER)),
                        cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PROFILE_IMAGE)), // Récupère l'image
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTHDAY)) // Récupère la date d'anniversaire
                );
            }
        } catch (SQLException e) {
            Log.e("DatabaseError", "Erreur lors de la récupération de l'utilisateur: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }
}
