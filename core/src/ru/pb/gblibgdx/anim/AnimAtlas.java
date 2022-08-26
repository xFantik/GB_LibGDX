package ru.pb.gblibgdx.anim;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.pb.gblibgdx.Movable;

public class AnimAtlas extends Anim {
    private final TextureAtlas atlas;


    public AnimAtlas(String atlasName, float defaultFrameDuration) {
        atlas = new TextureAtlas(atlasName);
        this.defaultFrameDuration = defaultFrameDuration;

    }

    public void initializeAction(Movable.Actions action, String name, Animation.PlayMode playMode) {
        initializeAction(action, name, playMode, defaultFrameDuration);
    }

    public void initializeAction(Movable.Actions action, String name, Animation.PlayMode playMode, float frameDuration) {

        anim = new Animation<TextureRegion>(frameDuration, atlas.findRegions(name));
        anim.setPlayMode(playMode);
        animationsMap.put(action, anim);
    }

    public void dispose() {
        atlas.dispose();
    }
}
