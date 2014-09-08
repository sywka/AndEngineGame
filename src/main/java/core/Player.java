package core;

import android.util.Log;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.IEntityModifier;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.MoveYModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.modifier.IModifier;

import game.lalki.com.game.R;

public class Player extends GameObject implements IEntityModifier.IEntityModifierListener {

    private MoveYModifier moveDown;
    private MoveYModifier moveUp;

    public static final int MOVE_RIGHT = 0;
    public static final int MOVE_LEFT = 1;
    public static final int NOT_MOVE_LEFT_RIGHT = 2;

    public static final int STEP = 10;

    public int move = NOT_MOVE_LEFT_RIGHT;
    public int startSpriteFrame = 0;
    public int endSpriteFrame = 3;

    public Player(BaseGameActivity activity, Engine engine, int positionX, int positionY) {
        super(activity, engine, positionX, positionY);

        moveDown = new MoveYModifier(1f, positionY, engine.getCamera().getHeight() - getSprite().getHeight(), this);
        moveUp = new MoveYModifier(1f, engine.getCamera().getHeight() - getSprite().getHeight(), positionY, this);
        getSprite().registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(moveDown, moveUp)));
    }

    @Override
    protected BitmapTextureAtlas getNewAtlas() {
        return new BitmapTextureAtlas(4096, 4096, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    }

    @Override
    protected TiledTextureRegion getNewRegion() {
        return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(getAtlas(), getActivity(), R.drawable.john, 0, 0, 8, 2);
    }

    @Override
    public void attachTo(Scene scene) {
        super.attachTo(scene);
        getSprite().animate(new long[]{100, 100, 100, 100}, 0, 3, true);
    }

    @Override
    public void onModifierStarted(IModifier<IEntity> iEntityIModifier, IEntity iEntity) {
        if (iEntityIModifier == moveDown) {
            startSpriteFrame = 0;
            endSpriteFrame = 3;
        } else {
            startSpriteFrame = 4;
            endSpriteFrame = 7;
        }
        getSprite().animate(new long[]{100, 100, 100, 100}, startSpriteFrame, endSpriteFrame, true);
    }

    @Override
    public void onModifierFinished(IModifier<IEntity> iEntityIModifier, IEntity iEntity) {
//        Log.d("isMoveDown", (iEntityIModifier == moveDown) ? "yes" : "no");
//        Log.d("isMoveUp", (iEntityIModifier == moveUp) ? "yes" : "no");
    }

    @Override
    public void onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
        Log.d("onAreaTouched", "touched");
        Log.d("X", pTouchAreaLocalX + "");
        Log.d("Y", pTouchAreaLocalY + "");
    }

    @Override
    public void onUpdateState(float v) {
        super.onUpdateState(v);
        switch (move) {
            case MOVE_LEFT:
                setPosition(getX() - STEP, getY());
                if (startSpriteFrame < 5) {
                    startSpriteFrame += 8;
                    endSpriteFrame += 8;
                    getSprite().animate(new long[]{100, 100, 100, 100}, startSpriteFrame, endSpriteFrame, true);
                }
                break;
            case MOVE_RIGHT:
                setPosition(getX() + STEP, getY());
                if (startSpriteFrame > 5) {
                    startSpriteFrame -= 8;
                    endSpriteFrame -= 8;
                    getSprite().animate(new long[]{100, 100, 100, 100}, startSpriteFrame, endSpriteFrame, true);
                }
                break;
            case NOT_MOVE_LEFT_RIGHT:
                break;
        }
        getSprite().setPosition(getX(), getY());
    }

    public void setMove(int move) {
        this.move = move;
    }
}
