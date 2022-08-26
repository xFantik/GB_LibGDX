package ru.pb.gblibgdx;

import com.badlogic.gdx.graphics.g2d.Animation;
import ru.pb.gblibgdx.anim.AnimAtlas;
import ru.pb.gblibgdx.anim.AnimTexture;

public class CharactersFactory {
//    private static Character cat;
    private static Character dino;
    private static Character greenDino;

//    public static Character getCat(Main main){
//        if (cat== null)    {
//            AnimTexture animCat;
//            animCat = new AnimTexture("anim_cat.png", 1 / 10f, 4,13);
//            animCat.initializeAction(Movable.Actions.GO, 0, 12, Animation.PlayMode.LOOP, true, false,false);
//            animCat.initializeAction(Movable.Actions.SIT, 12, 19, Animation.PlayMode.NORMAL, true, false,false);
//            animCat.initializeAction(Movable.Actions.STAND, 12, 19, Animation.PlayMode.REVERSED, true, false,false);
//            animCat.initializeAction(Movable.Actions.SIT_LEFT, 12, 19, Animation.PlayMode.NORMAL, false);
//            animCat.initializeAction(Movable.Actions.STAND_LEFT, 12, 19, Animation.PlayMode.REVERSED, false);
//            animCat.initializeAction(Movable.Actions.GO_LEFT, 0, 12,  Animation.PlayMode.LOOP,  false);
//
//            cat = new Character(main, animCat, 53, 0,2);
//        }
//        return cat;
//    }

    public static Character getDino(Main main){
        if (dino== null)    {
            AnimTexture anim;
            anim = new AnimTexture("dino.png", 1 / 10f, 8,7, true);
            anim.initializeAction(Movable.Actions.IDLE, 0, 15, Animation.PlayMode.LOOP);
            anim.initializeAction(Movable.Actions.RUN, 17, 25, Animation.PlayMode.LOOP);
            anim.initializeAction(Movable.Actions.DEAD, 25, 36, Animation.PlayMode.NORMAL);
            anim.initializeAction(Movable.Actions.JUMP, 41, 49, Animation.PlayMode.LOOP);

            dino = new Character(anim, 150, 0,2);

        }
        return dino;
    }
    public static Character getGreenDino(Main main){
        if (greenDino ==null){
            AnimAtlas anim = new AnimAtlas("atlas/green_dino.atlas", 1/10f);
            anim.initializeAction(Movable.Actions.IDLE, "Idle", Animation.PlayMode.LOOP);
            anim.initializeAction(Movable.Actions.RUN, "Run", Animation.PlayMode.LOOP);
            anim.initializeAction(Movable.Actions.DEAD, "Dead", Animation.PlayMode.NORMAL);
            anim.initializeAction(Movable.Actions.JUMP,"Jump", Animation.PlayMode.NORMAL, 1/10f);
            greenDino = new Character(anim, 150, 0,2);
        }
        return greenDino;
    }


}
