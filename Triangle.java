package menelaos.example.com.symmetry;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import java.util.Arrays;

/**
//Copyright © 2015 by Menelaos Kotsollaris
 *
 * Represents a basic Triangle Schema
 */
public class Triangle extends Schema
{
    private float startingX;
    private float startingY;
    private Path path;

    Coordinate [] coordinates;

    public Triangle(float startingX, float startingY, Paint paint)
    {
        /** Initialization */
        coordinates = new Coordinate[3];
        for(int i=0;i<coordinates.length;i++) coordinates[i] = new Coordinate(-1,-1);
        this.startingX = startingX;
        this.startingY = startingY;
        this.paint = new Paint(paint);
        path = new Path();
    }

    /**
     * Creates triangle's path based on endX and endY
     **/
    public void makeCalculations(float endX, float endY)
    {
        float vertexX = (startingX+endX)/2;
        float vertexY = startingY;

        coordinates[0].setCoord(vertexX,vertexY);
        coordinates[1].setCoord(endX,endY);
        coordinates[2].setCoord(startingX,endY);
        updatePath();
    }

    private void updatePath()
    {
        path.reset();

        path.moveTo(coordinates[0].getCordX(), coordinates[0].getCordY());
        path.lineTo(coordinates[1].getCordX(), coordinates[1].getCordY());
        path.lineTo(coordinates[2].getCordX(), coordinates[2].getCordY());
        path.lineTo(coordinates[0].getCordX(), coordinates[0].getCordY());
    }

    @Override
    public void relocate(float distanceX, float distanceY)
    {
        for(int i=0;i<coordinates.length;i++)
        {
            coordinates[i].setCordX(coordinates[i].getCordX()-distanceX);
            coordinates[i].setCordY(coordinates[i].getCordY() - distanceY);
        }
        updatePath();
    }

    public void draw(Canvas canvas)
    {
        GeneralMethods.drawTriangle(coordinates,paint,canvas);
    }

    public String getResourceTag()
    {
        return GeneralMethods.getAppContext().getResources().getString(R.string.triangleTag);
    }
}
