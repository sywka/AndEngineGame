package com.el.game.etc;

import com.el.game.objects.BonusC5;
import com.el.game.objects.BonusLife;
import com.el.game.objects.BonusPortal;
import com.el.game.objects.Enemy;
import com.el.game.objects.MovingCollisionObject;
import com.el.game.objects.Player;
import com.el.game.utils.Utils;
import com.el.game.utils.Vector2;

import org.andengine.engine.Engine;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;
import java.util.Random;

public class MovingCollisionObjectFactory {

    private ArrayList<MovingCollisionObject> movingObjectsList;
    //0 - 19 enemy
    //20 bonusLife
    //21 bonusC5
    //22 orangePortal
    //23 redPortal
    private Random random = new Random();
    private Player player;
    private boolean isThereObjects = false;     //Остались ли на экрана враги
    public float currentSpeed = 0.6f;

    public MovingCollisionObjectFactory(BaseGameActivity activity, Engine engine, Player player) {
        movingObjectsList = new ArrayList<MovingCollisionObject>();
        this.player = player;
        for (int i = 0; i < 20; i++)
            movingObjectsList.add(new Enemy(activity, engine, new Vector2(0, 0)));
        movingObjectsList.add(new BonusLife(activity, engine, new Vector2(0, 0)));
        movingObjectsList.add(new BonusC5(activity, engine, new Vector2(0, 0), movingObjectsList));

        BonusPortal orangePortal = new BonusPortal(activity, engine, new Vector2(0, 0), 0);
        BonusPortal redPortal = new BonusPortal(activity, engine, new Vector2(0, 0), 4);
        orangePortal.setAnotherPortal(redPortal);
        redPortal.setAnotherPortal(orangePortal);

        movingObjectsList.add(orangePortal);
        movingObjectsList.add(redPortal);

        for (MovingCollisionObject movingObject : movingObjectsList)
            movingObject.setPlayer(player);
    }


