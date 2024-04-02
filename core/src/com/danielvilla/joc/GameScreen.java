package com.danielvilla.joc;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.danielvilla.joc.Drop;
import com.danielvilla.joc.GameOverScreen;
import com.danielvilla.joc.MainMenuScreen;

public class GameScreen implements Screen {
	final Drop game;

	Texture dropImage, heartImage, bucketImage;
	Sound dropSound;
	Music rainMusic;
	OrthographicCamera camera;
	Rectangle bucket, heart;
	Array<Rectangle> raindrops;
	long lastDropTime;
	int dropsGathered, hearts = 3;

	public GameScreen(final Drop game) {
		this.game = game;

		//Load Images
		dropImage = new Texture(Gdx.files.internal("drop.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		heartImage = new Texture(Gdx.files.internal("heart.png"));

		//Load Sounds
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		rainMusic.setLooping(true);

		//Create the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		//Create the rectangle where the bucket go
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2; //Center the bucket horizontally
		bucket.y = 20; //Bottom left corner of the bucket is 20 pixels above
		//The bottom screen edge
		bucket.width = 64;
		bucket.height = 64;

		//Heart
		heart = new Rectangle();
		heart.width = 32;
		heart.height = 32;

		//Create the raindrops array and spawn the first raindrop
		raindrops = new Array<Rectangle>();
		spawnRaindrop();

	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800 - 64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render(float delta) {
		//Set the screen color
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();

		//Render in the camera
		game.batch.setProjectionMatrix(camera.combined);

		//Draw bucket and drops
		game.batch.begin();
		game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
		game.batch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height);
		for (Rectangle raindrop : raindrops) {
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		for (int i = 0; i< hearts; i++) {
			game.batch.draw(heartImage, 400+heartImage.getWidth() + 1+(i*25), 400, heart.width, heart.height);
		}
		game.batch.end();

		//User Input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 400 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 400 * Gdx.graphics.getDeltaTime();

		//Change the side when the bucket will go out
		if (bucket.x < 0) bucket.x = 800-65;
		if (bucket.x > 800 - 64) bucket.x = 1;

		//Create drop if necesary
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

		// Move and eliminate the drops
		Iterator<Rectangle> iter = raindrops.iterator();
		while (iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + 64 < 0) {
				iter.remove();
				hearts--;
			}

			if (raindrop.overlaps(bucket)) {
				dropsGathered++;
				dropSound.play();
				iter.remove();
			}
		}

		if (hearts <= 0) {
			game.setScreen(new GameOverScreen(game));
			dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		rainMusic.play();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
	}

}
