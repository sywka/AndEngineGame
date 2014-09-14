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
    private TiledTextureRegion region;      //Регион объекта
    private BitmapTextureAtlas atlas;       //Атлас объекта
    private Rectangle hitBoxRectangle;
    private Vector2 hitBoxMargin;
    private Sprite hitBoxSprite;          //Анимированный спрайт объекта

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

        if (hitBoxMargin != null) {
            BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
            BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(getActivity().getTextureManager(), 256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
            TextureRegion myTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, getActivity(), "hitbox.png", 0, 0);
            this.getEngine().getTextureManager().loadTexture(mBitmapTextureAtlas);
            hitBoxSprite = new Sprite(0, 0, hitBoxRectangle.getWidth(), hitBoxRectangle.getHeight(), myTextureRegion, getActivity().getVertexBufferObjectManager());
            scene.attachChild(hitBoxSprite);
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

    public Rectangle getHitboxRectangle(){ return hitBoxRectangle; }
}
