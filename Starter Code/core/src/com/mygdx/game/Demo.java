package com.mygdx.game;

import com.badlogic.gdx.Game;

public class Demo extends Game {

	@Override
	public void create() {
		DemoLevel level = new DemoLevel(this);
		setScreen(level);
	}
}
