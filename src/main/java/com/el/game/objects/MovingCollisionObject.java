package com.el.game.objects;

import com.el.game.utils.Utils;
import com.el.game.utils.Vector2;

import org.andengine.engine.Engine;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

abstract public class MovingCollisionObject extends CollisionObject {

    private float xSpeed;
    private BitmapTextureAtlas arrowAtlas;
    private TiledTextureRegion arrowRegion;
    private AnimatedSprite arrowSprite;

    public MovingCollisionObject(BaseGameActivity activity, Engine engine, Vector2 position, Vector2 scale) {
        super(activity, engine, position, scale);
        arrowAtlas = getNewArrowAtlas();
        if (arrowAtlas == null) return;
        arrowRegion = getNewArrowRegion(arrowAtlas);
        arrowAtlas.load();
        arrowSprite = new AnimatedSprite(0, 0, Utils.getPixelsOfPercentX(6), Utils.getPixelsOfPercentY(11), arrowRegion, engine.getVertexBufferObjectManager());
        arrowSprite.setVisible(false);
    }

    @Override
    abstract protected BitmapTextureAtlas getNewObjectAtlas();

    abstract protected BitmapTextureAtlas getNewArrowAtlas();

    @Override
    abstract protected TiledTextureRegion getNewObjectRegion(BitmapTextureAtlas objectAtlas);

    abstract protected TiledTextureRegion getNewArrowRegion(BitmapTextureAtlas arrowAtlas);

    @Override
    abstract protected void onCollision(Player player);

    @Override
    public void onUpdateState(float v) {
        if (!getIsAlife()) {
            if (getObjectSprite().getCurrentTileIndex() > 3) {  //Если у объекта проигрывается анимация смерти
                if (!getObjectSprite().isAnimationRunning()) {      //Если объект на сцене, но неанимирован, убираем его за пределы
                    setPositionX(Utils.getPixelsOfPercentX(-20));
                    getObjectSprite().animate(new long[]{100, 100, 100, 100}, 0, 3, true);
                } else
                    setPositionX(getPositionX() + xSpeed);
            }
            return;
        }
        if ((getPositionX() < Utils.getPixelsOfPercentX(-20) && xSpeed < 0) ||
                getPositionX() > Utils.getPixelsOfPercentX(120) && xSpeed > 0) {  //Проверка вылета за экран при скорости способствующей этому
            setIsAlife(false);
            arrowSprite.setVisible(false);
        }
        ///Установка отрисовки "стрелочки", указывающей вылет объекта
        else {
            if (getPositionX() < Utils.getPixelsOfPercentX(0) && getPositionX() > Utils.getPixelsOfPercentX(-20) && xSpeed > 0) {
                if (!arrowSprite.isVisible()) {
                    arrowSprite.setPosition(0, this.getPositionY());
                    arrowSprite.animate(new long[]{200, 200}, 0, 1, true);
                    arrowSprite.setVisible(true);
                }
            } else if (getPositionX() > Utils.getPixelsOfPercentX(100) && getPositionX() < Utils.getPixelsOfPercentX(120) && xSpeed < 0) {
                if (!arrowSprite.isVisible()) {
                    arrowSprite.setPosition(Utils.getPixelsOfPercentX(92), this.getPositionY());
                    arrowSprite.animate(new long[]{200, 200}, 2, 3, true);
                    arrowSprite.setVisible(true);
                }
            } else
                arrowSprite.setVisible(false);
        }
        ///Установка отрисовки "стрелочки", указывающей вылет врага
        setPositionX(getPositionX() + xSpeed);
        updateHitBox();
        super.onUpdateState(v);
    }

    public BitmapTextureAtlas getArrowAtlas() {
        return arrowAtlas;
    }

    public TiledTextureRegion getArrowRegion() {
        return arrowRegion;
    }

    public AnimatedSprite getArrowSprite() {
        return arrowSprite;
    }

    public void setXSpeed(float newXSpeed) {
        xSpeed = newXSpeed;
    }

    public float getXSpeed() {
        return xSpeed;
    }
}
