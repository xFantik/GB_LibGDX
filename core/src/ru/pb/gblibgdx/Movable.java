package ru.pb.gblibgdx;

public interface Movable {
    enum Actions {
        GO, GO_LEFT, GO_UP, GO_DOWN
    }

    boolean move (float deltaTime);
    void setAction(Actions action);
    float getX();
    float getY();
    void dispose();
    void reverse();

}
