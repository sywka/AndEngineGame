package com.el.game.objects;

import com.el.game.R;
import com.el.game.utils.Utils;
import com.el.game.utils.Vector2;

import org.andengine.engine.Engine;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;

/**
 * Created by User on 16.10.2014.
 */
public class BonusC5  extends MovingCollisionObject {

    private ArrayList<MovingCollisionObject> movingObjectsList;

    public BonusC5(BaseGameActivity activity, Engine engine, Vector2 position,  ArrayList<MovingCollisionObject> movingObjectsList) {
        super(activity, engine, position, new Vector2(Utils.getPixelsOfPercentX(6), Utils.getPixelsOfPercentY(11)));
        getObjectSprite().animate(new long[]{100, 100, 100, 100}, 0, 3, true);
        setHitBox(new Vector2(Utils.getPixelsOfPercentX(4), Utils.getPixelsOfPercentY(7)), new Vector2(Utils.getPixelsOfPercentX(1), Utils.getPixelsOfPercentY(2)));
        this.movingObjectsList = movingObjectsList;
    }

    @Override
    protected BitmapTextureAtlas getNewObjectAtlas() {
        return new BitmapTextureAtlas(getActivity().getTextureManager(), 512, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    }

    @Override
    protected TiledTextureRegion getNewObjectRegion(BitmapTextureAtlas objectAtlas) {
        return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(objectAtlas, getActivity(), R.drawable.bonus_c5, 0, 0, 4, 1);
    }

    @Override
    protected BitmapTextureAtlas getNewArrowAtlas() {
        return new BitmapTextureAtlas(getActivity().getTextureManager(), 512, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    }

    @Override
    protected TiledTextureRegion getNewArrowRegion(BitmapTextureAtlas arrowAtlas) {
        return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(arrowAtlas, getActivity(), R.drawable.c5_arrow, 0, 0, 4, 1);
    }

    @Override
    protected void onCollision(Player player) {
        for (int i = 0; i < 20; i++){
            if (!movingObjectsList.get(i).getIsAlife())
                continue;
            movingObjectsList.get(i).setIsAlife(false);
            movingObjectsList.get(i).getObjectSprite().animate(new long[]{100, 100, 100, 100}, 4, 7, false);
        }
        setPositionX(Utils.getPixelsOfPercentX(120));
        setIsAlife(false);
    }
}
