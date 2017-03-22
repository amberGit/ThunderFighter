package com.mygdx.game.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.ThunderFighter;
import com.mygdx.game.gamestart.GameStartScreen;


/**
 * Created by John on 2015/12/4.
 */
public class MainMenuScreen extends ScreenAdapter {
    private ThunderFighter game;
    private Stage stage;
    private Stage selectPlayerStage;

    private float stateTime;
    private Point currentCursorLocation;
    private boolean hasCreateSelectStage = false;
    private boolean isOpenSelectStage = false;

    public enum Point {
        FIRST(100, 82),
        SECOND(142, 82),
        THIRD(183, 82);

        Point(float x, float y){
            this.x = x;
            this.y = y;
        }
        float x;
        float y;
    }
    public enum MenuItem {
        GAME_START,
        GAME_SETTING,
        GAME_ACHIEVE,
        GAME_HELP,
        GAME_EXIT
    }
    public enum Command {
        NEXT,
        PREV,
        ENTER,
        ESCAPE
    }
    public MainMenuScreen(ThunderFighter game) {
        this.game = game;
        stage = new Stage(new StretchViewport(240, 320)); // FIXME set width and height
        selectPlayerStage = new Stage(new StretchViewport(240, 320));
        final float x = 70, y = 30;
        Texture background = new Texture("menu/MenuBackground.png");
        Texture title = new Texture("menu/Title.png");
        Texture gameStartTex = new Texture("menu/StartSelect1.png");
        Texture settingTex = new Texture("menu/StartSelect2.png");
        Texture achieveTex = new Texture("menu/StartSelect3.png");
        Texture helpTex = new Texture("menu/StartSelect4.png");
        Texture exitTex = new Texture("menu/StartSelect5.png");

        Actor backgroundActor = new BackgroundActor(background, title, 20f, 220f,"TITLE");
        Actor gameStartActor = new MenuItemActor(x, y, gameStartTex, MenuItem.GAME_START.name());
        Actor settingActor = new MenuItemActor(x, y, settingTex, MenuItem.GAME_SETTING.name());
        Actor achieveActor = new MenuItemActor(x, y, achieveTex, MenuItem.GAME_ACHIEVE.name());
        Actor helpActor = new MenuItemActor(x, y, helpTex, MenuItem.GAME_HELP.name());
        Actor exitActor = new MenuItemActor(x, y, exitTex, MenuItem.GAME_EXIT.name());

        settingActor.setVisible(false);
        achieveActor.setVisible(false);
        helpActor.setVisible(false);
        exitActor.setVisible(false);

        stage.addActor(backgroundActor);
        stage.addActor(gameStartActor);
        stage.addActor(settingActor);
        stage.addActor(achieveActor);
        stage.addActor(helpActor);
        stage.addActor(exitActor);

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touch down event triggered");
                return true;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    enterSelectedItem();
                }
                if (keycode == Input.Keys.ESCAPE) {
                    Gdx.app.exit();
                }
                if (keycode == Input.Keys.DOWN) {
                    selectNextItem(Command.NEXT);
                }
                if (keycode == Input.Keys.UP) {
                    selectNextItem(Command.PREV);
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
        AssetManager manager = game.getAssetManager();
        if (manager != null) {
            if (manager.update()) {
//                Gdx.app.log("assets-info", "load all assets done");
            }
        }
        if (isOpenSelectStage) {
            selectPlayerStage.act();
            selectPlayerStage.draw();
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
//        this.stage.dispose();
    }
    private void createSelectPlayerStage() {
        final Texture player1Tex = new Texture("select_player/SelectPlayer1.jpg");
        final Texture player2Tex = new Texture("select_player/SelectPlayer2.jpg");
        final Texture player3Tex = new Texture("select_player/SelectPlayer3.jpg");
        Texture select1Tex = new Texture("select_player/SelectPlayer4.png");
        Texture select2Tex = new Texture("select_player/SelectPlayer5.png");
        TextureRegion[] selectRegion = new TextureRegion[2];
        selectRegion[0] = new TextureRegion(select1Tex);
        selectRegion[1] = new TextureRegion(select2Tex);
        final Animation<TextureRegion> selectCursorAnimation = new Animation<>(0.5f, new Array<>(selectRegion), Animation.PlayMode.LOOP);

        final float x = 8, y = 80;
        currentCursorLocation = Point.FIRST;

        Actor player1Actor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                    batch.draw(player1Tex, x, y);
            }
        };
        Actor player2Actor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                    batch.draw(player2Tex, x, y);
            }
        };
        Actor player3Actor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                    batch.draw(player3Tex, x, y);
            }
        };

        player1Actor.setName(Point.FIRST.name());
        player2Actor.setName(Point.SECOND.name());
        player3Actor.setName(Point.THIRD.name());

        Actor selectCursor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                stateTime += Gdx.graphics.getDeltaTime();
                batch.draw(selectCursorAnimation.getKeyFrame(stateTime), currentCursorLocation.x, currentCursorLocation.y);
            }
        };
        // init first select player frame
        player1Actor.setVisible(true);
        player2Actor.setVisible(false);
        player3Actor.setVisible(false);


        selectPlayerStage.addActor(player1Actor);
        selectPlayerStage.addActor(player2Actor);
        selectPlayerStage.addActor(player3Actor);
        selectPlayerStage.addActor(selectCursor);

        selectPlayerStage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.LEFT) {
                    selectNextPlayer(Command.PREV);
                }
                if (keycode == Input.Keys.RIGHT) {
                    selectNextPlayer(Command.NEXT);
                }
                if (keycode == Input.Keys.ENTER) {
                    // jump to game start screen
                    game.setScreen(new GameStartScreen(game, currentCursorLocation));
                }
                if (keycode == Input.Keys.ESCAPE) {
                    // return to main menu stage
                    closeSelectPlayerStage();
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
    private void closeSelectPlayerStage() {
        isOpenSelectStage = false;
        Gdx.input.setInputProcessor(stage);
    }
    private void openSelectPlayerStage() {
        isOpenSelectStage = true;
        Gdx.input.setInputProcessor(selectPlayerStage);
    }
    private void enterSelectedItem() {
        int selectedIndex = getSelectedIndex();
        MenuItem[] itemArray = MenuItem.values();
        if (MenuItem.GAME_START == itemArray[selectedIndex]) {
            if (!hasCreateSelectStage) {
                createSelectPlayerStage();
            }
            // open Select player stage
            openSelectPlayerStage();
        } else if (MenuItem.GAME_SETTING == itemArray[selectedIndex]) {

        } else if (MenuItem.GAME_ACHIEVE == itemArray[selectedIndex]) {

        } else if (MenuItem.GAME_HELP == itemArray[selectedIndex]) {
            if (game != null) {
                Screen helpScreen =  game.getHelpScreen();
                game.setScreen(helpScreen);
                this.dispose();
            }
        } else {
             Gdx.app.exit();
        }
    }
    private int getSelectedIndex() {
        int selectedIndex = -1;
        for (Actor actor: new Array.ArrayIterator<>(stage.getActors())) {
            if (actor instanceof  MenuItemActor && actor.isVisible()) {
                MenuItem currentMenuItem = MenuItem.valueOf(actor.getName());
                return currentMenuItem.ordinal();
            }
        }
        return  selectedIndex;
    }
    private int getCurrentSelectPlayer() {
        return currentCursorLocation.ordinal();
    }

    private void resetSelectedItem() {
        for (Actor actor : new Array.ArrayIterator<>(stage.getActors())) {
            if (actor instanceof  MenuItemActor ) {
                actor.setVisible(false);
                if (MenuItem.valueOf(actor.getName()).ordinal() == 0) {
                    actor.setVisible(true);
                }
            }
        }
    }

    private void selectNextPlayer(Command command) {
        int currentSelectPlayer = getCurrentSelectPlayer();
        int prevSelectPlayer = currentSelectPlayer;

        int length = Point.values().length;
        if (command == Command.NEXT) {
            currentSelectPlayer = (currentSelectPlayer + 1) % length;
        } else { // prev command
            currentSelectPlayer = (currentSelectPlayer + length - 1) % length;
        }

        // set current select cursor location
        currentCursorLocation = Point.values()[currentSelectPlayer];
        // set current select player visible and prev select player invisible
        for (Array.ArrayIterator<Actor> it = new Array.ArrayIterator<>(selectPlayerStage.getActors()); it.hasNext();) {
            Actor playerActor = it.next();
            if (playerActor.getName()!= null) {
                if (playerActor.getName().equals(Point.values()[prevSelectPlayer].name())) {
                    playerActor.setVisible(false);
                } else if (playerActor.getName().equals(Point.values()[currentSelectPlayer].name())) {
                    playerActor.setVisible(true);
                }
            }
        }
    }
    private void selectNextItem(Command comm) {
        int currentSelectedIndex = getSelectedIndex();
        if (currentSelectedIndex == -1) {
            currentSelectedIndex = 0;
            resetSelectedItem();
        }
        int length = MenuItem.values().length;
        int nextSelectedIndex = -1;
        if (comm == Command.PREV)
            nextSelectedIndex = (currentSelectedIndex + length - 1) % length; // prevent negative number lead to MOD caculation disabled. e.g. -1 % 5 == -1 instead of 4
        else if (comm == Command.NEXT)
            nextSelectedIndex = (currentSelectedIndex + 1) % length;

        for (Array.ArrayIterator<Actor> iterator = new Array.ArrayIterator<>(stage.getActors()); iterator.hasNext();) {
            Actor actor = iterator.next();
            if (actor instanceof  MenuItemActor ) {
                if (MenuItem.valueOf(actor.getName()) == MenuItem.values()[nextSelectedIndex])
                    actor.setVisible(true);
                else
                    actor.setVisible(false);
            }
        }
    }
}
