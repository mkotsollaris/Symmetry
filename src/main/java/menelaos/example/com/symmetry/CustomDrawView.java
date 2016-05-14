/**
//Copyright © 2015 by Menelaos Kotsollaris
 */
package menelaos.example.com.symmetry;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//Todo use hardware - accelerator GPU ~ might speedup onDraw

public class CustomDrawView extends View
{
    /** Applies parameters to the entire list
     *
     * @param color the preferred color
     * @param paintStyle the preferred Paint.Style
     **/
    public void applyAll(int color, Paint.Style paintStyle)
    {
        Iterator itr = schemaList.iterator();
        while(itr.hasNext())
        {
            Schema schema = (Schema) itr.next();
            schema.getPaint().setColor(color);
            schema.getPaint().setStyle(paintStyle);
        }
        invalidate();
    }

    public void applyAll(int color)
    {
        Iterator itr = schemaList.iterator();
        while(itr.hasNext())
        {
            Schema schema = (Schema) itr.next();
            schema.getPaint().setColor(color);
        }
        invalidate();
    }

    /**
     * Gets called when SchemaList changes any of it's attributes
     **/
    public interface OnSchemaChangedListener
    {
        /** new Schema added in given index */
        void schemaCreated(int index);
        /** Schema deleted */
        void schemaDeleted(int index);
        /** all Schemas deleted */
        void schemasReseted();
        /** schema edited */
        void schemaEdited(int index);
    }

    public enum Mode
    {
        DRAWING, //drawing a Schema
        FOCUSING, //focusing a specific Schema
        DISABLED //disables the view
    }


    public void setOnSchemaChangedListener(OnSchemaChangedListener onSchemaCreatedListener)
    {
        this.onSchemaChangedListener = onSchemaCreatedListener;
    }

    private String extraInformation;

    /** Schema from the Palette that is selected to make draws */
    private SelectedSchema selectedSchema;

    /** Contains all shapes have been drawn during in the past */
    private List<Schema> schemaList;

    /** current Paint preferences */
    private Paint paint;

    /** OnSchemaCreated event */
    private OnSchemaChangedListener onSchemaChangedListener;

    /** Current Mode (Drawing or Focusing) a schema */
    private Mode mode;
    private int selectedSchemaIndex;
    public void setSelectedSchemaIndex(int selectedSchemaIndex)
    {
        this.selectedSchemaIndex = selectedSchemaIndex;
    }

    private int getSelectedSchemaIndex()
    {
        return selectedSchemaIndex;
    }

    public List<Schema> getSchemaList()
    {
        return schemaList;
    }

    public SelectedSchema getSelectedSchema()
    {
        return selectedSchema;
    }

    public void setSelectedSchema(SelectedSchema selectedSchema)
    {
        this.selectedSchema = selectedSchema;
    }

