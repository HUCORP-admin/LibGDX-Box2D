package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.actors.*;
import com.mygdx.game.utils.Constants;
import com.mygdx.game.utils.GameUtils;

import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.ui.Label.*;
import static com.mygdx.game.utils.Constants.*;
import static com.badlogic.gdx.Input.*;

public class DemoLevel extends BaseScreen {
    private TiledMap map;
    private OrthographicCamera camera;
    private TiledMapRenderer renderer;

    private World world;
    private Player player;
    private Ball ball;

    private int coinsCollected;
    private Label coinLabel;
    private String ballPlayerContact;

    private ArrayList<Box2DActor> removeList;

    public DemoLevel(Game g) {
        super(g);
        ballPlayerContact = "";
    }

    @Override
    public void create() {
        world = new World(new Vector2(0, -9.8f), true);
        removeList = new ArrayList<Box2DActor>();

        map = new TmxMapLoader().load(MAP_FILE);
        renderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.update();

        player = new Player();
        ball = new Ball();

        BaseActor coin = new BaseActor();
        coin.setTexture((Texture) new Texture(Gdx.files.internal("assets/coin-counter.png")));
        coin.setPosition(1080, 670);
        coin.setSize(32, 32);
        uiStage.addActor(coin);

        BitmapFont font = new BitmapFont();
        String text = "x 0";
        LabelStyle style = new LabelStyle(font, Color.WHITE);
        coinsCollected = 0;
        coinLabel = new Label(text, style);
        coinLabel.setFontScale(2);
        coinLabel.setPosition(1110, 680);

        uiStage.addActor(coinLabel);

        getObjectsFromMap();
    }

    @Override
    public void update(float delta) {
        world.step(1/60f, 6, 2);
        player.update();

        for (Box2DActor ba: removeList) {
            ba.destroy();
            world.destroyBody(ba.getBody());
            coinsCollected++;
        }

        removeList.clear();

        // tile-map update
        Camera mainCamera = mainStage.getCamera();
        mainCamera.position.x = player.getX() + player.getOriginX();
        mainCamera.position.y = player.getY() + player.getOriginY();
        mainCamera.position.x = MathUtils.clamp(mainCamera.position.x, (float) WINDOW_WIDTH/2,
                WINDOW_WIDTH - (float) WINDOW_WIDTH/2);
        mainCamera.position.y = MathUtils.clamp(mainCamera.position.y, (float) WINDOW_HEIGHT/2,
                WINDOW_HEIGHT - (float) WINDOW_HEIGHT/2);
        mainCamera.update();

        camera.position.x = mainCamera.position.x;
        camera.position.y = mainCamera.position.y;
        camera.update();
        renderer.setView(camera);
        renderer.render();

        checkCollisions();
        coinLabel.setText("x " + coinsCollected);

        if (!ballPlayerContact.isEmpty() && player.getAnimationName().equals("kicking")) {
            Vector2 kickVec = new Vector2(ballPlayerContact == "right" ? KICK_IMPULSE_X : -KICK_IMPULSE_X,
                    KICK_IMPULSE_Y);
            ball.applyImpulse(kickVec);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    private void getObjectsFromMap() {
        MapObjects objects = map.getLayers().get("objectData").getObjects();
        for (MapObject obj : objects) {
            String name = obj.getName();
            RectangleMapObject rectangleObject = (RectangleMapObject) obj;
            Rectangle r = rectangleObject.getRectangle();

            switch (name) {
                case "player":
                    player.setPosition(r.x, r.y);
                    player.setSize(r.width, r.height);
                    player.setProperties(world);
                    mainStage.addActor(player);
                    break;
                case "platform":
                    addSolid(rectangleObject, world);
                    break;
                case "coin":
                    Coin coin = new Coin();
                    coin.setEllipseBoundary();
                    coin.setPosition(r.x, r.y);
                    coin.initializePhysics(world);
                    mainStage.addActor(coin);
                    break;
                case "ball":
                    ball.setPosition(r.x, r.y);
                    ball.setSize(r.width, r.height);
                    ball.initializePhysics(world);
                    mainStage.addActor(ball);
                    break;
                case "domino":
                    Domino domino = new Domino();
                    domino.setPosition(r.x, r.y);
                    domino.setSize(r.width, r.height);
                    domino.initializePhysics(world);
                    mainStage.addActor(domino);
                    break;
                default:
                    break;
            }
        }
    }

    private void addSolid(RectangleMapObject rmo, World world) {
        Rectangle r = rmo.getRectangle();
        Box2DActor solid = new Box2DActor();
        solid.setPosition(r.x, r.y);
        solid.setSize(r.width, r.height);
        solid.setStatic();
        solid.setShapeRectangle();
        solid.initializePhysics(world);
    }

    private void checkCollisions() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object objP = GameUtils.getContactObject(contact, Player.class, "bottom");
                if (objP != null) {
                    Player p = (Player) objP;
                    p.adjustGroundCount(1);
                    p.setActiveAnimation("standing");
                }

                Object objC = GameUtils.getContactObject(contact, Coin.class);
                if (objC != null) {
                    Object p = GameUtils.getContactObject(contact, Player.class);
                    if (p != null) {
                        Coin c = (Coin) objC;
                        removeList.add(c);
                    }
                }

                Object objB = GameUtils.getContactObject(contact, Ball.class);
                if (objB != null) {
                    Object p = GameUtils.getContactObject(contact, Player.class, "right");
                    if (p != null) {
                        ballPlayerContact = "right";
                    }

                    p = GameUtils.getContactObject(contact, Player.class, "left");
                    if (p != null) {
                        ballPlayerContact = "left";
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                Object objP = GameUtils.getContactObject(contact, Player.class, "bottom");
                if (objP != null) {
                    Player p = (Player) objP;
                    p.adjustGroundCount(-1);
                }

                Object objB = GameUtils.getContactObject(contact, Ball.class);
                if (objB != null && !ballPlayerContact.isEmpty()) {
                    ballPlayerContact = "";
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {

            }
        });
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.W && player.isOnGround()) {
            Vector2 jumpVec = new Vector2(0, JUMP_IMPULSE);
            player.applyImpulse(jumpVec);
            player.setActiveAnimation("jumping");
        }

        if (keycode == Keys.J) {
            player.applyImpulse(new Vector2(player.getScaleX() == 1 ? KICK_JUMP : -KICK_JUMP, 0));
            player.setActiveAnimation("kicking");
        }

        return false;
    }
}
