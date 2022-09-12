package ru.pb.gblibgdx.anim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import ru.pb.gblibgdx.Movable;

import java.util.ArrayList;

public class Images implements Disposable {

    ArrayList<Disposable>  disposables;

    Texture box;
    Texture boxOpen;
    Texture key;
    AnimTexture animKey;





    public Images (){
        disposables = new ArrayList<>();
        box = new Texture("items/box.png");
        disposables.add(box);
        boxOpen = new Texture("items/box_open.png");
        disposables.add(boxOpen);
        key = new Texture("items/box_open.png");

        animKey = new AnimTexture("items/key.png", 1/3f, 4, 1, true);
        animKey.initializeAction(Movable.Actions.IDLE, 0, 4, Animation.PlayMode.LOOP);


    }

    public Texture getBox(boolean isOpen){
        return isOpen?boxOpen:box;
    }
    public TextureRegion getKey(float deltaTime){
        animKey.addDeltaTime(deltaTime);
        return animKey.getFrame();

    }

    @Override
    public void dispose() {
        box.dispose();
        boxOpen.dispose();
        key.dispose();
    }

}
