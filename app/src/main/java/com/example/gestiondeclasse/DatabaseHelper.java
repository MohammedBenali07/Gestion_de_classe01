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
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_SKILLS = "skills";

    // Colonnes des utilisateurs
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SURNAME = "surname";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_CAREER = "career";
    private static final String COLUMN_PROFILE_IMAGE = "Profile_Image";
    private static final String COLUMN_BIRTHDAY = "birthday";
    private static final String COLUMN_ADDRESS = "address";
    // Colonnes des compétences
    private static final String COLUMN_SKILL_ID = "skill_id";
    private static final String COLUMN_SKILL_NAME = "skill_name";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_PROGRESS = "progress";

    // Méthode Singleton pour une meilleure gestion
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SURNAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_CAREER + " TEXT, " +
                COLUMN_PROFILE_IMAGE + " BLOB, " +
                COLUMN_BIRTHDAY + " TEXT,"+
                COLUMN_ADDRESS + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Créer la table des compétences
        String CREATE_SKILLS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SKILLS + " (" +
                COLUMN_SKILL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SKILL_NAME + " TEXT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_PROGRESS + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(CREATE_SKILLS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_ADDRESS + " TEXT");
        }
    }

    // Vérifier si un utilisateur existe
    public boolean isUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USERS, null, COLUMN_EMAIL + " = ?", new String[]{email}, null, null, null);
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    // Ajouter un utilisateur
    public long addUser(String name, String surname, String email, String password, String status, String career, String birthday, byte[] profileImage,String address) {
        if (isUserExists(email)) return -1;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_SURNAME, surname);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_CAREER, career);
        values.put(COLUMN_PROFILE_IMAGE, profileImage);
        values.put(COLUMN_BIRTHDAY, birthday);
        values.put(COLUMN_ADDRESS, address);

        try {
            return db.insert(TABLE_USERS, null, values);
        } catch (SQLException e) {
            Log.e("DatabaseError", "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            return -1;
        }
    }

    // Ajouter une compétence
    public long addSkill(int userId, String skillName, int progress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SKILL_NAME, skillName);
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_PROGRESS, progress);

        try {
            return db.insert(TABLE_SKILLS, null, values);
        } catch (SQLException e) {
            Log.e("DatabaseError", "Erreur lors de l'ajout de la compétence : " + e.getMessage());
            return -1;
        }
    }

    // Récupérer les compétences et leur progression pour un utilisateur


    // Mettre à jour la progression d'une compétence
    public int updateSkillProgress(int skillId, int newProgress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROGRESS, newProgress);

        try {
            return db.update(TABLE_SKILLS, values, COLUMN_SKILL_ID + " = ?", new String[]{String.valueOf(skillId)});
        } catch (SQLException e) {
            Log.e("DatabaseError", "Erreur lors de la mise à jour de la progression de la compétence : " + e.getMessage());
            return -1;
        }
    }

    public String getSkillNameColumn() {
        return COLUMN_SKILL_NAME;
    }
    public String getSkillIdColumn() {
        return COLUMN_SKILL_ID;
    }

    public String getProgressColumn() {
        return COLUMN_PROGRESS;
    }

    public boolean isUserValid(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
            cursor = db.rawQuery(query, new String[]{email, password});
            return cursor != null && cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USERS, null, COLUMN_EMAIL + "=?",
                    new String[]{email}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SURNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CAREER)),
                        cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PROFILE_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTHDAY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
                );
            }
        } catch (SQLException e) {
            Log.e("DatabaseError", "Erreur lors de la récupération de l'utilisateur: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
    public Cursor getSkillsByUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                "skills",
                null,
                "user_id = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );
    }
}
