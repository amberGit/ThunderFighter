package com.mygdx.game.gamestart.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * Created by John on 2016/1/1.
 */
public class BlastUtil {
    private static AssetManager assetManager;
    private static final String assetKeyPrefix = "gameStart/blast/Blast";
    private static final String assetKeySuffix = ".png";
    private static final int blastTotalFrames = 6;
    private static final float blastFrameDuration = 0.2f;

    public static void setAssetManager(AssetManager assetManager) {
        BlastUtil.assetManager = assetManager;
    }
    public static void load() {
        for (int i = 3; i < 9; i++) {
            assetManager.load(assetKeyPrefix + i + assetKeySuffix, Texture.class);
        }
        for (int i = 1; i < 3; i++) {
            for (int j = 1; j < 5; j++) {
                assetManager.load(assetKeyPrefix + i + j + assetKeySuffix, Texture.class);
            }
        }
    }

    public static boolean update() {
        return assetManager.update();
    }

    public static Animation getRandomBlastAnimation() throws NullPointerException {
        if (assetManager == null) {
            throw new NullPointerException("Asset manager for blast is null");
        }
        int rand = MathUtils.random(1, 8);

        while (true) {
            if (rand < 3) {
                int i = 1;
                for (; i < 5; i++) {
                    if (!assetManager.isLoaded(assetKeyPrefix + rand + i + assetKeySuffix))
                        break;
                }
                if (i == 5) {
                    TextureRegion[] blastRegion = new TextureRegion[4];
                    for ( int j = blastRegion.length - 1; j > 0; j--) {
                        blastRegion[j] = new TextureRegion((Texture)assetManager.get(assetKeyPrefix + rand + j + assetKeySuffix));
                    }
                    return new Animation(blastFrameDuration, new Array<>(blastRegion), Animation.PlayMode.NORMAL);
                }
            } else if (assetManager.isLoaded(assetKeyPrefix + rand + assetKeySuffix)) {
                Texture randBlastTexture = assetManager.get(assetKeyPrefix + rand + assetKeySuffix);
                TextureRegion[][]  blastRegion = TextureRegion.split(randBlastTexture, randBlastTexture.getWidth() / blastTotalFrames, randBlastTexture.getHeight());
                return new Animation(blastFrameDuration, new Array<>(blastRegion[0]), Animation.PlayMode.NORMAL);
            }
            rand = MathUtils.random(1, 8);
        }
    }
}