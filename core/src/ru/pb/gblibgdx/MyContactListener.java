package ru.pb.gblibgdx;

import com.badlogic.gdx.physics.box2d.*;
import ru.pb.gblibgdx.screens.GameScreen;

public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData() != null && a.getUserData().equals("foot")) {
            GameScreen.heroOnGround = true;
        } else if (b.getUserData() != null && b.getUserData().equals("foot")) {
            GameScreen.heroOnGround = true;

        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if (a.getUserData() != null && a.getUserData().equals("foot")) {
            GameScreen.heroOnGround = false;
        } else if (b.getUserData() != null && b.getUserData().equals("foot")) {
            GameScreen.heroOnGround = false;

        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
