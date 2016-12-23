package com.example.admin.memorym1.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.memorym1.MemonimoUtilities;
import com.example.admin.memorym1.R;
import com.example.admin.memorym1.data.MemonimoContract;

/**
 * Created by admin on 16/12/2016.
 */
public class ListGameAdapter extends CursorAdapter {

    private static class SummaryGameViewHolder {
        public final ImageView mImageViewPattern;
        public final ImageView mImageViewDifficulty;
        public final TextView mTextViewTitle;

        public SummaryGameViewHolder(View view) {
            mImageViewPattern = (ImageView) view.findViewById(R.id.li_summary_game_pattern);
            mImageViewDifficulty = (ImageView) view.findViewById(R.id.li_summary_game_difficulty);
            mTextViewTitle = (TextView) view.findViewById(R.id.li_summary_game_title);
        }
    }


    public ListGameAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //
        SummaryGameViewHolder viewHolder = (SummaryGameViewHolder) view.getTag();

        String imgEncoded = cursor.getString(cursor.getColumnIndex(MemonimoContract.GameEntry.COLUMN_PATTERN));
        if (null != imgEncoded) {
            Bitmap bitmap = MemonimoUtilities.decodeBase64(imgEncoded);
            BitmapDrawable backgroundDrawable = new BitmapDrawable(bitmap);
            backgroundDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            viewHolder.mImageViewPattern.setImageDrawable(backgroundDrawable);
        }

        viewHolder.mTextViewTitle.setTypeface(MemonimoUtilities.getCustomFont(context.getAssets()));
        String title = String.format(
                context.getResources().getString(R.string.game_label),
                cursor.getLong(cursor.getColumnIndex(MemonimoContract.GameEntry._ID)));
        viewHolder.mTextViewTitle.setText(title);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        // Récupération du layout approprié
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_game, parent, false);

        SummaryGameViewHolder viewHolder = new SummaryGameViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    public long getIdItem(int _position) {
        Cursor cursor = (Cursor) getItem(_position);
        return cursor.getLong(cursor.getColumnIndex(MemonimoContract.GameEntry._ID));
    }
}





