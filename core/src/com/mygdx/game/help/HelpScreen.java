package com.mygdx.game.help;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by John on 2015/12/6.
 */
public class HelpScreen extends ScreenAdapter {
    private Game game;
    private Stage stage;
    private Screen mainMenuScreen;
    public HelpScreen(Game game, Screen mainMenuScreen) {
        this.game = game;
        this.mainMenuScreen = mainMenuScreen;
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(240, 320));
        final Texture mainHelpTex = new Texture("help/Help1.png");
        final Texture goodsHelpTex = new Texture("help/Help2.png");
        final Actor mainHelpActor = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
               batch.draw(mainHelpTex, 0, 0);
            }
        };
        mainHelpActor.setName("MAIN_HELP");

        final Actor goodsHelpActor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.draw(goodsHelpTex, 0, 0);
            }
        };
        goodsHelpActor.setName("GOODS_HELP");
        goodsHelpActor.setVisible(false);
        stage.addActor(mainHelpActor);
        stage.addActor(goodsHelpActor);

        Gdx.input.setInputProcessor(stage); // register input events
        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
               System.out.println("x:" + x + ",y:" + y);
                return true;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.UP || keycode == Input.Keys.DOWN) {
                   if (mainHelpActor.isVisible()) {
                       mainHelpActor.setVisible(false);
                       goodsHelpActor.setVisible(true);
                   } else {
                       mainHelpActor.setVisible(true);
                       goodsHelpActor.setVisible(false);
                   }
                }
                if (keycode == Input.Keys.ESCAPE) {
                    game.setScreen(mainMenuScreen);
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        System.out.println("dispose called");
    }
}
