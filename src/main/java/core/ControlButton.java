package core;

import com.el.game.R;

import org.andengine.engine.Engine;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class ControlButton extends GameObject {

    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = 30;
    private int buttonState = 0;    //0 - включен акселерометр, 1 - отжат акс., 2 - включен тач, 3 - отжат тач
    Rectangle buttonRectangle;          //Квадрат коллизии кнопки
    boolean isButtonPressed = false;    //Нажата ли кнопка

    public ControlButton(BaseGameActivity activity, Engine engine, int positionX, int positionY) {
        super(activity, engine, positionX, positionY, BUTTON_WIDTH, BUTTON_HEIGHT);
        buttonRectangle = new Rectangle( 10, 10, (float) BUTTON_WIDTH - 20, (float) BUTTON_HEIGHT - 15,
                activity.getVertexBufferObjectManager());
        getSprite().stopAnimation(0);
    }

    @Override
    protected BitmapTextureAtlas getNewAtlas() {
        return new BitmapTextureAtlas(getActivity().getTextureManager(), 512, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    }

    @Override
    protected TiledTextureRegion getNewRegion() {
        return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(getAtlas(), getActivity(), R.drawable.control, 0, 0, 4, 1);
    }

    /**
     * Вызывается по нажатию на sprite, если нужно переопределяется в классе наследнике
     *
     * @param pSceneTouchEvent содержит координаты, вид касания, ссылку на исходный MotionEvent (генерируется системой при касании)
     * @param pTouchAreaLocalX координата по X в sprit-е
     * @param pTouchAreaLocalY координата по Y в sprit-е
     */
    public void onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                if (!isButtonPressed) {     //Если кнопка ещё не нажималась
                    isButtonPressed = true;         //Устанавливаем что нажималась
                    buttonState++;                  //Увеличиваем состояние кнопки
                }
                break;
            case TouchEvent.ACTION_UP:              //Опускание кнопки
                if (isButtonPressed) {      //Если мы вышли за пределы кнопки, а потом вернулись и опустили её,
                    buttonState++;                  //isButtonPressed будет false
                    isButtonPressed = false;
                }
                break;
            case TouchEvent.ACTION_MOVE:            //Движение внутри кнопки
                if (!buttonRectangle.contains(pTouchAreaLocalX, pTouchAreaLocalY) && isButtonPressed) { //Если мы зашли за пределы квадрата внутри кнопки
                    isButtonPressed = false;        //Ставим, что кнопка не включена
                    buttonState--;
                }
                break;
        }
        if (buttonState > 3)
            buttonState = 0;
        getSprite().stopAnimation(buttonState);
    }

    public int getButtonState() {
        return buttonState;
    }
}
