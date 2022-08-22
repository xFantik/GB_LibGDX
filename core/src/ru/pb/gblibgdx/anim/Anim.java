package ru.pb.gblibgdx.anim;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.pb.gblibgdx.Movable;

import java.util.HashMap;
import java.util.Map;

public abstract class Anim {
    protected Animation<TextureRegion> anim;
    protected float animTime;
    protected float frameDuration;
    protected int regionWidth, regionHeight;

    protected Map<Movable.Actions, Animation<TextureRegion>> animationsMap = new HashMap<>();

    public boolean setAction(Movable.Actions action) {
        if (animationsMap.get(action) == null)
            return false;
        anim = animationsMap.get(action);
        animTime = 0;
        return true;
    }


    public void addDeltaTime(float delta) {
        animTime += delta;
    }

    public TextureRegion getFrame() {
        return anim.getKeyFrame(this.animTime);
    }

    public abstract void dispose();


}
