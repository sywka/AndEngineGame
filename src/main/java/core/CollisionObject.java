package core;

import org.andengine.engine.Engine;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

abstract public class CollisionObject extends GameObject {

    private Player player;

    public CollisionObject(BaseGameActivity activity, Engine engine, float positionX, float positionY, float width, float height) {
        super(activity, engine, positionX, positionY, width, height);
    }

    @Override
    abstract protected BitmapTextureAtlas getNewAtlas();

    @Override
    abstract protected TiledTextureRegion getNewRegion();

    @Override
    public void onUpdateState(float v) {
        super.onUpdateState(v);
        if (player != null && getSprite().collidesWith(player.getSprite()))
            onCollision(player);
    }

    abstract protected void onCollision(Player player);

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
