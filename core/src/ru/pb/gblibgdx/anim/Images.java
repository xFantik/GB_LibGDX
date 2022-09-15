package ru.pb.gblibgdx.anim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import ru.pb.gblibgdx.LogicProcessor;
import ru.pb.gblibgdx.Movable;


public class Images implements Disposable {

    private float time;

    public void addDeltaTime(float deltaTime) {
        time += deltaTime;
    }

    Texture box;
    Texture boxOpen;


    AnimTexture animKey;
    AnimTexture animPortal;


    public Images() {
        box = new Texture("items/box.png");
        boxOpen = new Texture("items/box_open.png");


        animKey = new AnimTexture("items/key.png", 1 / 3f, 4, 1, true);
        animKey.initializeAction(Movable.Actions.IDLE, 0, 4, Animation.PlayMode.LOOP);


        animPortal = new AnimTexture("items/portal.png", 1 / 7f, 6, 1, true);
        animPortal.initializeAction(Movable.Actions.IDLE, 0, 6, Animation.PlayMode.LOOP_PINGPONG);


    }



    public TextureRegion getImage(LogicProcessor.Objects type) {
        switch (type) {
            case BOX_OPEN:
                return new TextureRegion(boxOpen);
            case BOX:
                return new TextureRegion(box);
            case KEY:
                return animKey.getFrame(time);
            case PORTAL:
                return animPortal.getFrame(time);

            default:
                return null;
        }
    }


    @Override
    public void dispose() {
        box.dispose();
        boxOpen.dispose();

        animPortal.dispose();
        animKey.dispose();
    }

}
