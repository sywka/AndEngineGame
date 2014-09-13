package core;

import com.el.game.R;

import org.andengine.engine.Engine;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class Enemy extends CollisionObject {

    public Enemy(BaseGameActivity activity, Engine engine, float positionX, float positionY) {
        super(activity, engine, positionX, positionY, Utils.getPixelsOfPercentX(6), Utils.getPixelsOfPercentY(11));
    }

    @Override
    protected BitmapTextureAtlas getNewAtlas() {
        return new BitmapTextureAtlas(getActivity().getTextureManager(), 1024, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    }

    @Override
    protected TiledTextureRegion getNewRegion() {
        return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(getAtlas(), getActivity(), R.drawable.john, 0, 0, 8, 2);
    }

    @Override
    public void onUpdateState(float v) {
        super.onUpdateState(v);
    }

    @Override
    protected void onCollision(Player player) {
        player.die();
    }
}
