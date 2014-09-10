package core;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;

public class GameActivity extends BaseGameActivity implements SensorEventListener, IOnSceneTouchListener {

    private List<GameObject> objectList;    //Список игровых объектов
    private List<Integer> fingersId;        //Список Id пальцев (необходим для корректного мультитача)
    private Player player;                  //Объект игрока
    private SensorManager sensorManager;    //Менеджер сенсора
    private final int accelerometerYCencity = 2;    //Чувствительность акселлерометра по OY
    private double zRotation = 0;                   //Коэфициент погрешности поворота акс. по OZ
    private ControlButton controlButton;

    /**
     * Инициализация движка
     */
    @Override
    public EngineOptions onCreateEngineOptions() {
        fingersId = new ArrayList<Integer>();
        Camera camera = new Camera(0, 0, 300, 200);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
                new RatioResolutionPolicy(Utils.getScreenResolutionRatio()), camera);
    }

    /**
     * Инициализация игровых объектов
     */
    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) throws Exception {
        player = new Player(this, getEngine(), 0, 0);       //Добавляем игрока
        objectList = new ArrayList<GameObject>();           //Инициализируем массив игровых объектов
        objectList.add(player);                             //Добавляем к массиву игрока
        ///Добавление кнопки
        controlButton = new ControlButton(this, getEngine(), 240, 0);
        objectList.add(controlButton);
        ///
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);   //Определяем менеджер сенсора
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);         //Устанавливаем менеджер сенсора как работника с акселерометром
        onCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback onCreateSceneCallback) throws Exception {
        getEngine().registerUpdateHandler(new FPSLogger());

        final Scene scene = new Scene();        //Устанавливаем сцену

        ///Отрисовка поля
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        TextureRegion myTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "screen.png", 0, 0);
        this.getEngine().getTextureManager().loadTexture(mBitmapTextureAtlas);
        Sprite mySprite = new Sprite(0, 0, myTextureRegion, getVertexBufferObjectManager());
        scene.attachChild(mySprite);

        for (GameObject ob : objectList)        //Привязываем все игровые объекты к сцене
            ob.attachTo(scene);

        scene.setOnSceneTouchListener(this);    //Устанавливаем слушатель прикосновений на сцене

        scene.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                for (GameObject ob : objectList)
                    ob.onUpdateState(v);
            }                                   //Инициализируем update

            @Override
            public void reset() {
            }
        });

        onCreateSceneCallback.onCreateSceneFinished(scene);
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) throws Exception {
        onPopulateSceneCallback.onPopulateSceneFinished();
    }

    /**
     * Изменение значения датчика движения сенсора.
     * Угол акселерометра определяется через sensorEvent.values[i], при
     * i == 1: Y, i == 0: Z, i == 2: X
     * По Оси ОУ - Телефон лежит на боку = 10. Повёрнут положен на спину\лицо = 0.
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (controlButton.getButtonState() == 2 || controlButton.getButtonState() == 3)
            return;
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                /**
                 * Помимо наклонов по ОY cледует учитывать наклоны по ОZ. Необходимый коэфициент наклона рассчитывается
                 * по формуле (1 - OZ / 10 ) / чувствительность экселероментра по ОY
                 */
                zRotation = (1 - sensorEvent.values[0] / 10) / accelerometerYCencity;   //Устанавливаем погрешность OZ
                if (sensorEvent.values[1] < accelerometerYCencity - zRotation && sensorEvent.values[1] > -accelerometerYCencity + zRotation)
                    player.setMove(Player.IDLE);                //Если наклон не входит в промежуток, то не двигаем персонажа
                else {
                    if (sensorEvent.values[1] > accelerometerYCencity - zRotation)
                        player.setMove(Player.MOVE_RIGHT);      //Двигаем в зависимости от наклона телефона
                    if (sensorEvent.values[1] < -accelerometerYCencity + zRotation)
                        player.setMove(Player.MOVE_LEFT);
                }
                break;
        }
    }

    /**
     * Изменение точности показателя датчика(не используется)
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    /**
     * Ивент прикосновения экрана. При каждом новом прикосновении мы устанавливаем движение игрока
     * согласно координатам первого пальца и добавляем в список пальцев новый палец. При поднятии
     * мы удаляем из списка пальцев поднятый палец и устанавливаем движение либо по следующему пальцу
     * из списка либо останавливаем игрока, если таких нет.
     *
     * @return true
     */
    @Override
    public boolean onSceneTouchEvent(Scene scene, TouchEvent touchEvent) {
        if (controlButton.getButtonState() == 0 || controlButton.getButtonState() == 1)
            return true;
        MotionEvent motionEvent = touchEvent.getMotionEvent();  //Устанавливает событие движения
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                fingersId.add(motionEvent.getPointerId(motionEvent.getActionIndex()));  //Добавляем новый Id в список Id пальцев
                checkMovement(motionEvent);      //Устанавливаем направление персонажа согласно id первого пальца
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (!fingersId.isEmpty())
                    fingersId.remove(fingersId.indexOf(motionEvent.getPointerId(motionEvent.getActionIndex())));    //Удаляем из списка палец с данным Id
                checkMovement(motionEvent);     //Устанавливаем направление персонажа согласно id первого пальца
                break;
        }
        return true;
    }

    /**
     * Проверяет, есть ли зажатия экрана. Если нет, останавливает игрока. Если есть, устанавливает
     * движение согласно Id первого в списке пальцев пальца. Вызывается при поднятии пальца
     * или нажатии нового пальца
     */
    private void checkMovement(MotionEvent motionEvent) {
        if (fingersId == null || fingersId.isEmpty()) {
            player.setMove(Player.IDLE);            //Если список пальцев пуст, останавливает игрока
            return;
        }
        if (Utils.getScreenWidth() / 2 < motionEvent.getX(motionEvent.findPointerIndex(fingersId.get(0))))
            player.setMove(Player.MOVE_RIGHT);      //Устанавливает движение игрока вправо
        else if (Utils.getScreenWidth() / 2 > motionEvent.getX(motionEvent.findPointerIndex(fingersId.get(0))))
            player.setMove(Player.MOVE_LEFT);       //Устанавливает движение игрока влево
    }
}
