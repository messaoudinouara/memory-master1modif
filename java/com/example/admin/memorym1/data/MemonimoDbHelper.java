package com.example.admin.memorym1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.admin.memorym1.data.MemonimoContract.GameCardEntry;
import com.example.admin.memorym1.data.MemonimoContract.GameEntry;
import com.example.admin.memorym1.data.MemonimoContract.PatternEntry;

public class MemonimoDbHelper extends SQLiteOpenHelper {

    // Version de la base de données
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "memonimo.db";

    public MemonimoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_GAME_TABLE = "CREATE TABLE " + GameEntry.TABLE_NAME + " (" +
                GameEntry._ID + " INTEGER PRIMARY KEY," +
                GameEntry.COLUMN_FINISHED + " INTEGER NOT NULL, " +
                GameEntry.COLUMN_FIRST_POSITION_CHOOSEN + " INTEGER NOT NULL, " +
                GameEntry.COLUMN_SECOND_POSITION_CHOOSEN + " INTEGER NOT NULL, " +
                GameEntry.COLUMN_NUM_ATTEMPT + " INTEGER NOT NULL, " +
                GameEntry.COLUMN_DIFFICULTY + " INTEGER NOT NULL, " +
                GameEntry.COLUMN_PATTERN + " TEXT " +
                " );";

        final String SQL_CREATE_PATTERN_TABLE = "CREATE TABLE " + PatternEntry.TABLE_NAME + " (" +
                PatternEntry._ID + " INTEGER PRIMARY KEY," +
                PatternEntry.COLUMN_IMG_ENCODED + " TEXT NOT NULL, " +
                PatternEntry.COLUMN_ID_GAME + " INTEGER, " +
                // Clé étrangère vers la table "game"
                " FOREIGN KEY (" + PatternEntry.COLUMN_ID_GAME + ") REFERENCES " +
                GameEntry.TABLE_NAME + " (" + GameEntry._ID + ") " +
                " );";

        final String SQL_CREATE_GAME_CARD_TABLE = "CREATE TABLE " + GameCardEntry.TABLE_NAME + " (" +
                GameCardEntry._ID + " INTEGER PRIMARY KEY," +
                GameCardEntry.COLUMN_ID_GAME + " INTEGER NOT NULL, " +
                GameCardEntry.COLUMN_CODE_ANIMAL + " INTEGER NOT NULL, " +
                GameCardEntry.COLUMN_POSITION + " INTEGER NOT NULL, " +
                GameCardEntry.COLUMN_FOUND + " INTEGER NOT NULL, " +
                GameCardEntry.COLUMN_ATTEMPT + " INTEGER NOT NULL, " +
                // Clé étrangère vers la table "game"
                " FOREIGN KEY (" + GameCardEntry.COLUMN_ID_GAME + ") REFERENCES " +
                GameEntry.TABLE_NAME + " (" + GameEntry._ID + ") " +
                " );";

        // Création des tables
        sqLiteDatabase.execSQL(SQL_CREATE_GAME_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_GAME_CARD_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PATTERN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Suppression des tables si changement de version
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GameEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GameCardEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PatternEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
