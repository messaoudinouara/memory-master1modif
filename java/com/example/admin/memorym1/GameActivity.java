package com.example.admin.memorym1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;

import com.example.admin.memorym1.data.MemonimoProvider;
import com.example.admin.memorym1.model.BackgroundPattern;
import com.example.admin.memorym1.model.Game;

import java.util.List;
import java.util.Random;

public class GameActivity extends ActionBarActivity
        implements GameFragment.OnGameListener, RestartDialogFragment.RestartDialogListener {

    private static final String LOG_TAG = GameActivity.class.getSimpleName();
    private static final String INSTANCE_STATE_ID_GAME = "instance_state_id_game";
    private static final String FRAGMENT_TAG_SUMMARY_GAME = "FRA_TAG_SUM_GAM";

    public static final String BUNDLE_GAME_ID = "bundle_game_id";

    private ShareActionProvider mShareActionProvider;

    private List<BackgroundPattern> mBackgroundPatternList;

    private long mIdGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Récupération de la liste des patterns
        mBackgroundPatternList = MemonimoProvider.restoreAllPatternList(getContentResolver());

        // Récupération de l'identifiant de la partie avec création en base si nécessaire
        mIdGame = getIdGame(savedInstanceState);

        Bundle bundle = prepareGame(mIdGame);

        launchGameFragment(bundle);

        // On vérifie la présence ou non du fragment affichant le résumé en mode tablette
      /*  if (findViewById(R.id.container_annex) != null) {
            if (savedInstanceState == null) {
                launchSummaryGameFragment(bundle);
            }
        }*/
    }

    private long getIdGame(Bundle _savedInstanceState) {
        long idGame = -1;
        // Initialisation à défaut
        String mode = null;

        Game game = null;

        if (_savedInstanceState != null) {
            idGame = _savedInstanceState.getLong(INSTANCE_STATE_ID_GAME);
        } else {
            // Récupération des informations liées à l'Intent
            Intent intent  = this.getIntent();
            if (intent != null) {
                // Récupération de l'id de la partie si celui-ci a été passé par Intent
                if (intent.hasExtra(MemonimoUtilities.INTENT_EXTRA_ID_GAME)) {
                    idGame = intent.getLongExtra(MemonimoUtilities.INTENT_EXTRA_ID_GAME, -1);
                }
                // Récupération de la difficulté de la partie si celle-ci a été passée par Intent
                if (intent.hasExtra(MemonimoUtilities.INTENT_EXTRA_MODE_GAME)) {
                    mode = intent.getStringExtra(MemonimoUtilities.INTENT_EXTRA_MODE_GAME);
                }
            }
        }

        if (idGame == -1) {
            // Création de la partie
            game = createGame(mode);
        } else {
            // Restauration de la partie
            Log.d(LOG_TAG, ".getIdGame() : restoreGame, id --> " + idGame);
            game = MemonimoProvider.restoreGame(this.getContentResolver(), idGame);
        }

        return game.getId();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Sauvegarde de l'identifiant de la partie dans l'état
        savedInstanceState.putLong(INSTANCE_STATE_ID_GAME, mIdGame);
        Log.d(LOG_TAG, ".onSaveInstanceState() : id --> " + mIdGame + " saved");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_general, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        mShareActionProvider.setShareIntent(MemonimoUtilities.createShareIntent(getResources()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Correspondance pour l'accès à la configuration
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGameChanged(Game _game) {

        Log.v(LOG_TAG, ".onGameChanged()");


      /*  SummaryGameFragment summaryGameFragment =
                (SummaryGameFragment) getSupportFragmentManager().findFragmentById(R.id.container_annex);

        // On vérifie la présence ou non du fragment affichant le résumé en mode tablette
        if (summaryGameFragment != null) {
            // Si présent on met à jour le fragment
            summaryGameFragment.updateSummaryView(_game);
        }*/
    }

    private Game createGame(String _mode) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String numFamilyCustom = preferences.getString(
                getString(R.string.pref_num_family_custom_key),
                getString(R.string.pref_num_family_custom_default));

        BackgroundPattern backgroundPattern = null;

        if (mBackgroundPatternList != null || mBackgroundPatternList.size() > 0) {
            backgroundPattern = chooseBackground();
        }

        // Initialisation d'une nouvelle partie d'un point de vue du modèle
        Game game = new Game(
                Game.Mode.valueOf(_mode),
                Integer.parseInt(numFamilyCustom),
                backgroundPattern != null ? backgroundPattern.getImgEncoded() : null);
        // Persistance de la partie
        long idGame = MemonimoProvider.saveGame(this.getContentResolver(), game);
        game.setId(idGame);

        return game;
    }

    private void launchGameFragment(Bundle bundle) {
        // Déclaration dynamique du fragment affichant la partie
        GameFragment gameFragment = new GameFragment();
        gameFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_main, gameFragment)
                .commit();
    }

    private void launchSummaryGameFragment(Bundle bundle) {
        SummaryGameFragment summaryGameFragment = new SummaryGameFragment();
        summaryGameFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_annex,
                        summaryGameFragment,
                        FRAGMENT_TAG_SUMMARY_GAME)
                .commit();
    }

    /**
     * Obtention de l'identifiant d'une partie, soit par récupération de celui-ci,
     * soit par création d'une nouvelle partie. Puis mémorisation de l'identifiant qui sera transmis
     * aux fragments.
     */
    private Bundle prepareGame(long idGame) {
        // Mémorisation de l'identifiant
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_GAME_ID, idGame);

        return bundle;
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {
        Intent intent = new Intent(this, game_memory.class);
        startActivity(intent);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment, String _mode) {
        Game game = createGame(_mode);
        Bundle bundle = prepareGame(game.getId());
        launchGameFragment(bundle);
        // On vérifie la présence ou non du fragment affichant le résumé en mode tablette
        if (findViewById(R.id.container_annex) != null) {
            launchSummaryGameFragment(bundle);
        }
    }

    private BackgroundPattern chooseBackground() {

        BackgroundPattern backgroundPattern = null;

        // Vérification qu'une image peut être récupérée
        if (mBackgroundPatternList != null && mBackgroundPatternList.size() > 0) {
            // Récupération d'une image encodée au hasard
            int random = new Random().nextInt(mBackgroundPatternList.size());
            backgroundPattern = mBackgroundPatternList.get(random);
        }

        return backgroundPattern;
    }


}
