package com.mygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utils.SpriteSheet;

import static com.mygdx.game.utils.Constants.*;

public class Player extends Box2DActor {
    protected int groundCount;

    public Player() {
        super();
        loadAssets();
        groundCount = 0;
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

    public void setProperties(World world) {
        this.setDynamic();
        this.setShapeRectangle();
        this.setPhysicsProperties(DENSITY, FRICTION, RESTITUTION);
        this.setFixedRotation();
        this.initializePhysics(world);
    }

    @Override
    public void initializePhysics(World world) {
        super.initializePhysics(world);

        FixtureDef bottomSensor = new FixtureDef();
        FixtureDef rightSensor = new FixtureDef();
        FixtureDef leftSensor = new FixtureDef();

        bottomSensor.isSensor = true;
        rightSensor.isSensor = true;
        leftSensor.isSensor = true;

        PolygonShape sensorShape = new PolygonShape();

        // bottom fixture
        sensorShape.setAsBox((getWidth() - 8)/200, getHeight()/200, new Vector2(0, -20/200f), 0);
        bottomSensor.shape = sensorShape;
        Fixture bottomFixture = body.createFixture(bottomSensor);
        bottomFixture.setUserData("bottom");

        // right fixture
        sensorShape.setAsBox(getWidth()/200, (getHeight() - 8)/200, new Vector2(20/200f, 0), 0);
        rightSensor.shape = sensorShape;
        Fixture rightFixture = body.createFixture(rightSensor);
        rightFixture.setUserData("right");

        // left fixture
        sensorShape.setAsBox(getWidth()/200, (getHeight() - 8)/200, new Vector2(-20/200f, 0), 0);
        leftSensor.shape = sensorShape;
        Fixture leftFixture = body.createFixture(leftSensor);
        leftFixture.setUserData("left");
    }

    public void adjustGroundCount(int i) {
        groundCount += i;
    }

    public boolean isOnGround() {
        return groundCount > 0;
    }

    public void update() {
        this.move();
        this.animate();
    }

    private void move() {
        this.setMaxSpeedX(MAX_WALK_SPEED);

        if (Gdx.input.isKeyPressed(Keys.A)) {
            this.applyForce(new Vector2(-WALK_SPEED, 0));
            this.setScale(-1, 1);
        } else if (Gdx.input.isKeyPressed(Keys.D)) {
            this.applyForce(new Vector2(WALK_SPEED, 0));
            this.setScale(1, 1);
        }
    }

    private void animate() {
        switch (this.getAnimationName()) {
            case "standing":
                // set run animation
                if (this.getSpeed() > 0.1) {
                    this.setActiveAnimation("walking");
                }
                break;
            case "walking":
                if (this.getSpeed() < 0.1) {
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
