package com.example.admin.memorym1;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

public class MemonimoUtilities {

    public static final String INTENT_EXTRA_ID_GAME = "intent_extra_id_game";
    public static final String INTENT_EXTRA_MODE_GAME = "intent_extra_mode_game";

    /**
     * Fonction retournant un Bitmap à partir d'une image encodée en Base64
     *
     * @param _input image en codée en Base64
     * @return
     */
    public static Bitmap decodeBase64(String _input) {
        byte[] decodedByte = Base64.decode(_input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static Intent createShareIntent(Resources _resources) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
       // String message = _resources.getString(R.string.share_message);
       // shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        return shareIntent;
    }

    public static Button buildButtonWithFont(View _rootView, int _idResource , AssetManager _assetManager) {
        Button button = (Button) _rootView.findViewById(_idResource);
        Typeface typeface = Typeface.createFromAsset(_assetManager, "fonts/dimbo_regular.ttf");
        button.setTypeface(typeface);
        return button;
    }

    public static Typeface getCustomFont(AssetManager _assetManager) {
        Typeface typeface = Typeface.createFromAsset(_assetManager, "fonts/dimbo_regular.ttf");
        return typeface;
    }
}