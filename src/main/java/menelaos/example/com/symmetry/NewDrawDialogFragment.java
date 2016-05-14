package menelaos.example.com.symmetry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
//Copyright © 2015 by Menelaos Kotsollaris
 *
 * DialogFragment for settling up new Drawing interface
 */
public class NewDrawDialogFragment extends DialogFragment
{
    /* The activity that creates an instance of this dialog fragment must
* implement this interface in order to receive event callbacks.
* Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener
    {
        public void onDialogPortraitClick(int width, int height);
        public void onDialogLandscapeClick(int width, int height);
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
        }
        catch (ClassCastException e)
        {
            // The activity doesn't implement the interface, throws exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    private int width;
    private int height;

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_new_draw, null);
        final EditText widthEditText_newDrawDialog = (EditText)
                view.findViewById(R.id.widthEditText_newDrawDialog);
        final EditText heightEditText_newDrawDialog = (EditText)
                view.findViewById(R.id.heightEditText_newDrawDialog);
        widthEditText_newDrawDialog.setText
                (String.valueOf(GeneralMethods.getDeviceWidth()));
        heightEditText_newDrawDialog.setText
                (String.valueOf(GeneralMethods.getDeviceHeight()));

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.portrait, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        width = Integer.parseInt(String.valueOf(widthEditText_newDrawDialog.getText()));
                        height = Integer.parseInt(String.valueOf(heightEditText_newDrawDialog.getText()));
                        if (width < 10 || width > 4096 || height < 10 || height > 4096)
                        {
                            Context context = GeneralMethods.getAppContext();
                            CharSequence text = "Retry: Values required range: 10-4096!";
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        else  mListener.onDialogPortraitClick(width, height);
                    }
                })
                .setNegativeButton(R.string.landscape, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        width = Integer.parseInt(String.valueOf(widthEditText_newDrawDialog.getText()));
                        height = Integer.parseInt(String.valueOf(heightEditText_newDrawDialog.getText()));
                        mListener.onDialogLandscapeClick(width, height);
                    }
                });

        return builder.create();
    }
}