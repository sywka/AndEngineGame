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
 * Created by User on 28.10.2014.
 */
public class BonusPortal  extends MovingCollisionObject {

    private MovingCollisionObject anotherPortal;
    private int animationPosition;

    public BonusPortal(BaseGameActivity activity, Engine engine, Vector2 position, int animationPosition) {
        super(activity, engine, position, new Vector2(Utils.getPixelsOfPercentX(6), Utils.getPixelsOfPercentY(11)));
        getObjectSprite().animate(new long[]{100, 100, 100, 100}, animationPosition, animationPosition + 3, true);
        setHitBox(new Vector2(Utils.getPixelsOfPercentX(4), Utils.getPixelsOfPercentY(7)), new Vector2(Utils.getPixelsOfPercentX(1), Utils.getPixelsOfPercentY(2)));
        this.animationPosition = animationPosition;
    }

    public void setAnotherPortal(MovingCollisionObject anotherPortal){
        this.anotherPortal = anotherPortal;
    }

    @Override
    protected BitmapTextureAtlas getNewObjectAtlas() {
        return new BitmapTextureAtlas(getActivity().getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    }

    @Override
    protected TiledTextureRegion getNewObjectRegion(BitmapTextureAtlas objectAtlas) {
        return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(objectAtlas, getActivity(), R.drawable.portals, 0, 0, 4, 4);
    }

    @Override
    protected BitmapTextureAtlas getNewArrowAtlas() {
        return new BitmapTextureAtlas(getActivity().getTextureManager(), 512, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    }

    @Override
    protected TiledTextureRegion getNewArrowRegion(BitmapTextureAtlas arrowAtlas) {
        return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(arrowAtlas, getActivity(), R.drawable.portals_arrow, 0, 0, 4, 1);
    }

    @Override
    protected void onCollision(Player player) {
        if (player.getIsDead())
            return;
        player.setPosition(anotherPortal.getPositionX(), anotherPortal.getPositionY());
        this.setIsAlife(false);
        anotherPortal.setIsAlife(false);
        this.setXSpeed(0);
        anotherPortal.setXSpeed(0);
        getObjectSprite().animate(new long[]{100, 100, 100, 100}, animationPosition + 8, animationPosition + 11, false);
        anotherPortal.getObjectSprite().animate(new long[]{100, 100, 100, 100},
                ((BonusPortal)anotherPortal).getAnimationPosition() + 8, ((BonusPortal)anotherPortal).getAnimationPosition() + 11, false);
    }

    public int getAnimationPosition(){
        return animationPosition;
    }

}
