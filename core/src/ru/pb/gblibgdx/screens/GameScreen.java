package ru.pb.gblibgdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.pb.gblibgdx.*;
import ru.pb.gblibgdx.Character;

public class GameScreen implements Screen {
    private Main main;

    private SpriteBatch batch;

    private Character dino;
    private Rectangle main_rectangle;

    private float dinoRegionWidth, dinoRegionHeight;

    private float dinoFactor=0.3f;


    public GameScreen(Main main) {
        this.main = main;
        batch = new SpriteBatch();
        dino = CharactersFactory.getGreenDino(main);
        main_rectangle = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        dino.setAction(Movable.Actions.IDLE);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0.3f, 0, 1);

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dino.setReverse(false);
            dino.setAction(Movable.Actions.RUN);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dino.setReverse(true);
            dino.setAction(Movable.Actions.RUN);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            dino.setAction(Movable.Actions.DEAD);
        } else {
            dino.setAction(Movable.Actions.IDLE);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            dino.jump();

        dino.move(Gdx.graphics.getDeltaTime());

        dinoRegionWidth = dino.getFrame().getRegionWidth() * dinoFactor;
        dinoRegionHeight = dino.getFrame().getRegionHeight() * dinoFactor;




        if (!main_rectangle.contains(new Rectangle(dino.getX(), dino.getY(), dinoRegionWidth, dinoRegionHeight))) {
            dino.undo(Gdx.graphics.getDeltaTime());
        }

        batch.begin();
        batch.draw(dino.getFrame(), dino.getX(), dino.getY(), dinoRegionWidth, dinoRegionHeight);

        batch.end();


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            dispose();
            main.setScreen(new MenuScreen(main));

        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
