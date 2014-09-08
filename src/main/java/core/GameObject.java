package core;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

/**
 * Базовый класс для объектов
 */
abstract public class GameObject extends Entity {

    private BaseGameActivity activity;
    private Engine engine;
    private AnimatedSprite sprite;
    private TiledTextureRegion region;
    private BitmapTextureAtlas atlas;

    public GameObject(BaseGameActivity activity, Engine engine, int positionX, int positionY) {
        super(positionX, positionY);
        this.activity = activity;
        this.engine = engine;
        atlas = getNewAtlas();
        engine.getTextureManager().loadTexture(atlas);
        region = getNewRegion();
        sprite = new AnimatedSprite(getX(), getY(), region) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                GameObject.this.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
    }

    /**
     * Вызывается по нажатию на sprite, если нужно переопределяется в классе наследнике
     *
     * @param pSceneTouchEvent содержит координаты, вид касания, ссылку на исходный MotionEvent (генерируется системой при касании)
     * @param pTouchAreaLocalX координата по X в sprit-е
     * @param pTouchAreaLocalY координата по Y в sprit-е
     */
    public void onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
    }

    /**
     * Добавляет sprite на сцену, вызывается при инициализации.
     * Если нужно произвести какие-то дополнительные действия переопределяется в классе наследнике
     *
     * @param scene сцена
     */
    public void attachTo(Scene scene) {
        scene.attachChild(sprite);
        scene.registerTouchArea(sprite);
    }

    public void onUpdateState(float v) {}

    /**
     * @return атлас для хранения спрайта
     */
    abstract protected BitmapTextureAtlas getNewAtlas();

    /**
     * @return объект хранящий отдельные регионы
     */
    abstract protected TiledTextureRegion getNewRegion();

    public BaseGameActivity getActivity() {
        return activity;
    }

    public Engine getEngine() {
        return engine;
    }

    public BitmapTextureAtlas getAtlas() {
        return atlas;
    }

    public TiledTextureRegion getRegion() {
        return region;
    }

    public AnimatedSprite getSprite() {
        return sprite;
    }
}
