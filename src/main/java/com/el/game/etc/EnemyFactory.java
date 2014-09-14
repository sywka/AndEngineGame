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

    public EnemyFactory(BaseGameActivity activity, Engine engine, Player player){
        enemyList = new ArrayList<Enemy>();
        for (int i = 0; i < 20; i++)
            enemyList.add(new Enemy(activity, engine, new Vector2(0, 0)));
        for(Enemy enemy: enemyList)
            enemy.setPlayer(player);
    }

    public void Update(){
        if (!enemyList.get(enemyList.size() - 4).getIsAlife())
            GenerateEnemysPositions();
        for(Enemy enemy: enemyList)
            enemy.onUpdateState(0);
    }

    public void GenerateEnemysPositions(){
        for (int i = 0; i < enemyList.size(); i++) {
            if (enemyList.get(i).getIsAlife())
                return;
            enemyList.get(i).setPositionY(random.nextInt(10) * Utils.getPixelsOfPercentY(10));
            enemyList.get(i).setIsAlife(true);
            if (random.nextInt(2) == 1){
                enemyList.get(i).setXSpeed(-1f);
                enemyList.get(i).setPositionX(i * Utils.getPixelsOfPercentX(20) + Utils.getPixelsOfPercentX(120));
            }
            else{
                enemyList.get(i).setXSpeed(1f);
                enemyList.get(i).setPositionX(- i * Utils.getPixelsOfPercentX(20) - Utils.getPixelsOfPercentX(20));
            }
        }
    }

    public ArrayList<Enemy> getEnemyList(){ return enemyList; }
}
