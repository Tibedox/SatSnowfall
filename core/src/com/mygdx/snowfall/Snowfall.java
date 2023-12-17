package com.mygdx.snowfall;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;

public class Snowfall extends ApplicationAdapter {
	public static final float SCR_WIDTH = 1280, SCR_HEIGHT = 720;

	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;

	Texture imgSnowflake;
	Texture imgBackGround;

	Snowflake[] snowflakes = new Snowflake[220];
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		touch = new Vector3();

		imgSnowflake = new Texture("snowflake.png");
		imgBackGround = new Texture("forest.png");

		for (int i = 0; i < snowflakes.length; i++) {
			snowflakes[i] = new Snowflake();
		}
	}

	@Override
	public void render () {
		if(Gdx.input.justTouched()){
			float x = Gdx.input.getX();
			float y = Gdx.input.getY();
			touch.set(x, y, 0);
			camera.unproject(touch);
			System.out.println(touch.x+" "+ touch.y);
		}

		for (int i = 0; i < snowflakes.length; i++) {
			snowflakes[i].move();
		}

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
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
		imgBackGround.dispose();
	}
}
