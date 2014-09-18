package com.el.game.objects;

import org.andengine.engine.Engine;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import com.el.game.utils.Vector2;

/**
 * Базовый класс для объектов
 */
abstract public class GameObject {

    private BaseGameActivity activity;      //Текущая активити
    private Engine engine;                  //Текущий движок
    private AnimatedSprite sprite;          //Анимированный спрайт объекта
    private TiledTextureRegion objectRegion;      //Регион объекта
    private BitmapTextureAtlas objectAtlas;       //Атлас объекта
    private Rectangle hitBoxRectangle;
    private Vector2 hitBoxMargin;
    private Sprite hitBoxSprite;          //Анимированный спрайт объекта

    /**
     * Конструктор
     */
    public GameObject(BaseGameActivity activity, Engine engine, Vector2 position, Vector2 spriteScale) {
        this.activity = activity;
        this.engine = engine;
        objectAtlas = getNewObjectAtlas();
        objectRegion = getNewObjectRegion(objectAtlas);
        sprite = new AnimatedSprite(position.x, position.y, spriteScale.x, spriteScale.y, objectRegion, engine.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                GameObject.this.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        objectAtlas.load();
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

        if (hitBoxMargin != null) {
            BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
            BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(getActivity().getTextureManager(), 256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
            TextureRegion myTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, getActivity(), "hitbox.png", 0, 0);
            this.getEngine().getTextureManager().loadTexture(mBitmapTextureAtlas);
            hitBoxSprite = new Sprite(0, 0, hitBoxRectangle.getWidth(), hitBoxRectangle.getHeight(), myTextureRegion, getActivity().getVertexBufferObjectManager());
            //scene.attachChild(hitBoxSprite);
        }

        scene.attachChild(sprite);
        scene.registerTouchArea(sprite);
    }

    public void onUpdateState(float v) {
    }

    public void setHitBox(Vector2 hitBoxScale, Vector2 hitBoxMargin){
        hitBoxRectangle = new Rectangle(0f, 0f, hitBoxScale.x, hitBoxScale.y, activity.getVertexBufferObjectManager());
        this.hitBoxMargin = hitBoxMargin;
    }

    public void updateHitBox(){
        hitBoxRectangle.setPosition(this.getPositionX() + hitBoxMargin.x, this.getPositionY() + hitBoxMargin.y);
        hitBoxSprite.setPosition(hitBoxRectangle.getX(), hitBoxRectangle.getY());
    }

    /**
     * @return атлас для хранения спрайта
     */
    abstract protected BitmapTextureAtlas getNewObjectAtlas();

    /**
     * @return объект хранящий отдельные регионы
     */
    abstract protected TiledTextureRegion getNewObjectRegion(BitmapTextureAtlas objectAtlas);

    /**
     * Устанавливает новую позицию спрайта и объекта
     */
    public void setPosition(float positionX, float positionY) {
        setPositionX(positionX);
        setPositionY(positionY);
    }

    public void setPositionX(float positionX) {
        getObjectSprite().setPosition(positionX, getPositionY());
    }

    public float getPositionX() {
        return getObjectSprite().getX();
    }

    public void setPositionY(float positionY) {
        getObjectSprite().setPosition(getPositionX(), positionY);
    }

    public float getPositionY() {
        return getObjectSprite().getY();
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
    public BitmapTextureAtlas getObjectAtlas() {
        return objectAtlas;
    }

    /*
    *   Возвращает текущий регион
    */
    public TiledTextureRegion getObjectRegion() {
        return objectRegion;
    }

    /*
    *   Возвращает текущий спрайт
    */
    public AnimatedSprite getObjectSprite() {
        return sprite;
    }

    public Rectangle getHitboxRectangle(){ return hitBoxRectangle; }
}
