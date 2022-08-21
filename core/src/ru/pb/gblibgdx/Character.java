package ru.pb.gblibgdx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Character implements Movable {

    Anim anim;
    float speed;
    float intX;
    float intY;

    private int mapSizeX, mapSizeY;

    Actions currentAction;

    public Character(Anim anim, float speed, float intX, float intY, int mapSizeX, int mapSizeY) {
        this.anim = anim;
        this.speed = speed;
        this.intX = intX;
        this.intY = intY;
        setAction(Actions.GO);

        this.mapSizeX = mapSizeX;
        this.mapSizeY = mapSizeY;

    }

    @Override
    public void setAction(Actions action) {
        if (currentAction != action)
            anim.setAction(action);
        currentAction = action;
    }

    @Override
    public boolean move(float deltaTime) {
        anim.addDeltaTime(deltaTime);
        switch (currentAction) {
            case GO:
                if (intX + speed * deltaTime + 80 <= mapSizeX) {
                    intX += speed * deltaTime;
                } else {
                    return false;
                }
                break;
            case GO_LEFT:
                if (intX - speed * deltaTime >= 0) {
                    intX -= speed * deltaTime;
                } else {

                    return false;
                }
                break;
            case GO_UP:
                if (intY + speed * deltaTime + 80 <= mapSizeY) {
                    intY += speed * deltaTime;
                } else {

                    return false;
                }
                break;
            case GO_DOWN:
                if (intY - speed * deltaTime >= 0) {
                    intY -= speed * deltaTime;
                } else {
                    return false;
                }
                break;
        }
        return true;
    }

    public void reverse() {
        switch (currentAction) {
            case GO:
                currentAction = Actions.GO_LEFT;
                break;
            case GO_LEFT:
                currentAction = Actions.GO;
                break;
            case GO_UP:
                currentAction = Actions.GO_DOWN;
                break;
            case GO_DOWN:
                currentAction = Actions.GO_UP;
                break;
        }

        anim.setAction(currentAction);
    }

    public float getX() {
        return intX;
    }

    public float getY() {
        return intY;
    }


    public TextureRegion getFrame() {
        return anim.getFrame();
    }

    public void dispose() {
        anim.dispose();
    }

}
