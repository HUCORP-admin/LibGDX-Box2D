package com.mygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import static com.badlogic.gdx.graphics.Texture.*;

public class Ball extends AnimatedActor {
    public Ball() {
        super();
        loadAssets();
    }

    private void loadAssets() {
        Texture texture = new Texture(Gdx.files.internal("assets/ball.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.storeAnimation("default", texture);
    }
}
