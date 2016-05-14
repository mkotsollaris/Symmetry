/**
//Copyright © 2015 by Menelaos Kotsollaris
 */
package menelaos.example.com.symmetry;

public class Coordinate
{
    private float cordX;
    private float cordY;

    public Coordinate(float cordX, float cordY)
    {
        this.cordX = cordX;
        this.cordY = cordY;
    }

    public void setCoord(float cordX, float cordY)
    {
        this.cordX = cordX;
        this.cordY = cordY;
    }

    public Coordinate() {}

    public void setCordX(float cordX){this.cordX = cordX;}
    public void setCordY(float cordY){this.cordY = cordY;}
    public float getCordX(){return cordX;}
    public float getCordY(){return cordY;}
}
