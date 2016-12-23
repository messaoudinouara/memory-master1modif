package com.example.admin.memorym1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.memorym1.data.MemonimoProvider;
import com.example.admin.memorym1.model.Game;


public class SummaryGameFragment extends Fragment {

    private final String LOG_TAG = SummaryGameFragment.class.getSimpleName();

    private TextView mTextNumGame;
    private TextView mTextNumFamilyFound;
    private TextView mTextNumAttempt;
    private TextView mTextDifficulty;
    private ImageView mImgDifficulty;
    private ImageView mImgPattern;

    private Game mGame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_summary_game, container, false);
        mTextNumGame = (TextView) rootView.findViewById(R.id.txt_numGame);
        mTextNumFamilyFound = (TextView) rootView.findViewById(R.id.txt_numFamilyFound);
        mTextNumAttempt = (TextView) rootView.findViewById(R.id.txt_numAttempt);
        mTextDifficulty = (TextView) rootView.findViewById(R.id.txt_difficulty);
        mImgDifficulty = (ImageView) rootView.findViewById(R.id.img_difficulty);

        // Récupération de l'identifiant de la partie envoyée par l'activitée
        long idGame = getArguments().getLong(GameActivity.BUNDLE_GAME_ID);
        // Récupération de la partie via le Provider
        mGame = MemonimoProvider.restoreGame(getActivity().getContentResolver(), idGame);

        updateSummaryView(mGame);

        return rootView;
    }

    public void updateSummaryView(Game _game) {
        mGame = _game;
        mTextDifficulty.setText("Difficulté : " + mGame.getMode().toString());
        mTextNumGame.setText("Familles trouvées : " + mGame.getNumFamilyFound() + " / " + mGame.getNumFamily());
        mTextNumAttempt.setText("Tentatives : " + mGame.getNumAttempt());
    }
}
