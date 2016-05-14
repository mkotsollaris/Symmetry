package menelaos.example.com.symmetry;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
//Copyright © 2015 by Menelaos Kotsollaris
 *
 * Represents a basic rectangle schema
 */
public class Rectangle extends Schema
{
    /** Container of core coordinate*/
    private float startingX;
    private float startingY;

    private float left;
    private float top;
    private float right;
    private float bottom;

    public Rectangle(float startingX, float startingY, Paint paint)
    {
        this.startingX = startingX;
        this.startingY = startingY;
        this.paint = new Paint(paint);
        makeCalculations(startingX,startingY);
    }

    /**
     * Makes calculations made on object on Action_UP
     **/
    public void makeCalculations(float endX, float endY)
    {
        boolean flag1=false,flag2=false;
        if(endX<startingX) flag1=true;
        if(endY<startingY) flag2=true;

        if(flag1 && flag2)
        {
            left=endX;
            top=endY;
            right=startingX;
            bottom=startingY;
        }
        else if(flag1)
        {
            left=endX;
            top=startingY;
            right=startingX;
            bottom=endY;
        }
        else if(flag2)
        {
            left=startingX;
            top=endY;
            right=endX;
            bottom=startingY;
        }
        else
        {
            left=startingX;
            top=startingY;
            right=endX;
            bottom=endY;
        }
    }

    @Override
    public void relocate(float distanceX, float distanceY)
    {
        left -=distanceX;
        right-=distanceX;
        top -=distanceY;
        bottom -=distanceY;
    }

    /**
     *todo check speed, slow at some point (after 60-70 schemas)
     **/
    public void draw(Canvas canvas)
    {
        GeneralMethods.drawRect((int)left,(int)top,(int)right,(int)bottom,paint,canvas);
    }

    public String getResourceTag()
    {
        return GeneralMethods.getAppContext().getResources().getString(R.string.rectangleTag);
    }
}