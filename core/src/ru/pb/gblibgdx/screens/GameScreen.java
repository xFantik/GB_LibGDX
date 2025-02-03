package ru.pb.gblibgdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.pb.gblibgdx.*;
import ru.pb.gblibgdx.Character;
import ru.pb.gblibgdx.anim.Images;

import java.util.Iterator;

public class GameScreen implements Screen {
    private final Main main;

    private final SpriteBatch batch;

    private final Character dino;
    private final Rectangle main_rectangle;
    private int mapWidth;

    private float dinoRegionWidth, dinoRegionHeight;


    private final Stage stage;
    private final float keyAnimTime = 1f;


    private final float dinoPerimeter;

    private float dinoToPortalAnimTime = 2.0f;


    private final OrthographicCamera camera;
    private final Array<Float> camerasLevels;
    private float newCameraPos;

    private final OrthogonalTiledMapRenderer mapRenderer;


    private final Texture imgBG;
    private final Texture imgKey;
    private final Texture imgDino;

    private final int[] bg;
    private final int[] l2;

    private final Physics physics;
    private final LogicProcessor logicProcessor;

    private final Body hero;
    private final Rectangle heroRect;


    private static final int heroSpeed = 27; //27
    private static final int heroJumpForce = 20000;
    private static final int forceInJump = 600;


    private final Sounds sounds;


    private final Images images;


