/**
//Copyright © 2015 by Menelaos Kotsollaris
 */
package menelaos.example.com.symmetry;
/**
 *  All possible Shapes that can be drawn in CustomDrawView
 **/
public enum SelectedSchema
{
    NONE,//for possible Navigation in CustomDrawView
    LINE,
    CROOKED_LINE,
    USER_CREATED,//not restricted from any Geometry
    CIRCLE,
    TRIANGLE,
    RECTANGULAR,
    SQUARE,
    POLYGON
}