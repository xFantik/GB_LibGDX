package ru.pb.gblibgdx;


import com.badlogic.gdx.Gdx;import com.badlogic.gdx.Input;import com.badlogic.gdx.graphics.Color;import com.badlogic.gdx.graphics.Texture;import com.badlogic.gdx.graphics.g2d.Batch;import com.badlogic.gdx.scenes.scene2d.InputEvent;import com.badlogic.gdx.scenes.scene2d.InputListener;import com.badlogic.gdx.scenes.scene2d.actions.*;import com.badlogic.gdx.scenes.scene2d.ui.Image;import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;import static com.badlogic.gdx.scenes.scene2d.actions.Actions.addAction;

public class MyActor extends Image {
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(this.getColor());
        ((TextureRegionDrawable) getDrawable()).draw(batch, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public MyActor(Texture texture) {
        super(texture);
        //setBounds(getX(), getY(), getWidth(), getHeight());
    }
}