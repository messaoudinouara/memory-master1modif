package com.example.admin.memorym1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.memorym1.adapter.ListGameAdapter;
import com.example.admin.memorym1.data.MemonimoContract;


public class GameListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = GameListFragment.class.getSimpleName();

    private ListGameAdapter mListSummaryGameAdapter;

    private TextView mTextViewTitle;
    private ListView mListViewGame;
    private int mPosition = ListView.INVALID_POSITION;

    private static final int GAME_LIST_LOADER = 0;

    private static final String SELECTED_KEY = "selected_position";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Initialisation de l'Adapter sans le curseur
        mListSummaryGameAdapter = new ListGameAdapter(
                getActivity(),
                null,
                0
        );

        // Récupération des vues
        View rootView = inflater.inflate(R.layout.fragment_game_list, container, false);
        mTextViewTitle = (TextView) rootView.findViewById(R.id.tv_title);
        mTextViewTitle.setTypeface(MemonimoUtilities.getCustomFont(getActivity().getAssets()));
        mListViewGame = (ListView) rootView.findViewById(R.id.lv_game);

        // Affectation de l'Adapter à la ListView
        mListViewGame.setAdapter(mListSummaryGameAdapter);
        mListViewGame.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Récupération de l'identifiant de la partie
                long idGame = mListSummaryGameAdapter.getIdItem(position);

                Intent intent = new Intent(getActivity(), GameActivity.class)
                        // Envoi de l'identifiant de la partie
                        .putExtra(MemonimoUtilities.INTENT_EXTRA_ID_GAME, idGame);
                startActivity(intent);

                mPosition = position;
            }
        });

        // Dans le cas où une position a été sélectionnée précedemment
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Sauvegarde de la position sélectionnée dans la liste en cas de rotation
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(GAME_LIST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Création du Loader pour récupérer les données sur la liste de parties
        return new CursorLoader(getActivity(),
                MemonimoContract.GameEntry.CONTENT_URI, // URI
                null, // Colonnes interogées
                null, // Colonnes pour la condition WHERE
                null, // Valeurs pour la condition WHERE
                null // Tri
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mListSummaryGameAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            mListViewGame.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mListSummaryGameAdapter.swapCursor(null);
    }

}
