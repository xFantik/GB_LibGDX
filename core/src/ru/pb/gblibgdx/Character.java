package ru.pb.gblibgdx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.pb.gblibgdx.anim.Anim;

public class Character implements Movable {


    private Anim anim;
    private float speed;
    private float intX;
    private float intY;

    private Actions currentAction;
    private boolean reverse = false;



    public Character(Main main, Anim anim, float speed, float intX, float intY) {
        this.anim = anim;
        this.speed = speed;
        this.intX = intX;
        this.intY = intY;
        setAction(Actions.IDLE);


    }

    @Override
    public void setAction(Actions action) {
        if (currentAction != action)
            anim.setAction(action);
        currentAction = action;
    }

    @Override
    public void move(float deltaTime) {
        anim.addDeltaTime(deltaTime);
        switch (currentAction) {
            case RUN:
                if (!reverse)
                    intX += speed * deltaTime;
                else
                    intX -= speed * deltaTime;
                break;
        }
    }
    public void undo(float deltaTime){
        switch (currentAction) {
            case RUN:
                if (!reverse)
                    intX -= speed * deltaTime;
                else
                    intX += speed * deltaTime;
                break;
        }
    }

    public void jump(){
        anim.jump(-2);
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public boolean getReverse() {
        return reverse;
    }

    public float getX() {
        return intX;
    }

    public float getY() {
        return intY;
    }


    public TextureRegion getFrame() {
        if (anim.getFrame().isFlipX() && !reverse)
            anim.getFrame().flip(true, false);

        if (!anim.getFrame().isFlipX() && reverse)
            anim.getFrame().flip(true, false);


        return anim.getFrame();
    }

    public void dispose() {
        anim.dispose();
    }

    public void reset(int x, int y) {
        intX = x;
        intY = y;
    }

}