    public CustomDrawView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomDrawView,
                0, 0
        );
        //release the TypedArray so that it can be reused.
        typedArray.recycle();
        init();
    }

    private AttributeSet attributeSet;

    public void setAttributeSet(AttributeSet attributeSet)
    {
        this.attributeSet = attributeSet;
    }

    public AttributeSet getAttributeSet()
    {
        return attributeSet;
    }

    /**
     * Helpful List containing startingX,startingY,endingX,endingY between
     * Action_DOWN and and Action_MOVE. Resets on Action_UP
     * */
    List<Coordinate> coordinateContainer;

    /**
     * Initialization of CustomDrawView
     **/
    private void init()
    {
        //todo current linkedList .add 0(1) but O(n) get/remove
        schemaList = new LinkedList<>();
        paint = new Paint();
        paint.setColor(Color.BLACK);//setting the color
        paint.setStrokeWidth(5);//setting the size
        paint.setDither(false);//setting the dither to true
        paint.setStyle(Paint.Style.STROKE);//setting to STOKE
        paint.setStrokeJoin(Paint.Join.ROUND);//setting the join to round you want
        paint.setStrokeCap(Paint.Cap.ROUND);//setting the paint cap to round too
        //paint.setPathEffect(new CornerPathEffect(10) );//set the path effect when they join
        paint.setAntiAlias(true);//setting anti alias so it smooths
        mode = Mode.DRAWING;

        /** Initializing objects onCreate for better performance */
        coordinateContainer = new ArrayList<>(2);
        for(int i=0;i<2;i++)
        {
            coordinateContainer.add(new Coordinate(-1,-1));
        }
        System.out.println(coordinateContainer.size());
    }

    public void setMode(Mode mode)
    {
        this.mode = mode;
    }

    public void swapSchemaList(int selectedItemIndex1, int selectedItemIndex2)
    {
        Collections.swap(schemaList,selectedItemIndex1,selectedItemIndex2);
        invalidate();
    }

    public void setPaintColor(int color)
    {
        paint.setColor(color);
    }

    public void clear()
    {
        schemaList.clear();
        onSchemaChangedListener.schemasReseted();
        invalidate();
    }

    public void deleteSchema(int index)
    {
        try
        {
            onSchemaChangedListener.schemaDeleted(index);
            schemaList.remove(index);
            invalidate();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Being called in initialization and after each invalidate()
     **/
    @Override
    public void onDraw(Canvas canvas)
    {
        Iterator itr = schemaList.iterator();
        while(itr.hasNext())
        {
            Schema schema = (Schema) itr.next();
            schema.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event)
    {
        if (mode != Mode.DISABLED && getParent() != null)
        {
            getParent().requestDisallowInterceptTouchEvent(true);
        } else return false;

        int action = MotionEventCompat.getActionMasked(event);
        switch(action)
        {
            case (MotionEvent.ACTION_DOWN) :
                if(mode == Mode.DRAWING) initializeSchema(event);
                else if(mode == Mode.FOCUSING && selectedSchemaIndex>-1)
                {
                    coordinateContainer.get(0).setCordX(event.getX());
                    coordinateContainer.get(0).setCordY(event.getY());
                }
                return true;
            case (MotionEvent.ACTION_MOVE) :
                if(mode == Mode.DRAWING) applySchemaChanges(event);
                else if(mode == Mode.FOCUSING && selectedSchemaIndex>-1)
                {
                    coordinateContainer.get(1).setCordX(event.getX());
                    coordinateContainer.get(1).setCordY(event.getY());
                    changeSchemaPosition();
                    coordinateContainer.get(0).setCordX(event.getX());
                    coordinateContainer.get(0).setCordY(event.getY());
                }
                return true;
            case (MotionEvent.ACTION_UP) :
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                System.out.println("Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                System.out.println("Action was OUTSIDE OF BOUNDS");
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }

    /**
     * While in Mode.FOCUSING, controls position of selected Schema.
     * Runs on Action_Move
     * */
    private void changeSchemaPosition()
    {
        if(selectedSchemaIndex>-1)
        {
            float distanceX = coordinateContainer.get(0).getCordX() - coordinateContainer.get(1).getCordX() ;
            float distanceY = coordinateContainer.get(0).getCordY() - coordinateContainer.get(1).getCordY() ;
            Schema schema = schemaList.get(selectedSchemaIndex);
            schema.relocate(distanceX,distanceY);
        }
        invalidate();
    }

    /**
     *  Applies changes to the schema getting drawn, getting called while Action_MOVE
     **/
    private void applySchemaChanges(MotionEvent event)
    {
        float endX = event.getX();
        float endY = event.getY();

        if(!(selectedSchema == SelectedSchema.NONE))
        {
            Schema processedSchema = schemaList.get(schemaList.size()-1);
            processedSchema.makeCalculations(endX,endY);
            invalidate();
        }
    }

    /**
     * Runs when an ACTION_UP MotionEvent is detected by the View.
     * Initializes the schema that is getting drawn at that moment.
     **/
    private void initializeSchema(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        if(selectedSchema == SelectedSchema.NONE)
        {

        }
        else if(selectedSchema == SelectedSchema.LINE)
        {
            Line line = new Line(x,y,x,y,paint);
            schemaList.add(line);
            int index = schemaList.size()-1;
            onSchemaChangedListener.schemaCreated(index);
        }
        else if(selectedSchema == SelectedSchema.CIRCLE)
        {
            Circle circle = new Circle(x,y,x,y,0,paint);
            schemaList.add(circle);
            int index = schemaList.size()-1;
            onSchemaChangedListener.schemaCreated(index);
        }
        else if(selectedSchema == SelectedSchema.RECTANGULAR)
        {
            Rectangle rectangle = new Rectangle(x,y,paint);
            schemaList.add(rectangle);
            int index = schemaList.size()-1;
            onSchemaChangedListener.schemaCreated(index);
        }
        else if(selectedSchema == SelectedSchema.USER_CREATED)
        {
            UserCreated userCreated = new UserCreated(paint);
            userCreated.initializeStart(x,y);
            schemaList.add(userCreated);
            int index = schemaList.size()-1;
            onSchemaChangedListener.schemaCreated(index);
        }
        else if(selectedSchema == SelectedSchema.SQUARE)
        {
            Square square = new Square(x,y,paint);
            schemaList.add(square);
            int index = schemaList.size()-1;
            onSchemaChangedListener.schemaCreated(index);
        }
        else if(selectedSchema == SelectedSchema.TRIANGLE)
        {
            Triangle triangle = new Triangle(x,y,paint);
            schemaList.add(triangle);
            int index = schemaList.size()-1;
            onSchemaChangedListener.schemaCreated(index);
        }
        else if(selectedSchema == SelectedSchema.CROOKED_LINE)
        {
            if(schemaList.size()>0)
            {
                Schema processedSchema = schemaList.get(schemaList.size()-1);
                if(processedSchema instanceof CrookedLine)
                {
                    if(((CrookedLine) processedSchema).getSettled())
                    {
                        CrookedLine crookedLine = new CrookedLine(x,y,paint);
                        schemaList.add(crookedLine);
                        int index = schemaList.size()-1;
                        onSchemaChangedListener.schemaCreated(index);
                    }
                    else
                    {
                        ((CrookedLine)processedSchema).addCrookedLine(x,y);
                        invalidate();
                    }
                }
                else
                {
                    CrookedLine crookedLine = new CrookedLine(x,y,paint);
                    schemaList.add(crookedLine);
                    int index = schemaList.size()-1;
                    onSchemaChangedListener.schemaCreated(index);
                }
            }
            else
            {
                CrookedLine crookedLine = new CrookedLine(x,y,paint);
                schemaList.add(crookedLine);
                int index = schemaList.size()-1;
                onSchemaChangedListener.schemaCreated(index);
            }
        }
        else if(selectedSchema == SelectedSchema.POLYGON)
        {
            if(schemaList.size()>0)
            {
                Schema processedSchema = schemaList.get(schemaList.size()-1);
                if(processedSchema instanceof Polygon)
                {
                    if(((Polygon) processedSchema).getSettled())
                    {
                        Polygon polygon = new Polygon(x,y,paint);
                        schemaList.add(polygon);
                        int index = schemaList.size()-1;
                        onSchemaChangedListener.schemaCreated(index);
                    }
                    else
                    {
                        ((Polygon)processedSchema).addCrookedLine(x,y);
                        invalidate();
                    }
                }
                else
                {
                    Polygon polygon = new Polygon(x,y,paint);
                    schemaList.add(polygon);
                    int index = schemaList.size()-1;
                    onSchemaChangedListener.schemaCreated(index);
                }
            }
            else
            {
                Polygon polygon = new Polygon(x,y,paint);
                schemaList.add(polygon);
                int index = schemaList.size()-1;
                onSchemaChangedListener.schemaCreated(index);
            }
        }
    }

    int viewWidth;
    int viewHeight;

    public void setViewWidth(int viewWidth)
    {
        this.viewWidth = viewWidth;
    }

    public void setViewHeight(int viewHeight)
    {
        this.viewHeight = viewHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int desiredWidth = viewWidth;
        int desiredHeight = viewHeight;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        }
        else if (widthMode == MeasureSpec.AT_MOST)
        {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        }
        else
        {
            //Be whatever you want
            width = desiredWidth;
        }
        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY)
        {
            //Must be this size
            height = heightSize;
        }
        else if (heightMode == MeasureSpec.AT_MOST)
        {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        }
        else
        {
            //Be whatever you want
            height = desiredHeight;
        }
        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    /*TODO implement in order to fit in ScrollViews, possible rotations etc.
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
    }
    */
}