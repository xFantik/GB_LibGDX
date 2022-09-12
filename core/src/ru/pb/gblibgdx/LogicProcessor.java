package ru.pb.gblibgdx;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import ru.pb.gblibgdx.screens.GameScreen;

import java.util.ArrayList;
import java.util.Iterator;

public class LogicProcessor {
    public enum Objects {KEY, DANGER, BOX}

    private ArrayList<Item> items;

    public GameScreen.SoundTag soundToPlay;



    private int keysCount = 0;

    private int heroHealth = 1;

    public boolean isBoxOpen = false;

    public  LogicProcessor (){
       items = new ArrayList<>();
    }


    public void addItem(RectangleMapObject rectangleMapObject) {
        if (rectangleMapObject.getName().equals("box"))
            items.add(new Item(Objects.BOX, rectangleMapObject.getRectangle()));
        else if (rectangleMapObject.getName().equals("key"))
            items.add(new Item(Objects.KEY, rectangleMapObject.getRectangle()));
    }
    public void contact(Fixture fixture) {
        if (fixture.getUserData().equals("damage")) {
            heroHealth--;
            if (heroHealth == 0){
                soundToPlay = GameScreen.SoundTag.GAME_OVER;
            }
            return;
        }
        Item item = getItem(fixture.getBody().getPosition());
        if (item.type == Objects.KEY && !item.isUsed) {
            keysCount++;
            soundToPlay = GameScreen.SoundTag.GET_KEY;
            item.isUsed = true;
//            items.remove(item);
        } else if (item.type == Objects.BOX && !item.isUsed && keysCount > 0) {
            keysCount--;
            item.isUsed = true;
            isBoxOpen = true;
            soundToPlay = GameScreen.SoundTag.WIN;
        }

    }


    public boolean isAlive() {
        return heroHealth > 0;
    }

    public boolean hasKey() {
        return keysCount > 0;
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

    public class Item {
        boolean isUsed = false;
        private Objects type;
        private Rectangle rect;

        public Item(Objects type, Rectangle rect) {
            this.type = type;
            this.rect = rect;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "isUsed=" + isUsed +
                    ", type=" + type +
                    ", rect=" + rect +
                    '}';
        }
    }
}
