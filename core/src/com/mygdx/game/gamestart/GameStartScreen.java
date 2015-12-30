package com.mygdx.game.gamestart;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.mainmenu.MainMenuScreen;

/**
 * Created by John on 2015/12/6.
 */
public class GameStartScreen extends ScreenAdapter {

    private static final int playerStatusTotalCount = 6;
    private TextureRegion[][] playerTextureRegion;

    private Game game;
    private Stage stage;
    private float playerPosX = 100, playerPosY = 0;
    private float backgroundCurrentLocation = 0;
    private float backgroundMoveSpeed = -0.5f;

    public  enum PlayerStatus{
        UP,
        DOWN,
        LEFT,
        RIGHT,
        FIRE,
        IDLE,
        ALIVE,
        DESTROYED
    }
    public GameStartScreen(Game game, MainMenuScreen.Point selectedPlayerType) {
        this.game = game;
        stage = new Stage(new StretchViewport(240, 320));
        init(selectedPlayerType);
    }
    private void init(final MainMenuScreen.Point selectedPlayerType) {
        // set background texture
        final Texture background = new Texture("gameStart/Background/BackScr1.jpg");


        Texture selectPlayerTexture =  getSelectPlayerTexture(selectedPlayerType);
        playerTextureRegion = TextureRegion.split(selectPlayerTexture, selectPlayerTexture.getWidth() / playerStatusTotalCount, selectPlayerTexture.getHeight());

        Actor backgroundActor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                if (backgroundCurrentLocation + background.getHeight() <= 0) {
                    backgroundCurrentLocation = 0;
                } else {
                    backgroundCurrentLocation += backgroundMoveSpeed;
                }
                batch.draw(background, 0, backgroundCurrentLocation);
                batch.draw(background, 0, backgroundCurrentLocation + background.getHeight()); // avoid blank background while background location > background height - screen height
            }
        };

        Actor playerActor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.draw(playerTextureRegion[0][0], playerPosX, playerPosY);
            }
        };
        stage.addActor(backgroundActor);
        stage.addActor(playerActor);
    }


    private Texture getSelectPlayerTexture(final MainMenuScreen.Point selectedPlayerType) {
        Texture playerTexture = new Texture("gameStart/player/Player" + (selectedPlayerType.ordinal() + 1) + ".png");
        return playerTexture;
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
}
