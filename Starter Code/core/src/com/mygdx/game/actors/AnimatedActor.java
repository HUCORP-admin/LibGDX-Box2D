package com.mygdx.game.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import java.util.HashMap;

public class AnimatedActor extends BaseActor
{
    public float elapsedTime;
    protected Animation<TextureRegion> activeAnim;
    private String activeName;
    private HashMap<String, Animation<TextureRegion>> animationStorage;

    public AnimatedActor()
    {
        super();
        elapsedTime = 0;
        activeAnim = null;
        activeName = null;
        animationStorage = new HashMap<String,Animation<TextureRegion>>();
    }

    public void storeAnimation(String name, Animation<TextureRegion> anim)
    {
        animationStorage.put(name, anim);
        if (activeName == null)
            setActiveAnimation(name);
    }

    public void storeAnimation(String name, Texture tex)
    {
        TextureRegion reg = new TextureRegion(tex);
        TextureRegion[] frames = { reg };
        Animation<TextureRegion> anim = new Animation<TextureRegion>(1.0f, frames);
        storeAnimation(name, anim);
    }

    public void storeAnimation(String name, Texture tex, float duration)
    {
        TextureRegion reg = new TextureRegion(tex);
        TextureRegion[] frames = { reg };
        Animation<TextureRegion> anim = new Animation<TextureRegion>(duration, frames);
        storeAnimation(name, anim);
    }

    public void storeAnimation(String name, TextureRegion reg) {
        TextureRegion[] frames = { reg };
        Animation<TextureRegion> anim = new Animation<TextureRegion>(1, frames);
        storeAnimation(name, anim);
    }

    public void setActiveAnimation(String name)
    {
        if ( !animationStorage.containsKey(name) )
        {
            System.out.println("No animation: " + name);
            return;
        }

        if ( name.equals(activeName) ) {
            return; // already playing; no need to change...
        }

        activeName = name;
        activeAnim = animationStorage.get(name);
        elapsedTime = 0;

        // if width or height not set, then set them...
        if ( getWidth() == 0 || getHeight() == 0 )
        {
            Texture tex = activeAnim.getKeyFrame(0).getTexture();
            setWidth( tex.getWidth() );
            setHeight( tex.getHeight() );
        }
    }

    public String getAnimationName()
    {
        return activeName;
    }

    public void act(float dt)
    {
        super.act( dt );
        elapsedTime += dt;
    }

    public void draw(Batch batch, float parentAlpha)
    {
        region.setRegion( activeAnim.getKeyFrame(elapsedTime) );
        this.setSize(region.getRegionWidth(), region.getRegionHeight());
        super.draw(batch, parentAlpha);
    }

    public void copy(AnimatedActor original)
    {
        super.copy(original);
        this.elapsedTime = 0;
        this.animationStorage = original.animationStorage; // sharing a reference
        this.activeName = original.activeName;
        this.activeAnim = this.animationStorage.get( this.activeName );
    }

    public boolean isAnimationFinished() {
        return this.activeAnim.isAnimationFinished(elapsedTime);
    }

    public void resetAnimation(String name) {
        if ( !animationStorage.containsKey(name) )
        {
            System.out.println("No animation: " + name);
            return;
        }
        activeName = name;
        activeAnim = animationStorage.get(name);
        elapsedTime = 0;

        // if width or height not set, then set them...
        if ( getWidth() == 0 || getHeight() == 0 )
        {
            Texture tex = activeAnim.getKeyFrame(0).getTexture();
            setWidth( tex.getWidth() );
            setHeight( tex.getHeight() );
        }
    }

}