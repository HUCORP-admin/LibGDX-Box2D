package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.mygdx.game.utils.Constants.WINDOW_HEIGHT;
import static com.mygdx.game.utils.Constants.WINDOW_WIDTH;

public abstract class BaseScreen implements Screen, InputProcessor {
    protected Game game;

    protected Stage mainStage;
    protected Stage uiStage;

    public BaseScreen(Game g) {
        this.game = g;
        mainStage = new Stage(new FitViewport(WINDOW_WIDTH, WINDOW_HEIGHT));
        uiStage = new Stage(new FitViewport(WINDOW_WIDTH, WINDOW_HEIGHT));

        InputMultiplexer im = new InputMultiplexer(this, mainStage);
        Gdx.input.setInputProcessor(im);

        create();
    }

    public abstract void create();
    public abstract void update(float delta);

    public void render(float delta) {
        mainStage.act(delta);
        uiStage.act(delta);

        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        mainStage.draw();
        uiStage.draw();
    }

    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height, true);
        uiStage.getViewport().update(width, height, true);
    }

    // methods required by Screen interface
    public void pause() { }
    public void resume() { }
    public void dispose() { }
    public void show() { }
    public void hide() { }

    // methods required by InputProcessor interface
    public boolean keyDown(int keycode) { return false; }
    public boolean keyUp(int keycode) { return false; }
    public boolean keyTyped(char c) { return false; }
    public boolean mouseMoved(int screenX, int screenY) { return false; }
    public boolean scrolled(float amountX, float amountY) { return false; }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
    public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
}
