package ru.pb.gblibgdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Anim {
    private Texture img;
    private Animation<TextureRegion> anim;
    private float time;
    private float frameDuration;



    private Map<Movable.Actions, Animation<TextureRegion>> animationsMap = new HashMap<>();

    public Anim(String image_name, float frameDuration) {
        this.frameDuration = frameDuration;
        img = new Texture(image_name);
    }


    public void initializeAction(Movable.Actions action, int start, int end, Animation.PlayMode playMode) {
        initializeAction(action, start, end, playMode, false, false);

    }
    public void initializeAction(Movable.Actions action, int start, int end, Animation.PlayMode playMode, boolean flipX, boolean flipY) {
        TextureRegion region0 = new TextureRegion(img);
        TextureRegion[][] regions = region0.split(80, 80);
        TextureRegion[] regions1 = new TextureRegion[regions.length * regions[1].length];
        int c = 0;
        for (int i = 0; i < regions.length; i++) {
            for (int j = 0; j < regions[1].length; j++) {
                regions1[c] = regions[i][j];
                c++;
            }
        }
        anim = new Animation<TextureRegion>(frameDuration, Arrays.copyOfRange(regions1, start, end));
        anim.setPlayMode(playMode);
        setFlip(flipX, flipY);
        animationsMap.put(action, anim);

    }



    public boolean setAction(Movable.Actions action) {
        if (animationsMap.get(action) == null)
            return false;
        anim = animationsMap.get(action);
        time = 0;
        return true;
    }

    private void setFlip(boolean x, boolean y) {
        for (TextureRegion keyFrame : anim.getKeyFrames()) {
            keyFrame.flip(x, y);
        }
    }

    public void addDeltaTime(float delta) {
        time += delta;
    }

    public TextureRegion getFrame() {
        return anim.getKeyFrame(this.time);
    }

    public void dispose() {
        img.dispose();
    }
}
