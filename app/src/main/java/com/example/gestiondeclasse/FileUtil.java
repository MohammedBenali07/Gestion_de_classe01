package com.example.gestiondeclasse;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtil {

    // Méthode pour obtenir le chemin absolu à partir d'un URI
    public static String getPath(Context context, Uri uri) {
        String fileName = getFileName(context, uri); // Nom du fichier
        if (fileName != null) {
            File cacheDir = context.getCacheDir(); // Répertoire de cache
            File file = new File(cacheDir, fileName);
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                if (inputStream != null) {
                    try (FileOutputStream outputStream = new FileOutputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                        return file.getAbsolutePath();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Méthode pour obtenir le nom d'un fichier
    private static String getFileName(Context context, Uri uri) {
        String fileName = null;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        fileName = cursor.getString(index);
                    }
                }
            }
        }
        if (fileName == null) {
            fileName = new File(uri.getPath()).getName();
        }
        return fileName;
    }
}
