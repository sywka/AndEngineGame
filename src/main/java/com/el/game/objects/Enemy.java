package com.el.game.objects;

import com.el.game.R;
import com.el.game.utils.Utils;

import org.andengine.engine.Engine;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import com.el.game.utils.Vector2;

public class Enemy extends CollisionObject {

    private float xSpeed;
    private AnimatedSprite arrowSprite;

    public Enemy(BaseGameActivity activity, Engine engine, Vector2 position) {
        super(activity, engine, position, new Vector2(Utils.getPixelsOfPercentX(6), Utils.getPixelsOfPercentY(11)));
        getSprite().animate(new long[]{100, 100, 100, 100}, 0, 3, true);
        setHitBox(new Vector2(Utils.getPixelsOfPercentX(4), Utils.getPixelsOfPercentY(7)), new Vector2(Utils.getPixelsOfPercentX(1), Utils.getPixelsOfPercentY(2)));

        ///Устанавливаем спрайт со "стрелочками"
        BitmapTextureAtlas myBitmapTextureAtlas = new BitmapTextureAtlas(getActivity().getTextureManager(), 512, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        TiledTextureRegion myTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(myBitmapTextureAtlas, getActivity(), R.drawable.arrow, 0, 0, 4, 1);
        this.getEngine().getTextureManager().loadTexture(myBitmapTextureAtlas);
        arrowSprite = new AnimatedSprite(0, 0, Utils.getPixelsOfPercentX(6), Utils.getPixelsOfPercentY(11), myTiledTextureRegion, engine.getVertexBufferObjectManager());
        arrowSprite.setVisible(false);
        ///Устанавливаем спрайт со "стрелочками"
    }

    public AnimatedSprite getArrowSprite(){ return arrowSprite; }

    @Override
    protected BitmapTextureAtlas getNewAtlas() {
        return new BitmapTextureAtlas(getActivity().getTextureManager(), 1024, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    }

    @Override
    protected TiledTextureRegion getNewRegion() {
        return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(getAtlas(), getActivity(), R.drawable.enemy, 0, 0, 4, 1);
    }

    @Override
    public void onUpdateState(float v) {
        if (!getIsAlife())
            return;
        if ((getPositionX() < Utils.getPixelsOfPercentX(-20) && xSpeed < 0) ||
            getPositionX() > Utils.getPixelsOfPercentX(120) && xSpeed > 0) {  //Проверка вылета за экран при скорости способствующей этому
            setIsAlife(false);
            arrowSprite.setVisible(false);
        }
        ///Установка отрисовки "стрелочки", указывающей вылет врага
        else{
            if (getPositionX() < Utils.getPixelsOfPercentX(0) && getPositionX() > Utils.getPixelsOfPercentX(-20)  && xSpeed > 0) {
                if(!arrowSprite.isVisible()) {
                    arrowSprite.setPosition(0, this.getPositionY());
                    arrowSprite.animate(new long[]{200, 200}, 0, 1, true);
                    arrowSprite.setVisible(true);
                }
            }
            else
                if (getPositionX() > Utils.getPixelsOfPercentX(100) && getPositionX() < Utils.getPixelsOfPercentX(120) && xSpeed < 0) {
                    if (!arrowSprite.isVisible()) {
                        arrowSprite.setPosition(Utils.getPixelsOfPercentX(92), this.getPositionY());
                        arrowSprite.animate(new long[]{200, 200}, 2, 3, true);
                        arrowSprite.setVisible(true);
                    }
                }
                else
                    arrowSprite.setVisible(false);
        }
        ///Установка отрисовки "стрелочки", указывающей вылет врага
        setPositionX(getPositionX() + xSpeed);
        updateHitBox();
        super.onUpdateState(v);
    }

    @Override
    protected void onCollision(Player player) {
        if (!player.getIsDead())    //Достаточно один раз убить персонажа
            player.die();
    }

    public void setXSpeed(float newXSpeed){
        xSpeed = newXSpeed;
    }

    public float getXSpeed(){ return xSpeed; }
}
