package core;

import android.util.Log;

import com.el.game.R;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.modifier.IModifier;

public class Player extends GameObject implements IEntityModifier.IEntityModifierListener {

    private MoveYModifier moveDown;         //Модификатор движения вниз
    private MoveYModifier moveUp;           //Модификатор движения вверх

    public static final int MOVE_RIGHT = 0; //Значение движения вправо
    public static final int MOVE_LEFT = 1;  //Значение движения влево
    public static final int IDLE = 2;       //Значение остановки

    public float step;      //Значение движения в пикселях

    public int move = IDLE;                 //Значение  текущего движения
    public int startSpriteFrame = 0;        //Значение начального фрейма у спрайта
    public int endSpriteFrame = 3;          //Значение конечного фрейма у спрайта

    //private float upperPoint;
    //private static final int DOWNER_POINT = 143;
    private float playerWidth;
    private float playerHeight;
    private float fieldExtremeRightPoint;
    private float fieldExtremeLeftPoint;
    //private static final int FIELD_EXTREME_RIGHT_POINT = 314;
    //private static final int FIELD_EXTREME_LEFT_POINT = -14;

    /**
     * Конструктор
     */
    public Player(BaseGameActivity activity, Engine engine, int positionX, int positionY, float percentX, float percentY) {
        super(activity, engine, positionX, positionY, percentX * 6, percentY * 11);
        playerWidth = percentX * 6;
        playerHeight = percentY * 11;
        fieldExtremeRightPoint = percentX * 105;
        fieldExtremeLeftPoint = percentX * -5;
        step = 2 * percentX;
        //Устанавливаем значения движений по OY
        moveDown = new MoveYModifier(percentY / 10, percentY * 15, percentY * 85 - playerHeight, this);
        moveUp = new MoveYModifier(percentY / 10, percentY * 85 - playerHeight, percentY * 15, this);
        //Зацикливаем движение спрайта по OY
        getSprite().registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(moveDown, moveUp)));
    }

    /**
     * Устанавливаем новый атлас
     *
     * @return atlas
     */
    @Override
    protected BitmapTextureAtlas getNewAtlas() {
        return new BitmapTextureAtlas(getActivity().getTextureManager(), 1024, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    }

    /**
     * Устанавливаем новый регион
     */
    @Override
    protected TiledTextureRegion getNewRegion() {
        return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(getAtlas(), getActivity(), R.drawable.john, 0, 0, 8, 2);
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
     * Устанавливаем анимацию в зависимости от поворота персонажа
     */
    @Override
    public void onModifierStarted(IModifier<IEntity> iEntityIModifier, IEntity iEntity) {
        if (iEntityIModifier == moveDown) {
            startSpriteFrame = (startSpriteFrame < 5) ? 0 : 8;
            endSpriteFrame = (endSpriteFrame < 8) ? 3 : 11;
        } else {
            startSpriteFrame = (startSpriteFrame < 5) ? 4 : 12;
            endSpriteFrame = (endSpriteFrame < 8) ? 7 : 15;
        }
        getSprite().animate(new long[]{100, 100, 100, 100}, startSpriteFrame, endSpriteFrame, true);
    }

    @Override
    public void onModifierFinished(IModifier<IEntity> iEntityIModifier, IEntity iEntity) {
//        Log.d("isMoveDown", (iEntityIModifier == moveDown) ? "yes" : "no");
//        Log.d("isMoveUp", (iEntityIModifier == moveUp) ? "yes" : "no");
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
        if (getPositionX() > fieldExtremeRightPoint)
            setPosition(fieldExtremeLeftPoint, getPositionY());
        if (getPositionX() < fieldExtremeLeftPoint)
            setPosition(fieldExtremeRightPoint, getPositionY());
    }

    public void setMove(int move) {
        this.move = move;
    }
}
