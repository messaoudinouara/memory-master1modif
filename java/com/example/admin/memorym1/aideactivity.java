package com.example.admin.memorym1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by admin on 11/12/2016.
 */
public class aideactivity extends Activity {
    private Button Previous=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aide);



        Previous = (android.widget.Button) findViewById(R.id.previous);
        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }
}
