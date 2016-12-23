package com.example.admin.memorym1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.admin.memorym1.model.Game;
import com.example.admin.memorym1.service.BackgroundPatternService;

/**
 * Created by admin on 11/12/2016.
 */
public class game_memory extends Activity {

    private final String LOG_TAG = game_memory.class.getSimpleName();

    private ShareActionProvider mShareActionProvider;
    private Intent mBackgroundPatternService;
    private Button Previous=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partial_start_game);

        // Initialisation du service pour récupérer les patterns
        mBackgroundPatternService = new Intent(this, BackgroundPatternService.class);
        // lancement du service pour récupérer les patterns
        startService(mBackgroundPatternService);
        Previous = (android.widget.Button) findViewById(R.id.previous);
        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_general, menu);

        // Récupération de l'item de partage pour le menu
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Récupération du Provider de partage et association à l'item
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

    /*
        Pour arriver sur la vue d'une nouvelle partie
     */
    public void startGame(View _view, String _mode) {
        Intent intent = new Intent(this, GameActivity.class)
                // Envoi du nombre de familles via l'Intent
                .putExtra(MemonimoUtilities.INTENT_EXTRA_MODE_GAME, _mode);
        startActivity(intent);
    }
    public void startEasyGame(View _view) {
        startGame(_view, Game.Mode.EASY.toString());
    }
  //  public void startNormalGame(View _view) {startGame(_view, Game.Mode.NORMAL.toString());}
   // public void startHardGame(View _view) { startGame(_view, Game.Mode.HARD.toString()); }
   // public void startCustomGame(View _view) { startGame(_view, Game.Mode.CUSTOM.toString()); }

    /*
        Pour arriver sur la vue listant les parties non terminées
     */
   /* public void findGame(View _view) {
        Intent intent = new Intent(this, GameListActivity.class);
        startActivity(intent);
    }*/

    /*
        Pour arriver sur la vue de configuration de l'application
    */
    public void goToSettings(View _view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }








}
