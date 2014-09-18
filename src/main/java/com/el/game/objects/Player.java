package com.el.game.objects;

import android.util.Log;

import com.el.game.R;
import com.el.game.utils.ScoreHelper;
import com.el.game.utils.Utils;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import com.el.game.utils.Vector2;

public class Player extends GameObject {

    public static final int MOVE_RIGHT = 0; //Значение движения вправо
    public static final int MOVE_LEFT = 1;  //Значение движения влево
    public static final int IDLE = 2;       //Значение остановки

    public float step;      //Значение движения в пикселях

    public int move = IDLE;                 //Значение  текущего движения
    public int startSpriteFrame = 0;        //Значение начального фрейма у спрайта
    public int endSpriteFrame = 3;          //Значение конечного фрейма у спрайта

    private float fieldExtremeRightPoint;
    private float fieldExtremeLeftPoint;
    private float fieldExtremeUpPoint;
    private float fieldExtremeDownPoint;
    private float fallSpeed;

    private boolean isDead = false;

    private ScoreHelper scoreHelper;

    /**
     * Конструктор
     */
    public Player(BaseGameActivity activity, Engine engine, Vector2 position) {
        super(activity, engine, position, new Vector2(Utils.getPixelsOfPercentX(6), Utils.getPixelsOfPercentY(11)));
        fieldExtremeRightPoint = Utils.getPixelsOfPercentX(105);
        fieldExtremeLeftPoint = Utils.getPixelsOfPercentX(-5);
        fieldExtremeUpPoint = Utils.getPixelsOfPercentY(0);
        fieldExtremeDownPoint = Utils.getPixelsOfPercentY(100) - Utils.getPixelsOfPercentY(11);
        step = Utils.getPixelsOfPercentX(1);
        fallSpeed = Utils.getPixelsOfPercentY(1);
        setHitBox(new Vector2(Utils.getPixelsOfPercentX(3), Utils.getPixelsOfPercentY(6)),
                new Vector2(Utils.getPixelsOfPercentX(2), Utils.getPixelsOfPercentY(2)));
        scoreHelper = new ScoreHelper(getActivity());
    }

    /**
     * Устанавливаем новый атлас
     *
     * @return atlas
     */
    @Override
    protected BitmapTextureAtlas getNewAtlas() {
        return new BitmapTextureAtlas(getActivity().getTextureManager(), 728, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    }

    /**
     * Устанавливаем новый регион
     */
    @Override
    protected TiledTextureRegion getNewRegion() {
        return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(getAtlas(), getActivity(), R.drawable.john, 0, 0, 8, 3);
    }

    /**
     * Привязываем спрайт к сцене и запускаем анимацию
     *
     * @param scene сцена
     */
    @Override
    public void attachTo(Scene scene) {
        super.attachTo(scene);
        getSprite().animate(new long[]{100, 100, 100, 100}, 0, 3, true);
    }

    /**
     * Действие при прикосновении к персонажу пальцем
     */
    @Override
    public void onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
        Log.d("onAreaTouched", "touched");
        Log.d("X", pTouchAreaLocalX + "");
        Log.d("Y", pTouchAreaLocalY + "");
    }

    /**
     * Апдейт игрока
     */
    @Override
    public void onUpdateState(float v) {
        super.onUpdateState(v);
        if (isDead) {
                return;
            } else scoreHelper.updateScore();

        switch (move) {
            case MOVE_LEFT:
                setPositionX(getPositionX() - step);
                if (startSpriteFrame < 5) {
                    startSpriteFrame += 8;
                    endSpriteFrame += 8;
                    getSprite().animate(new long[]{100, 100, 100, 100}, startSpriteFrame, endSpriteFrame, true);
                }
                break;
            case MOVE_RIGHT:
                setPositionX(getPositionX() + step);
                if (startSpriteFrame > 5) {
                    startSpriteFrame -= 8;
                    endSpriteFrame -= 8;
                    getSprite().animate(new long[]{100, 100, 100, 100}, startSpriteFrame, endSpriteFrame, true);
                }
                break;
            case IDLE:
                break;
        }

        ///Устанавливаем движение по OY
        if (fallSpeed > 0 && getPositionY() > fieldExtremeDownPoint) {
            fallSpeed *= -1;
            startSpriteFrame = (startSpriteFrame < 5) ? 0 : 8;
            endSpriteFrame = (endSpriteFrame < 8) ? 3 : 11;
            getSprite().animate(new long[]{100, 100, 100, 100}, startSpriteFrame, endSpriteFrame, true);
        }
        if (fallSpeed < 0 && getPositionY() < fieldExtremeUpPoint) {
            fallSpeed *= -1;
            startSpriteFrame = (startSpriteFrame < 5) ? 4 : 12;
            endSpriteFrame = (endSpriteFrame < 8) ? 7 : 15;
            getSprite().animate(new long[]{100, 100, 100, 100}, startSpriteFrame, endSpriteFrame, true);
        }
        setPositionY(getPositionY() + fallSpeed);
        ///Устанавливаем движение по OY

        ///Ограничеваем движение за пределы поля по OX
        if (getPositionX() > fieldExtremeRightPoint)
            setPosition(fieldExtremeLeftPoint, getPositionY());
        if (getPositionX() < fieldExtremeLeftPoint)
            setPosition(fieldExtremeRightPoint, getPositionY());
        ///Ограничеваем движение за пределы поля по OX

        updateHitBox();
    }

    public void setMove(int move) {
        this.move = move;
    }

    public void die() {
        getSprite().animate(new long[]{200, 200, 200, 200, 200}, 16, 20, false);
        isDead = true;
    }

    public void setIsDead(boolean state){
        isDead = state;
    }


    public void setFallSpeed(float speed){
        fallSpeed = speed;
    }

    public float getFallSpeed(){ return fallSpeed; }

    public boolean getIsDead() {
        return isDead;
    }

    public ScoreHelper getScoreHelper() { return scoreHelper; }
}
