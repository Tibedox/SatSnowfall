package com.mygdx.snowfall;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Snowfall extends ApplicationAdapter {
	public static final float SCR_WIDTH = 1280, SCR_HEIGHT = 720;

	SpriteBatch batch;
	OrthographicCamera camera;

	Texture imgSnowflake;
	Texture ibgBackGround;

	Snowflake[] snowflakes = new Snowflake[20];
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);

		imgSnowflake = new Texture("snowflake.png");
		ibgBackGround = new Texture("forest.png");

		for (int i = 0; i < snowflakes.length; i++) {
			snowflakes[i] = new Snowflake();
		}
	}

	@Override
	public void render () {
		for (int i = 0; i < snowflakes.length; i++) {
			snowflakes[i].move();
		}

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(ibgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
		for (int i = 0; i < snowflakes.length; i++) {
			batch.draw(imgSnowflake, snowflakes[i].x, snowflakes[i].y,
					snowflakes[i].width/2, snowflakes[i].height/2, snowflakes[i].width, snowflakes[i].height,
					1, 1, snowflakes[i].angle, 0, 0, 413, 477, false, false);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		imgSnowflake.dispose();
		ibgBackGround.dispose();
	}
}
