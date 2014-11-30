package com.el.game.objects;

import com.el.game.R;
import com.el.game.utils.Utils;

import org.andengine.engine.Engine;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import com.el.game.utils.Vector2;

public class Enemy extends MovingCollisionObject {

    public Enemy(BaseGameActivity activity, Engine engine, Vector2 position) {
        super(activity, engine, position, new Vector2(Utils.getPixelsOfPercentX(6), Utils.getPixelsOfPercentY(11)));
        getObjectSprite().animate(new long[]{100, 100, 100, 100}, 0, 3, true);
        setHitBox(new Vector2(Utils.getPixelsOfPercentX(4), Utils.getPixelsOfPercentY(7)), new Vector2(Utils.getPixelsOfPercentX(1), Utils.getPixelsOfPercentY(2)));
    }

    @Override
    protected BitmapTextureAtlas getNewObjectAtlas() {
        return new BitmapTextureAtlas(getActivity().getTextureManager(), 1024, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    }

    @Override
    protected TiledTextureRegion getNewObjectRegion(BitmapTextureAtlas objectAtlas) {
        return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(objectAtlas, getActivity(), R.drawable.enemy, 0, 0, 4, 2);
    }

    @Override
    protected BitmapTextureAtlas getNewArrowAtlas() {
        return new BitmapTextureAtlas(getActivity().getTextureManager(), 512, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    }

    @Override
    protected TiledTextureRegion getNewArrowRegion(BitmapTextureAtlas arrowAtlas) {
        return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(arrowAtlas, getActivity(), R.drawable.arrow, 0, 0, 4, 1);
    }

    @Override
    protected void onCollision(Player player) {
        if (getIsAlife() == false)              //Блок может быть мёртвым, но при этом, пока проигрывает смертную анимацию, он будет находиться
            return;                             //На сцене
        if (!player.getIsDead()) {              //Достаточно один раз убить персонажа
            player.die();
            if (player.getCountLife() != 0) {     //Если у персонажа не закончились жизни, то мы проигрываем смерть блока
                getObjectSprite().animate(new long[]{100, 100, 100, 100}, 4, 7, false);
                setIsAlife(false);
            }
        }
    }
}
