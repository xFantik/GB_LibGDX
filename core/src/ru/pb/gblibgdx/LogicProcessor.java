package ru.pb.gblibgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import ru.pb.gblibgdx.screens.GameScreen;

import java.util.ArrayList;
import java.util.Iterator;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.addAction;

public class LogicProcessor {
    public enum Objects {KEY, DANGER, BOX, PORTAL, BOX_OPEN}

    private ArrayList<Item> items;

    public GameScreen.SoundTag soundToPlay;

    Item portal;



    private int keysCount = 0;

    private int heroHealth = 1;

    private int boxCount = 0;

    public Vector2 flyToPortal;
    public Vector2 gettingKeyPosition;



    public LogicProcessor() {
        items = new ArrayList<>();
    }


    public void addItem(RectangleMapObject rectangleMapObject) {
        if (rectangleMapObject.getName().equals("box")) {
            items.add(new Item(Objects.BOX, rectangleMapObject.getRectangle()));
            boxCount++;
        } else if (rectangleMapObject.getName().equals("key"))
            items.add(new Item(Objects.KEY, rectangleMapObject.getRectangle()));
        else if (rectangleMapObject.getName().equals("portal")) {
            portal = new Item(Objects.PORTAL, rectangleMapObject.getRectangle());
            portal.isUsed = true;

            items.add(portal);
        }
    }

    public void contact(Fixture fixture) {
        if (fixture.getUserData().equals("damage")) {
            heroHealth--;
            if (heroHealth == 0) {
                soundToPlay = GameScreen.SoundTag.GAME_OVER;
            }
            return;
        }

        Item item = getItem(fixture.getBody().getPosition());
        if (item.isUsed) return;

        if (item.type == Objects.KEY) {
            keysCount++;
            soundToPlay = GameScreen.SoundTag.GET_KEY;
            item.isUsed = true;


            gettingKeyPosition = new Vector2(item.rect.x, item.rect.y);


//            items.remove(item);
        } else if (item.type == Objects.BOX && keysCount > 0) {
            keysCount--;
            item.isUsed = true;
            boxCount--;
            if (boxCount == 0 && portal != null) {
                portal.isUsed = false;
                soundToPlay = GameScreen.SoundTag.WIN;
            } else
                soundToPlay = GameScreen.SoundTag.GET_KEY;
        } else if (item.type == Objects.PORTAL) {
            flyToPortal = new Vector2(portal.rect.x+portal.rect.width/2, portal.rect.y+portal.rect.height/2);
            soundToPlay = GameScreen.SoundTag.WIN;
        }

    }


    public boolean isAlive() {
        return heroHealth > 0;
    }

    public int getKeysCount() {
        return keysCount;
    }

    public Iterator<Item> iterator() {
        return items.iterator();
    }


    public Item getItem(Vector2 pos) {
        pos.x *= Physics.PPM;
        pos.y *= Physics.PPM;
        System.out.println(pos);


        for (Item item : items) {
            System.out.println(item);
            if (item.rect.contains(pos)) {
                return item;
            }
        }
        return null;
    }

}
