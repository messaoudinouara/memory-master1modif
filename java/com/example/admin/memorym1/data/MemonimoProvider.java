package com.example.admin.memorym1.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.admin.memorym1.data.MemonimoContract.GameCardEntry;
import com.example.admin.memorym1.data.MemonimoContract.GameEntry;
import com.example.admin.memorym1.data.MemonimoContract.PatternEntry;
import com.example.admin.memorym1.model.BackgroundPattern;
import com.example.admin.memorym1.model.Game;

import java.util.ArrayList;
import java.util.List;

public class MemonimoProvider extends ContentProvider {

    private static final String LOG_TAG = MemonimoProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MemonimoDbHelper mMemonimoDbHelper;

    static final int CODE_GAME = 100;
    static final int CODE_GAME_ID = 101;
    static final int CODE_GAME_CARD = 200;
    static final int CODE_PATTERN = 300;


    @Override
    public boolean onCreate() {
        mMemonimoDbHelper = new MemonimoDbHelper(getContext());
        return true;
    }

    public static Game restoreGame(ContentResolver _contentResolver, long _idGame) {

        Log.d(LOG_TAG, ".restoreGame() : id -> " + _idGame);

        Game game;

        // Récupération de la partie
        Cursor cursor = _contentResolver.query(
                MemonimoContract.GameEntry.CONTENT_URI, // URI
                null, // Colonnes interogées
                MemonimoContract.GameEntry._ID + "=?", // Colonnes pour la condition WHERE
                new String[]{"" + _idGame}, // Valeurs pour la condition WHERE
                null // Tri
        );
        cursor.moveToNext();
        game = ProviderUtilities.convertGameCursorToGameModel(cursor);

        // Récupération des données des cartes via le Content Provider
        cursor = _contentResolver.query(
                MemonimoContract.GameCardEntry.CONTENT_URI, // URI
                null, // Colonnes interogées
                MemonimoContract.GameCardEntry.COLUMN_ID_GAME + "=?", // Colonnes pour la condition WHERE
                new String[]{"" + _idGame}, // Valeurs pour la condition WHERE
                null // Tri
        );
        while(cursor.moveToNext()) {
            game.addGameCard(ProviderUtilities.convertGameCardCursorToGameCardModel(cursor));
        }

        return game;
    }

    public static long saveGame(ContentResolver _contentResolver, Game _game) {

        Game game = null;

        try {
            game = (Game) _game.clone();

        } catch (CloneNotSupportedException e) {
            // TODO
        }

        ContentValues gameValue = ProviderUtilities.convertGameModelToGameValues(game);

        if (game.getId() == -1) {
            // Insertion d'une partie via le Provider
            Uri uri = _contentResolver.insert(
                    MemonimoContract.GameEntry.CONTENT_URI,
                    gameValue
            );
            // Récupération de l'identifiant généré
            game.setId(ContentUris.parseId(uri));
            Log.d(LOG_TAG, ".saveGame() : id (generated) -> " + game.getId());

        } else {

            // Insertion d'une partie via le Provider
            int numRowsUpdated = _contentResolver.update(
                    MemonimoContract.GameEntry.CONTENT_URI,
                    gameValue,
                    MemonimoContract.GameEntry._ID + "=?",
                    new String[] {Long.toString(game.getId())}
            );

            Log.d(LOG_TAG, ".saveGame() : id -> " + game.getId() + " updated");
        }

        // Conversion pour le Provider
        ContentValues[] gameCards = ProviderUtilities.convertGameModelToGameCardValues(game);

        // Suppressions massives via le Provider
        int numRowsDeleted = _contentResolver.delete(
                MemonimoContract.GameCardEntry.CONTENT_URI,
                MemonimoContract.GameCardEntry.COLUMN_ID_GAME + "=?",
                new String[] {Long.toString(game.getId())}
        );
        Log.d(LOG_TAG, numRowsDeleted + " game_card rows deleted into database");

        // Insertions massives via le Provider
        int numRowsInserted = _contentResolver.bulkInsert(
                MemonimoContract.GameCardEntry.CONTENT_URI,
                gameCards
        );
        Log.d(LOG_TAG, numRowsInserted + " game_cards rows inserted into database");

        return game.getId();
    }

