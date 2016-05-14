package menelaos.example.com.symmetry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

/**
//Copyright © 2015 by Menelaos Kotsollaris
 *
 *
 */
public class SchemaSimpleSettingsDialogFragment extends DialogFragment
        implements ColorPickerDialog.OnColorChangedListener, View.OnClickListener
{
    /* The activity that creates an instance of this dialog fragment must
 * implement this interface in order to receive event callbacks.
 * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener
    {
        public void onDialogPositiveClick(int color);
        public void onDialogNegativeClick();
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try
        {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e)
        {
            // The activity doesn't implement the interface, throws exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    private int color;
    private int selectedItemIndex;
    private Paint.Style paintStyle;

    public void setPaintStyle(Paint.Style paintStyle)
    {
        this.paintStyle = paintStyle;
    }

    public void setSelectedItemIndex(int selectedItemIndex){this.selectedItemIndex = selectedItemIndex;}

    public void setColor(int color)
    {
        this.color = color;
    }
    public int getColor()
    {
        return color;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_simple_settings, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        mListener.onDialogPositiveClick(color);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick();
                        SchemaSimpleSettingsDialogFragment.this.getDialog().cancel();
                    }
                });

        final Button colorButton_dialogSettings = (Button) view.findViewById(R.id.colorButton_dialogSimpleSettings);

        colorButton_dialogSettings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog(
                        getActivity(),
                        SchemaSimpleSettingsDialogFragment.this,
                        Color.BLACK);
                colorPickerDialog.show();
                colorPickerDialog.getWindow().
                        setLayout(225,330);
            }
        });
        return builder.create();
    }

    @Override
    public void colorChanged(int color)
    {
        this.color = color;
    }

    @Override
    public void onClick(View v)
    {
        System.out.println("test");
    }
}