    public GameScreen(Main main) {
        this.main = main;
        logicProcessor = new LogicProcessor();
        physics = new Physics(new MyContactListener(logicProcessor));

        sounds = Sounds.getInstance();
        sounds.playMusic(true);

        stage = new Stage();
        batch = new SpriteBatch();


        dino = CharactersFactory.getGreenDino(main);
        main_rectangle = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        dino.setAction(Movable.Actions.IDLE);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        TiledMap map = new TmxMapLoader().load("map/map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        imgBG = new Texture("bg.png");
        imgKey = new Texture(Gdx.files.internal("items/key.png"));
        imgDino = new Texture(Gdx.files.internal("items/dino.png"));


        camerasLevels = new Array<>();
        for (int i = 1; true; i++) {
            RectangleMapObject t = (RectangleMapObject) map.getLayers().get("settings").getObjects().get("camera_" + i);
            if (t == null)
                break;
            camerasLevels.add(t.getRectangle().y);
            if (i == 1) {
                camera.position.y = t.getRectangle().y;
//                camera.position.x = t.getRectangle().x;
            }
        }

        bg = new int[2];
        bg[0] = map.getLayers().getIndex("bg");
        bg[1] = map.getLayers().getIndex("l1");
        l2 = new int[1];
        l2[0] = map.getLayers().getIndex("l2");


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

        if (!logicProcessor.isGameOver()) {
            batch.draw(dino.getFrame(), heroRect.x * Physics.PPM, heroRect.y * Physics.PPM, dinoRegionWidth, dinoRegionHeight);
        }

        if (logicProcessor.portalCenter != null) {
            final MyActor myKeyActor = new MyActor(imgDino);
            stage.addActor(myKeyActor);

            myKeyActor.setPosition(heroRect.x * Physics.PPM - camera.position.x + camera.viewportWidth / 2, heroRect.y * Physics.PPM - camera.position.y + camera.viewportHeight / 2);

            myKeyActor.setRotation(0);

            MoveToAction mta = new MoveToAction();

            mta.setPosition(logicProcessor.portalCenter.x - camera.position.x + camera.viewportWidth / 2, logicProcessor.portalCenter.y - camera.position.y + camera.viewportHeight / 2);
            mta.setDuration(dinoToPortalAnimTime);
            ScaleByAction sba = new ScaleByAction();
            sba.setAmount(-1f);
            sba.setDuration(dinoToPortalAnimTime);

            RotateToAction rta = new RotateToAction();
            rta.setRotation(-360f);
            rta.setDuration(dinoToPortalAnimTime);
            ParallelAction pa = new ParallelAction(mta, sba, rta);
            myKeyActor.addAction(pa);
            sounds.playMusic(false);
            logicProcessor.portalCenter = null;
        }


        batch.end();

        mapRenderer.render(l2);

        physics.step();
//        physics.debugDraw(camera);
        handleControls();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();



        if (logicProcessor.isGameOver()) {
            dinoToPortalAnimTime -= Gdx.graphics.getDeltaTime();
            if (dinoToPortalAnimTime < 0) {

            }
        }

    }

    private void updateCamera() {
        camera.position.x = heroRect.x * Physics.PPM;
        if (camera.position.x - main_rectangle.width / 2 < main_rectangle.x)
            camera.position.x = main_rectangle.width / 2;
        else if (camera.position.x > mapWidth - main_rectangle.width / 2)
            camera.position.x = mapWidth - main_rectangle.width / 2;

        newCameraPos = findNearestCamera();
        if (camera.position.y - camera.viewportHeight / 3 > heroRect.y * Physics.PPM)
            camera.position.y = heroRect.y * Physics.PPM + camera.viewportHeight / 3;
        if (Math.abs(camera.position.y - newCameraPos) > 5) {
            if (camera.position.y > newCameraPos)
                camera.position.y -= 2;
            else camera.position.y += 2;
        }
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    private float findNearestCamera() {
        float dinoY = heroRect.y * Physics.PPM;

        float minDistance = Float.MAX_VALUE - 1000;
        float nearest = camerasLevels.get(0);

        for (int i = 0; i < camerasLevels.size; i++) {
            if (Math.abs(camerasLevels.get(i) - dinoY) < minDistance) {
                minDistance = Math.abs(camerasLevels.get(i) - dinoY);
                nearest = camerasLevels.get(i);
            }
        }
        return nearest;
    }


    private void handleLogic() {
        dino.move(Gdx.graphics.getDeltaTime());

        heroRect.x = hero.getPosition().x - heroRect.width / 2 / Physics.PPM;
        heroRect.y = hero.getPosition().y - heroRect.height / 2 / Physics.PPM;


        sounds.play(logicProcessor.soundToPlay);
        logicProcessor.soundToPlay = null;


        if (!logicProcessor.isAlive()) {
            dinoRegionHeight = heroRect.height;
            dinoRegionWidth = dinoPerimeter - dinoRegionHeight;
            dino.setAction(Movable.Actions.DEAD);
            sounds.playMusic(false);
        }


        images.addDeltaTime(Gdx.graphics.getDeltaTime());

        Iterator<Item> iterator = logicProcessor.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
//                batch.draw(images.getImage(item, Gdx.graphics.getDeltaTime()), item.rect.x, item.rect.y, item.rect.width, item.rect.height);

            if (item.type == LogicProcessor.Objects.BOX) {
//                batch.draw(images.getBox(item.isUsed), item.rect.x, item.rect.y, item.rect.width, item.rect.height);
                batch.draw(images.getImage(item.isUsed ? LogicProcessor.Objects.BOX_OPEN : LogicProcessor.Objects.BOX), item.rect.x, item.rect.y, item.rect.width, item.rect.height);
            } else if (item.type == LogicProcessor.Objects.KEY && !item.isUsed) {
//                batch.draw(images.getKeyFrame(Gdx.graphics.getDeltaTime()), item.rect.x, item.rect.y, item.rect.width, item.rect.height);
                batch.draw(images.getImage(LogicProcessor.Objects.KEY), item.rect.x, item.rect.y, item.rect.width, item.rect.height);
            } else if (item.type == LogicProcessor.Objects.PORTAL && !item.isUsed) {
                batch.draw(images.getImage(LogicProcessor.Objects.PORTAL), item.rect.x, item.rect.y, item.rect.width, item.rect.height);

            }
        }


        if (logicProcessor.gettingKeyPosition != null) {
            final MyActor myKeyActor = new MyActor(imgKey);
            stage.addActor(myKeyActor);

            myKeyActor.setPosition(heroRect.x * Physics.PPM - camera.position.x + camera.viewportWidth / 2, heroRect.y * Physics.PPM - camera.position.y + camera.viewportHeight / 2);

            myKeyActor.setRotation(0);

            logicProcessor.gettingKeyPosition = null;
            MoveToAction mta = new MoveToAction();

            mta.setPosition(20 * logicProcessor.getKeysCount(), camera.viewportHeight - myKeyActor.getHeight() - 10);
            mta.setDuration(keyAnimTime);
//            ScaleByAction sba = new ScaleByAction();
//            sba.setAmount(20f / 16f);
//            sba.setDuration(keyAnimTime);

            RotateToAction rta = new RotateToAction();
            rta.setRotation(360f);
            rta.setDuration(keyAnimTime);
            ParallelAction pa = new ParallelAction(mta, rta);
            myKeyActor.addAction(pa);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep((long) (keyAnimTime * 1000f));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    stage.getActors().removeIndex(0);
                }
            }).start();
        }


        for (int i = 0; i < logicProcessor.getKeysCount() - stage.getActors().size; i++) {
//            batch.draw(images.getImage(LogicProcessor.Objects.KEY), (camera.position.x - camera.viewportWidth / 2) + 20 * i + 10, 300, 16, 16);
            batch.draw(images.getImage(LogicProcessor.Objects.KEY), (camera.position.x - camera.viewportWidth / 2) + 20 * i + 10, camera.position.y + camera.viewportHeight / 2 - 20, 20, 20);
        }


    }

    private void handleControls() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            dispose();
            main.setScreen(new GameScreen(main));
            return;
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            dispose();
            main.setScreen(new MenuScreen(main));
            return;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.M)) {
            sounds.playMusic(false);
        }

        if (!logicProcessor.isAlive()) return;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
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
                ((PolygonShape) hero.getFixtureList().get(2).getShape()).setAsBox((heroRect.width / 2 - 0.1f) / Physics.PPM, 5 / Physics.PPM, new Vector2(-1f / Physics.PPM, -heroRect.height / 2 / Physics.PPM), 0);
                dino.setReverse(false);
            }
            dino.setAction(Movable.Actions.RUN);

            if (hero.getLinearVelocity().x > heroSpeed) hero.getLinearVelocity().x = heroSpeed;

        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
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

        } else {
            if (physics.contactListener.isOnGround())
                dino.setAction(Movable.Actions.IDLE);
            else
                dino.setAction(Movable.Actions.JUMP);
        }

        if (physics.contactListener.isOnGround()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                sounds.play(Sounds.SoundTag.JUMP);
                hero.applyForceToCenter(0, heroJumpForce, true);
            }
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
        imgKey.dispose();
        physics.dispose();
        sounds.playMusic(false);
        images.dispose();
    }
}
