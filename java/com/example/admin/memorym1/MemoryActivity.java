package com.example.admin.memorym1;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.example.admin.memorym1.model.Game;

public class MemoryActivity extends Activity {
    private Button jouer = null;
    private Button Apropos=null;
    private Button quitter=null;
    private Button Aide=null;
    private Button Score = null;
    private RadioButton actson;
    MediaPlayer Son;
    boolean clic;
    boolean sel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        jouer = (Button) findViewById(R.id.button);
        quitter = (Button) findViewById(R.id.button4);
        Apropos = (Button) findViewById(R.id.button3);
        actson = (RadioButton) findViewById(R.id.radiobutton);
        Aide = (Button) findViewById(R.id.button5);
        Son = MediaPlayer.create(getBaseContext(), R.raw.son);
        clic = false;

        actson.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (actson.isChecked()) {
                    sel = true;
                    Son.start();

                } else {
                    Son.pause();
                    sel = false;
                }
            }
        });


        jouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination
                clic = true;
                Intent Activite2 = new Intent(MemoryActivity.this, game_memory.class);
                startActivity(Activite2);

            }

            ;


        });


        Apropos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination
                clic = true;
                Intent Activite2 = new Intent(MemoryActivity.this, aproposactivity.class);
                startActivity(Activite2);

            }

            ;


        });


        quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Son.pause();
                finish();
            }
        });


        Aide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination
                clic = true;
                Intent Activite2 = new Intent(MemoryActivity.this, aideactivity.class);
                startActivity(Activite2);

            }

            ;


        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_memory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onStop() {
        super.onStop();
        if (!clic)
            Son.pause();

    }


    protected void onStart() {

        super.onStart();

        clic = false;
        if (sel)
            Son.start();
    }





}
