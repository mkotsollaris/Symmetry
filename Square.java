package menelaos.example.com.symmetry;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
//Copyright © 2015 by Menelaos Kotsollaris
 *
 * Represents a basic unordered square schema, given 2 corners
 */

public class Square extends Schema
{
    private float startingX;//starting X coordinate
    private float startingY;//starting Y coordinate

    private float left;
    private float top;
    private float right;
    private float bottom;

    public Square(float corner1X, float corner1Y, Paint paint)
    {
        this.startingX = corner1X;
        this.startingY = corner1Y;
        this.paint = new Paint(paint);
    }

    public float getStartingX(){return startingX;}
    public float getStartingY(){return startingY;}

    public void setStartingX(float startingX){this.startingX = startingX;}
    public void setStartingY(float startingY){this.startingY = startingY;}

    /**
     * Prepares object parameters given the new Corner parameters while being inside Action_MOVE.
     * Drawing based on max given input (horizontal vs vertical)
     * and computes based on Pythagoras Theorem the rest sides
     **/
    public void makeCalculations( float endX, float endY)
    {
        /** Finding minimum distance in order to calculate the new corner2X,corner2Y */
        float possibleVerticalDistance = GeneralMethods.getAbsDistance(
                getStartingX(),
                getStartingY(),
                endX,
                getStartingY()
        );
        float possibleHorizontalDistance = GeneralMethods.getAbsDistance(
                getStartingX(),
                getStartingY(),
                getStartingX(),
                endY
        );
        boolean flag1=false;
        boolean flag2=false;

        if(endX< getStartingX()) flag1=true;
        if(endY< getStartingY()) flag2=true;

        float minDistance;
        minDistance = (possibleHorizontalDistance<possibleVerticalDistance) ?
                possibleHorizontalDistance:possibleVerticalDistance;

        float corner2X,corner2Y;
        if(flag1 && flag2)
        {
            corner2X = startingX - minDistance;
            corner2Y = startingY - minDistance;

            left=corner2X;
            top=corner2Y;
            right= startingX;
            bottom= startingY;
        }
        else if(flag1)
        {
            corner2X = startingX - minDistance;
            corner2Y = startingY + minDistance;

            left=corner2X;
            top= startingY;
            right= startingX;
            bottom=corner2Y;
        }
        else if(flag2)
        {
            corner2X = startingX + minDistance;
            corner2Y = startingY - minDistance;

            left= startingX;
            top=corner2Y;
            right=corner2X;
            bottom= startingY;
        }
        else
        {
            corner2X = startingX + minDistance;
            corner2Y = startingY + minDistance;

            left= startingX;
            top= startingY;
            right=corner2X;
            bottom=corner2Y;
        }
    }

    @Override
    public void relocate(float distanceX, float distanceY)
    {
        left-=distanceX;
        right-=distanceX;
        top-=distanceY;
        bottom-=distanceY;
    }

    public void draw(Canvas canvas)
    {
        GeneralMethods.drawRect((int)left,(int)top,(int)right,(int)bottom,paint,canvas);
    }
    public String getResourceTag()
    {
        return GeneralMethods.getAppContext().getResources().getString(R.string.squareTag);
    }
}