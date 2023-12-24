package com.mygdx.snowfall;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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
	BitmapFont font;

	Texture imgSnowflake;
	Texture imgBackGround;
	Texture imgSoundOn, imgSoundOff;
	Sound sndChpok;

	Snowflake[] snowflakes = new Snowflake[220];
	boolean soundOn = true;
	MyButton btnSound;
	int score;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		touch = new Vector3();

		generateFont();

		imgSnowflake = new Texture("snowflake.png");
		imgBackGround = new Texture("forest.png");
		imgSoundOn = new Texture("soundon.png");
		imgSoundOff = new Texture("soundoff.png");
		sndChpok = Gdx.audio.newSound(Gdx.files.internal("sunchpok.mp3"));

		btnSound = new MyButton(10, SCR_HEIGHT-60, 50);
		for (int i = 0; i < snowflakes.length; i++) {
			snowflakes[i] = new Snowflake();
		}

		setInput();
	}

	@Override
	public void render () {
		// обработка касаний
		/*if(Gdx.input.isTouched()){
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
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
		for (int i = 0; i < snowflakes.length; i++) {
			batch.draw(imgSnowflake, snowflakes[i].x, snowflakes[i].y,
					snowflakes[i].width/2, snowflakes[i].height/2, snowflakes[i].width, snowflakes[i].height,
					1, 1, snowflakes[i].angle, 0, 0, 413, 477, false, false);
		}
		batch.draw(soundOn?imgSoundOn:imgSoundOff, btnSound.x, btnSound.y, btnSound.width, btnSound.height);
		font.draw(batch, "SCORE: "+score, SCR_WIDTH-220, SCR_HEIGHT-20);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		imgSnowflake.dispose();
		imgBackGround.dispose();
		sndChpok.dispose();
	}

	void generateFont(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("isabella.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 40;
		parameter.color = Color.GREEN;
		parameter.borderColor = Color.WHITE;
		parameter.borderWidth = 1;
		parameter.shadowColor = Color.BLACK;
		parameter.shadowOffsetX = 2;
		parameter.shadowOffsetY = 2;
		font = generator.generateFont(parameter);
	}
	void setInput(){
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
				if(btnSound.hit(touch.x, touch.y)){
					soundOn = !soundOn;
				}
				for (int i = 0; i < snowflakes.length; i++) {
					if(snowflakes[i].hit(touch.x, touch.y)){
						snowflakes[i].respawn();
						if(soundOn) {
							sndChpok.play();
						}
						score++;
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
}