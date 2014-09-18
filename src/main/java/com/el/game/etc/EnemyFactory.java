package com.el.game.etc;

import com.el.game.objects.Enemy;
import com.el.game.objects.Player;
import com.el.game.utils.Utils;
import com.el.game.utils.Vector2;

import org.andengine.engine.Engine;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyFactory {

    private ArrayList<Enemy> enemyList;
    private Random random = new Random();
    private Player player;
    private boolean isThereEnemyes = false;     //Остались ли на экрана враги
    public float currentSpeed = 0.6f;

    public EnemyFactory(BaseGameActivity activity, Engine engine, Player player){
        enemyList = new ArrayList<Enemy>();
        this.player = player;
        for (int i = 0; i < 20; i++)
            enemyList.add(new Enemy(activity, engine, new Vector2(0, 0)));
        for(Enemy enemy: enemyList)
            enemy.setPlayer(player);
    }

    public void Update(){
        if (player.getIsDead()){
            UpdateUntilPlayerDead();
            return;
        }
        if (!enemyList.get(enemyList.size() - 1).getIsAlife()) {
            if (currentSpeed < 0.8f)
                currentSpeed += 0.1f;
            else{
                if (currentSpeed < 1.0f)
                    currentSpeed += 0.05f;
                else
                    currentSpeed += 0.01f;
            }

            GenerateEnemysPositions();
        }
        for(Enemy enemy: enemyList)
            enemy.onUpdateState(0);
    }

    public void UpdateUntilPlayerDead(){
        isThereEnemyes = false;
        for(Enemy enemy: enemyList){
            if ((enemy.getPositionX() > Utils.getPixelsOfPercentX(120)) ||
                    (enemy.getPositionX() < Utils.getPixelsOfPercentX(-20))) {
                enemy.setIsAlife(false);
                enemy.getArrowSprite().setVisible(false);
            }
            enemy.onUpdateState(0);
            if (!isThereEnemyes)
                isThereEnemyes = enemy.getIsAlife();
        }
        if (isThereEnemyes == false && !player.getSprite().isAnimationRunning()){
            player.setPositionX(Utils.getPixelsOfPercentX(50));
            player.setPositionY(Utils.getPixelsOfPercentY(50));
            player.setIsDead(false);
            player.getSprite().animate(new long[]{100, 100, 100, 100}, 0, 3, true);
            player.setFallSpeed(Math.abs(player.getFallSpeed()));
            player.getScoreHelper().resetScore();
            currentSpeed = 0.6f;
        }
    }

    public void GenerateEnemysPositions(){
        for (int i = 0; i < enemyList.size(); i++) {
            if (enemyList.get(i).getIsAlife())
                return;
            enemyList.get(i).setPositionY(random.nextInt(10) * Utils.getPixelsOfPercentY(10));
            enemyList.get(i).setIsAlife(true);
            if (random.nextInt(2) == 1){
                enemyList.get(i).setXSpeed(Utils.getPixelsOfPercentX(-currentSpeed));
                enemyList.get(i).setPositionX(i * Utils.getPixelsOfPercentX(20) + Utils.getPixelsOfPercentX(120));
            }
            else{
                enemyList.get(i).setXSpeed(Utils.getPixelsOfPercentX(currentSpeed));
                enemyList.get(i).setPositionX(- i * Utils.getPixelsOfPercentX(20) - Utils.getPixelsOfPercentX(20));
            }
        }
    }

    public ArrayList<Enemy> getEnemyList(){ return enemyList; }
}