    public static BackgroundPattern restoreBackgroundPatternByIdGame(ContentResolver _contentResolver, long _idGame) {

        Log.d(LOG_TAG, ".restoreBackgroundPatternByIdGame() : idGame -> " + _idGame);

        BackgroundPattern backgroundPattern = null;

        // Récupération de la partie
        Cursor cursor = _contentResolver.query(
                PatternEntry.CONTENT_URI, // URI
                null, // Colonnes interogées
                PatternEntry.COLUMN_ID_GAME + "=?", // Colonnes pour la condition WHERE
                new String[]{"" + _idGame}, // Valeurs pour la condition WHERE
                null // Tri
        );
        if (cursor.moveToNext()) {
            backgroundPattern = ProviderUtilities.convertBackgroundPatternCursorToBackgroundPatternModel(cursor);
        }

        return backgroundPattern;
    }

    public static List<BackgroundPattern> restoreAllPatternList(ContentResolver _contentResolver) {

        Log.d(LOG_TAG, ".restorePatternList()");

        List<BackgroundPattern> backgroundPatternList = new ArrayList<BackgroundPattern>();

        // Récupération de la partie
        Cursor cursor = _contentResolver.query(
                PatternEntry.CONTENT_URI, // URI
                null, // Colonnes interogées
                null, // Colonnes pour la condition WHERE
                null, // Valeurs pour la condition WHERE
                null // Tri
        );

        while (cursor.moveToNext()) {
            backgroundPatternList.add(ProviderUtilities.convertBackgroundPatternCursorToBackgroundPatternModel(cursor));
        }

        Log.v(LOG_TAG, ".restorePatternList() :: " + backgroundPatternList.size());

        return backgroundPatternList;
    }

    public static void savePatternList(ContentResolver _contentResolver, List<BackgroundPattern> _backgroundPatternList) {


        // Conversion pour le Provider
        ContentValues[] patternList = ProviderUtilities
                .convertBackgroundPatternModelListToBackgroundPatternValues(_backgroundPatternList);

//        // Suppressions massives via le Provider
//        int numRowsDeleted = _contentResolver.delete(
//                MemonimoContract.GameCardEntry.CONTENT_URI,
//                MemonimoContract.GameCardEntry.COLUMN_ID_GAME + "=?",
//                new String[] {Long.toString(game.getId())}
//        );
//        Log.d(LOG_TAG, numRowsDeleted + " game_card rows deleted into database");

        // Insertions massives via le Provider
        int numRowsInserted = _contentResolver.bulkInsert(
                MemonimoContract.PatternEntry.CONTENT_URI,
                patternList
        );
        Log.d(LOG_TAG, numRowsInserted + " patterns rows inserted into database");

    }

    public static void removeGame(ContentResolver _contentResolver, long _idGame) {

        Log.d(LOG_TAG, ".removeGame() : id -> " + _idGame);

        // Suppression des cartes
        int numCardsDeleted = _contentResolver.delete(
                MemonimoContract.GameCardEntry.CONTENT_URI, // URI
                MemonimoContract.GameCardEntry.COLUMN_ID_GAME + "=?", // Colonnes pour la condition WHERE
                new String[]{"" + _idGame} // Valeurs pour la condition WHERE
        );
        Log.d(LOG_TAG, numCardsDeleted + " rows deleted from game_card with id_card " + _idGame);

        // Suppression de la partie
        int numGamesDeleted = _contentResolver.delete(
                MemonimoContract.GameEntry.CONTENT_URI, // URI
                MemonimoContract.GameEntry._ID + "=?", // Colonnes pour la condition WHERE
                new String[]{"" + _idGame} // Valeurs pour la condition WHERE
        );
        Log.d(LOG_TAG, numGamesDeleted + " rows deleted from game with id " + _idGame);
    }

    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.

     */
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case CODE_GAME:
                return GameEntry.CONTENT_TYPE;
            case CODE_GAME_CARD:
                return GameCardEntry.CONTENT_TYPE;
            case CODE_PATTERN:
                return PatternEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /*
        Méthode effectuant toutes les opérations nécessaires pour mettre à jour la base
     */
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        // Récupération de la base SQLite
        final SQLiteDatabase db = mMemonimoDbHelper.getWritableDatabase();

        int returnCount = 0;
        //
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CODE_GAME:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
//                        normalizeDate(value);
                        long _id = db.insert(GameEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);

                return returnCount;

            case CODE_GAME_CARD:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
//                        normalizeDate(value);
                        long _id = db.insert(GameCardEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);

                return returnCount;

