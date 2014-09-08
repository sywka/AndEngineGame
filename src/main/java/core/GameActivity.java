package core;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GameActivity extends BaseGameActivity implements SensorEventListener {

    /**
     * Массив игровых объектов
     */
    private List<GameObject> objectList;
    private Player player;
    private SensorManager sensorManager;
    /** Чувствительность акселерометра по ОХ */
    private final int accelerometerXCencity = 2;

    @Override
    public Engine onLoadEngine() {
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
        player = new Player(this, getEngine(), 0, 0);
        objectList = new ArrayList<GameObject>();
        objectList.add(player);
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

        sensorManager = (SensorManager) this.getSystemService(this.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sensorManager.SENSOR_DELAY_GAME);

        scene.setOnSceneTouchListener(new Scene.IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene scene, TouchEvent touchEvent) {
                if (touchEvent.isActionDown()) {
                    if (Utils.getScreenWidth() / 2 < touchEvent.getMotionEvent().getX())
                        player.setMove(Player.MOVE_RIGHT);
                    else if (Utils.getScreenWidth() / 2 > touchEvent.getMotionEvent().getX())
                        player.setMove(Player.MOVE_LEFT);
                }
                if (touchEvent.isActionUp()) {
                    player.setMove(Player.IDLE);
                }
                return true;
            }
        });

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

    @Override
    public void onLoadComplete() {

    }

    /**
     *  Движение по акселлерометру
     *  угол акселерометра определяется через sensorEvent.values[i], при
     *  i == 1: Х, i == 0: Y, i == 2: Z
     *  */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                if(sensorEvent.values[1] < accelerometerXCencity && sensorEvent.values[1] > -accelerometerXCencity)
                    player.setMove(Player.IDLE);
                else{
                    if (sensorEvent.values[1] > 2)
                        player.setMove(Player.MOVE_RIGHT);
                    if (sensorEvent.values[1] < -2)
                        player.setMove(Player.MOVE_LEFT);
                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
