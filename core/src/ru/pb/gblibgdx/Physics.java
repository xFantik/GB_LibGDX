package ru.pb.gblibgdx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Physics {
    private final World world;
    private final Box2DDebugRenderer debugRenderer;

    private static final BodyDef.BodyType DEFAULT_BODY_TYPE = BodyDef.BodyType.StaticBody;
    private static final float DEFAULT_GRAVITY_SCALE = 12;
    public static final float DEFAULT_FRICTION = 5;
    public static final float PPM = 10;
    public MyContactListener contactListener;


    public Physics(MyContactListener myContactListener) {
        world = new World(new Vector2(0, -9.81f), true);  //определяет гравитацию
        debugRenderer = new Box2DDebugRenderer();
        contactListener = myContactListener;
        world.setContactListener(contactListener);

    }

    public void setGravity(Vector2 gravity) {
        world.setGravity(gravity);
    }

    public void step() {
        world.step(1 / 60.0f, 2, 2);
    }

    public void debugDraw(OrthographicCamera cam) {
        debugRenderer.render(world, cam.combined);
    }


    public void addSensor(RectangleMapObject object) {
        addSensor(object, object.getName());

    }

    public void addSensor(RectangleMapObject object, String data) {
        BodyDef def = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();


        Rectangle rect = object.getRectangle();
        def.position.set(rect.x / Physics.PPM + rect.width / 2 / PPM, rect.y / Physics.PPM + rect.height / 2 / PPM);

        polygonShape.setAsBox(rect.width / 2 / PPM, rect.height / 2 / PPM);
        fDef.shape = polygonShape;
        fDef.isSensor = true;


        Body body = world.createBody(def);
        body.createFixture(fDef).setUserData(data); //любой класс, можно передать сложный объект

        polygonShape.dispose();

    }

    public Body addObject(RectangleMapObject object, RectangleMapObject object2) {
        FixtureDef fDef = new FixtureDef();

        Body body = addObject(object);


        Rectangle rect = object.getRectangle();


        Rectangle rect2 = object2.getRectangle();
        PolygonShape polygonShape = new PolygonShape();

        polygonShape.setAsBox(rect2.width / 2 / PPM, rect2.height / 2 / PPM, new Vector2(0, -rect.height / 2 / Physics.PPM), 0);
        fDef.shape = polygonShape;

        fDef.friction = DEFAULT_FRICTION;

        body.createFixture(fDef).setUserData(object.getName()); //любой класс, можно передать сложный объект


        polygonShape = new PolygonShape();
        polygonShape.setAsBox(object.getRectangle().width / 2.2f / PPM, object.getRectangle().height / 4 / PPM, new Vector2(0, -object.getRectangle().getWidth() / 2 / PPM), 0);
        fDef = new FixtureDef();
        fDef.shape = polygonShape;
        fDef.isSensor = true;

        body.createFixture(fDef).setUserData("foot");

        polygonShape.dispose();


        return body;
    }

    public Body addObject(RectangleMapObject object) {
        BodyDef def = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();


        Rectangle rect = object.getRectangle();
        def.position.set(rect.x / Physics.PPM + rect.width / 2 / PPM, rect.y / Physics.PPM + rect.height / 2 / PPM);

        polygonShape.setAsBox(rect.width / 2 / PPM, rect.height / 2 / PPM);
        fDef.shape = polygonShape;


        String type = (String) object.getProperties().get("BodyType");
        if (type != null)
            switch (type) {
                case "DynamicBody":
                    def.type = BodyDef.BodyType.DynamicBody;
                    break;
                case "KinematicBody":
                    def.type = BodyDef.BodyType.KinematicBody;
                    break;
                default:

                    def.type = DEFAULT_BODY_TYPE;
            }
        else def.type = DEFAULT_BODY_TYPE;


        def.gravityScale = DEFAULT_GRAVITY_SCALE;


        if (object.getProperties().get("friction") == null) {
            fDef.friction = DEFAULT_FRICTION;                                          // трение
        } else {
            fDef.friction = (float) object.getProperties().get("friction");
        }

        fDef.density = 1;      //плотность


        if (object.getProperties().get("restitution") == null) {
            fDef.restitution = 0;                                       // упругость
        } else
            fDef.restitution = (float) object.getProperties().get("restitution");


        Body body = world.createBody(def);

        body.createFixture(fDef).setUserData(object.getName()); //любой класс, можно передать сложный объект

        polygonShape.dispose();
        return body;

    }

    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }
}
