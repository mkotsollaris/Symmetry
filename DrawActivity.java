//Copyright © 2015 by Menelaos Kotsollaris

package menelaos.example.com.symmetry;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;


public class DrawActivity extends ActionBarActivity implements
        ColorPickerDialog.OnColorChangedListener, SchemaSimpleSettingsDialogFragment.NoticeDialogListener,
        NewDrawDialogFragment.NoticeDialogListener
{
    @Override
    public void onDialogPositiveClick(int color)
    {
        if(selectedItemIndex>-1)
        {
            Schema schema = customDrawView.getSchemaList().get(selectedItemIndex);
            schema.getPaint().setColor(color);
         /*   schema.getPaint().setStyle(paintStyle);*/
            this.color = color;
            customDrawView.invalidate();
        }
        else
        {
          /*  customDrawView.applyAll(color, paintStyle);*/
            customDrawView.applyAll(color);
        }
    }

    @Override
    public void onDialogNegativeClick()
    {

    }

    @Override
    public void onDialogPortraitClick(int width, int height)
    {
        Intent intent = getIntent();
        finish();
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        intent.putExtra("SCREEN_ORIENTATION", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startActivity(intent);
    }

    @Override
    public void onDialogLandscapeClick(int width, int height)
    {
        Intent intent = getIntent();
        finish();
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        intent.putExtra("SCREEN_ORIENTATION", ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        startActivity(intent);
    }

    private interface OnButtonClickListener
    {
        void onSchemaButtonClicked();
        void buttonClicked();
        void focusUIClicked();
    }

    /** Runs On Difference Color Selection */
    @Override
    public void colorChanged(int color)
    {
        this.color = color;
        customDrawView.setPaintColor(color);

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.ic_color);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        colorButton_activityDraw.setImageDrawable(drawable);
        System.out.println("selectedItemIndex "+selectedItemIndex);
        if(selectedItemIndex>-1)
        {
            customDrawView.getSchemaList().get(selectedItemIndex).getPaint().setColor(color);
            customDrawView.invalidate();
        }
    }

    /** Custom View used to develop all the draws */
    private CustomDrawView customDrawView;

    /** Selected Color */
    private int color;
    private ColorPickerDialog colorPickerDialog;
    private ImageButton colorButton_activityDraw;
    private HorizontalScrollView pallete_horizontalScrollView_activity_draw;
    /** Indicator for slide up/down animation of the HorizontalViews */
    private boolean palleteIsShowing;

    private TableRow schemaListTableRow_drawActivity; /** container list (HorizontalView)*/
    private int selectedItemIndex;/** item selected in container list (HorizontalView)*/
    private Button prevSelectedButton;/** used to reformat the Button to non-focused situation */

    private OnButtonClickListener onButtonClickListener;
    private ImageButton nextButton_activityDraw;
    private ImageButton prevButton_activityDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        loadActivity();
    }

    private void loadActivity()
    {
        customDrawView = (CustomDrawView) findViewById(R.id.activity_draw_customDrawView);
        pallete_horizontalScrollView_activity_draw = (HorizontalScrollView)
                findViewById(R.id.pallete_horizontalScrollView_activity_draw);
        colorButton_activityDraw = (ImageButton) findViewById(R.id.colorButton_activityDraw);
        ImageButton circleImageButton_activityDraw = (ImageButton) findViewById(R.id.circleImageButton_activityDraw);
        ImageButton crookedLineImageButton_activityDraw = (ImageButton) findViewById(R.id.crookedLineImageButton_activityDraw);
        ImageButton lineImageButton_activityDraw = (ImageButton) findViewById(R.id.lineImageButton_activityDraw);
        ImageButton polygonImageButton_activityDraw = (ImageButton) findViewById(R.id.polygonImageButton_activityDraw);
        ImageButton rectangleImageButton_activityDraw = (ImageButton) findViewById(R.id.rectangleImageButton_activityDraw);
        ImageButton squareImageButton_activityDraw = (ImageButton) findViewById(R.id.squareImageButton_activityDraw);
        ImageButton triangleImageButton_activityDraw = (ImageButton) findViewById(R.id.triangleImageButton_activityDraw);
        ImageButton userGivenImageButton_activityDraw = (ImageButton) findViewById(R.id.userGivenImageButton_activityDraw);
        schemaListTableRow_drawActivity = (TableRow) findViewById(R.id.schemaListTableRow_drawActivity);
        ImageButton removeButton_activityDraw =
                (ImageButton) findViewById(R.id.removeButton_activityDraw);
        ImageButton settingsButton_activityDraw = (ImageButton) findViewById(R.id.settingsButton_activityDraw);
        ImageButton backgroundPaintImageButton_activityDraw =
                (ImageButton) findViewById(R.id.backgroundPaintImageButton_activityDraw);
        nextButton_activityDraw = (ImageButton) findViewById(R.id.nextButton_activityDraw);
        prevButton_activityDraw = (ImageButton) findViewById(R.id.prevButton_activityDraw);
        ImageButton focusImageButton_activityDraw = (ImageButton) findViewById(R.id.focusImageButton_activityDraw);
        ImageButton newButton_activityDraw = (ImageButton) findViewById(R.id.newButton_activityDraw);
        final ImageButton showEntireViewButton_activityDraw =
                (ImageButton) findViewById(R.id.showEntireViewButton_activityDraw);

        Bundle extras = getIntent().getExtras();
        final int SCREEN_ORIENTATION = extras.getInt("SCREEN_ORIENTATION");
        if(SCREEN_ORIENTATION ==0) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        colorPickerDialog = new ColorPickerDialog(DrawActivity.this,
                DrawActivity.this,Color.BLACK);
        customDrawView.setPaintColor(Color.BLACK);
        customDrawView.setSelectedSchema(SelectedSchema.USER_CREATED);
        palleteIsShowing=true;
        selectedItemIndex = -1;
        colorChanged(Color.BLACK);
        int widthGiven = extras.getInt("width");
        int heightGiven = extras.getInt("height");
        customDrawView.setViewWidth(widthGiven);
        customDrawView.setViewHeight(heightGiven);
        customDrawView.measure(widthGiven,heightGiven);

        showEntireViewButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Animation slide1;//used for 1st HorizontalView
                Animation slide2;//used for 2nd HorizontalView
                int viewType;
                HorizontalScrollView schemaListHorizontalView_activity_draw =
                        (HorizontalScrollView) findViewById(R.id.schemaListHorizontalView_activity_draw);
                ImageButton removeButton = (ImageButton) findViewById(R.id.removeButton_activityDraw);
                ImageButton settingsButton = ( ImageButton) findViewById(R.id.settingsButton_activityDraw);
                if(palleteIsShowing)
                {
                    palleteIsShowing = false;
                    viewType = View.GONE;
                    Resources res = getResources();
                    Drawable drawable = res.getDrawable(R.drawable.ic_unhide);
                    showEntireViewButton_activityDraw.setImageDrawable(drawable);
                }
                else
                {
                    Resources res = getResources();
                    Drawable drawable = res.getDrawable(R.drawable.ic_hide);
                    showEntireViewButton_activityDraw.setImageDrawable(drawable);
                    palleteIsShowing = true;
                    slide1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                    slide2 =  AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_from_down);
                    pallete_horizontalScrollView_activity_draw.startAnimation(slide1);
                    schemaListHorizontalView_activity_draw.startAnimation(slide2);
                    removeButton.startAnimation(slide2);
                    settingsButton.startAnimation(slide2);
                    nextButton_activityDraw.startAnimation(slide2);
                    prevButton_activityDraw.startAnimation(slide2);
                    viewType = View.VISIBLE;
                }
                pallete_horizontalScrollView_activity_draw.setVisibility(viewType);
                schemaListHorizontalView_activity_draw.setVisibility(viewType);
                removeButton.setVisibility(viewType);
                settingsButton.setVisibility(viewType);
                nextButton_activityDraw.setVisibility(viewType);
                prevButton_activityDraw.setVisibility(viewType);
            }
        });

        backgroundPaintImageButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickListener.focusUIClicked();
                ColorPickerDialog cPicker;
                cPicker = new ColorPickerDialog(DrawActivity.this,new ColorPickerDialog.OnColorChangedListener()
                {
                    @Override
                    public void colorChanged(int color)
                    {
                        customDrawView.setBackgroundColor(color);
                    }
                },Color.WHITE);
                cPicker.show();
                cPicker.getWindow().setLayout(225,330);
            }
        });

        newButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickListener.buttonClicked();
                NewDrawDialogFragment newDrawDialogFragment = new NewDrawDialogFragment();
                newDrawDialogFragment.show(getSupportFragmentManager(), "newDrawDialogFragment");
            }
        });

        focusImageButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickListener.focusUIClicked();
                customDrawView.setMode(CustomDrawView.Mode.DISABLED);
                unFocusSchema();
            }
        });

        prevButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                View view1 = schemaListTableRow_drawActivity.getChildAt(selectedItemIndex);
                View view2 = schemaListTableRow_drawActivity.getChildAt(selectedItemIndex-1);
                if(view1!=null && view2 != null)
                {
                    schemaListTableRow_drawActivity.removeView(view1);
                    schemaListTableRow_drawActivity.removeView(view2);
                    schemaListTableRow_drawActivity.addView(view1, selectedItemIndex - 1);
                    schemaListTableRow_drawActivity.addView(view2,selectedItemIndex);
                    customDrawView.swapSchemaList(selectedItemIndex,selectedItemIndex-1);
                    selectedItemIndex--;
                    customDrawView.setSelectedSchemaIndex(selectedItemIndex);
                }
            }
        });

        nextButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                View view1 = schemaListTableRow_drawActivity.getChildAt(selectedItemIndex);
                View view2 = schemaListTableRow_drawActivity.getChildAt(selectedItemIndex+1);
                if(view1!=null && view2 != null)
                {
                    schemaListTableRow_drawActivity.removeView(view1);
                    schemaListTableRow_drawActivity.removeView(view2);
                    schemaListTableRow_drawActivity.addView(view2,selectedItemIndex);
                    schemaListTableRow_drawActivity.addView(view1,selectedItemIndex+1);
                    customDrawView.swapSchemaList(selectedItemIndex,selectedItemIndex+1);
                    selectedItemIndex++;
                    customDrawView.setSelectedSchemaIndex(selectedItemIndex);
                }
            }
        });

        settingsButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickListener.buttonClicked();
                System.out.println("selectedItemIndex "+selectedItemIndex);
                SchemaSimpleSettingsDialogFragment settingsDialogFragment = new SchemaSimpleSettingsDialogFragment();
                settingsDialogFragment.setSelectedItemIndex(selectedItemIndex);
                settingsDialogFragment.setColor(color);
                if(selectedItemIndex>-1)
                {
                    settingsDialogFragment.setPaintStyle(
                            customDrawView.getSchemaList().get(selectedItemIndex).getPaint().getStyle()
                    );
                }
                settingsDialogFragment.show(getSupportFragmentManager(), "settingsDialogFragment");
            }
        });

        removeButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickListener.buttonClicked();
                List<Schema> schemaList = customDrawView.getSchemaList();
                if(schemaList.size()>0 && selectedItemIndex>-1)
                {
                    customDrawView.deleteSchema(selectedItemIndex);
                }
                else
                {
                    CharSequence text = "Select an item to delete!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
            }
        });

        customDrawView.setOnSchemaChangedListener(new CustomDrawView.OnSchemaChangedListener()
        {
            @Override
            public void schemaCreated(int index)
            {
                addSchemaToContainer(index);
            }

            @Override
            public void schemaDeleted(int index)
            {
                deleteSchemaFromContainer(index);
            }

            @Override
            public void schemasReseted()
            {
                resetSchemasFromContainer();
            }

            @Override
            public void schemaEdited(int index)
            {
                System.out.println("schema edited Listener");
            }
        });

        colorButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                colorPickerDialog = new ColorPickerDialog(DrawActivity.this,
                        DrawActivity.this,Color.BLACK);
                colorPickerDialog.show();
                colorPickerDialog.getWindow().
                        setLayout(225,330);
            }
        });

        polygonImageButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickListener.onSchemaButtonClicked();
                customDrawView.setMode(CustomDrawView.Mode.DRAWING);
                customDrawView.setSelectedSchema((SelectedSchema.POLYGON));
            }
        });
        crookedLineImageButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickListener.onSchemaButtonClicked();
                customDrawView.setMode(CustomDrawView.Mode.DRAWING);
                customDrawView.setSelectedSchema(SelectedSchema.CROOKED_LINE);
            }
        });

        triangleImageButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickListener.onSchemaButtonClicked();
                customDrawView.setMode(CustomDrawView.Mode.DRAWING);
                customDrawView.setSelectedSchema(SelectedSchema.TRIANGLE);
            }
        });

        squareImageButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickListener.onSchemaButtonClicked();
                customDrawView.setMode(CustomDrawView.Mode.DRAWING);
                customDrawView.setSelectedSchema(SelectedSchema.SQUARE);
            }
        });

        userGivenImageButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickListener.onSchemaButtonClicked();
                customDrawView.setMode(CustomDrawView.Mode.DRAWING);
                customDrawView.setSelectedSchema(SelectedSchema.USER_CREATED);
            }
        });

        lineImageButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickListener.onSchemaButtonClicked();
                customDrawView.setMode(CustomDrawView.Mode.DRAWING);
                customDrawView.setSelectedSchema(SelectedSchema.LINE);
            }
        });

        circleImageButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickListener.onSchemaButtonClicked();
                customDrawView.setMode(CustomDrawView.Mode.DRAWING);
                customDrawView.setSelectedSchema(SelectedSchema.CIRCLE);
            }
        });

        rectangleImageButton_activityDraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClickListener.onSchemaButtonClicked();
                customDrawView.setMode(CustomDrawView.Mode.DRAWING);
                customDrawView.setSelectedSchema(SelectedSchema.RECTANGULAR);
            }
        });

        onButtonClickListener = new OnButtonClickListener()
        {
            @Override
            public void onSchemaButtonClicked()
            {
                List<Schema> schemaList = customDrawView.getSchemaList();
                if (schemaList.size() > 0)
                {
                    Schema lastSchema = schemaList.get(schemaList.size() - 1);
                    if (lastSchema instanceof CrookedLine)
                        ((CrookedLine) lastSchema).setSettled(true);
                }
                unFocusSchema();
                selectedItemIndex=-1;
                customDrawView.setSelectedSchemaIndex(selectedItemIndex);
                customDrawView.setMode(CustomDrawView.Mode.FOCUSING);
            }

            @Override
            public void buttonClicked()
            {
                System.out.println("button Clicked!");
                List<Schema> schemaList = customDrawView.getSchemaList();
                if (schemaList.size() > 0)
                {
                    Schema lastSchema = schemaList.get(schemaList.size() - 1);
                    if (lastSchema instanceof CrookedLine)
                        ((CrookedLine) lastSchema).setSettled(true);
                }
                customDrawView.setMode(CustomDrawView.Mode.FOCUSING);
                customDrawView.setSelectedSchemaIndex(selectedItemIndex);
            }

            @Override
            public void focusUIClicked()
            {
                customDrawView.setMode(CustomDrawView.Mode.DISABLED);
                unFocusSchema();
            }
        };
    }


    private void resetSchemasFromContainer()
    {
        schemaListTableRow_drawActivity.removeAllViews();
    }

    /** Loads all schemas that are inside schemaList in TableLayout
     *
     *  Note: schemaList is treated as LinkedList ( get/remove: O(n), add O(1) ).
     *  In case of choosing other type (like ArrayList), consider using the best-cost
     *  method.
     **/
    private void loadSchemaContainer(List<Schema> schemaList)
    {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                60,
                40);
        params.setMargins(15, 0, 15, 0);
        Iterator itr = schemaList.iterator();
        int counter=0;
        while (itr.hasNext())
        {
            Schema schema = (Schema) itr.next();
            /** Can get maxWidth in pixels in order to handle names etc */
            final Button view = (Button) LayoutInflater.from(this).inflate(R.layout.item_button, null);
            view.setTag(counter);
            schemaListTableRow_drawActivity.addView(view, params);
            counter++;
        }
    }

    /**
     * Adds the last element of schemaList on the UI list.
     * @param index the index that will be appended
     **/
    public void addSchemaToContainer(final int index)
    {
        List<Schema> schemaList = customDrawView.getSchemaList();
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                60,
                40);
        params.setMargins(15, 0, 15, 0);
        final Button button = (Button) LayoutInflater.from(this).inflate(R.layout.item_button, null);
        button.setTag(index);
        button.setText(schemaList.get(index).getResourceTag());
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /** Re-formatting the previous selected View */
                if (prevSelectedButton != null) {
                    prevSelectedButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.item_textview_shape));
                    prevSelectedButton.setTextColor(Color.BLACK);
                }
                /** Focusing Schema */
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_item_shape));
                button.setTextColor(Color.WHITE);
                System.out.println("Index: " + schemaListTableRow_drawActivity.indexOfChild(button));
                selectedItemIndex = schemaListTableRow_drawActivity.indexOfChild(button);
                prevSelectedButton = button;
                onButtonClickListener.buttonClicked();
            }
        });
        schemaListTableRow_drawActivity.addView(button,params);
        /*focusSchema(index);*/
    }

    private void unFocusSchema()
    {
        if (prevSelectedButton != null)
        {
            prevSelectedButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.item_textview_shape));
            prevSelectedButton.setTextColor(Color.BLACK);
        }
        selectedItemIndex=-1;
        customDrawView.setSelectedSchemaIndex(selectedItemIndex);
    }

    private void focusSchema(int index)
    {
        /** Re-formatting the previous selected View (defocus) */
        if (prevSelectedButton != null)
        {
            prevSelectedButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.item_textview_shape));
            prevSelectedButton.setTextColor(Color.BLACK);
        }
        /** focus */
        Button button = (Button)  schemaListTableRow_drawActivity.getChildAt(index);
        System.out.println("sel: "+selectedItemIndex);
        button.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_item_shape));
        button.setTextColor(Color.WHITE);
        prevSelectedButton = button;
        selectedItemIndex = index;
    }

    private void deleteSchemaFromContainer(int index)
    {
        try
        {
            schemaListTableRow_drawActivity.removeViewAt(index);
            List<Schema> schemaList = customDrawView.getSchemaList();
            if(schemaList.size()>0)
            {
                if(selectedItemIndex>0)
                {
                    selectedItemIndex--;
                    customDrawView.setSelectedSchemaIndex(selectedItemIndex);
                    focusSchema(selectedItemIndex);
                }
                else
                {
                    schemaList = customDrawView.getSchemaList();
                    if(schemaList.size()>1)
                    {
                        selectedItemIndex = 0;
                        customDrawView.setSelectedSchemaIndex(selectedItemIndex);
                        focusSchema(selectedItemIndex);
                    }
                    else
                    {
                        selectedItemIndex = -1;
                        customDrawView.setSelectedSchemaIndex(selectedItemIndex);
                    }
                }
            }
            System.out.println("selected on del: "+selectedItemIndex);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /** Runs On Hardware Menu Click */
    @Override
    public boolean onKeyDown(int keycode, KeyEvent e)
    {
        switch(keycode)
        {
            case KeyEvent.KEYCODE_MENU:
                Animation slide1;//used for 1st HorizontalView
                Animation slide2;//used for 2nd HorizontalView
                int viewType;
                HorizontalScrollView schemaListHorizontalView_activity_draw =
                        (HorizontalScrollView) findViewById(R.id.schemaListHorizontalView_activity_draw);
                ImageButton removeButton = (ImageButton) findViewById(R.id.removeButton_activityDraw);
                ImageButton settingsButton = ( ImageButton) findViewById(R.id.settingsButton_activityDraw);
                if(palleteIsShowing)
                {
                    palleteIsShowing = false;
                    viewType = View.GONE;
                }
                else
                {
                    palleteIsShowing = true;
                    slide1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                    slide2 =  AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_from_down);
                    pallete_horizontalScrollView_activity_draw.startAnimation(slide1);
                    schemaListHorizontalView_activity_draw.startAnimation(slide2);
                    removeButton.startAnimation(slide2);
                    settingsButton.startAnimation(slide2);
                    nextButton_activityDraw.startAnimation(slide2);
                    prevButton_activityDraw.startAnimation(slide2);
                    viewType = View.VISIBLE;
                }
                pallete_horizontalScrollView_activity_draw.setVisibility(viewType);
                schemaListHorizontalView_activity_draw.setVisibility(viewType);
                removeButton.setVisibility(viewType);
                settingsButton.setVisibility(viewType);
                nextButton_activityDraw.setVisibility(viewType);
                prevButton_activityDraw.setVisibility(viewType);
                return true;
        }
        return super.onKeyDown(keycode, e);
    }

    @Override
    public void onBackPressed()
    {
        finish();
        overridePendingTransition(R.anim.backspace_slide_in, R.anim.backspace_slide_out);
    }
}