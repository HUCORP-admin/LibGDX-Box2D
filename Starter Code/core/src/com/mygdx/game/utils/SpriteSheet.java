package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.graphics.Texture.*;
import static com.badlogic.gdx.graphics.g2d.Animation.*;

/**
 * This class is used to open a sprite-sheet and extract
 * images from said sprite-sheet. The extracted images
 * can be used to make an animation.
 * @author Arib Hussain
 */
public class SpriteSheet {
    private Texture spriteSheet;

    /**
     * Create a new SpriteSheet object and initialize the sheet itself.
     * @param filename represents the filename of the SpriteSheet.
     */
    public SpriteSheet(String filename) {
        spriteSheet = new Texture(Gdx.files.internal(filename));
        spriteSheet.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }

    /**
     * Get an animation from the SpriteSheet images
     * @param dimensions represents a 2-D array containing x, y, width, and height.
     * @param frameDuration represents how long each image in the animation should be played for.
     * @param mode represents the PlayMode.
     * @return the animation with the associated frames.
     */
    public Animation<TextureRegion> getAnimation(int[][] dimensions, float frameDuration,
                                                 PlayMode mode) {
        // create a new TextureRegion array containing the frames of the animation.
        TextureRegion[] frames = new TextureRegion[dimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            // add images to TextureRegion array.
            TextureRegion image = getImage
                    (dimensions[i][0], dimensions[i][1], dimensions[i][2], dimensions[i][3]);
            frames[i] = image;
        }

        // create a new animation and return
        Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);
        return new Animation<TextureRegion>(frameDuration, framesArray, mode);
    }

    /**
     * Get an image from the SpriteSheet.
     * @param x represents the x-coordinate of the image in the SpriteSheet.
     * @param y represents the y-coordinate of the image in the SpriteSheet.
     * @param width represents the width of the image in the SpriteSheet.
     * @param height represents the height of the image in the SpriteSheet.
     * @return a new TextureRegion representing the image in the SpriteSheet.
     */
    public TextureRegion getImage(int x, int y, int width, int height) {
        return new TextureRegion(spriteSheet, x, y, width, height);
    }
}
