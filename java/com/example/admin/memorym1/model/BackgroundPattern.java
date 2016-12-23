package com.example.admin.memorym1.model;

import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;

import com.example.admin.memorym1.MemonimoUtilities;

public class BackgroundPattern {

    private long mId;
    private String mImgEncoded;

    public BackgroundPattern(long _id, String _imgEncoded) {
        this.mId = _id;
        this.mImgEncoded = _imgEncoded;
    }

    public BackgroundPattern(byte[] _byteArray) {
        this.mImgEncoded = Base64.encodeToString(_byteArray, Base64.DEFAULT);
    }

    public long getId() {
        return mId;
    }

    public void setId(long _id) {
        this.mId = _id;
    }

    public String getImgEncoded() {
        return mImgEncoded;
    }

    public void setmImgEncoded(String _imgEncoded) {
        this.mImgEncoded = _imgEncoded;
    }

    public BitmapDrawable getBackgroundDrawable() {
        // Création du background avec l'image
        Bitmap bitmap = MemonimoUtilities.decodeBase64(mImgEncoded);
        BitmapDrawable backgroundDrawable = new BitmapDrawable(bitmap);
        backgroundDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        return backgroundDrawable;
    }
}
