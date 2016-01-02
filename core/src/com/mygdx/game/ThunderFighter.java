package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.gamestart.util.BlastUtil;
import com.mygdx.game.help.HelpScreen;
import com.mygdx.game.mainmenu.MainMenuScreen;

/**
 * Created by John on 2015/12/4.
 */
public class ThunderFighter extends Game {
    private Screen helpScreen;
    private AssetManager assetManager;
    public Screen getHelpScreen() {
        return helpScreen;
    }

    @Override
    public void create() {
        Screen mainMenuScreen = new MainMenuScreen(this);
        helpScreen = new HelpScreen(this, mainMenuScreen);
        this.setScreen(mainMenuScreen);
        assetManager = new AssetManager();
        BlastUtil.setAssetManager(assetManager);
        BlastUtil.load();
    }

    @Override
    public void resume() {
        super.resume();
    }
}
