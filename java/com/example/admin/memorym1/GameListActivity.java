package com.example.admin.memorym1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;


public class GameListActivity extends ActionBarActivity {

    private final String LOG_TAG = GameListActivity.class.getSimpleName();
    private static final String FRAGMENT_TAG_SUMMARY_GAME = "FRA_TAG_SUM_GAM";

    private ShareActionProvider mShareActionProvider;

    private boolean mIsTabletLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_main, new GameListFragment())
                    .commit();
        }


        // On vérifie la présence ou non du fragment affichant le résumé en mode tablette
        if (findViewById(R.id.container_annex) != null) {
            mIsTabletLayout = true;
            if (savedInstanceState == null) {

                SummaryGameFragment summaryGameFragment = new SummaryGameFragment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_annex,
                                summaryGameFragment,
                                FRAGMENT_TAG_SUMMARY_GAME)
                        .commit();
            }
        }
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
}
