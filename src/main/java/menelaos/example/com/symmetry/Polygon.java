package menelaos.example.com.symmetry;

import android.graphics.Paint;
import java.util.List;

/**
//Copyright © 2015 by Menelaos Kotsollaris
 *
 * Represents a basic Polygon Schema as a CrookedLine
 */
public class Polygon extends CrookedLine
{
    public Polygon(float startingX, float startingY, Paint paint)
    {
        super(startingX,startingY,paint);
    }

    /**
     * Indicates whether or not the Polygon should be settled after Action_UP
     * */
    private boolean closeRestricts(float endX, float endY)
    {
        int th = 30;//Threshold
        Line firstLine = getLineList().get(0);
        float startX = firstLine.startX;
        float startY = firstLine.startY;
        return(GeneralMethods.getAbsDistance(startX, startY, endX, endY)<th);
    }

    @Override
    public void makeCalculations(float endX, float endY)
    {
        List<Line> lineList = getLineList();
        Line line = lineList.get(lineList.size()-1);
        if(closeRestricts(endX,endY) && lineList.size()>1)
        {
            Line firstLine = lineList.get(0);
            float startX = firstLine.startX;
            float startY = firstLine.startY;
            line.setEndX(startX);
            line.setEndY(startY);
            setSettled(true);
        }
        else
        {
            line.setEndX(endX);
            line.setEndY(endY);
            setSettled(false);
        }
        updatePath();
    }

    @Override
    public String getResourceTag()
    {
        return GeneralMethods.getAppContext().getResources().getString(R.string.polygonTag);
    }
}
