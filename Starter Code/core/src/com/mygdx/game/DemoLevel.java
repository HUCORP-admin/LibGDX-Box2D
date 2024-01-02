package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.actors.Ball;
import com.mygdx.game.actors.Coin;
import com.mygdx.game.actors.Domino;
import com.mygdx.game.actors.Player;

import static com.mygdx.game.utils.Constants.*;

public class DemoLevel extends BaseScreen {
    private TiledMap map;
    private OrthographicCamera camera;
    private TiledMapRenderer renderer;

    private Player player;

    public DemoLevel(Game g) {
        super(g);
    }

    @Override
    public void create() {
        map = new TmxMapLoader().load(MAP_FILE);
        renderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.update();

        player = new Player();

        getObjectsFromMap();
    }

    @Override
    public void update(float delta) {
        player.update();

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
                    mainStage.addActor(player);
                    break;
                case "platform":
                    break;
                case "coin":
                    Coin coin = new Coin();
                    coin.setPosition(r.x, r.y);
                    mainStage.addActor(coin);
                    break;
                case "ball":
                    Ball ball = new Ball();
                    ball.setPosition(r.x, r.y);
                    ball.setSize(r.width, r.height);
                    mainStage.addActor(ball);
                    break;
                case "domino":
                    Domino domino = new Domino();
                    domino.setPosition(r.x, r.y);
                    domino.setSize(r.width, r.height);
                    mainStage.addActor(domino);
                    break;
            }
        }
    }
}