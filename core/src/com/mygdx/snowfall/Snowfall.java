package com.mygdx.snowfall;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

public class Snowfall extends ApplicationAdapter {
	public static final float SCR_WIDTH = 1280, SCR_HEIGHT = 720;

	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;
	BitmapFont font, fontLarge;

	Texture imgSnowflake;
	Texture imgBackGround;
	Texture imgSoundOn, imgSoundOff;
	Sound sndChpok;

	Snowflake[] snowflakes = new Snowflake[220];
	MyButton btnSound;
	Player[] players = new Player[11];
	boolean soundOn = true;
	boolean gameOver = false;
	int score;
	String namePlayer = "Player";
	long timeStartGame;
	long timeGameDuration = 5100;
	String time;
	
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
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player("Noname", 0);
		}
		for (int i = 0; i < snowflakes.length; i++) {
			snowflakes[i] = new Snowflake();
		}

		Gdx.input.setInputProcessor(new MyInputProcessor());
		timeStartGame = TimeUtils.millis();
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

		if(!gameOver) {
			if(TimeUtils.millis()-timeStartGame >= timeGameDuration) {
				gameOver = true;
				loadRecords();
				sortRecords();
				saveRecords();
			}
			time = getTime();
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
		font.draw(batch, "SCORE: "+score, SCR_WIDTH-220, SCR_HEIGHT-10);
		font.draw(batch, time, 0, SCR_HEIGHT-10, SCR_WIDTH, Align.center, true);
		if(gameOver) {
			fontLarge.draw(batch, "Time Out", 0, SCR_HEIGHT-100, SCR_WIDTH, Align.center, true);
			for (int i = 0; i < players.length; i++) {
				font.draw(batch, (i+1)+". "+players[i].name+"....."+players[i].score, 0, SCR_HEIGHT-200-40*i, SCR_WIDTH, Align.center, true);
			}
		}
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

	void generateFont(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("isabella.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 40;
		parameter.color = Color.GREEN;
		parameter.borderColor = Color.valueOf("#0D4D1F");
		parameter.borderWidth = 1;
		parameter.shadowColor = Color.BLACK;
		parameter.shadowOffsetX = 2;
		parameter.shadowOffsetY = 2;
		font = generator.generateFont(parameter);
		parameter.size = 100;
		fontLarge = generator.generateFont(parameter);
		generator.dispose();
	}

	String getTime(){
		long time = TimeUtils.millis()-timeStartGame;
		long msec = time%1000;
		long sec = time/1000%60;
		long min = time/1000/60%60;
		long hour = time/1000/60/60;
		return ""+min/10+min%10+":"+sec/10+sec%10+":"+msec/100;
	}

	void saveRecords(){
		Preferences preferences = Gdx.app.getPreferences("records");
		for (int i = 0; i < players.length; i++) {
			preferences.putString("name"+i, players[i].name);
			preferences.putInteger("score"+i, players[i].score);
		}
		preferences.flush();
	}

	void loadRecords(){
		Preferences preferences = Gdx.app.getPreferences("records");
		for (int i = 0; i < players.length; i++) {
			if(preferences.contains("name"+i)) players[i].name = preferences.getString("name"+i, "None");
			if(preferences.contains("score"+i)) players[i].score = preferences.getInteger("score"+i, 0);
		}
		players[players.length-1].name = namePlayer;
		players[players.length-1].score = score;
	}

	void sortRecords() {
		for (int j = 0; j < players.length; j++) {
			for (int i = 0; i < players.length - 1; i++) {
				if (players[i].score < players[i + 1].score) {
					Player c = players[i];
					players[i] = players[i + 1];
					players[i + 1] = c;
				}
			}
		}
	}

	void gameRestart() {
		score = 0;
		for (int i = 0; i < snowflakes.length; i++) {
			snowflakes[i].respawn();
		}
		timeStartGame = TimeUtils.millis();
		gameOver = false;
	}

	class MyInputProcessor implements InputProcessor{
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
			if(!gameOver) {
				for (int i = 0; i < snowflakes.length; i++) {
					if (snowflakes[i].hit(touch.x, touch.y)) {
						snowflakes[i].respawn();
						if (soundOn) {
							sndChpok.play();
						}
						score++;
					}
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
			if(gameOver) {
				gameRestart();
			}
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
	}
}