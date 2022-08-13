package ru.pb.gblibgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class GB_LibGDX extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    float x_old = 0;
    float y_old = 0;
    int img_width;
    int img_height;


    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("ladybug2.png");

        img_width = img.getWidth();
        img_height = img.getHeight();

        x_old = Gdx.input.getX() - img.getWidth() / 2;
        y_old = Gdx.graphics.getHeight() - Gdx.input.getY() - img.getHeight() / 2;

    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0.3f, 0, 1);

        float x = Gdx.input.getX() - img.getWidth() / 2;
        float y = Gdx.graphics.getHeight() - Gdx.input.getY() - img.getHeight() / 2;

        batch.begin();
        TextureRegion tr = new TextureRegion(img);

        double rotation = (Math.atan2(y - y_old, x - x_old) / Math.PI * 180);



        batch.draw(tr, x, y, img_width / 2, img_height / 2, img_width, img_height, 1, 1, (float) rotation, true);


        if (Math.abs(x_old - x) + Math.abs(y_old - y) > 30) {
            x_old = x;
            y_old = y;
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
