package ru.pb.gblibgdx.anim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.pb.gblibgdx.Movable;

import java.util.Arrays;

public class AnimTexture extends Anim {
    private Texture img;
    private boolean horizontalSprite;


    public AnimTexture(String image_name, float frameDuration, int imagesCountWidth, int imagesCountHeight, boolean horizontalSprite) {
        this.defaultFrameDuration = frameDuration;

        img = new Texture(image_name);
        this.horizontalSprite = horizontalSprite;

        this.regionHeight = img.getHeight() / imagesCountHeight;
        this.regionWidth = img.getWidth() / imagesCountWidth;
    }

    public void initializeAction(Movable.Actions action, int start, int end, Animation.PlayMode playMode) {
        initializeAction(action,  start,  end,  playMode, this.defaultFrameDuration);

    }

    public void initializeAction(Movable.Actions action, int start, int end, Animation.PlayMode playMode, float frameDuration) {
        TextureRegion region0 = new TextureRegion(img);
        TextureRegion[][] regions = region0.split(regionWidth, regionHeight);
        TextureRegion[] regions1 = new TextureRegion[regions.length * regions[1].length];
        int c = 0;
        if (horizontalSprite)
            for (int i = 0; i < regions.length; i++) {
                for (int j = 0; j < regions[i].length; j++) {
                    regions1[c] = regions[i][j];
                    c++;
                }
            }
        else
            for (int j = 0; j < regions[0].length; j++) {
                for (int i = 0; i < regions.length; i++) {
                    regions1[c] = regions[i][j];
                    c++;
                }
            }


        anim = new Animation<TextureRegion>(frameDuration, Arrays.copyOfRange(regions1, start, end));
        anim.setPlayMode(playMode);

        animationsMap.put(action, anim);

    }


    public void dispose() {
        img.dispose();
    }
}
