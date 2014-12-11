package com.el.game.ui;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.LayoutGameActivity;

import java.util.ArrayList;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.el.game.R;
import com.el.game.etc.MovingCollisionObjectFactory;
import com.el.game.objects.MovingCollisionObject;
import com.el.game.ui.menu.ExitConfirmMenu;
import com.el.game.ui.menu.MainMenu;
import com.el.game.ui.menu.MenuWindowModel;
import com.el.game.utils.SoundControl;
import com.el.game.utils.Utils;
import com.el.game.objects.GameObject;
import com.el.game.objects.Player;

import com.el.game.utils.Vector2;

public class GameActivity extends LayoutGameActivity implements SensorEventListener, IOnSceneTouchListener, OnResumeMainMenu {

    private List<GameObject> objectList;                //Список игровых объектов
    private List<Integer> fingersId;                    //Список Id пальцев (необходим для корректного мультитача)
    private MovingCollisionObjectFactory movingCollisionObjectFactory;
    private Player player;                              //Объект игрока
    private SensorManager sensorManager;                //Менеджер сенсора
    private final float accelerometerYCencity = 1.0f;        //Чувствительность акселлерометра по OY
    private final float accelerometerMaxYCencity = 3.0f;        //Чувствительность акселлерометра по OY
    private float zRotation = 0;

    private boolean isAdaptiveAccelerometr = true;

    private int startLandscapeOrientation;

