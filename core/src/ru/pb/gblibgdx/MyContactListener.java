package ru.pb.gblibgdx;

import com.badlogic.gdx.physics.box2d.*;

public class MyContactListener implements ContactListener {
    private int footContactCount = 0;
    LogicProcessor logicProcessor;

    public MyContactListener(LogicProcessor logicProcessor) {
        this.logicProcessor = logicProcessor;
    }



    public boolean isOnGround() {
        return footContactCount > 0;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

//        System.out.println(a.getUserData() + "   " + b.getUserData());
//
//        System.out.println(footContactCount);

        if (a.getUserData() == null || b.getUserData() == null)
            return;

        if (a.getUserData().equals("object") && b.getUserData().equals("foot")) {
            footContactCount++;
        } else if (b.getUserData().equals("object")&& a.getUserData().equals("foot")) {
            footContactCount++;
        }else if (b.getUserData().equals("hero")) {
            logicProcessor.contact(a);
        } else if (a.getUserData().equals("hero")) {
            logicProcessor.contact(b);
        }


    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if (a.getUserData() == null || b.getUserData() == null)
            return;

        if (a.getUserData().equals("object") && b.getUserData().equals("foot")) {
            footContactCount--;
        } else if (b.getUserData().equals("object") && a.getUserData().equals("foot")) {
            footContactCount--;

        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
