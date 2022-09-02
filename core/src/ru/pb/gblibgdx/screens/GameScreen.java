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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import org.w3c.dom.ls.LSOutput;
import ru.pb.gblibgdx.*;
import ru.pb.gblibgdx.Character;

public class GameScreen implements Screen {
    private Main main;

    private SpriteBatch batch;

    private Character dino;
    //private Rectangle main_rectangle;

    private float dinoRegionWidth, dinoRegionHeight;

    private float dinoFactor = 0.1f;

    private OrthographicCamera camera;

    private TiledMap map;

    private OrthogonalTiledMapRenderer mapRenderer;

    //  private Vector2 mapPosition;
    private Vector2 dinoPosition;

    private final int[] bg;
    private final int[] l1;
    private Array<RectangleMapObject> objects;
    private Physics physics;
    private Body hero;
    private Rectangle heroRect;

    private static final int heroSpeed = 100;
    private static final int heroJumpSpeed = 160;
    private static final int forceInJump = 400000;


    public GameScreen(Main main) {
        this.main = main;
        batch = new SpriteBatch();
        dino = CharactersFactory.getGreenDino(main);
//        main_rectangle = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        dino.setAction(Movable.Actions.IDLE);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = new TmxMapLoader().load("map/map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        //  mapPosition = new Vector2();

        //map.getLayers().get("objects").getObjects().getByType(RectangleMapObject.class);// выбор объектов по типу
        RectangleMapObject t = (RectangleMapObject) map.getLayers().get("camera").getObjects().get("camera");
        //  t.getRectangle().getPosition(mapPosition);

        camera.position.x = t.getRectangle().x;
        camera.position.y = t.getRectangle().y;

        dinoPosition = new Vector2();

        bg = new int[1];
        bg[0] = map.getLayers().getIndex("bg");
        l1 = new int[2];
        l1[0] = map.getLayers().getIndex("l1");
        l1[1] = map.getLayers().getIndex("l2");


        objects = map.getLayers().get("objects").getObjects().getByType(RectangleMapObject.class);

        physics = new Physics();

        for (RectangleMapObject object : objects) {
            physics.addObject(object);
        }


        RectangleMapObject h = (RectangleMapObject) map.getLayers().get("camera").getObjects().get("hero");
        hero = physics.addObject(h, (RectangleMapObject) map.getLayers().get("camera").getObjects().get("hero_bottom"));

        heroRect = h.getRectangle();
        hero.setFixedRotation(true);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0.3f, 0, 1);

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (isOnGround())
                hero.setLinearVelocity(heroSpeed, hero.getLinearVelocity().y);
            else if (hero.getLinearVelocity().x < heroSpeed)
                hero.applyForceToCenter(forceInJump, 0, true);

            dino.setReverse(false);
            dino.setAction(Movable.Actions.RUN);


        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (isOnGround())
                hero.setLinearVelocity(-heroSpeed, hero.getLinearVelocity().y);
            else if (hero.getLinearVelocity().x > -heroSpeed)
                hero.applyForceToCenter(-forceInJump, 0, true);


            dino.setReverse(true);
            dino.setAction(Movable.Actions.RUN);

        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            dino.setAction(Movable.Actions.DEAD);
        } else {
            dino.setAction(Movable.Actions.IDLE);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (isOnGround()) {
                dino.jump();
//                hero.applyForceToCenter(0, 6000, false);
                hero.setLinearVelocity(hero.getLinearVelocity().x, heroJumpSpeed);
                // hero.getFixtureList().get(0).setFriction(0);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
//            RectangleMapObject h = (RectangleMapObject) map.getLayers().get("camera").getObjects().get("hero");
//            heroRect = h.getRectangle();
        }


        dino.move(Gdx.graphics.getDeltaTime());

        dinoRegionWidth = dino.getFrame().getRegionWidth() * dinoFactor;
        dinoRegionHeight = dino.getFrame().getRegionHeight() * dinoFactor;


        new Rectangle(dino.getX(), dino.getY(), dinoRegionWidth, dinoRegionHeight).getPosition(dinoPosition);

//        if (!main_rectangle.contains(dinoPosition)) {
//            dino.undo(Gdx.graphics.getDeltaTime());
//        }


        camera.position.x = heroRect.x;
//        camera.position.y = dinoPosition.y;

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        mapRenderer.setView(camera);

        mapRenderer.render(bg);
        mapRenderer.render(l1);


        heroRect.x = hero.getPosition().x - heroRect.width / 2;
        heroRect.y = hero.getPosition().y - heroRect.height / 2;

        batch.begin();

        batch.draw(dino.getFrame(), heroRect.x, heroRect.y, dinoRegionWidth, dinoRegionHeight);
        batch.end();

        isOnGround();

        physics.step();
        physics.debugDraw(camera);

//        ShapeRenderer sr = new ShapeRenderer();
//        sr.setProjectionMatrix(camera.combined);
//        sr.setColor(Color.BLUE);
//        sr.begin(ShapeRenderer.ShapeType.Line);
//
//        for (RectangleMapObject object : objects) {
//            Rectangle r = object.getRectangle();
//            sr.rect(r.x, r.y, r.getWidth(), r.getHeight());
//
//        }
//        sr.end();


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            dispose();
            main.setScreen(new MenuScreen(main));

        }
    }

    private boolean isOnGround() {
        if (Math.abs(hero.getLinearVelocity().y) < 0.005f) {
//            System.out.println("on ground");
            return true;
        }
        return false;
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
