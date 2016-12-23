package com.example.admin.memorym1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.admin.memorym1.data.MemonimoProvider;

public class RestartDialogFragment extends DialogFragment {

    private static final String LOG_TAG = RestartDialogFragment.class.getSimpleName();

    private static final String ARGUMENT_ID_GAME = "argument_id_game";
    private static final String ARGUMENT_MODE = "argument_mode";

    long mIdgame;
    String mMode;
    RestartDialogListener mRestartDialogListener;

    public interface RestartDialogListener {
        public void onDialogNegativeClick(DialogFragment dialogFragment);
        public void onDialogPositiveClick(DialogFragment dialogFragment, String mode);
    }

    static RestartDialogFragment newInstance(long _idGame, String _mode) {
        RestartDialogFragment f = new RestartDialogFragment();

        Bundle args = new Bundle();
        args.putLong(ARGUMENT_ID_GAME, _idGame);
        args.putString(ARGUMENT_MODE, _mode);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Vérification que l'activité appelant ce fragment implements l'interface
        try {
            // Instanciation de l'interface
            mRestartDialogListener = (RestartDialogListener) activity;
        } catch (ClassCastException e) {
            // L'activité n'implemente pas l'interface
            throw new ClassCastException(activity.toString()
                    + " must implement " + RestartDialogFragment.class.getSimpleName());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIdgame = getArguments().getLong(ARGUMENT_ID_GAME);
        mMode = getArguments().getString(ARGUMENT_MODE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_restart, (ViewGroup) getActivity().findViewById(R.id.ll_dialog_restart));

        Button btnNewGame = (Button) view.findViewById(R.id.btn_new_game);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Suppression de la partie
                MemonimoProvider.removeGame(getActivity().getContentResolver(), mIdgame);
                // Appel l'événement de l'activité pour démarrer une nouvelle partie
                mRestartDialogListener.onDialogPositiveClick(RestartDialogFragment.this, mMode);
                // Disparition de la fenêtre de dialogue
                dismiss();
            }
        });

        Button btnHome = (Button) view.findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Suppression de la partie
                MemonimoProvider.removeGame(getActivity().getContentResolver(), mIdgame);
                // Appel l'événement de l'activité pour retourner à l'accueil
                mRestartDialogListener.onDialogNegativeClick(RestartDialogFragment.this);
            }
        });

        builder.setView(view);
        return builder.create();
    }
}
