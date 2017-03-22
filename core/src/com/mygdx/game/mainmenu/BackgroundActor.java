package com.mygdx.game.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * Created by John on 2015/12/5.
 */
public class BackgroundActor extends Actor {
    private Animation<TextureRegion> backgroundAnim;
    private Texture title;
    private static float stateTime;
    public BackgroundActor(Texture backgroundTex, Texture title, float x, float y, String name) {
        this.setX(x);
        this.setY(y);
        this.setName(name);
        this.title = title;
        TextureRegion[][] backgroundRegion = TextureRegion.split(backgroundTex, 240, 320);
        this.backgroundAnim = new Animation<>(.3f, new Array<>(backgroundRegion[0]), Animation.PlayMode.LOOP);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        stateTime += Gdx.graphics.getDeltaTime();

        batch.draw(backgroundAnim.getKeyFrame(stateTime), 0, 0);
        batch.draw(title, getX(), getY());
    }
}
