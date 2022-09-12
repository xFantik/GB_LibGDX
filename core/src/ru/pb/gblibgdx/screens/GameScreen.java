package ru.pb.gblibgdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.pb.gblibgdx.*;
import ru.pb.gblibgdx.Character;
import ru.pb.gblibgdx.anim.Images;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameScreen implements Screen {
    private final Main main;

    private final SpriteBatch batch;

    private final Character dino;
    private final Rectangle main_rectangle;
    private int mapWidth;

    private float dinoRegionWidth, dinoRegionHeight;


    private float dinoPerimeter;

    private final OrthographicCamera camera;

    private final OrthogonalTiledMapRenderer mapRenderer;


    private final Texture imgBG;
//    private final Texture imgKey;


    private final int[] bg;
    private final int[] l2;

    private final Physics physics;
    private final LogicProcessor logicProcessor;

    private final Body hero;
    private final Rectangle heroRect;


    private static final int heroSpeed = 27; //27
    // private static final float heroSpeedFactor = 1f;

    //    private static final int heroSpeedJump = 80;
    private static final int heroJumpForce = 20000;
    private static final int forceInJump = 600;

    private final Music music;
    private final Map<SoundTag, Sound> sounds = new HashMap<>();

    public enum SoundTag {GAME_OVER, WIN, JUMP, GET_KEY}


//    private final int[] layer_key = new int[1];

    private final Images images;


    public GameScreen(Main main) {
        this.main = main;
        logicProcessor = new LogicProcessor();
        physics = new Physics(new MyContactListener(logicProcessor));


        batch = new SpriteBatch();
        dino = CharactersFactory.getGreenDino(main);
        main_rectangle = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        dino.setAction(Movable.Actions.IDLE);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        TiledMap map = new TmxMapLoader().load("map/map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        imgBG = new Texture("bg.png");
//        imgKey = new Texture("key.png");
//        imgCake = new Texture("cake.png");
        //map.getLayers().get("objects").getObjects().getByType(RectangleMapObject.class);// выбор объектов по типу
        RectangleMapObject t = (RectangleMapObject) map.getLayers().get("settings").getObjects().get("camera");
        camera.position.x = t.getRectangle().x * Physics.PPM;
        camera.position.y = t.getRectangle().y;

        bg = new int[2];
        bg[0] = map.getLayers().getIndex("bg");
        bg[1] = map.getLayers().getIndex("l1");
        l2 = new int[1];
        l2[0] = map.getLayers().getIndex("l2");


//        layer_key[0] = map.getLayers().getIndex("key");


        Array<RectangleMapObject> objects = map.getLayers().get("objects").getObjects().getByType(RectangleMapObject.class);

        for (RectangleMapObject object : objects) {
            physics.addObject(object);
        }


        for (RectangleMapObject rectangleMapObject : map.getLayers().get("dangers").getObjects().getByType(RectangleMapObject.class)) {
            physics.addSensor(rectangleMapObject, "damage");
        }


        for (RectangleMapObject rectangleMapObject : map.getLayers().get("items").getObjects().getByType(RectangleMapObject.class)) {
            logicProcessor.addItem(rectangleMapObject);
            physics.addSensor(rectangleMapObject);
        }


        RectangleMapObject h = (RectangleMapObject) map.getLayers().get("settings").getObjects().get("hero");
        heroRect = h.getRectangle();

        dinoRegionWidth = heroRect.width;
        dinoRegionHeight = dino.getFrame().getRegionHeight() * heroRect.width / dino.getFrame().getRegionWidth();
        dinoPerimeter = dinoRegionHeight + dinoRegionWidth;


        hero = physics.addObject(h, (RectangleMapObject) map.getLayers().get("settings").getObjects().get("hero_bottom"));
        hero.setFixedRotation(true);
        hero.setGravityScale(13);

        images = new Images();


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

        updateCamera();

        mapRenderer.setView(camera);

        mapRenderer.render(bg);

//        mapRenderer.render();




        handleLogic();

        batch.draw(dino.getFrame(), heroRect.x * Physics.PPM, heroRect.y * Physics.PPM, dinoRegionWidth, dinoRegionHeight);
        batch.end();


        mapRenderer.render(l2);


        physics.step();
        physics.debugDraw(camera);


        handleControls();

    }

    private void updateCamera() {
        camera.position.x = heroRect.x * Physics.PPM;
        if (camera.position.x - main_rectangle.width / 2 < main_rectangle.x)
            camera.position.x = main_rectangle.width / 2;
        else if (camera.position.x > mapWidth - main_rectangle.width / 2)
            camera.position.x = mapWidth - main_rectangle.width / 2;

        camera.update();

        batch.setProjectionMatrix(camera.combined);
    }

    private void handleLogic() {
        dino.move(Gdx.graphics.getDeltaTime());

        heroRect.x = hero.getPosition().x - heroRect.width / 2 / Physics.PPM;
        heroRect.y = hero.getPosition().y - heroRect.height / 2 / Physics.PPM;


        if (logicProcessor.soundToPlay != null) {
            sounds.get(logicProcessor.soundToPlay).play(0.5f);
            logicProcessor.soundToPlay = null;

        }

        if (!logicProcessor.isAlive()) {
            dinoRegionHeight = heroRect.height;
            dinoRegionWidth = dinoPerimeter - dinoRegionHeight;
            dino.setAction(Movable.Actions.DEAD);
            music.stop();
        }


        if (logicProcessor.isBoxOpen) {
            music.stop();
        }


        Iterator<LogicProcessor.Item> iterator = logicProcessor.iterator();
        while (iterator.hasNext()) {
            LogicProcessor.Item item = iterator.next();
            if (item.type == LogicProcessor.Objects.BOX) {
                batch.draw(images.getBox(item.isUsed), item.rect.x, item.rect.y, item.rect.width, item.rect.height);
            } else if (item.type == LogicProcessor.Objects.KEY && !item.isUsed) {
                batch.draw(images.getKey(Gdx.graphics.getDeltaTime()), item.rect.x, item.rect.y, item.rect.width, item.rect.height);
            }
        }
        if (logicProcessor.hasKey()) {
            batch.draw(images.getKey(Gdx.graphics.getDeltaTime()), camera.position.x - camera.viewportWidth / 2 + 10, 300, 16, 16);

        }
    }

    private void handleControls() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && logicProcessor.isAlive()) {
            if (physics.contactListener.isOnGround()) {
                hero.setLinearVelocity(heroSpeed, hero.getLinearVelocity().y);
//                hero.applyForceToCenter(9000, 0, true);

            } else if (hero.getLinearVelocity().x < heroSpeed) {
                if (hero.getLinearVelocity().x < 0) {
                    hero.setLinearVelocity(0, hero.getLinearVelocity().y);
                }
                hero.applyForceToCenter(forceInJump, 0, true);
            }
            if (dino.getReverse()) {
                //Сдвигаем сенсор (лечение от застреваний)
                ((PolygonShape) hero.getFixtureList().get(2).getShape()).setAsBox((heroRect.width / 2 - 0.5f) / Physics.PPM, 5 / Physics.PPM, new Vector2(-1f / Physics.PPM, -heroRect.height / 2 / Physics.PPM), 0);
                dino.setReverse(false);
            }
            dino.setAction(Movable.Actions.RUN);

            if (hero.getLinearVelocity().x > heroSpeed) hero.getLinearVelocity().x = heroSpeed;

        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && logicProcessor.isAlive()) {
            if (physics.contactListener.isOnGround()) {
                hero.setLinearVelocity(-heroSpeed, hero.getLinearVelocity().y);
//                hero.applyForceToCenter(-9000, 0, true);
            } else if (hero.getLinearVelocity().x > -heroSpeed) {
                if (hero.getLinearVelocity().x > 0) {
                    hero.setLinearVelocity(0, hero.getLinearVelocity().y);
                }
                hero.applyForceToCenter(-forceInJump, 0, true);
            }

            if (!dino.getReverse()) {
                ((PolygonShape) hero.getFixtureList().get(2).getShape()).setAsBox((heroRect.width / 2 - 0.5f) / Physics.PPM, 5 / Physics.PPM, new Vector2(1f / Physics.PPM, -heroRect.height / 2 / Physics.PPM), 0);
                dino.setReverse(true);
            }
            dino.setAction(Movable.Actions.RUN);
            if (hero.getLinearVelocity().x < -heroSpeed) hero.getLinearVelocity().x = -heroSpeed;

        } else if (Gdx.input.isKeyPressed(Input.Keys.M)) {
            music.stop();

        } else if (logicProcessor.isAlive()) {
            if (physics.contactListener.isOnGround())
                dino.setAction(Movable.Actions.IDLE);
            else
                dino.setAction(Movable.Actions.JUMP);
        }


        if (physics.contactListener.isOnGround()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && logicProcessor.isAlive()) {
                // dino.jump();
                sounds.get(SoundTag.JUMP).play(0.5f);
                hero.applyForceToCenter(0, heroJumpForce, true);
//                hero.setLinearVelocity(hero.getLinearVelocity().x, heroJumpSpeed);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            dispose();
            music.stop();
            main.setScreen(new GameScreen(main));
            return;
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            dispose();
            main.setScreen(new MenuScreen(main));
            music.stop();
            return;
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
        imgBG.dispose();
        physics.dispose();
        music.dispose();
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
    }
}