            case CODE_PATTERN:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(PatternEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);

                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }
    }



    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mMemonimoDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case CODE_GAME: {
//                normalizeDate(values);
                long _id = db.insert(GameEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = GameEntry.buildGameUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CODE_GAME_CARD: {
//                normalizeDate(values);
                long _id = db.insert(GameCardEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = GameCardEntry.buildGameCardUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CODE_PATTERN: {
                long _id = db.insert(PatternEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = PatternEntry.buildPatternUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        db.close();

        return returnUri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor cursor;


        // Récupération de la base SQLite
        final SQLiteDatabase db = mMemonimoDbHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_GAME: {
                // Requête de récupération de données via la table
                cursor = db.query(
                        GameEntry.TABLE_NAME, // Table
                        projection, // Colonnes interogées
                        selection, // Colonnes pour la condition WHERE
                        selectionArgs, // Valeurs pour la condition WHERE
                        null, // Colonnes pour le GROUP BY
                        null, // Colonnes pour le filtre
                        sortOrder // Tri
                );
                break;
            }
            case CODE_GAME_ID: {
                // Requête de récupération de données via la table
                cursor = getGameById(uri);
                break;
            }
            case CODE_GAME_CARD: {
                // Requête de récupération de données via la table
                cursor = db.query(
                        GameCardEntry.TABLE_NAME, // Table
                        projection, // Colonnes interogées
                        selection, // Colonnes pour la condition WHERE
                        selectionArgs, // Valeurs pour la condition WHERE
                        null, // Colonnes pour le GROUP BY
                        null, // Colonnes pour le filtre
                        sortOrder // Tri
                );
                break;
            }
            case CODE_PATTERN: {
                // Requête de récupération de données via la table
                cursor = db.query(
                        PatternEntry.TABLE_NAME, // Table
                        projection, // Colonnes interogées
                        selection, // Colonnes pour la condition WHERE
                        selectionArgs, // Valeurs pour la condition WHERE
                        null, // Colonnes pour le GROUP BY
                        null, // Colonnes pour le filtre
                        sortOrder // Tri
                );
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

//    private Cursor getGameById(long _idGame) {
//        return getGame(
//                null,
//                null,
//                MemonimoContract.GameEntry._ID + "=?",
//                new String[]{"" + _idGame},
//                null);
//
//
////        Cursor cursor = _contentResolver.query(
////                MemonimoContract.GameEntry.CONTENT_URI, // URI
////                null, // Colonnes interogées
////                MemonimoContract.GameEntry._ID + "=?", // Colonnes pour la condition WHERE
////                new String[]{"" + _idGame}, // Valeurs pour la condition WHERE
////                null // Tri
//    }

    private Cursor getGameById(Uri _uri) {

        long idGame = GameEntry.getIdGameFromUri(_uri);

        // Récupération de la base SQLite
        final SQLiteDatabase db = mMemonimoDbHelper.getReadableDatabase();
        // Requête de récupération de données via la table
        Cursor cursor = db.query(
                GameEntry.TABLE_NAME, // Table
                null, // Colonnes interogées
                MemonimoContract.GameEntry._ID + "=?", // Colonnes pour la condition WHERE
                new String[]{"" + idGame}, // Valeurs pour la condition WHERE
                null, // Colonnes pour le GROUP BY
                null, // Colonnes pour le filtre
                null // Tri
        );

        return cursor;

//        String locationSetting = WeatherContract.WeatherEntry.getLocationSettingFromUri(uri);
//        long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
//
//        return sWeatherByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
//                projection,
//                sLocationSettingAndDaySelection,
//                new String[]{locationSetting, Long.toString(date)},
//                null,
//                null,
//                sortOrder
//        );
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // Récupération de la base SQLite
        final SQLiteDatabase db = mMemonimoDbHelper.getReadableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case CODE_GAME:
//                normalizeDate(values);
                rowsUpdated = db.update(
                        GameEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CODE_GAME_CARD:
//                normalizeDate(values);
                rowsUpdated = db.update(
                        GameCardEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CODE_PATTERN:
                rowsUpdated = db.update(
                        PatternEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // Récupération de la base SQLite
        final SQLiteDatabase db = mMemonimoDbHelper.getReadableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        switch (match) {
            case CODE_GAME:
                rowsDeleted = db.delete(GameEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CODE_GAME_CARD:
                rowsDeleted = db.delete(GameCardEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CODE_PATTERN:
                rowsDeleted = db.delete(PatternEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MemonimoContract.CONTENT_AUTHORITY;

        // Liaison entre les URI et les codes d'identification
        matcher.addURI(authority, MemonimoContract.PATH_GAME, CODE_GAME);
        matcher.addURI(authority, MemonimoContract.PATH_GAME + "/#", CODE_GAME_ID);
        matcher.addURI(authority, MemonimoContract.PATH_GAME_CARD, CODE_GAME_CARD);
        matcher.addURI(authority, MemonimoContract.PATH_PATTERN, CODE_PATTERN);

        return matcher;
    }
}
