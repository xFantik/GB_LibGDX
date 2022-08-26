package ru.pb.gblibgdx;

public interface Movable {
    enum Actions {
         RUN, JUMP, IDLE, DEAD
    }

    void move (float deltaTime);
    void undo(float deltaTime);
    void setAction(Actions action);
    float getX();
    float getY();
    void setReverse(boolean reverse);

}
