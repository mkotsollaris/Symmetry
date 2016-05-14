

package menelaos.example.com.symmetry;

import android.app.Application;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import java.util.Iterator;
import java.util.List;

/**
//Copyright © 2015 by Menelaos Kotsollaris
 *
 * Contains General static Helper methods and Applications context
 */
public class GeneralMethods extends Application
{
    private static Context context;

    public void onCreate()
    {
        super.onCreate();
        GeneralMethods.context = getApplicationContext();
    }

    public static Context getAppContext()
    {
        return GeneralMethods.context;
    }

    /**
     *  Returns the distance between (x1,y1) and (x2,y2), based on Pythagoras theorem
     *  example: http://www.mathsisfun.com/algebra/distance-2-points.html
     **/
    public static float getAbsDistance(float x1, float y1, float x2, float y2)
    {
        return (float) Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1),2));
    }

    public static int getDeviceHeight()
    {
        Point point = GeneralMethods.getDeviceSize();
        return point.y;
    }

    public static int getDeviceWidth()
    {
        Point point = GeneralMethods.getDeviceSize();
        return point.x;
    }

    public static Point getDeviceSize()
    {
        Point deviceSize = new Point();
        WindowManager wm = (WindowManager) GeneralMethods.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(deviceSize);
        return deviceSize;
    }

    /**
     *  Implements Bresenham line drawing algorithm
     * */
    public static void drawLine(int x1, int y1, int x2, int y2, Paint paint, Canvas canvas)
    {
        int d = 0;

        int dy = Math.abs(y2 - y1);
        int dx = Math.abs(x2 - x1);

        int dy2 = (dy << 1);
        int dx2 = (dx << 1);

        int ix = x1 < x2 ? 1 : -1;
        int iy = y1 < y2 ? 1 : -1;

        if (dy <= dx)
        {
            for (;;)
            {
                //plot(g, x1, y1);
                canvas.drawPoint(x1,y1,paint);
                if (x1 == x2)
                    break;
                x1 += ix;
                d += dy2;
                if (d > dx)
                {
                    y1 += iy;
                    d -= dx2;
                }
            }
        } else
        {
            for (;;)
            {
                canvas.drawPoint(x1,y1,paint);
                if (y1 == y2)
                    break;
                y1 += iy;
                d += dx2;
                if (d > dy)
                {
                    x1 += ix;
                    d -= dy2;
                }
            }
        }
    }

    /**
     * Implements Bresenham's circle (midpoint) drawing algorithm
     **/
    public static void drawCircle(int x, int y, int radius, Paint paint, Canvas canvas)
    {
        int discriminant = (5 - radius<<2)>>2 ;
        int i = 0, j = radius ;
        while (i<=j)
        {
            canvas.drawPoint(x+i,y-j,paint);
            canvas.drawPoint(x+j,y-i,paint);
            canvas.drawPoint(x+i,y+j,paint);
            canvas.drawPoint(x+j,y+i,paint);
            canvas.drawPoint(x-i,y-j,paint);
            canvas.drawPoint(x-j,y-i,paint);
            canvas.drawPoint(x-i,y+j,paint);
            canvas.drawPoint(x-j,y+i,paint);
            i++ ;
            if (discriminant<0)
            {
                discriminant += (i<<1) + 1 ;
            }
            else
            {
                j-- ;
                discriminant += (1 + i - j)<<1 ;
            }
        }
    }


    public static void drawLines(List<Line> lineList, Paint paint, Canvas canvas)
    {
        Iterator itr = lineList.iterator();
        while (itr.hasNext())
        {
            Line line = (Line) itr.next();
            GeneralMethods.drawLine((int)line.startX,(int)line.startY,
                    (int)line.endX,(int)line.endY,paint,canvas);
        }
    }

    public static void drawPath(List<Coordinate> coordinateList, Paint paint, Canvas canvas)
    {
        for(int i=1;i<coordinateList.size();i++)
        {
            Coordinate coordinate1 = coordinateList.get(i-1);
            Coordinate coordinate2 = coordinateList.get(i);
            GeneralMethods.drawLine((int)coordinate1.getCordX(),(int)coordinate1.getCordY(),
                    (int)coordinate2.getCordX(),(int)coordinate2.getCordY(),paint,canvas);
        }
    }

    public static void drawRect(int left, int top, int right, int bottom, Paint paint, Canvas canvas)
    {
        GeneralMethods.drawLine(left,top,right,top,paint,canvas);
        GeneralMethods.drawLine(right,top,right,bottom,paint,canvas);
        GeneralMethods.drawLine(right,bottom,left,bottom,paint,canvas);
        GeneralMethods.drawLine(left,bottom,left,top,paint,canvas);
    }

    public static void drawTriangle(Coordinate[] coordinates, Paint paint, Canvas canvas)
    {
        GeneralMethods.drawLine((int)coordinates[0].getCordX(),(int)coordinates[0].getCordY(),
                (int)coordinates[1].getCordX(),(int)coordinates[1].getCordY(),paint,canvas);
        GeneralMethods.drawLine((int)coordinates[1].getCordX(),(int)coordinates[1].getCordY(),
                (int)coordinates[2].getCordX(),(int)coordinates[2].getCordY(),paint,canvas);
        GeneralMethods.drawLine((int)coordinates[2].getCordX(),(int)coordinates[2].getCordY(),
                (int)coordinates[0].getCordX(),(int)coordinates[0].getCordY(),paint,canvas);
    }
}