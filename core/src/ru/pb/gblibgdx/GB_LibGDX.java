package ru.pb.gblibgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GB_LibGDX extends ApplicationAdapter {
    SpriteBatch batch;

    Character spirit;

    @Override
    public void create() {
        batch = new SpriteBatch();

        Anim anim_spirit;
        anim_spirit = new Anim("human.png", 1 / 60f);
        anim_spirit.initializeAction(Movable.Actions.GO, 0, 8, Animation.PlayMode.LOOP);
        anim_spirit.initializeAction(Movable.Actions.GO_UP, 60, 66, Animation.PlayMode.LOOP);
        anim_spirit.initializeAction(Movable.Actions.GO_DOWN, 66, 71, Animation.PlayMode.LOOP);
        anim_spirit.initializeAction(Movable.Actions.GO_LEFT, 0, 8,  Animation.PlayMode.LOOP, true, false);

        spirit = new Character(anim_spirit, 300, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0.3f, 0, 1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            spirit.setAction(Movable.Actions.GO_UP);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            spirit.setAction(Movable.Actions.GO);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            spirit.setAction(Movable.Actions.GO_LEFT);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            spirit.setAction(Movable.Actions.GO_DOWN);
        }
        if (!spirit.move(Gdx.graphics.getDeltaTime())){
            spirit.reverse();
            spirit.move(Gdx.graphics.getDeltaTime());
        }

        batch.begin();
        batch.draw(spirit.getFrame(), spirit.getX(), spirit.getY());
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        spirit.dispose();
    }
}
