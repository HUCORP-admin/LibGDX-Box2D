package com.mygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utils.Constants;

import static com.badlogic.gdx.graphics.Texture.*;
import static com.mygdx.game.utils.Constants.*;

public class Ball extends Box2DActor {
    public Ball() {
        super();
        this.loadAssets();
    }

    private void loadAssets() {
        Texture texture = new Texture(Gdx.files.internal("assets/ball.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.storeAnimation("default", texture);
    }

    public void initializePhysics(World world) {
        setDynamic();
        setShapeCircle();
        setPhysicsProperties(DENSITY, FRICTION, RESTITUTION);
        super.initializePhysics(world);
    }
}
