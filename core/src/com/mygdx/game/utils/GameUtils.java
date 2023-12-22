package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

import com.badlogic.gdx.physics.box2d.Contact;

public class GameUtils
{
    // creates an Animation from a set of image files
    // name format: fileNamePrefix + N + fileNameSuffix, where 0 <= N < frameCount
    public static Animation parseImageFiles(String fileNamePrefix, String fileNameSuffix,
                                            int frameCount, float frameDuration, PlayMode mode)
    {
        TextureRegion[] frames = new TextureRegion[frameCount];

        for (int n = 0; n < frameCount; n++)
        {
            String fileName = fileNamePrefix + n + fileNameSuffix;
            Texture tex = new Texture(Gdx.files.internal(fileName));
            tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            frames[n] = new TextureRegion( tex );
        }

        Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);
        return new Animation(frameDuration, framesArray, mode);
    }

    // searches Contact for given type of object; returns null if not found
    // assumes Body user data stores reference to associated object
    public static Object getContactObject(Contact theContact, Class theClass)
    {
        Object objA = theContact.getFixtureA().getBody().getUserData();
        Object objB = theContact.getFixtureB().getBody().getUserData();

        if ( objA.getClass().equals(theClass) )
            return objA;
        else if ( objB.getClass().equals(theClass) )
            return objB;
        else
            return null;
    }

    // searches Contact for given type of object and given Fixture name;
    //    returns null if not found
    // assumes Body user data stores reference to associated object
    //    and Fixture user data stores String containing name
    public static Object getContactObject(Contact theContact, Class theClass, String fixtureName)
    {
        Object objA  = theContact.getFixtureA().getBody().getUserData();
        String nameA = (String)theContact.getFixtureA().getUserData();
        Object objB  = theContact.getFixtureB().getBody().getUserData();
        String nameB = (String)theContact.getFixtureB().getUserData();

        if ( objA.getClass().equals(theClass) && nameA.equals(fixtureName) )
            return objA;
        else if ( objB.getClass().equals(theClass) && nameB.equals(fixtureName))
            return objB;
        else
            return null;
    }
}