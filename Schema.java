/**
//Copyright © 2015 by Menelaos Kotsollaris
 */
package menelaos.example.com.symmetry;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Abstract class representing an abstract form of any drawable Shape
 **/

public abstract class Schema
{
    protected Paint paint;

    public Paint getPaint()
    {
        return paint;
    }
    public void setPaint(Paint paint)
    {
        this.paint = paint;
    }

    /**
     * NOTE: Use as less computes as possible in the method, as it will run
     * over and over again onDraw in the particular View.
     **/
    public abstract void draw(Canvas canvas);

    /**
     * Makes calculations of the Shape in order to full-fill its particular needs
     **/
    public abstract void makeCalculations(float endX, float endY);

    /**
     * Relocates the object based on its current position by
     * @param distanceX the X axis
     * @param distanceY the Y axis
     **/
    public abstract void relocate(float distanceX, float distanceY);

    /**
     * Gets the Tag of the object, that explains the kind of the class.
     * Tag is used for UI-Representation purposes
     **/
    public abstract String getResourceTag();
}