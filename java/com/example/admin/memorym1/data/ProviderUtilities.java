package com.example.admin.memorym1.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.admin.memorym1.data.MemonimoContract.PatternEntry;
import com.example.admin.memorym1.model.BackgroundPattern;
import com.example.admin.memorym1.model.Game;
import com.example.admin.memorym1.model.GameCard;

import java.util.ArrayList;
import java.util.List;

public class ProviderUtilities {

    private static final String LOG_TAG = ProviderUtilities.class.getSimpleName();



    public static ContentValues convertGameModelToGameValues(Game _game) {

        ContentValues value = new ContentValues();
        value.put(MemonimoContract.GameEntry.COLUMN_FINISHED, _game.isFinished());
        value.put(MemonimoContract.GameEntry.COLUMN_FIRST_POSITION_CHOOSEN, _game.getFirstPositionChosen());
        value.put(MemonimoContract.GameEntry.COLUMN_SECOND_POSITION_CHOOSEN, _game.getSecondPositionChosen());
        value.put(MemonimoContract.GameEntry.COLUMN_NUM_ATTEMPT, _game.getNumAttempt());
        value.put(MemonimoContract.GameEntry.COLUMN_DIFFICULTY, _game.getMode().toString());
        value.put(MemonimoContract.GameEntry.COLUMN_PATTERN, _game.getBackgroundPattern());

        return value;
    }

    public static Game convertGameCursorToGameModel(Cursor _cursor) {

        Game game = new Game(
            _cursor.getLong(_cursor.getColumnIndex(MemonimoContract.GameEntry._ID)),
            DbUtilities.getBooleanValue(_cursor.getString(_cursor.getColumnIndex(MemonimoContract.GameEntry.COLUMN_FINISHED))),
            _cursor.getInt(_cursor.getColumnIndex(MemonimoContract.GameEntry.COLUMN_FIRST_POSITION_CHOOSEN)),
            _cursor.getInt(_cursor.getColumnIndex(MemonimoContract.GameEntry.COLUMN_SECOND_POSITION_CHOOSEN)),
            _cursor.getInt(_cursor.getColumnIndex(MemonimoContract.GameEntry.COLUMN_NUM_ATTEMPT)),
            _cursor.getString(_cursor.getColumnIndex(MemonimoContract.GameEntry.COLUMN_DIFFICULTY)),
            _cursor.getString(_cursor.getColumnIndex(MemonimoContract.GameEntry.COLUMN_PATTERN))
        );

        return game;
    }

    public static ContentValues[] convertGameModelToGameCardValues(Game _game) {

        List<ContentValues> values = new ArrayList<ContentValues>();

        int index = 0;

        for (GameCard cardGame : _game.getGameCardList()) {
            ContentValues value = new ContentValues();
            value.put(MemonimoContract.GameCardEntry.COLUMN_POSITION, index);
            value.put(MemonimoContract.GameCardEntry.COLUMN_ID_GAME, _game.getId());
            value.put(MemonimoContract.GameCardEntry.COLUMN_CODE_ANIMAL, cardGame.getAnimalGame().getCode());
            value.put(MemonimoContract.GameCardEntry.COLUMN_FOUND, cardGame.isCardFound());
            value.put(MemonimoContract.GameCardEntry.COLUMN_ATTEMPT, cardGame.isAttempt());

            values.add(value);

            index ++;
        }

        return values.toArray(new ContentValues[values.size()]);
    }

    public static ContentValues[] convertBackgroundPatternModelListToBackgroundPatternValues(List<BackgroundPattern> _backgroundPatternList) {

        List<ContentValues> values = new ArrayList<ContentValues>();

        int index = 0;

        for (BackgroundPattern backgroundPattern : _backgroundPatternList) {
            ContentValues value = new ContentValues();
//            value.put(MemonimoContract.GameCardEntry.COLUMN_POSITION, index);
            value.put(PatternEntry.COLUMN_IMG_ENCODED, backgroundPattern.getImgEncoded());

            values.add(value);
        }

        return values.toArray(new ContentValues[values.size()]);
    }

    public static BackgroundPattern convertBackgroundPatternCursorToBackgroundPatternModel(Cursor _cursor) {

        BackgroundPattern backgroundPattern = new BackgroundPattern(
                _cursor.getInt(_cursor.getColumnIndex(PatternEntry._ID)),
                _cursor.getString(_cursor.getColumnIndex(PatternEntry.COLUMN_IMG_ENCODED))
        );

        return backgroundPattern;
    }

    public static GameCard convertGameCardCursorToGameCardModel(Cursor _cursor) {

        GameCard gameCard = new GameCard(
                _cursor.getInt(_cursor.getColumnIndex(MemonimoContract.GameCardEntry.COLUMN_CODE_ANIMAL)),
                DbUtilities.getBooleanValue(_cursor.getString(_cursor.getColumnIndex(MemonimoContract.GameCardEntry.COLUMN_FOUND))),
                DbUtilities.getBooleanValue(_cursor.getString(_cursor.getColumnIndex(MemonimoContract.GameCardEntry.COLUMN_ATTEMPT))),
                false);

        return gameCard;
    }
}
