package core;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

/**
 * Базовый класс для объектов
 */
abstract public class GameObject {

    private BaseGameActivity activity;      //Текущая активити
    private Engine engine;                  //Текущий движок
    private AnimatedSprite sprite;          //Анимированный спрайт объекта
    private TiledTextureRegion region;      //Регион объекта
    private BitmapTextureAtlas atlas;       //Атлас обёекта

    private static final float PART = 0.1f;

    /**
     * Конструктор
     */
    public GameObject(BaseGameActivity activity, Engine engine, Vector2 position, Vector2 spriteScale) {
        this.activity = activity;
        this.engine = engine;
        atlas = getNewAtlas();
        engine.getTextureManager().loadTexture(atlas);
        region = getNewRegion();
        sprite = new AnimatedSprite(position.x, position.y, spriteScale.x, spriteScale.y, region, engine.getVertexBufferObjectManager()) {
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

    public void onUpdateState(float v) {
    }

    /**
     * @return атлас для хранения спрайта
     */
    abstract protected BitmapTextureAtlas getNewAtlas();

    /**
     * @return объект хранящий отдельные регионы
     */
    abstract protected TiledTextureRegion getNewRegion();

    /**
     * Устанавливает новую позицию спрайта и объекта
     */
    public void setPosition(float positionX, float positionY) {
        setPositionX(positionX);
        setPositionY(positionY);
    }

    public void setPositionX(float positionX) {
        getSprite().setPosition(positionX, getPositionY());
    }

    public float getPositionX() {
        return getSprite().getX();
    }

    public void setPositionY(float positionY) {
        getSprite().setPosition(getPositionX(), positionY);
    }

    public float getPositionY() {
        return getSprite().getY();
    }

    /*
    *   Возвращает текущую Активити
    */
    public BaseGameActivity getActivity() {
        return activity;
    }

    /*
        *   Возвращает текущий движок
        */
    public Engine getEngine() {
        return engine;
    }

    /*
    *   Возвращает текущий Атлас
    */
    public BitmapTextureAtlas getAtlas() {
        return atlas;
    }

    /*
    *   Возвращает текущий регион
    */
    public TiledTextureRegion getRegion() {
        return region;
    }

    /*
    *   Возвращает текущий спрайт
    */
    public AnimatedSprite getSprite() {
        return sprite;
    }
}
