package com.mygdx.game.mainmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by John on 2015/12/4.
 */
public class MenuItemActor extends Actor {
    private Texture texture;
    public MenuItemActor(float x, float y, Texture texture, String name) {
        this.setX(x);
        this.setY(y);
        this.texture = texture;
        this.setName(name);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, getX(), getY());
    }
}
