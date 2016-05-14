package menelaos.example.com.symmetry;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
//Copyright © 2015 by Menelaos Kotsollaris
 *
 * Represents a basic Line Schema
 */
public class Line extends Schema
{
    float startX;
    float startY;
    float endX;
    float endY;

    public Line(float startX, float startY, float endX, float endY, Paint paint)
    {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.paint = new Paint(paint);
    }

    public void setEndX(float endX){this.endX = endX;}
    public void setEndY(float endY){this.endY = endY;}

    public void draw(Canvas canvas)
    {
        GeneralMethods.drawLine((int)startX,(int)startY,(int)endX,(int)endY,paint,canvas);
    }
    public void makeCalculations(float endX, float endY)
    {
        this.endX = endX;
        this.endY = endY;
    }

    @Override
    public void relocate(float distanceX, float distanceY)
    {
        startX -= distanceX;
        startY -= distanceY;
        endX -= distanceX;
        endY -= distanceY;
    }

    public String getResourceTag()
    {
        return GeneralMethods.getAppContext().getResources().getString(R.string.lineTag);
    }
}