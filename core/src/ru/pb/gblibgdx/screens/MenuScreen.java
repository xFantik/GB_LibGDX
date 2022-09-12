package ru.pb.gblibgdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.pb.gblibgdx.*;
import ru.pb.gblibgdx.Character;

public class MenuScreen implements Screen {

    private final int button_width = 200;
    private int button_height;

   // private Character cat;


    private Main main;

    private TextureRegion[][] menu_buttons;
    private Texture img_menu_buttons;

    private SpriteBatch batch;

    Rectangle main_rectangle;

    Rectangle rect_btn_1, rect_btn_2, rect_btn_3;


    public MenuScreen(Main main) {
        this.main = main;
        batch = new SpriteBatch();
        img_menu_buttons = new Texture("menu_buttons.png");

        TextureRegion region0 = new TextureRegion(img_menu_buttons);
        menu_buttons = region0.split(img_menu_buttons.getWidth() / 2, img_menu_buttons.getHeight() / 6);

        button_height = menu_buttons[0][0].getRegionHeight() * button_width / menu_buttons[0][0].getRegionWidth();

        main_rectangle = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        rect_btn_1 = new Rectangle(Gdx.graphics.getWidth() / 2.0f - button_width / 2.0f, Gdx.graphics.getHeight() / 2.0f + button_height / 2.0f, button_width, button_height);
        rect_btn_2 = new Rectangle(Gdx.graphics.getWidth() / 2.0f - button_width / 2.0f, Gdx.graphics.getHeight() / 2.0f - button_height / 2.0f, button_width, button_height);
        rect_btn_3 = new Rectangle(Gdx.graphics.getWidth() / 2.0f - button_width / 2.0f, Gdx.graphics.getHeight() / 2.0f - button_height * 1.5f, button_width, button_height);

       // cat = CharactersFactory.getCat(main);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.4f, 0.1f, 1);


        float mouse_x = Gdx.input.getX();
        float mouse_Y = Gdx.graphics.getHeight() - Gdx.input.getY();

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (rect_btn_1.contains(mouse_x, mouse_Y)) {
                dispose();
                main.setScreen(new GameScreen(main));
                return;
            }
            if (rect_btn_3.contains(mouse_x, mouse_Y)) {
                dispose();
                System.exit(0);
            }
        }

        batch.begin();
        if (!rect_btn_1.contains(mouse_x, mouse_Y))
            batch.draw(menu_buttons[0][0], rect_btn_1.x, rect_btn_1.y, rect_btn_1.getWidth(), rect_btn_1.getHeight());
        else
            batch.draw(menu_buttons[0][1], rect_btn_1.x, rect_btn_1.y, rect_btn_1.getWidth(), rect_btn_1.getHeight());
        if (!rect_btn_2.contains(mouse_x, mouse_Y))

            batch.draw(menu_buttons[1][0], rect_btn_2.x, rect_btn_2.y, rect_btn_2.getWidth(), rect_btn_2.getHeight());
        else
            batch.draw(menu_buttons[1][1], rect_btn_2.x, rect_btn_2.y, rect_btn_2.getWidth(), rect_btn_2.getHeight());
        if (!rect_btn_3.contains(mouse_x, mouse_Y))
            batch.draw(menu_buttons[4][0], rect_btn_3.x, rect_btn_3.y, rect_btn_3.getWidth(), rect_btn_3.getHeight());
        else
            batch.draw(menu_buttons[4][1], rect_btn_3.x, rect_btn_3.y, rect_btn_3.getWidth(), rect_btn_3.getHeight());


        batch.end();


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        img_menu_buttons.dispose();
        batch.dispose();
    }
}
