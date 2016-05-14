//Copyright © 2015 by Menelaos Kotsollaris

package menelaos.example.com.symmetry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Try_me34 on 30-Dec-14.
 *
 * Represents a crooked line (set of Lines)
 */
public class CrookedLine extends Schema
{
    private List<Line> lineList;
    /** Detects whether the creation of the CrookedLine has been settled (finished appending) */
    private boolean settled;
    private Path path;

    public CrookedLine(float startingX, float startingY, Paint paint)
    {
        Line line = new Line(startingX,startingY,startingX,startingY,paint);
        lineList = new LinkedList<>();
        lineList.add(line);
        settled = false;
        this.paint = new Paint(paint);
        path = new Path();
    }

    /**
     * Adds a new Line, based on the previous Line coordinate
     * Note that if there is no other previous line created, this method will throw
     * @throws IndexOutOfBoundsException exception
     * */
    public void addCrookedLine(float endX, float endY)
    {
        try
        {
            Line prevLine = lineList.get((lineList.size() - 1));
            float newStartX = prevLine.endX;
            float newStartY = prevLine.endY;
            Line line = new Line(newStartX, newStartY, endX, endY, paint);
            lineList.add(line);
            makeCalculations(endX,endY);
        }
        catch (IndexOutOfBoundsException ex)
        {
            ex.printStackTrace();
        }
    }
    public boolean getSettled()
    {
        return settled;
    }

    public List<Line> getLineList(){return lineList;}

    public void setSettled(boolean settled)
    {
        this.settled = settled;
    }

    public void makeCalculations(float endX, float endY)
    {
        Line line = lineList.get(lineList.size()-1);
        line.setEndX(endX);
        line.setEndY(endY);
        updatePath();
    }

    @Override
    public void relocate(float distanceX, float distanceY)
    {
        Iterator itr = lineList.iterator();
        while (itr.hasNext())
        {
            Line line = (Line) itr.next();
            line.relocate(distanceX,distanceY);
        }
        updatePath();
    }

    /**
     * Creates Path based on lineList
     * */
    protected void updatePath()
    {
        path.reset();

        Iterator itr = lineList.iterator();
        if(itr.hasNext())
        {
            Line currLine = (Line) itr.next();
            path.moveTo(currLine.startX,currLine.startY);
            path.lineTo(currLine.endX,currLine.endY);
        }
        while(itr.hasNext())
        {
            Line currLine = (Line) itr.next();
            path.lineTo(currLine.endX,currLine.endY);
        }
    }

    public void draw(Canvas canvas)
    {
        GeneralMethods.drawLines(lineList,paint,canvas);
    }

    public String getResourceTag()
    {
        return GeneralMethods.getAppContext().getResources().getString(R.string.crookedLineTag);
    }
}