    public void Update() {
        if (player.getIsDead()) {
            UpdateUntilPlayerDead();
            ///Костыли
            {
                if (!movingObjectsList.get(23).getIsAlife())
                    movingObjectsList.get(23).getObjectSprite().setVisible(false);      //Фикс Вылета мёртвого портала на экран
                for(int i = 0; i < 20; i++)     //Фикс Вылета на экран живого врага, но проигрывающего анимацию смерти.
                    if (movingObjectsList.get(i).getObjectSprite().getCurrentTileIndex() > 3 &&
                            movingObjectsList.get(i).getIsAlife())
                        movingObjectsList.get(i).getObjectSprite().animate(new long[]{100, 100, 100, 100}, 0, 3, true);
            }
            ///Костыли
            return;
        }
        if (!movingObjectsList.get(movingObjectsList.size() - 5).getIsAlife() &&     //Обновляем летящие объекты только в том случае, если
            movingObjectsList.get(movingObjectsList.size() - 5).getObjectSprite().getCurrentTileIndex() < 4 &&//Мертвы 3 последних врага. Это спасёт от неоправданного
                !movingObjectsList.get(movingObjectsList.size() - 6).getIsAlife() &&  //Увеличения скорости
                    movingObjectsList.get(movingObjectsList.size() - 6).getObjectSprite().getCurrentTileIndex() < 4 &&//Также проверяем, не проигрывается ли у них
                        !movingObjectsList.get(movingObjectsList.size() - 7).getIsAlife() &&
                            movingObjectsList.get(movingObjectsList.size() - 7).getObjectSprite().getCurrentTileIndex() < 4){
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
        int i = random.nextInt(4);
        switch (i) {
            case 0:
                generateRandomPositionsForEnemy();
                break;
            case 1:
                generateStairPositionsForEnemy();
                break;
            case 2:
                generateTrianglePositionsForEnemy();
                break;
            case 3:
                generateBoxPositionsForEnemy();
                break;
        }
        //generateRandomPositionsForEnemy();
        //generateStairPositionsForEnemy();
        //generateTrianglePositionsForEnemy();
        //generateBoxPositionsForEnemy
        //if (random.nextInt(5) == 3) {
        if (!movingObjectsList.get(20).getIsAlife()) {      //Только если бонусный предмет мёртв, можем обновлять его
            if (random.nextInt(2) == 1) {
                movingObjectsList.get(20).setXSpeed(Utils.getPixelsOfPercentX(currentSpeed));
                movingObjectsList.get(20).setPositionX(-random.nextInt(20) * Utils.getPixelsOfPercentX(20) - Utils.getPixelsOfPercentX(20));

                movingObjectsList.get(21).setXSpeed(Utils.getPixelsOfPercentX(currentSpeed));
                movingObjectsList.get(21).setPositionX(-random.nextInt(20) * Utils.getPixelsOfPercentX(20) - Utils.getPixelsOfPercentX(20));
            } else {
                movingObjectsList.get(20).setXSpeed(Utils.getPixelsOfPercentX(-currentSpeed));
                movingObjectsList.get(20).setPositionX(random.nextInt(20) * Utils.getPixelsOfPercentX(20) + Utils.getPixelsOfPercentX(120));

                movingObjectsList.get(21).setXSpeed(Utils.getPixelsOfPercentX(-currentSpeed));
                movingObjectsList.get(21).setPositionX(random.nextInt(20) * Utils.getPixelsOfPercentX(20) + Utils.getPixelsOfPercentX(120));
            }
            movingObjectsList.get(20).setPositionY(random.nextInt(10) * Utils.getPixelsOfPercentY(10));
            movingObjectsList.get(20).setIsAlife(true);

            movingObjectsList.get(21).setPositionY(random.nextInt(10) * Utils.getPixelsOfPercentY(10));
            movingObjectsList.get(21).setIsAlife(true);

            if (movingObjectsList.get(22).getIsAlife() == false && movingObjectsList.get(23).getIsAlife() == false) {
                float newPortalsXPosition = random.nextInt(20) * Utils.getPixelsOfPercentX(20);

                movingObjectsList.get(22).setPositionX(Utils.getPixelsOfPercentX(-20) - newPortalsXPosition);
                movingObjectsList.get(23).setPositionX(Utils.getPixelsOfPercentX(120) + newPortalsXPosition);

                movingObjectsList.get(22).setPositionY(random.nextInt(10) * Utils.getPixelsOfPercentY(10));
                movingObjectsList.get(22).setIsAlife(true);
                movingObjectsList.get(22).setXSpeed(Utils.getPixelsOfPercentX(currentSpeed));

                movingObjectsList.get(23).setPositionY(random.nextInt(10) * Utils.getPixelsOfPercentY(10));
                movingObjectsList.get(23).setIsAlife(true);
                movingObjectsList.get(23).setXSpeed(Utils.getPixelsOfPercentX(-currentSpeed));

                movingObjectsList.get(23).getObjectSprite().setVisible(true);

                movingObjectsList.get(22).getObjectSprite().animate(new long[]{100, 100, 100, 100}, 0, 3, true);
                movingObjectsList.get(23).getObjectSprite().animate(new long[]{100, 100, 100, 100}, 4, 7, true);
            }
        }
    }

    /**
     * Генерирует позиции врагов перевёрнутым прямоугольником
     */
    public void generateTrianglePositionsForEnemy(){
        int orientation = random.nextInt(2), startPosition = 120;
        if (orientation == 0) {
            orientation = -1;
            startPosition = -20;
        }
        for (int i = 0; i < movingObjectsList.size() - 4; i++) {
            if (movingObjectsList.get(i).getIsAlife() || //Если враг жив, или проигрывает анимацию смерти, то не трогаем его
                    (!movingObjectsList.get(i).getIsAlife() && movingObjectsList.get(movingObjectsList.size() - 4).getObjectSprite().getCurrentTileIndex() > 3))
                continue;

            int z = i % 4;
            switch (z){
                case 0:
                    movingObjectsList.get(i).setPositionY(Utils.getPixelsOfPercentY(50));
                    movingObjectsList.get(i).setPositionX(i / 4 * Utils.getPixelsOfPercentX(60) * orientation + Utils.getPixelsOfPercentX(startPosition));
                    break;
                case 1:
                    movingObjectsList.get(i).setPositionY(Utils.getPixelsOfPercentY(10));
                    movingObjectsList.get(i).setPositionX((i / 4 * Utils.getPixelsOfPercentX(60) + 120) * orientation + Utils.getPixelsOfPercentX(startPosition));
                    break;
                case 2:
                    movingObjectsList.get(i).setPositionY(Utils.getPixelsOfPercentY(90));
                    movingObjectsList.get(i).setPositionX((i / 4 * Utils.getPixelsOfPercentX(60) + 120) * orientation + Utils.getPixelsOfPercentX(startPosition));
                    break;
                case 3:
                    movingObjectsList.get(i).setPositionY(Utils.getPixelsOfPercentY(50));
                    movingObjectsList.get(i).setPositionX((i / 4 * Utils.getPixelsOfPercentX(60) + 240) * orientation + Utils.getPixelsOfPercentX(startPosition));
                    break;
            }

            //movingObjectsList.get(i).setPositionX(i / 3 * Utils.getPixelsOfPercentX(20) * orientation + Utils.getPixelsOfPercentX(startPosition));

            movingObjectsList.get(i).setXSpeed(Utils.getPixelsOfPercentX(-currentSpeed * orientation));
            movingObjectsList.get(i).setIsAlife(true);
            movingObjectsList.get(i).getObjectSprite().animate(new long[]{100, 100, 100, 100}, 0, 3, true);
        }
    }


    /**
     * Генерирует позиции врагов прямоугольником
     */
    public void generateBoxPositionsForEnemy(){
        int orientation = random.nextInt(2), startPosition = 120;
        float x18 = 0, y18 = 0;
        if (orientation == 0) {
            orientation = -1;
            startPosition = -20;
        }
        for (int i = 0; i < movingObjectsList.size() - 4; i++) {
            if (movingObjectsList.get(i).getIsAlife() || //Если враг жив, или проигрывает анимацию смерти, то не трогаем его
                    (!movingObjectsList.get(i).getIsAlife() && movingObjectsList.get(movingObjectsList.size() - 4).getObjectSprite().getCurrentTileIndex() > 3))
                continue;
            movingObjectsList.get(i).setPositionY(Utils.getPixelsOfPercentY((10 + i % 3 * 40)));
            movingObjectsList.get(i).setXSpeed(Utils.getPixelsOfPercentX(-currentSpeed * orientation));
            movingObjectsList.get(i).setPositionX(i / 3 * Utils.getPixelsOfPercentX(20) * orientation + Utils.getPixelsOfPercentX(startPosition));
            if (i == 9){
                y18 = movingObjectsList.get(i).getPositionY();
                startPosition += orientation * 40;
                x18 = i / 3 * Utils.getPixelsOfPercentX(20) * orientation + Utils.getPixelsOfPercentX(startPosition);
                movingObjectsList.get(i).setPositionY(Utils.getPixelsOfPercentY((50)));
                movingObjectsList.get(i).setPositionX(i / 3 * Utils.getPixelsOfPercentX(20) * orientation + Utils.getPixelsOfPercentX(startPosition - orientation * 30));
            }
            if (i == 18) {
                movingObjectsList.get(i).setPositionY(y18);
                movingObjectsList.get(i).setPositionX(x18);
            }
            movingObjectsList.get(i).setIsAlife(true);
            movingObjectsList.get(i).getObjectSprite().animate(new long[]{100, 100, 100, 100}, 0, 3, true);
        }
    }

    /**
     * Генерирует позицию для врагов "лесенкой"
     */
    public void generateStairPositionsForEnemy() {
        int orientation = random.nextInt(2), startPosition = 120;
        if (orientation == 0) {
            orientation = -1;
            startPosition = -20;
        }
        for (int i = 0; i < movingObjectsList.size() - 4; i++) {
            if (movingObjectsList.get(i).getIsAlife() || //Если враг жив, или проигрывает анимацию смерти, то не трогаем его
                    (!movingObjectsList.get(i).getIsAlife() && movingObjectsList.get(movingObjectsList.size() - 4).getObjectSprite().getCurrentTileIndex() > 3))
                continue;
            movingObjectsList.get(i).setPositionY(Utils.getPixelsOfPercentY((i % 3 * 40)));
            movingObjectsList.get(i).setXSpeed(Utils.getPixelsOfPercentX(-currentSpeed * orientation));
            movingObjectsList.get(i).setPositionX(i * Utils.getPixelsOfPercentX(20) * orientation + Utils.getPixelsOfPercentX(startPosition) - Utils.getPixelsOfPercentX(i % 3 * 10) * orientation);
            movingObjectsList.get(i).setIsAlife(true);
            movingObjectsList.get(i).getObjectSprite().animate(new long[]{100, 100, 100, 100}, 0, 3, true);
        }
    }


    /**
     * Генерирует позицию для врага по генератору случайных чисел
     */
    public void generateRandomPositionsForEnemy(){
        for (int i = 0; i < movingObjectsList.size() - 4; i++) {
            if (movingObjectsList.get(i).getIsAlife() || //Если враг жив, или проигрывает анимацию смерти, то не трогаем его
                    (!movingObjectsList.get(i).getIsAlife() && movingObjectsList.get(movingObjectsList.size() - 4).getObjectSprite().getCurrentTileIndex() > 3))
                continue;
            movingObjectsList.get(i).setPositionY(random.nextInt(10) * Utils.getPixelsOfPercentY(10));
            if (random.nextInt(2) == 1) {
                movingObjectsList.get(i).setXSpeed(Utils.getPixelsOfPercentX(-currentSpeed));
                movingObjectsList.get(i).setPositionX(i * Utils.getPixelsOfPercentX(20) + Utils.getPixelsOfPercentX(120));
            } else {
                movingObjectsList.get(i).setXSpeed(Utils.getPixelsOfPercentX(currentSpeed));
                movingObjectsList.get(i).setPositionX(-i * Utils.getPixelsOfPercentX(20) - Utils.getPixelsOfPercentX(20));
            }
            movingObjectsList.get(i).setIsAlife(true);
            movingObjectsList.get(i).getObjectSprite().animate(new long[]{100, 100, 100, 100}, 0, 3, true);
        }
    }

    public ArrayList<MovingCollisionObject> getMovingObjectsList() {
        return movingObjectsList;
    }

    //public BonusLife getBonusLife(){ return bonusLife; }
}
