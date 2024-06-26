package com.danielvilla.joc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {

    final Drop game;

    OrthographicCamera camera;

    public GameOverScreen(final Drop game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 400);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(255,0,0.2f,1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "You Loose", 100, 100);
        game.batch.end();


        if (Gdx.input.isTouched()){
            game.setScreen(new MainMenuScreen(game));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            dispose();
        }


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
