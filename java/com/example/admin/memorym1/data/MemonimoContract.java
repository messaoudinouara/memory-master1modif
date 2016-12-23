package com.example.admin.memorym1.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

public class MemonimoContract {

    // "Content authority" permet de s'adresser au Content Provider
    public static final String CONTENT_AUTHORITY = "com.example.admin.memorym1.app";

    // URI du Content Provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Chemin d'accès aux données
    public static final String PATH_GAME = "game";
    public static final String PATH_PATTERN = "pattern";
    public static final String PATH_GAME_CARD = "game_card";

    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class GameEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GAME).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAME;

        // Nom de la table
        public static final String TABLE_NAME = "game";
        // Colonnes de la table
        public static final String COLUMN_FINISHED = "finished";
        public static final String COLUMN_FIRST_POSITION_CHOOSEN = "first_position_choosen";
        public static final String COLUMN_SECOND_POSITION_CHOOSEN = "second_position_choosen";
        public static final String COLUMN_NUM_ATTEMPT = "num_attempt";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_PATTERN = "pattern";

        public static Uri buildGameUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdGameFromUri(Uri uri) {
            return Long.valueOf(uri.getPathSegments().get(1)).longValue();
        }
    }

    public static final class PatternEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PATTERN).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PATTERN;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PATTERN;

        // Nom de la table
        public static final String TABLE_NAME = "pattern";
        // Colonnes de la table
        public static final String COLUMN_IMG_ENCODED = "img_encoded";
        public static final String COLUMN_ID_GAME = "id_game";

        public static Uri buildPatternUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class GameCardEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GAME_CARD).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAME_CARD;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAME_CARD;

        // Nom de la table
        public static final String TABLE_NAME = "gamecard";
        // Colonnes de la table
        public static final String COLUMN_POSITION = "position";
        public static final String COLUMN_ID_GAME = "id_game";
        public static final String COLUMN_CODE_ANIMAL = "animal";
        public static final String COLUMN_FOUND = "found";
        public static final String COLUMN_ATTEMPT = "attempt";

        public static Uri buildGameCardUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
