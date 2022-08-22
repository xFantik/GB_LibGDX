package ru.pb.gblibgdx.anim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.pb.gblibgdx.Movable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnimAtlas extends Anim{
    private final TextureAtlas atlas;


    public AnimAtlas(String atlasName, float frameDuration) {

        atlas = new TextureAtlas(atlasName);

        this.frameDuration = frameDuration;

//
//        this.regionHeight = img.getHeight() / imagesCountHeight;
//        this.regionWidth = img.getWidth() / imagesCountWidth;


    }

    public void initializeAction(Movable.Actions action, String name, Animation.PlayMode playMode) {

        anim = new Animation<TextureRegion>(frameDuration, atlas.findRegions(name));

        anim.setPlayMode(playMode);

        animationsMap.put(action, anim);

    }



    public void dispose() {
        atlas.dispose();
    }
}
