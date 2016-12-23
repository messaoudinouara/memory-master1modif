package com.example.admin.memorym1.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.admin.memorym1.R;
import com.example.admin.memorym1.model.Game;
import com.example.admin.memorym1.model.GameCard;

/**
 * Created by admin on 16/12/2016.
 */
public class GridGameAdapter extends BaseAdapter {
    private static final int FLIP_DURATION = 150;

    private Context mContext;
    private Game mGame;

    public GridGameAdapter(Context context, Game _game) {
        this.mContext = context;
        this.mGame = _game;
    }

    @Override
    public int getCount() {
        return mGame.getGameCardList().size();
    }

    @Override
    public GameCard getItem(int _position) {
        return mGame.getGameCardList().get(_position);
    }

    @Override
    public long getItemId(int _position) {
        return 0;
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {

        // Récupération du layout approprié
        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_game, _parent, false);
        final ImageView frontCardView = (ImageView) view.findViewById(R.id.gi_front_card);
        final ImageView backCardView = (ImageView) view.findViewById(R.id.gi_back_card);

        GameCard gameCard = getItem(_position);

        frontCardView.setImageDrawable(getFrontCardDrawable(gameCard, mContext.getResources()));
        backCardView.setImageDrawable(getBackCardDrawable(gameCard, mContext.getResources()));

        // Par défaut la carte représentant la valeur est cachée
        frontCardView.setVisibility(View.GONE);

        // On vérifie si la carte est retournée
        if (gameCard.isReturned()) {
            // On vérifie si la carte va être retournée (pour l'animation)
            if (gameCard.isToReturn()) {
                ObjectAnimator flipOut = ObjectAnimator.ofFloat(backCardView, "rotationY", 0f, 90f);
                flipOut.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) { }
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        backCardView.setVisibility(View.GONE);
                        ObjectAnimator flipIn = ObjectAnimator.ofFloat(frontCardView, "rotationY", -90f, 0f);
                        flipIn.setDuration(FLIP_DURATION);
                        flipIn.start();
                        frontCardView.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) { }
                    @Override
                    public void onAnimationRepeat(Animator animation) { }
                });
                flipOut.setDuration(FLIP_DURATION);
                flipOut.start();

            } else {
                backCardView.setVisibility(View.GONE);
                frontCardView.setVisibility(View.VISIBLE);
            }

        }

        return view;
    }

    private static LayerDrawable getFrontCardDrawable(GameCard _cardGame, Resources _resources ) {
        Drawable[] layers = new Drawable[2];
        layers[0] = _resources.getDrawable(getInsideDrawable(_cardGame));
        layers[1] = _resources.getDrawable(getBorderDrawable(_cardGame));
        return new LayerDrawable(layers);
    }

    private static LayerDrawable getBackCardDrawable(GameCard _cardGame, Resources _resources ) {
        Drawable[] layers = new Drawable[2];
        layers[0] = _resources.getDrawable(R.drawable.card_hidden);
        layers[1] = _resources.getDrawable(getBorderDrawable(_cardGame));
        return new LayerDrawable(layers);
    }

    private static int getBorderDrawable(GameCard _cardGame) {
        if (_cardGame.isCardFound()) {
            return R.drawable.card_found;
        } else {
            return R.drawable.card_neutral;
        }
    }

    private static int getInsideDrawable(GameCard _cardGame) {
        return getAnimalDrawable(_cardGame.getAnimalGame());
    }

    private static int getAnimalDrawable(GameCard.AnimalGame _animalGame) {
        switch (_animalGame) {
            case ALLIGATOR: return R.drawable.card_alligator;
            case BEAVER: return R.drawable.card_beaver;
            case BIRDY: return R.drawable.card_birdy;
            case BUFFALO: return R.drawable.card_buffalo;
            case CAT: return R.drawable.card_cat;
            case COW: return R.drawable.card_cow;
            case ELEPHANT: return R.drawable.card_elephant;
            case FISH: return R.drawable.card_fish;
            case FOX: return R.drawable.card_fox;
            case GIRAFFE: return R.drawable.card_giraffe;
            case GOOSE: return R.drawable.card_goose;
            case HORSE: return R.drawable.card_horse;
            case IGUANA: return R.drawable.card_iguana;
            case JELLYFISH: return R.drawable.card_jellyfish;
            case KOALA: return R.drawable.card_koala;
            case LION: return R.drawable.card_lion;
            case MONKEY: return R.drawable.card_monkey;
            case PHEASANT: return R.drawable.card_pheasant;
            case PIG: return R.drawable.card_pig;
            case RABBIT: return R.drawable.card_rabbit;
            case SHEEP: return R.drawable.card_sheep;
            case TURTLE: return R.drawable.card_turtle;
            case WOLF: return R.drawable.card_wolf;
            case WHALE: return R.drawable.card_whale;
            // TODO exception
            default: return 0;
        }
    }
}
