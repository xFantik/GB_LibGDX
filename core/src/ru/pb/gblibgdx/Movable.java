package ru.pb.gblibgdx;

public interface Movable {
    enum Actions {
         RUN, JUMP, IDLE, DEAD
    }

    void move (float deltaTime);
    void setAction(Actions action);
    void setReverse(boolean reverse);

}
