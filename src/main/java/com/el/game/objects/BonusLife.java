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

public class BonusLife extends MovingCollisionObject {

    public BonusLife(BaseGameActivity activity, Engine engine, Vector2 position) {
        super(activity, engine, position, new Vector2(Utils.getPixelsOfPercentX(6), Utils.getPixelsOfPercentY(11)));
    }

    @Override
    protected BitmapTextureAtlas getNewObjectAtlas() {
        return new BitmapTextureAtlas(getActivity().getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    }

    @Override
    protected TiledTextureRegion getNewObjectRegion(BitmapTextureAtlas objectAtlas) {
        return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(objectAtlas, getActivity(), R.drawable.bonus_life, 0, 0, 4, 1);
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
        if (player.getCountLife() == Player.DEFAULT_COUNT_LIFE)
            player.setCountLife(Player.DEFAULT_COUNT_LIFE + 1);
    }
}
