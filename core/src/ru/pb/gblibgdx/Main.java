package ru.pb.gblibgdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import ru.pb.gblibgdx.screens.MenuScreen;

public class Main extends Game {


    private int mapSizeX, mapSizeY;

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
        mapSizeX =  Gdx.graphics.getWidth();
        mapSizeY =  Gdx.graphics.getHeight();
    }
    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
    }

    public int getMapSizeX() {
        return mapSizeX;
    }

    public int getMapSizeY() {
        return mapSizeY;
    }


}
