package com.mygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.utils.SpriteSheet;

import static com.badlogic.gdx.Input.*;
import static com.mygdx.game.utils.Constants.*;

public class Player extends AnimatedActor {
    public Player() {
        super();
        loadAssets();
    }

    private void loadAssets() {
        SpriteSheet spriteSheet = new SpriteSheet(PLAYER_WALK_SHEET);

        // walking animation
        int[][] walking_coordinates = {{100, 2, 47, 64}, {1, 67, 48, 64}, {50, 67, 49, 63},
                {99, 67, 50, 63}, {1, 134, 48, 63}, {51, 135, 47, 64}};

        Animation<TextureRegion> walkAnim = spriteSheet.getAnimation(walking_coordinates,
                0.1f, Animation.PlayMode.LOOP);
        this.storeAnimation("walking", walkAnim);

        // standing animation
        TextureRegion standingFrame = spriteSheet.getImage(2, 3, 46, 64);
        this.storeAnimation("standing", standingFrame);

        // jumping animation
        Texture jumpingFrame = new Texture(Gdx.files.internal(PLAYER_JUMP));
        this.storeAnimation("jumping", jumpingFrame);

        // kicking animation
        Texture kickingFrame = new Texture(Gdx.files.internal(PLAYER_KICK));
        this.storeAnimation("kicking", kickingFrame, 0.4f);
    }

    public void update() {
        this.move();
        this.animate();
    }

    private void move() {
        this.velocityX = 0;
        this.velocityY = 0;

        if (Gdx.input.isKeyPressed(Keys.A)) {
            this.velocityX -= 100;
        } else if (Gdx.input.isKeyPressed(Keys.D)) {
            this.velocityX += 100;
        } else if (Gdx.input.isKeyPressed(Keys.W)) {
            this.velocityY += 100;
        } else if (Gdx.input.isKeyPressed(Keys.S)) {
            this.velocityY -= 100;
        }
    }

    private void animate() {
        switch (this.getAnimationName()) {
            case "standing":
                // set run animation
                if (Math.abs(this.velocityX) > 0.1) {
                    this.setActiveAnimation("walking");
                }
                break;
            case "walking":
                if (Math.abs(this.velocityX) < 0.1) {
                    this.setActiveAnimation("standing");
                }
                break;
            case "kicking":
                if (this.isAnimationFinished()) {
                    this.setActiveAnimation("standing");
                }
                break;
        }
    }
}
