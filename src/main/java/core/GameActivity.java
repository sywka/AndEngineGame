package core;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;

public class GameActivity extends BaseGameActivity implements SensorEventListener, IOnSceneTouchListener {

    /**
     * Массив игровых объектов
     */
    private List<GameObject> objectList;
    /**
     * Объект игрока
     */
    private Player player;
    /**
     * Менеджер сенсора*
     */
    private SensorManager sensorManager;
    /**
     * Чувствительность акселерометра по ОY
     */
    private final int accelerometerYCencity = 2;
    /**
     * Коэфициент погрешности поворота по OZ
     */
    private double zRotation = 0;

    private List<Integer> fingersId;

    /**
     * Инициализация движка
     */
    @Override
    public Engine onLoadEngine() {
        fingersId = new ArrayList<Integer>();
        final Camera camera = new Camera(0, 0, Utils.getScreenWidth(), Utils.getScreenHeight());

        return new Engine(
                new EngineOptions(true, EngineOptions.ScreenOrientation.LANDSCAPE,
                        new RatioResolutionPolicy(Utils.getScreenResolutionRatio()), camera));
    }

    /**
     * Инициализация игровых объектов
     */
    @Override
    public void onLoadResources() {
        player = new Player(this, getEngine(), 0, 0);       //Добавляем игрока
        objectList = new ArrayList<GameObject>();           //Инициализируем массив игровых объектов
        objectList.add(player);                             //Добавляем к массиву игрока
//        sensorManager = (SensorManager) this.getSystemService(this.SENSOR_SERVICE); //Определяем менеджер сенсора
//        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                sensorManager.SENSOR_DELAY_GAME);   //Устанавливаем менеджер сенсора как работника с акселерометром
    }

    /**
     * Отрисовка игровых объектов на сцене
     *
     * @return готовая сцена
     */
    @Override
    public Scene onLoadScene() {
        getEngine().registerUpdateHandler(new FPSLogger());

        final Scene scene = new Scene();
        for (GameObject ob : objectList)
            ob.attachTo(scene);

        scene.setOnSceneTouchListener(this);

        scene.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float v) {
                for (GameObject ob : objectList)
                    ob.onUpdateState(v);
            }

            @Override
            public void reset() {
            }
        });

        return scene;
    }

    /**
     * Завершение загрузки
     */
    @Override
    public void onLoadComplete() {
    }

    /**
     * Изменение значения датчика движения сенсора.
     * Угол акселерометра определяется через sensorEvent.values[i], при
     * i == 1: Y, i == 0: Z, i == 2: X
     * По Оси ОУ - Телефон лежит на боку = 10. Повёрнут положен на спину\лицо = 0.
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                /**
                 * Помимо наклонов по ОY cледует учитывать наклоны по ОZ. Необходимый коэфициент наклона рассчитывается
                 * по формуле (1 - OZ / 10 ) / чувствительность экселероментра по ОY
                 */
                zRotation = (1 - sensorEvent.values[0] / 10) / accelerometerYCencity;
                if (sensorEvent.values[1] < accelerometerYCencity - zRotation && sensorEvent.values[1] > -accelerometerYCencity + zRotation)
                    player.setMove(Player.IDLE);
                else {
                    if (sensorEvent.values[1] > accelerometerYCencity - zRotation)
                        player.setMove(Player.MOVE_RIGHT);
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

    @Override
    public boolean onSceneTouchEvent(Scene scene, TouchEvent touchEvent) {
        MotionEvent motionEvent = touchEvent.getMotionEvent();
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                fingersId.add(motionEvent.getPointerId(motionEvent.getActionIndex()));
                checkMove(motionEvent);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                fingersId.remove(fingersId.indexOf(motionEvent.getPointerId(motionEvent.getActionIndex())));
                checkMove(motionEvent);
                break;
        }
        return true;
    }

    private void checkMove(MotionEvent motionEvent) {
        if (fingersId.isEmpty()) {
            player.setMove(Player.IDLE);
            return;
        }
        if (Utils.getScreenWidth() / 2 < motionEvent.getX(motionEvent.findPointerIndex(fingersId.get(0))))
            player.setMove(Player.MOVE_RIGHT);
        else if (Utils.getScreenWidth() / 2 > motionEvent.getX(motionEvent.findPointerIndex(fingersId.get(0))))
            player.setMove(Player.MOVE_LEFT);
    }
}
