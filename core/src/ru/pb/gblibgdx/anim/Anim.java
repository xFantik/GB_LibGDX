package ru.pb.gblibgdx.anim;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.pb.gblibgdx.Movable;

import java.util.HashMap;
import java.util.Map;

public abstract class Anim {
    protected Animation<TextureRegion> anim;
    protected float animTime;
    protected float jumpTime;
    protected float defaultFrameDuration;
    protected int regionWidth, regionHeight;

    protected Map<Movable.Actions, Animation<TextureRegion>> animationsMap = new HashMap<>();

    public boolean setAction(Movable.Actions action) {
        if (animationsMap.get(action) == null)
            return false;
        anim = animationsMap.get(action);
        if (jumpTime >= 0)
            animTime = 0;
        return true;
    }


    public void addDeltaTime(float delta) {
        jumpTime += delta;
        animTime += delta;
    }

    public TextureRegion getFrame() {
        if (jumpTime < 0) {
            return animationsMap.get(Movable.Actions.JUMP).getKeyFrame(this.animTime);
        }
        return anim.getKeyFrame(this.animTime);
    }

    public void jump() {
        if (this.jumpTime >= 0) {
            animTime = 0;
            this.jumpTime = - animationsMap.get(Movable.Actions.JUMP).getAnimationDuration();
        }
    }
    public abstract void dispose();
}
