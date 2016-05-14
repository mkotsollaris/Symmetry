//Copyright © 2014 by Menelaos Kotsollaris

package menelaos.example.com.symmetry;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import menelaos.example.com.symmetry.R;

public class AboutActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public void onBackPressed()
    {
        finish();
        overridePendingTransition(R.anim.backspace_slide_in, R.anim.backspace_slide_out);
    }
}
