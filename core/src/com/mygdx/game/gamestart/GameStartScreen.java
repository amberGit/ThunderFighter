package com.mygdx.game.gamestart;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.ThunderFighter;
import com.mygdx.game.gamestart.util.BlastUtil;
import com.mygdx.game.mainmenu.MainMenuScreen;

import java.util.Iterator;

/**
 * Created by John on 2015/12/6.
 */
public class GameStartScreen extends ScreenAdapter {

    private static final int playerStatusTotalCount = 6;
    private TextureRegion[][] playerTextureRegion;

    private final ThunderFighter game;
    private Stage stage;
    private float playerPosX = 100, playerPosY = 0;
    private float backgroundCurrentLocation = 0;
    private float backgroundMoveSpeed = -0.5f;


    public GameStartScreen(ThunderFighter game, MainMenuScreen.Point selectedPlayerType) {
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

        final PlayerActor playerActor = new PlayerActor(playerTextureRegion[0], playerPosX, playerPosY, "player", BlastUtil.getRandomBlastAnimation(game.getAssetManager()));
        stage.addActor(backgroundActor);
        stage.addActor(playerActor);

        stage.addListener(new InputListener(){
            private ObjectSet<PlayerActor.Status> playerStatusSet = playerActor.getStatusSet();
            @Override
            public boolean keyDown(InputEvent event, int keycode) {

                if (keycode == Input.Keys.UP) {
                    playerStatusSet.add(PlayerActor.Status.UP);
                }
                if (keycode == Input.Keys.DOWN) {
                    playerStatusSet.add(PlayerActor.Status.DOWN);
                }
                if (keycode == Input.Keys.LEFT) {
                    playerStatusSet.add(PlayerActor.Status.LEFT);
                }
                if (keycode == Input.Keys.RIGHT) {
                    playerStatusSet.add(PlayerActor.Status.RIGHT);
                }
                if (keycode == Input.Keys.SPACE) { // fire
                    playerStatusSet.add(PlayerActor.Status.FIRE);
                }
                if (keycode == Input.Keys.A) {  // bomb
                    Gdx.app.log("key-down", "press key A");
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.UP) {
                    playerStatusSet.remove(PlayerActor.Status.UP);
                }
                if (keycode == Input.Keys.DOWN) {
                    playerStatusSet.remove(PlayerActor.Status.DOWN);
                }
                if (keycode == Input.Keys.LEFT) {
                    playerStatusSet.remove(PlayerActor.Status.LEFT);
                }
                if (keycode == Input.Keys.RIGHT) {
                    playerStatusSet.remove(PlayerActor.Status.RIGHT);
                }
                if (keycode == Input.Keys.SPACE) { // fire
                    playerStatusSet.remove(PlayerActor.Status.FIRE);
                }
                if (keycode == Input.Keys.A) {  // suicide test for BlastUtil's blast animation
                    playerStatusSet.remove(PlayerActor.Status.ALIVE);
                }
                if (keycode == Input.Keys.Q) {
                    playerStatusSet.add(PlayerActor.Status.ALIVE);
                    boolean hasPlayer = false;

                    for (Array.ArrayIterator<Actor> iterator = new Array.ArrayIterator<>(stage.getActors()); iterator.hasNext(); ) {
                        Actor actor = iterator.next();
                        if (playerActor.getName().equals(actor.getName())) {
                            hasPlayer = true;
                            break;
                        }
                    }
                    if (!hasPlayer)
                        stage.addActor(playerActor);

                }
                return true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touch down event triggered");
                return true;
            }
        });
    }


    private Texture getSelectPlayerTexture(final MainMenuScreen.Point selectedPlayerType) {
        return new Texture("gameStart/player/Player" + (selectedPlayerType.ordinal() + 1) + ".png");
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
