/**
//Copyright © 2015 by Menelaos Kotsollaris
 */
package menelaos.example.com.symmetry;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents user-created Schemas, saving up all given gestures
 **/
public class UserCreated extends Schema
{
    protected Path path;
    private List<Coordinate> coordinateList;

    public UserCreated(Paint paint)
    {
        path = new Path();
        coordinateList = new LinkedList<>();
        this.paint = new Paint(paint);
        this.paint.setStyle(Paint.Style.STROKE);//avoiding Paint.Style.Fill possibility
    }

    public void initializeStart(float startX, float startY)
    {
        Coordinate coordinate = new Coordinate(startX,startY);
        coordinateList.add(coordinate);
        path.moveTo(startX,startY);
    }

    public void draw(Canvas canvas)
    {
        GeneralMethods.drawPath(coordinateList,paint,canvas);
    }

    public void makeCalculations(float endX, float endY)
    {
        Coordinate coordinate = new Coordinate(endX,endY);
        coordinateList.add(coordinate);
        path.lineTo(endX,endY);
    }

    public void relocate(float distanceX, float distanceY)
    {
        Iterator itr = coordinateList.iterator();
        while (itr.hasNext())
        {
            Coordinate coordinate = (Coordinate) itr.next();
            float x = coordinate.getCordX()-distanceX;
            float y = coordinate.getCordY()-distanceY;
            coordinate.setCordX(x);
            coordinate.setCordY(y);
        }
        recreatePath();
    }

    private void recreatePath()
    {
        path.reset();
        Iterator itr = coordinateList.iterator();
        if(itr.hasNext())
        {
            Coordinate coordinate = (Coordinate) itr.next();
            float x = coordinate.getCordX();
            float y = coordinate.getCordY();
            path.moveTo(x,y);
        }
        while(itr.hasNext())
        {
            Coordinate coordinate = (Coordinate) itr.next();
            float x = coordinate.getCordX();
            float y = coordinate.getCordY();
            path.lineTo(x,y);
        }
    }

    public String getResourceTag()
    {
        return GeneralMethods.getAppContext().getResources().getString(R.string.user_createdTag);
    }
}