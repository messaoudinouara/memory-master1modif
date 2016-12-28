package com.example.admin.memorym1.model;

import java.util.ArrayList;
import java.util.List;

public class Game implements Cloneable {


    private static final int NUM_FAMILY_MODE_EASY = 4;
   // private static final int NUM_FAMILY_MODE_NORMAL = 9;
   // private static final int NUM_FAMILY_MODE_HARD = 15;

    private static final int CARD_NO_CHOSEN = -1;

    private long mId;
    private boolean mFinished = false;
    private int mFirstPositionChosen = CARD_NO_CHOSEN;
    private int mSecondPositionChosen = CARD_NO_CHOSEN;
    private List<GameCard> mGameCardList = new ArrayList<GameCard>();
    private Mode mMode;
    private String mBackgroundPattern;
    private int mNumAttempt = 0;

    public enum Mode {
        CUSTOM,
        EASY,
        NORMAL,
        HARD;
    }

    public Game(long _id, boolean _finished, int _firstPositionChosen, int _secondPositionChosen,
                int _numAttempt, String _difficulty, String _backgroundPattern) {
        this.mId = _id;
        this.mFinished = _finished;
        this.mFirstPositionChosen = _firstPositionChosen;
        this.mSecondPositionChosen = _secondPositionChosen;
        this.mNumAttempt = _numAttempt;
        this.mMode = Mode.valueOf(_difficulty);
        this.mBackgroundPattern = _backgroundPattern;
    }

    public Game (Mode _mode, int _numFamilyCustom, String _backgroundPattern) {
        this.mId = CARD_NO_CHOSEN;
        this.mMode = _mode;
        switch (_mode) {
            case EASY:
                this.mGameCardList = GameCard.getRandomList(NUM_FAMILY_MODE_EASY);
                break;
            case NORMAL:
               // this.mGameCardList = GameCard.getRandomList(NUM_FAMILY_MODE_NORMAL);
                break;
            case HARD:
               // this.mGameCardList = GameCard.getRandomList(NUM_FAMILY_MODE_HARD);
                break;
            default:
                this.mGameCardList = GameCard.getRandomList(_numFamilyCustom);
        }
        this.mBackgroundPattern = _backgroundPattern;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public long getId() {
        return mId;
    }

    public void setId(long _id) {
        this.mId = _id;
    }

    public boolean isFinished() {
        return mFinished;
    }

    public void setFinished(boolean _finished) {
        this.mFinished = _finished;
    }

    public int getFirstPositionChosen() {
        return mFirstPositionChosen;
    }

    public void setFirstPositionChosen(int _firstPositionChosen) {
        this.mFirstPositionChosen = _firstPositionChosen;
    }

    public int getSecondPositionChosen() {
        return mSecondPositionChosen;
    }

    public void setSecondPositionChosen(int _secondPositionChosen) {
        this.mSecondPositionChosen = _secondPositionChosen;
    }

    public List<GameCard> getGameCardList() {
        return mGameCardList;
    }

    public void addGameCard(GameCard _gamGameCard) {
        this.mGameCardList.add(_gamGameCard);
    }

    public boolean isAFoundCard(int _position) {
        return mGameCardList.get(_position).isCardFound();
    }

    public void setAttemptFirstPositionChosen() {
        getFirstCardChosen().setAttempt(true);
        getFirstCardChosen().setToReturn(true);
    }

    public void setAttemptSecondPositionChosen() {
        getSecondCardChosen().setAttempt(true);
        getSecondCardChosen().setToReturn(true);
        getFirstCardChosen().setToReturn(false);
    }

    public void chooseFirstCard(int _position) {
        setFirstPositionChosen(_position);
        setAttemptFirstPositionChosen();
    }

    public void chooseSecondCard(int _position) {
        setSecondPositionChosen(_position);
        setAttemptSecondPositionChosen();
        // on incrémente le nombre de tentatives sur une partie
        mNumAttempt++;
    }

    public boolean isFirstCardChosen() {
        return getFirstPositionChosen() != CARD_NO_CHOSEN;
    }

    public boolean isSecondCardChosen() {
        return getSecondPositionChosen() != CARD_NO_CHOSEN;
    }

    public boolean isFamilyFound() {
        return getFirstCardChosen().getAnimalGame() == getSecondCardChosen().getAnimalGame();
    }

    public void checkFamilyFound() {
        if (isFamilyFound()) {
            getFirstCardChosen().setCardFound(true);
            getSecondCardChosen().setCardFound(true);
        }
    }

    public boolean isCardAlreadyChosen(int _position) {
        return mFirstPositionChosen == _position;
    }

    public boolean isAllCardsFound() {

        boolean allCardsFound = true;

        for (GameCard gameCard : mGameCardList) {
            if (!gameCard.isCardFound()) {
                allCardsFound = false;
                break;
            }
        }

        return allCardsFound;
    }

    public boolean isNextTurn() {
        return isFirstCardChosen() && isSecondCardChosen();
    }

    private GameCard getFirstCardChosen() {
        return mGameCardList.get(mFirstPositionChosen);
    }

    private GameCard getSecondCardChosen() {
        return mGameCardList.get(mSecondPositionChosen);
    }

    public void initNewTurn() {
        //
        getFirstCardChosen().setAttempt(false);
        getFirstCardChosen().setToReturn(false);
        getSecondCardChosen().setAttempt(false);
        getSecondCardChosen().setToReturn(false);
        //
        mFirstPositionChosen = -1;
        mSecondPositionChosen = -1;
    }

    public int getNumFamily() {
        return mGameCardList.size() / 2;
    }

    public int getNumFamilyFound() {
        int cardFound = 0;

        for (GameCard gameCard : mGameCardList) {
            if (gameCard.isCardFound()) {
                cardFound++;
            }
        }

        return cardFound / 2;
    }

    public String getBackgroundPattern() {
        return mBackgroundPattern;
    }

    public int getNumAttempt() {
        return mNumAttempt;
    }

    public Mode getMode() {
        return mMode;
    }
}
