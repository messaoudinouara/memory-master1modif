package com.example.admin.memorym1.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameCard implements Cloneable {

    public enum AnimalGame {
        ALLIGATOR(0), BEAVER(1), BIRDY(2), BUFFALO(3), CAT(4), COW(5), ELEPHANT(6), FISH(7), FOX(8),
        GIRAFFE(9), GOOSE(10), HORSE(11), IGUANA(12), JELLYFISH(13), KOALA(14), LION(15), MONKEY(16),
        PHEASANT(17), PIG(18), RABBIT(19), SHEEP(20), TURTLE(21), WOLF(22), WHALE(23);

        private AnimalGame(int _code) {
            this.mCode = _code;
        }

        private final int mCode;

        public int getCode() {
            return mCode;
        }

        private static final List<AnimalGame> VALUES =
                Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();

        public static List<AnimalGame> getModifiableList() {
            return new ArrayList<AnimalGame>(Arrays.asList(values()));
        }
    }

    private AnimalGame mAnimalGame;
    private boolean mCardFound = false;
    private boolean mAttempt = false;
    private boolean mToReturn = false;

    public GameCard(int _codeAnimal, boolean _cardFound,
                    boolean _firstAttempt, boolean _toReturn) {
        this.mAnimalGame = AnimalGame.values()[_codeAnimal];
        this.mCardFound = _cardFound;
        this.mAttempt = _firstAttempt;
        this.mToReturn = _toReturn;
    }

    private GameCard(AnimalGame _animalGame) {
        this.mAnimalGame = _animalGame;
    }

    private static GameCard getRandomCardOnce(List<AnimalGame> _list) {
        Random random = new Random();
        int selection = random.nextInt(_list.size());
        AnimalGame animalSelected = (AnimalGame) _list.get(selection);
        _list.remove(selection);

        return new GameCard(animalSelected);
    }

    public static List<GameCard> getRandomList(int _size) {
        List<GameCard> cardGameList = new ArrayList<GameCard>();
        List<AnimalGame> animalGameList = AnimalGame.getModifiableList();

        // TODO
        // if _size > animalGameList.size();

        for (int i = 0; i < _size; i++) {
            GameCard cardGenerated = getRandomCardOnce(animalGameList);
            cardGameList.add(cardGenerated);
            try {
                cardGameList.add((GameCard) cardGenerated.clone());
            } catch (CloneNotSupportedException e) {

            }
        }

        Collections.shuffle(cardGameList);

        return cardGameList;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public AnimalGame getAnimalGame() {
        return mAnimalGame;
    }

    public boolean isCardFound() {
        return mCardFound;
    }

    public void setCardFound(boolean _cardFound) {
        this.mCardFound = _cardFound;
    }

    public boolean isAttempt() {
        return mAttempt;
    }

    public void setAttempt(boolean _attempt) {
        this.mAttempt = _attempt;
    }

    public boolean isToReturn() {
        return mToReturn;
    }

    public void setToReturn(boolean _toReturn) {
        this.mToReturn = _toReturn;
    }

    public boolean isReturned() {
        return mAttempt || mCardFound;
    }
}
