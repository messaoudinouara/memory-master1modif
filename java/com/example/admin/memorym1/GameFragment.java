package com.example.admin.memorym1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import  com.example.admin.memorym1.adapter.GridGameAdapter;
import  com.example.admin.memorym1.data.MemonimoProvider;
import  com.example.admin.memorym1.model.BackgroundPattern;
import  com.example.admin.memorym1.model.Game;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    private final String LOG_TAG = GameFragment.class.getSimpleName();

    private static final String INSTANCE_STATE_ID_GAME = "instance_state_id_game";


    private ShareActionProvider mShareActionProvider;

    OnGameListener mGameCallback;

    private Game mGame;

    private List<BackgroundPattern> mBackgroundPatternList;

    // Views
    private View mRootView;

    public GameFragment() {
        // Required empty public constructor
    }

    // Interface pour indiquer à l'activité parente que l'état de la partie a changé
    public interface OnGameListener {
        public void onGameChanged(Game game);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_game, container, false);

        startGame();

        return mRootView;
    }

    private void startGame() {
        // Récupération de l'identifiant de la partie envoyée par l'activitée
        long idGame = getArguments().getLong(GameActivity.BUNDLE_GAME_ID);
        // Récupération de la partie via le Provider
        mGame = MemonimoProvider.restoreGame(getActivity().getContentResolver(), idGame);

        if (null != mGame.getBackgroundPattern()) {
            Bitmap bitmap = MemonimoUtilities.decodeBase64(mGame.getBackgroundPattern());
            BitmapDrawable backgroundDrawable = new BitmapDrawable(bitmap);
            backgroundDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            mRootView.setBackgroundDrawable(backgroundDrawable);
        }

        GridView gridGameView = (GridView) mRootView.findViewById(R.id.gridMemory);


        final GridGameAdapter gridGameAdapter = new GridGameAdapter(getActivity(), mGame);

        gridGameView.setAdapter(gridGameAdapter);

        gridGameView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // dans le cas d'un précédent tour de jeu
                if (mGame.isNextTurn()) {
                    // on modifie l'état des cartes pour qu'elles ne soient plus affichées,
                    // si celles-ci n'ont pas été trouvées
                    mGame.initNewTurn();
                }

                // On vérifie si la première carte n'a pas déjà été choisie
                if (!mGame.isFirstCardChosen()) {
                    if (mGame.isAFoundCard(position)) {
                        //
                        Toast.makeText(
                                getActivity(),
                                "Carte déjà trouvée à la position N°" + position,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // on affecte la position sélectionnée
                        mGame.chooseFirstCard(position);

                        finishChoice();

                        // Notification du changement d'état pour la vue
                        gridGameAdapter.notifyDataSetChanged();
                    }
                }
                // deuxième coup du tour de jeu
                else {

                    if (mGame.isCardAlreadyChosen(position)) {
                        Toast.makeText(
                                getActivity(),
                                "Carte déjà sélectionnée à la position N°" + position,
                                Toast.LENGTH_SHORT).show();
                    } else if (mGame.isAFoundCard(position)) {
                        //
                        Toast.makeText(
                                getActivity(),
                                "Carte déjà trouvée à la position N°" + position,
                                Toast.LENGTH_SHORT).show();
                    } else {

                        mGame.chooseSecondCard(position);

                        mGame.checkFamilyFound();

                        if (mGame.isAllCardsFound()) {
                            //
                            mGame.setFinished(true);

                            // Affichage d'une fenêtre pour recommencer une partie ou retourner à l'accueil
                            StartFragment.RestartDialogFragment restartDialogFragment = StartFragment.RestartDialogFragment.newInstance(
                                    mGame.getId(),
                                    mGame.getMode().toString());
                            restartDialogFragment.show(getActivity().getSupportFragmentManager(), "restartDialogFragment");

                        }
                        // Notification du changement d'état pour la vue
                        gridGameAdapter.notifyDataSetChanged();

                    }
                }

                finishChoice();

            }
        });

    }

    private void finishChoice() {
        // Enregistrement de la partie en base via le Provider
        long idGame = MemonimoProvider.saveGame(getActivity().getContentResolver(), mGame);
        mGame.setId(idGame);
        // Notification du changement d'état de la partie à l'activité pour les autres fragments
        mGameCallback.onGameChanged(mGame);
    }

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mGameCallback = (OnGameListener) _activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(_activity.toString()
                    + " must implement OnGameListener");
        }
    }

}
