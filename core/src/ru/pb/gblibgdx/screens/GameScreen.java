package ru.pb.gblibgdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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

    private OrthographicCamera camera;

    private TiledMap map;

    private OrthogonalTiledMapRenderer mapRenderer;

    private Vector2 mapPosition;
    private Vector2 dinoPosition;



    public GameScreen(Main main) {
        this.main = main;
        batch = new SpriteBatch();
        dino = CharactersFactory.getGreenDino(main);
        main_rectangle = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        dino.setAction(Movable.Actions.IDLE);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = new TmxMapLoader().load("map/map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapPosition = new Vector2();

        map.getLayers().get("objects").getObjects().getByType(RectangleMapObject.class);// выбор объектов по типу
        RectangleMapObject t = (RectangleMapObject) map.getLayers().get("objects").getObjects().get("camera");
        t.getRectangle().getPosition(mapPosition);

        camera.position.x = t.getRectangle().x;
        camera.position.y = t.getRectangle().y;

        dinoPosition = new Vector2();
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



        new Rectangle(dino.getX(), dino.getY(), dinoRegionWidth, dinoRegionHeight).getPosition(dinoPosition);

        if (!main_rectangle.contains(dinoPosition)) {
            dino.undo(Gdx.graphics.getDeltaTime());
        }


        camera.position.x = dinoPosition.x;
//        camera.position.y = dinoPosition.y;

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        mapRenderer.setView(camera);
        mapRenderer.render();

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
        camera.viewportHeight = height;
        camera.viewportWidth = width;
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
