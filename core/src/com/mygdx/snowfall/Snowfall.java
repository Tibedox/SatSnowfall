package com.mygdx.snowfall;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class Snowfall extends ApplicationAdapter {
	public static final float SCR_WIDTH = 1280, SCR_HEIGHT = 720;

	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;

	Texture imgSnowflake;
	Texture ibgBackGround;

	Snowflake[] snowflakes = new Snowflake[20];
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		touch = new Vector3();

		imgSnowflake = new Texture("snowflake.png");
		ibgBackGround = new Texture("forest.png");

		for (int i = 0; i < snowflakes.length; i++) {
			snowflakes[i] = new Snowflake();
		}

		InputProcessor processor = new InputProcessor() {
			@Override
			public boolean keyDown(int keycode) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				touch.set(screenX, screenY, 0);
				camera.unproject(touch);

				for (int i = 0; i < snowflakes.length; i++) {
					if(snowflakes[i].hit(touch.x, touch.y)){
						snowflakes[i].respawn();
					}
				}
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean scrolled(float amountX, float amountY) {
				return false;
			}
		};
		Gdx.input.setInputProcessor(processor);
	}

	@Override
	public void render () {
		// обработка касаний
		/*if(Gdx.input.justTouched()){
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touch);

			for (int i = 0; i < snowflakes.length; i++) {
				if(snowflakes[i].hit(touch.x, touch.y)){
					snowflakes[i].respawn();
				}
			}
		}*/

		// события игры
		for (int i = 0; i < snowflakes.length; i++) {
			snowflakes[i].move();
		}

		// отрисовка всего
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
