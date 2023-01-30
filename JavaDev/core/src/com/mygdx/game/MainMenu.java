package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;
import org.lwjgl.Sys;

import java.awt.*;
import java.io.FileNotFoundException;

public class MainMenu extends ScreenAdapter implements InputProcessor {
    VideoPlayer videoPlayer;
    final PiazzaPanicGame game;
    SpriteBatch batch;
    BitmapFont font;
    final String INPUT = String.format("Piazza Panic %n Press S to Start Game %n Press Backspace to exit game");


    public MainMenu(PiazzaPanicGame game){
        this.game = game;
        /*Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        Gdx.graphics.setWindowedMode((int)width, (int)height);*/
        Graphics.DisplayMode dm = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setWindowedMode(dm.width, dm.height);
    }

    @Override
    public void show(){
        videoPlayer = VideoPlayerCreator.createVideoPlayer();

        try {
            videoPlayer.play(Gdx.files.internal("mainMenu.webm"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        videoPlayer.setOnCompletionListener(new VideoPlayer.CompletionListener() {
            @Override
            public void onCompletionListener(FileHandle file) {
                game.setScreen(new Map(game));
            }
        });

        batch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("karmatic-arcade/ka1.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.BLUE;
        font = generator.generateFont(parameter);

        Gdx.input.setInputProcessor(this);

    }
    @Override
    public void render(float delta){
        videoPlayer.update();

        int middleScreenX = Gdx.graphics.getWidth() / 3;
        int middleScreenY = Gdx.graphics.getHeight() / 2;

        Texture frame = videoPlayer.getTexture();
        batch.begin();
        batch.draw(frame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font.draw(batch, INPUT, middleScreenX, middleScreenY);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.S){
            game.setScreen(new Map(game));
        } else if (keycode == Input.Keys.BACKSPACE){
            Gdx.app.exit();
        }
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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

}
