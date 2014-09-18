package com.el.game.etc;

import com.el.game.objects.BonusLife;
import com.el.game.objects.Enemy;
import com.el.game.objects.MovingCollisionObject;
import com.el.game.objects.Player;
import com.el.game.utils.Utils;
import com.el.game.utils.Vector2;

import org.andengine.engine.Engine;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;
import java.util.Random;

public class EnemyFactory {

    private ArrayList<MovingCollisionObject> movingObjectsList;
    //0 - 19 enemy
    //20 bonusLife
    private Random random = new Random();
    private Player player;
    private boolean isThereObjects = false;     //Остались ли на экрана враги
    public float currentSpeed = 0.6f;
    //private BonusLife bonusLife;

    public EnemyFactory(BaseGameActivity activity, Engine engine, Player player) {
        movingObjectsList = new ArrayList<MovingCollisionObject>();
        this.player = player;
        for (int i = 0; i < 20; i++)
            movingObjectsList.add(new Enemy(activity, engine, new Vector2(0, 0)));
        movingObjectsList.add(new BonusLife(activity, engine, new Vector2(0, 0)));
        for (MovingCollisionObject movingObject : movingObjectsList)
            movingObject.setPlayer(player);
        //bonusLife = new BonusLife();
       // bonusLife.setPlayer(player);
    }


    public void Update() {
        if (player.getIsDead()) {
            UpdateUntilPlayerDead();
            return;
        }
        if (!movingObjectsList.get(movingObjectsList.size() - 2).getIsAlife()) {
            if (currentSpeed < 0.8f)
                currentSpeed += 0.1f;
            else {
                if (currentSpeed < 1.0f)
                    currentSpeed += 0.05f;
                else
                    currentSpeed += 0.01f;
            }
            generateObjectsPositions();
        }
        for (MovingCollisionObject movingObject : movingObjectsList)
            movingObject.onUpdateState(0);
    }

    public void UpdateUntilPlayerDead() {
        isThereObjects = false;
        currentSpeed = 1.5f;
        for (MovingCollisionObject movingObject : movingObjectsList) {
            if ((movingObject.getPositionX() > Utils.getPixelsOfPercentX(120)) ||
                    (movingObject.getPositionX() < Utils.getPixelsOfPercentX(-20))) {
                movingObject.setIsAlife(false);
                movingObject.getArrowSprite().setVisible(false);
            }
            movingObject.setXSpeed(Utils.getPixelsOfPercentX(currentSpeed)
                    * movingObject.getXSpeed() / Math.abs(movingObject.getXSpeed()));
            movingObject.onUpdateState(0);
            if (!isThereObjects)
                isThereObjects = movingObject.getIsAlife();
        }
        if (isThereObjects == false && !player.getObjectSprite().isAnimationRunning()) {
            player.setPositionX(Utils.getPixelsOfPercentX(50));
            player.setPositionY(Utils.getPixelsOfPercentY(50));
            player.setIsDead(false);
            player.getObjectSprite().animate(new long[]{100, 100, 100, 100}, 0, 3, true);
            player.setFallSpeed(Math.abs(player.getFallSpeed()));
            player.getScoreHelper().resetScore();
            player.setCountLife(Player.DEFAULT_COUNT_LIFE);
            currentSpeed = 0.6f;
        }
    }

    public void generateObjectsPositions() {
        for (int i = 0; i < movingObjectsList.size() - 1; i++) {
            if (movingObjectsList.get(i).getIsAlife())
                return;
            movingObjectsList.get(i).setPositionY(random.nextInt(10) * Utils.getPixelsOfPercentY(10));
            movingObjectsList.get(i).setIsAlife(true);
            if (random.nextInt(2) == 1) {
                movingObjectsList.get(i).setXSpeed(Utils.getPixelsOfPercentX(-currentSpeed));
                movingObjectsList.get(i).setPositionX(i * Utils.getPixelsOfPercentX(20) + Utils.getPixelsOfPercentX(120));
            } else {
                movingObjectsList.get(i).setXSpeed(Utils.getPixelsOfPercentX(currentSpeed));
                movingObjectsList.get(i).setPositionX(-i * Utils.getPixelsOfPercentX(20) - Utils.getPixelsOfPercentX(20));
            }
            //if (random.nextInt(5) == 3) {
                if (random.nextInt(2) == 1) {
                    movingObjectsList.get(20).setXSpeed(Utils.getPixelsOfPercentX(currentSpeed));
                    movingObjectsList.get(20).setPositionX(-random.nextInt(20) * Utils.getPixelsOfPercentX(20) - Utils.getPixelsOfPercentX(20));
                }
                else{
                    movingObjectsList.get(20).setXSpeed(Utils.getPixelsOfPercentX(-currentSpeed));
                    movingObjectsList.get(20).setPositionX(random.nextInt(20) * Utils.getPixelsOfPercentX(20) + Utils.getPixelsOfPercentX(20));
                }
                movingObjectsList.get(20).setPositionY(random.nextInt(10) * Utils.getPixelsOfPercentY(10));
                movingObjectsList.get(20).setIsAlife(true);
            //}
        }
    }

    public ArrayList<MovingCollisionObject> getMovingObjectsList() {
        return movingObjectsList;
    }

    //public BonusLife getBonusLife(){ return bonusLife; }
}
