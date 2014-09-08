package core;

import android.util.DisplayMetrics;

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

public class GameActivity extends BaseGameActivity {

    /**
     * Массив игровых объектов
     */
    private List<GameObject> objectList;
    private Player player;

    @Override
    public Engine onLoadEngine() {
        final Camera camera = new Camera(0, 0, getScreenWidth(), getScreenHeight());

        return new Engine(
                new EngineOptions(true, EngineOptions.ScreenOrientation.LANDSCAPE,
                        new RatioResolutionPolicy(getScreenResolutionRatio()), camera));
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

        scene.setOnSceneTouchListener(new Scene.IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene scene, TouchEvent touchEvent) {
                if (touchEvent.isActionDown()) {
                    if (getScreenWidth() / 2 < touchEvent.getMotionEvent().getX())
                        player.setMove(Player.MOVE_RIGHT);
                    else if (getScreenWidth() / 2 > touchEvent.getMotionEvent().getX())
                        player.setMove(Player.MOVE_LEFT);
                }
                if (touchEvent.isActionUp()) {
                    player.setMove(Player.NOT_MOVE_LEFT_RIGHT);
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

    private float getScreenResolutionRatio() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return ((float) dm.widthPixels) / ((float) dm.heightPixels);
    }

    private int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    private float getWidthFromPercent(float partOf) {
        return getScreenWidth() * partOf;
    }

    private float getHeightFromPercent(float partOf) {
        return getScreenHeight() * partOf;
    }
}
