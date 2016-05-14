/**
 * //Copyright © 2015 by Menelaos Kotsollaris
 */
package menelaos.example.com.symmetry;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Contains Starting Point and Ending Point as members
 * Calculates what's needed in order to draw Circle
 **/
public class Circle extends Schema
{
    /**
     * Container of above objects in order to avoid more calculations while being onDraw
     **/
    private final float startingX;
    private final float startingY;

    /**
     * members needed for draw()
     **/
    private float cx;
    private float cy;
    private float radius;

    public Circle(float startingX, float startingY, float cx, float cy, float radius, Paint paint)
    {
        this.startingX=startingX;
        this.startingY=startingY;
        this.cx=cx;
        this.cy=cy;
        this.radius=radius;
        this.paint = new Paint(paint);
    }

    private float getStartingX(){return startingX;}
    private float getStartingY(){return startingY;}

    /**
     * Prepares object parameters given the new parameters while being inside Action_MOVE
     **/
    public void makeCalculations( float endX, float endY)
    {
        /** Finding minimum distance in order to calculate the new cx,cy,radius */
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
        boolean flag1 = false;
        boolean flag2 = false;

        if (endX < getStartingX()) flag1 = true;
        if (endY < getStartingY()) flag2 = true;

        float minDistance;
        minDistance = (possibleHorizontalDistance < possibleVerticalDistance) ?
                possibleHorizontalDistance : possibleVerticalDistance;

        float wantedX;
        float wantedY;

        if (flag1 && flag2)
        {
            wantedX = startingX-minDistance;
            wantedY = startingY-minDistance;
        }
        else if (flag1)
        {
            wantedX = startingX-minDistance;
            wantedY = startingY+minDistance;
        }
        else if (flag2)
        {
            wantedX = startingX+minDistance;
            wantedY = startingY-minDistance;
        }
        else
        {
            wantedX = startingX+minDistance;
            wantedY = startingY+minDistance;
        }

        float medianX = (startingX+wantedX)/2;
        float medianY = (startingY+wantedY)/2;
        float distance = GeneralMethods.getAbsDistance(medianX, medianY, (startingX + wantedX) / 2, wantedY);

        cx = medianX;
        cy = medianY;
        radius = distance;
    }

    public void relocate(float distanceX, float distanceY)
    {
        cx -= distanceX;
        cy -= distanceY;
    }

    public void draw(Canvas canvas)
    {
        GeneralMethods.drawCircle((int)cx,(int)cy,(int)radius,paint,canvas);
    }

    public String getResourceTag()
    {
        return GeneralMethods.getAppContext().getResources().getString(R.string.circleTag);
    }
}