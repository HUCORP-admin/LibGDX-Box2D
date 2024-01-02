package com.mygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

import static com.badlogic.gdx.graphics.Texture.*;

public class Coin extends Box2DActor {
    public Coin() {
        super();
        this.loadAssets();
    }

    private void loadAssets() {
        Texture texture = new Texture(Gdx.files.internal("assets/coin.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.storeAnimation("default", texture);
    }

    public void initializePhysics(World world) {
        setStatic();
        setShapeCircle();
        fixtureDef.isSensor = true;
        super.initializePhysics(world);
    }
}
