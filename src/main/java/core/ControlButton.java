package core;

import android.view.MotionEvent;

import com.el.game.R;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

/**
 * Created by User on 10.09.2014.
 */
public class ControlButton extends GameObject {

    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = 30;
    private int buttonState = 0;    //0 - включен акселерометр, 1 - отжат акс., 2 - включен тач, 3 - отжат тач
    //Rectangle buttonRectangle;

    public ControlButton(BaseGameActivity activity, Engine engine, int positionX, int positionY) {
        super(activity, engine, positionX, positionY, BUTTON_WIDTH, BUTTON_HEIGHT);
        //buttonRectangle = new Rectangle(getX() + 3, getY() + 3, BUTTON_WIDTH - 3, BUTTON_HEIGHT - 3);
    }


    @Override
    protected BitmapTextureAtlas getNewAtlas() {
        return new BitmapTextureAtlas(4096, 4096, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    }

    @Override
    protected TiledTextureRegion getNewRegion() {
        return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(getAtlas(), getActivity(), R.drawable.control, 0, 0, 4, 1);
    }

    @Override
    public void attachTo(Scene scene) {
        super.attachTo(scene);
        getSprite().stopAnimation(0);
    }

    /**
     * Вызывается по нажатию на sprite, если нужно переопределяется в классе наследнике
     *
     * @param pSceneTouchEvent содержит координаты, вид касания, ссылку на исходный MotionEvent (генерируется системой при касании)
     * @param pTouchAreaLocalX координата по X в sprit-е
     * @param pTouchAreaLocalY координата по Y в sprit-е
     */
    public void onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        switch (pSceneTouchEvent.getAction()){
            case TouchEvent.ACTION_DOWN:
            case TouchEvent.ACTION_UP:
                buttonState++;
                break;
        }
        if (buttonState > 3)
            buttonState = 0;
        getSprite().stopAnimation(buttonState);
        //if(buttonState == 0)
            //((GameActivity)(getActivity())).setAccelerometerState(true);
        //if(buttonState == 2)
            //((GameActivity)(getActivity())).setAccelerometerState(false);
    }

    public int getButtonState(){
        return buttonState;
    }
}
