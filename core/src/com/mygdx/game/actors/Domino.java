package com.mygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.game.utils.Constants.*;

public class Domino extends Box2DActor {
    public Domino() {
        super();
        this.loadAssets();
    }

    private void loadAssets() {
        Texture texture = new Texture(Gdx.files.internal("assets/domino.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.storeAnimation("default", texture);
    }

    public void initializePhysics(World world) {
        setDynamic();
        setShapeRectangle();
        setPhysicsProperties(DENSITY, FRICTION, RESTITUTION);
        super.initializePhysics(world);
    }
}
