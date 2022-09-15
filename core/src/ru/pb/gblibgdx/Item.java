package ru.pb.gblibgdx;

import com.badlogic.gdx.math.Rectangle;

public class Item {
    public boolean isUsed = false;
    public LogicProcessor.Objects type;
    public Rectangle rect;

    public Item(LogicProcessor.Objects type, Rectangle rect) {
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
