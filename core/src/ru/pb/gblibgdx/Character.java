package ru.pb.gblibgdx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.pb.gblibgdx.anim.Anim;

public class Character implements Movable {


    private Anim anim;
    private float speed;

    private Actions currentAction;
    private boolean reverse = false;

    private float time=0;


    public Character(Anim anim, float speed) {
        this.anim = anim;
        this.speed = speed;

        setAction(Actions.IDLE);

    }

    @Override
    public void setAction(Actions action) {
        if (currentAction != action) {
            anim.setAction(action);
            time = 0;
        }
        currentAction = action;
    }


    @Override
    public void move(float deltaTime) {
        time+=deltaTime;
    }



    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public boolean getReverse() {
        return reverse;
    }


    public TextureRegion getFrame() {
        if (anim.getFrame(time).isFlipX() && !reverse)
            anim.getFrame(time).flip(true, false);

        if (!anim.getFrame(time).isFlipX() && reverse)
            anim.getFrame(time).flip(true, false);


        return anim.getFrame(time);
    }

    public void dispose() {
        anim.dispose();
    }



}
