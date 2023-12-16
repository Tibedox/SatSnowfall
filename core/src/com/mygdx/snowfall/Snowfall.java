package com.mygdx.snowfall;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Snowfall extends ApplicationAdapter {
	public static final float SCR_WIDTH = 1280, SCR_HEIGHT = 720;

	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;
	BitmapFont font;

	Texture imgSnowflake;
	Texture imgBackGround;
	Texture imgSoundOn, imgSoundOff;
	Sound sndChpok;

	Snowflake[] snowflakes = new Snowflake[20];
	MyButton btnSound;

	boolean isSoundOn = true;
	int score;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		touch = new Vector3();
		font = new BitmapFont();

		imgSnowflake = new Texture("snowflake.png");
		imgBackGround = new Texture("forest.png");
		imgSoundOn = new Texture("soundon.png");
		imgSoundOff = new Texture("soundoff.png");
		sndChpok = Gdx.audio.newSound(Gdx.files.internal("chpok.mp3"));

		btnSound = new MyButton(10, SCR_HEIGHT-60, 50);
		for (int i = 0; i < snowflakes.length; i++) {
			snowflakes[i] = new Snowflake();
		}
	}

	@Override
	public void render () {
		// обработка касаний
		if(Gdx.input.justTouched()){
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touch);

			for (int i = 0; i < snowflakes.length; i++) {
				if(snowflakes[i].hit(touch.x, touch.y)){
					score++;
					snowflakes[i].respawn();
					if(isSoundOn) {
						sndChpok.play();
					}
				}
			}
			if(btnSound.hit(touch.x, touch.y)) {
				isSoundOn = !isSoundOn;
			}
		}

		// события игры
		for (int i = 0; i < snowflakes.length; i++) {
			snowflakes[i].move();
		}

		// отрисовка всего
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
		for (int i = 0; i < snowflakes.length; i++) {
			batch.draw(imgSnowflake, snowflakes[i].x, snowflakes[i].y,
					snowflakes[i].width/2, snowflakes[i].height/2, snowflakes[i].width, snowflakes[i].height,
					1, 1, snowflakes[i].angle, 0, 0, 413, 477, false, false);
		}
		batch.draw(isSoundOn?imgSoundOn:imgSoundOff, btnSound.x, btnSound.y, btnSound.width, btnSound.height);
		font.draw(batch, "SCORE: "+score, SCR_WIDTH-100, SCR_HEIGHT-20);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		imgSnowflake.dispose();
		imgBackGround.dispose();
		imgSoundOn.dispose();
		imgSoundOff.dispose();
		sndChpok.dispose();
	}
}
