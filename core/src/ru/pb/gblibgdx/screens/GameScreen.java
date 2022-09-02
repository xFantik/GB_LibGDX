package ru.pb.gblibgdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.pb.gblibgdx.*;
import ru.pb.gblibgdx.Character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameScreen implements Screen {
    private Main main;

    private SpriteBatch batch;

    private Character dino;
    private Rectangle main_rectangle;
    private int mapWidth;

    private float dinoRegionWidth, dinoRegionHeight;

    private float dinoFactor = 0.1f;

    private OrthographicCamera camera;

    private TiledMap map;

    private OrthogonalTiledMapRenderer mapRenderer;

    //  private Vector2 mapPosition;
    private Vector2 dinoPosition;


    private Texture imgBG;
    private Texture imgKey;
    private Texture imgCake;


    private final int[] bg;
    private final int[] l2;
    private Array<RectangleMapObject> objects;
    private Physics physics;
    private Body hero;
    private Rectangle heroRect;

    private Body car;
    private Rectangle carRect;
    private Texture carImg;
    private TextureRegion carTexture;

    private static final int heroSpeed = 100;
    private static final float heroSpeedFactor = 0.7f;

    //    private static final int heroSpeedJump = 80;
    private static final int heroJumpSpeed = 160;
    private static final int forceInJump = 200000;

    private final Music music;
    private final Map<SoundTag, Sound> sounds = new HashMap<>();

    private enum SoundTag {GAME_OVER, WIN, JUMP, GET_KEY}


    private boolean hasHey = false;
    private boolean isBoxOpen = false;
    private Rectangle keyRect;
    private Rectangle boxRect;
    private final int[] layer_openBox = new int[1];
    private final int[] layer_key = new int[1];

    private final ArrayList<Rectangle> dangersRect = new ArrayList<>();
    private boolean heroDead = false;

    public static boolean heroOnGround;


    public GameScreen(Main main) {
        this.main = main;
        batch = new SpriteBatch();
        dino = CharactersFactory.getGreenDino(main);
        main_rectangle = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        dino.setAction(Movable.Actions.IDLE);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = new TmxMapLoader().load("map/map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        imgBG = new Texture("bg.png");
        imgKey = new Texture("key.png");
        imgCake = new Texture("cake.png");
        //map.getLayers().get("objects").getObjects().getByType(RectangleMapObject.class);// выбор объектов по типу
        RectangleMapObject t = (RectangleMapObject) map.getLayers().get("items").getObjects().get("camera");
        camera.position.x = t.getRectangle().x * Physics.PPM;
        camera.position.y = t.getRectangle().y;

        dinoPosition = new Vector2();

        bg = new int[2];
        bg[0] = map.getLayers().getIndex("bg");
        bg[1] = map.getLayers().getIndex("l1");
        l2 = new int[1];
        l2[0] = map.getLayers().getIndex("l2");

        layer_openBox[0] = map.getLayers().getIndex("open_box");
        layer_key[0] = map.getLayers().getIndex("key");


        objects = map.getLayers().get("objects").getObjects().getByType(RectangleMapObject.class);

        physics = new Physics();

        for (RectangleMapObject object : objects) {
            physics.addObject(object);
        }


        Array<RectangleMapObject> dang = map.getLayers().get("dangers").getObjects().getByType(RectangleMapObject.class);

        for (RectangleMapObject rectangleMapObject : dang) {
            Rectangle r = rectangleMapObject.getRectangle();
            r.x /= Physics.PPM;
            r.y /= Physics.PPM;
            r.width /= Physics.PPM;
            r.height /= Physics.PPM;
            dangersRect.add(r);
        }

        RectangleMapObject h = (RectangleMapObject) map.getLayers().get("items").getObjects().get("hero");
        heroRect = h.getRectangle();

        hero = physics.addObject(h, (RectangleMapObject) map.getLayers().get("items").getObjects().get("hero_bottom"));
        hero.setFixedRotation(true);
        hero.setGravityScale(13);


        car = physics.addObject((RectangleMapObject) map.getLayers().get("items").getObjects().get("car"));
        carRect = ((RectangleMapObject) map.getLayers().get("items").getObjects().get("car")).getRectangle();
        carImg = new Texture("car.png");
        carTexture = new TextureRegion(carImg);

        keyRect = ((RectangleMapObject) map.getLayers().get("items").getObjects().get("key")).getRectangle();
        boxRect = ((RectangleMapObject) map.getLayers().get("items").getObjects().get("box")).getRectangle();

        MapProperties prop = map.getProperties();
        mapWidth = prop.get("width", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        mapWidth *= tilePixelWidth;


        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/mario/super-mario-saundtrek.mp3"));
        music.setLooping(true);
        music.setVolume(0.2f);
        music.play();


        sounds.put(SoundTag.GAME_OVER, Gdx.audio.newSound(Gdx.files.internal("sounds/mario/super-mario-game-over.mp3")));
        sounds.put(SoundTag.WIN, Gdx.audio.newSound(Gdx.files.internal("sounds/mario/win.mp3")));
        sounds.put(SoundTag.GET_KEY, Gdx.audio.newSound(Gdx.files.internal("sounds/mario/get_key.mp3")));
        sounds.put(SoundTag.JUMP, Gdx.audio.newSound(Gdx.files.internal("sounds/mario/jump.mp3")));

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.4f, 0.5f, 0.5f, 1f);

        batch.begin();
        batch.draw(imgBG, camera.position.x - main_rectangle.width / 2, camera.position.y - main_rectangle.height / 2, main_rectangle.width, main_rectangle.height);


        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !heroDead) {
            if (heroOnGround)
                hero.setLinearVelocity(heroSpeed, hero.getLinearVelocity().y);
            else if (hero.getLinearVelocity().x < heroSpeed * heroSpeedFactor) {
                if (hero.getLinearVelocity().x < 0) {
                    hero.setLinearVelocity(0, hero.getLinearVelocity().y);
                }
                hero.applyForceToCenter(forceInJump, 0, true);
            }
            dino.setReverse(false);
            dino.setAction(Movable.Actions.RUN);


        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !heroDead) {
            if (heroOnGround)
                hero.setLinearVelocity(-heroSpeed, hero.getLinearVelocity().y);

            else if (hero.getLinearVelocity().x > -heroSpeed * heroSpeedFactor) {
                if (hero.getLinearVelocity().x > 0) {
                    hero.setLinearVelocity(0, hero.getLinearVelocity().y);
                }
                hero.applyForceToCenter(-forceInJump, 0, true);
            }

            dino.setReverse(true);
            dino.setAction(Movable.Actions.RUN);

        } else if (Gdx.input.isKeyPressed(Input.Keys.M)) {
            music.stop();

        } else if (!heroDead) {
            dino.setAction(Movable.Actions.IDLE);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !heroDead) {
            if (heroOnGround) {
                dino.jump();
                sounds.get(SoundTag.JUMP).play(0.5f);
//                hero.applyForceToCenter(0, 6000, false);
                hero.setLinearVelocity(hero.getLinearVelocity().x * heroSpeedFactor, heroJumpSpeed);
                heroOnGround = false;
                // hero.getFixtureList().get(0).setFriction(0);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            dispose();
            music.stop();
            main.setScreen(new GameScreen(main));
        }


        dino.move(Gdx.graphics.getDeltaTime());
        dinoRegionWidth = dino.getFrame().getRegionWidth() * dinoFactor;
        dinoRegionHeight = dino.getFrame().getRegionHeight() * dinoFactor;


        camera.position.x = heroRect.x * Physics.PPM;
        if (camera.position.x - main_rectangle.width / 2 < main_rectangle.x)
            camera.position.x = main_rectangle.width / 2;
        else if (camera.position.x > mapWidth - main_rectangle.width / 2)
            camera.position.x = mapWidth - main_rectangle.width / 2;


        camera.update();
        batch.setProjectionMatrix(camera.combined);
        mapRenderer.setView(camera);

        mapRenderer.render(bg);

//        mapRenderer.render();


        heroRect.x = hero.getPosition().x - heroRect.width / 2;
        heroRect.y = hero.getPosition().y - heroRect.height / 2;

        if (heroRect.contains(keyRect)) {
            if (!hasHey)
                sounds.get(SoundTag.GET_KEY).play(0.5f);
            hasHey = true;
        }
        if (heroRect.overlaps(boxRect) && hasHey) {
            if (!isBoxOpen) {
                sounds.get(SoundTag.WIN).play(0.5f);
                music.stop();
            }
            isBoxOpen = true;
            hasHey = false;
        }


        for (Rectangle rectangle : dangersRect) {
            if (rectangle.overlaps(heroRect)) {
                if (!heroDead)
                    sounds.get(SoundTag.GAME_OVER).play(0.5f);
                heroDead = true;
                dino.setAction(Movable.Actions.DEAD);
                music.stop();
            }
        }


        carRect.x = car.getPosition().x - carRect.width / 2;
        carRect.y = car.getPosition().y - carRect.height / 2;


        if (isBoxOpen) {
            mapRenderer.render(layer_openBox);

        }


        batch.draw(dino.getFrame(), heroRect.x * Physics.PPM, heroRect.y * Physics.PPM, dinoRegionWidth * Physics.PPM, dinoRegionHeight * Physics.PPM);
        if (hasHey) {
            if (dino.getReverse())
                batch.draw(imgKey, (heroRect.x + heroRect.width / 3) * Physics.PPM, (heroRect.y + heroRect.height / 3) * Physics.PPM);
            else
                batch.draw(imgKey, (heroRect.x + heroRect.width / 3 + imgKey.getWidth()) * Physics.PPM, (heroRect.y + heroRect.height / 3) * Physics.PPM, -imgKey.getWidth(), imgKey.getHeight());
        } else if (isBoxOpen) {
            if (dino.getReverse())
                batch.draw(imgCake, (heroRect.x - heroRect.width / 2 + 2) * Physics.PPM, (heroRect.y + 5) * Physics.PPM, imgCake.getWidth() / 1.5f, imgCake.getHeight() / 1.5f);
            else
                batch.draw(imgCake, (heroRect.x + heroRect.width - 6) * Physics.PPM, (heroRect.y + 5) * Physics.PPM, imgCake.getWidth() / 1.5f, imgCake.getHeight() / 1.5f);
        }
        batch.draw(carTexture, carRect.x * Physics.PPM, carRect.y * Physics.PPM, carRect.width / 2, carRect.height / 2, carRect.width, carRect.height, 1, 1, car.getAngle() * 57);
        batch.end();


        mapRenderer.render(l2);
        if (!hasHey)
            mapRenderer.render(layer_key);

        physics.step();
//        physics.debugDraw(camera);

//        ShapeRenderer sr = new ShapeRenderer();
//        sr.setProjectionMatrix(camera.combined);
//        sr.setColor(Color.BLUE);
//        sr.begin(ShapeRenderer.ShapeType.Line);
//
//        for (Rectangle object : dangersRect) {
//            Rectangle r = object;//.getRectangle();
//            sr.rect(r.x, r.y, r.getWidth(), r.getHeight());
//
//        }
//        sr.end();


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            dispose();
            main.setScreen(new MenuScreen(main));
            music.stop();
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
        carImg.dispose();
        imgBG.dispose();
        imgKey.dispose();
        imgCake.dispose();
    }
}
