//Copyright © 2015 by Menelaos Kotsollaris

package menelaos.example.com.symmetry;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashBoardActivity extends ActionBarActivity implements
        NewDrawDialogFragment.NoticeDialogListener
{
    @Override
    public void onDialogPortraitClick(int width, int height)
    {
        Intent intent = new Intent(DashBoardActivity.this, DrawActivity.class);
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        intent.putExtra("SCREEN_ORIENTATION", 1);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void onDialogLandscapeClick(int width, int height)
    {
        Intent intent = new Intent(DashBoardActivity.this, DrawActivity.class);
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        intent.putExtra("SCREEN_ORIENTATION", 0);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        Button newButton = (Button) findViewById(R.id.newDrawActivity_dashBoardActivity);
        Button aboutButton = (Button) findViewById(R.id.about_dashBoardActivity);

        aboutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(DashBoardActivity.this, AboutActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        newButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NewDrawDialogFragment newDrawDialogFragment = new NewDrawDialogFragment();
                newDrawDialogFragment.show(getSupportFragmentManager(), "newDrawDialogFragment");
            }
        });
    }
}