    private MainMenu mainMenu;
    private ControlButton controlButton;
    private SoundControl soundControl;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_game;
    }

    @Override
    protected int getRenderSurfaceViewID() {
        return R.id.game_field;
    }

    @Override
    protected void onSetContentView() {
        super.onSetContentView();
        soundControl = SoundControl.getInstance(getApplicationContext(), getMusicManager());
        mainMenu = new MainMenu(this, (FrameLayout) findViewById(R.id.activity_content), MainMenu.Modification.RESUME_MENU, this);
        controlButton = (ControlButton) findViewById(R.id.button_control);
        ((Button) findViewById(R.id.button_menu)).setOnClickListener(new OnButtonClick() {
            @Override
            public void onClick(Button button, View view) {
                showMainMenu();
            }
        });
    }

    /**
     * Инициализация движка
     */
    @Override
    public EngineOptions onCreateEngineOptions() {
        Utils.calculateResolution(this);
        startLandscapeOrientation = 1;
        fingersId = new ArrayList<>();
        Camera camera = new Camera(0, 0, Utils.getResolutionWidth(), Utils.getResolutionHeight());
        EngineOptions options = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
                new RatioResolutionPolicy(Utils.getScreenResolutionRatio(this)), camera);
        options.getAudioOptions().setNeedsMusic(true);
        return options;
    }

    /**
     * Инициализация игровых объектов
     */
    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) throws Exception {
        player = new Player(this, getEngine(), new Vector2(Utils.getPixelsOfPercentX(50), Utils.getPixelsOfPercentY(50)));       //Добавляем игрока
        controlButton.setPlayer(player);

        objectList = new ArrayList<>();           //Инициализируем массив игровых объектов
        objectList.add(player);                             //Добавляем к массиву игрока

        movingCollisionObjectFactory = new MovingCollisionObjectFactory(this, getEngine(), player);

        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);   //Определяем менеджер сенсора
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);         //Устанавливаем менеджер сенсора как работника с акселерометром

        soundControl.initBgMusicFromAsset("snd/background_music.mp3", true);

        onCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback onCreateSceneCallback) throws Exception {
        getEngine().registerUpdateHandler(new FPSLogger());

        final Scene scene = new Scene();        //Устанавливаем сцену

        for (GameObject ob : objectList)        //Привязываем все игровые объекты к сцене
            ob.attachTo(scene);

        for (MovingCollisionObject enemy : movingCollisionObjectFactory.getMovingObjectsList()) {
            enemy.attachTo(scene);
            scene.attachChild(enemy.getArrowSprite());
        }

        //scene.attachChild(enemyFactory.getBonusLife().getArrowSprite());
        //enemyFactory.getBonusLife().attachTo(scene);

        scene.setOnSceneTouchListener(this);    //Устанавливаем слушатель прикосновений на сцене

        soundControl.playBgMusic();
        onCreateSceneCallback.onCreateSceneFinished(scene);

        scene.registerUpdateHandler(new TimerHandler(0.015f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                for (GameObject ob : objectList)
                    ob.onUpdateState(0);
                movingCollisionObjectFactory.Update();
            }
        }));
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
        if (controlButton.getControl() == ControlButton.CONTROL_TOUCH)
            return;

        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                /**
                 * Помимо наклонов по ОY cледует учитывать наклоны по ОZ. Необходимый коэфициент наклона рассчитывается
                 * по формуле (1 - OZ / 10 ) / чувствительность экселероментра по ОY
                 */
                zRotation = (1 - Math.abs(sensorEvent.values[0]) / 10) / accelerometerYCencity;   //Устанавливаем погрешность OZ

                if (sensorEvent.values[1] < accelerometerYCencity - zRotation && sensorEvent.values[1] > -accelerometerYCencity + zRotation)
                    player.setMove(Player.IDLE);                //Если наклон не входит в промежуток, то не двигаем персонажа
                else {
                    if (isAdaptiveAccelerometr) {
                        if (Math.abs(sensorEvent.values[1]) > accelerometerYCencity - zRotation) {
                            if (Math.abs(sensorEvent.values[1]) > accelerometerMaxYCencity - zRotation)
                                player.setNewStep(1);
                            else
                                player.setNewStep(Math.abs(sensorEvent.values[1]) / (accelerometerMaxYCencity - zRotation));
                        }
                    }
                    if (getWindowManager().getDefaultDisplay().getRotation() == startLandscapeOrientation) {     //При стандартном повороте экрана
                        if (sensorEvent.values[1] > accelerometerYCencity - zRotation)
                            player.setMove(Player.MOVE_RIGHT);      //Двигаем в зависимости от наклона телефона
                        if (sensorEvent.values[1] < -accelerometerYCencity + zRotation)
                            player.setMove(Player.MOVE_LEFT);
                    } else {
                        if (sensorEvent.values[1] > accelerometerYCencity - zRotation)
                            player.setMove(Player.MOVE_LEFT);      //Двигаем в зависимости от наклона телефона
                        if (sensorEvent.values[1] < -accelerometerYCencity + zRotation)
                            player.setMove(Player.MOVE_RIGHT);
                    }
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
        if (controlButton.getControl() == ControlButton.CONTROL_ACCELEROMETER)
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
            case MotionEvent.ACTION_MOVE:
                checkMovement(motionEvent);
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

    @Override
    protected void onPause() {
        super.onPause();
        if (!isGameLoaded()) return;

        getEngine().getScene().setIgnoreUpdate(true);
        soundControl.pauseBgMusic();
    }

    @Override
    protected synchronized void onResume() {
        super.onResume();
        soundControl.resumeBgMusic();
        showMainMenu();
    }

    @Override
    public void resume() {
        getEngine().getScene().setIgnoreUpdate(false);
        soundControl.setVolume(SoundControl.Volume.FULL_VOLUME);
    }

    private void pause() {
        if (isGameLoaded()) {
            getEngine().getScene().setIgnoreUpdate(true);
            soundControl.setVolume(SoundControl.Volume.ONE_FIFTH_VOLUME);
        }
    }

    private void showMainMenu() {
        if (isGameLoaded() && MenuWindowModel.getBackListMenu().size() <= 1) {
            pause();
            mainMenu.showWindow(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (MenuWindowModel.getBackListMenu().isEmpty()) {
                pause();
                new ExitConfirmMenu(this, (FrameLayout) findViewById(R.id.activity_content), ExitConfirmMenu.Modification.RESUME_MENU, this)
                        .showWindow(true);
            } else
                MainMenu.onBackPressed();

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainMenu.getBackListMenu().clear();
        System.exit(0);
    }
}
