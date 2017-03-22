package com.mygdx.game.gamestart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectSet;

/**
 * Created by John on 2015/12/31.
 */
public class PlayerActor extends Actor {

    private TextureRegion[] playerTextureRegion ;
    private float destroyStateTime;
    private Animation<TextureRegion> destroyAnimation;
    private float velocity = 1;
    private ObjectSet<Status> statusSet;
    public PlayerActor(TextureRegion[] playerTextureRegion, float x, float y, String name, Animation<TextureRegion> destroyAnimation) {
        this.setX(x);
        this.setY(y);
        this.playerTextureRegion = playerTextureRegion;
        this.setName(name);
        statusSet = new ObjectSet<>();
        statusSet.add(Status.ALIVE);
        this.destroyAnimation = destroyAnimation;
    }

    public enum Status{
        UP,
        DOWN,
        LEFT,
        RIGHT,
        FIRE,
        ALIVE,
    }

    private TextureRegion getTextureByStatus() {
        if (statusSet.contains(Status.ALIVE)) {
            int xAxisForce = 0;
            int yAxisForce = 0;
            if (statusSet.contains(Status.LEFT))
                xAxisForce--;
            if (statusSet.contains(Status.RIGHT))
                xAxisForce++;
            if (statusSet.contains(Status.UP))
                yAxisForce++;
            if (statusSet.contains(Status.DOWN))
                yAxisForce--;

            if (xAxisForce == 0) {
                if (yAxisForce == 0) {
                    return playerTextureRegion[0];
                } else if (yAxisForce < 0) {
                    return playerTextureRegion[5];
                }
                return playerTextureRegion[1];
            } else if (xAxisForce < 0) {
                return playerTextureRegion[2];
            }
            return playerTextureRegion[3];
        }
        // means player has destroyed
        destroyStateTime += Gdx.graphics.getDeltaTime();
        return destroyAnimation.getKeyFrame(destroyStateTime);
    }

    private boolean isFiring() {
        return statusSet.contains(Status.ALIVE) && statusSet.contains(Status.FIRE);
    }

    private void removeOnDead() {
        if (!statusSet.contains(Status.ALIVE) && destroyAnimation.isAnimationFinished(destroyStateTime)) {// while player has dead do something on it
            System.out.println("player has been destroyed");
            destroyStateTime = 0f;
            this.remove();
        }
    }
    private void handlePlayerOperations() {
        if (!statusSet.contains(Status.ALIVE)) return;
        for (Status status: statusSet) {
            if (status == Status.UP && getY() < this.getStage().getHeight() - playerTextureRegion[0].getRegionHeight()) {
                setY(getY() + velocity);
            } else if (status == Status.DOWN && getY() > 0) {
                setY(getY() - velocity);
            } else if (status == Status.LEFT && getX() > 0) {
                setX(getX() - velocity);
            } else if (status == Status.RIGHT && getX() < this.getStage().getWidth() - playerTextureRegion[0].getRegionWidth()) {
                setX(getX() + velocity);
            }
        }
    }
    public ObjectSet<Status> getStatusSet() {
        return this.statusSet;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        handlePlayerOperations();
        batch.draw(getTextureByStatus(), getX(), getY());
        if (isFiring()) {
            batch.draw(playerTextureRegion[4], getX(), getY());
        }
        removeOnDead();
    }

}
