package com.nicaiya.canvaslayout.sample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nicaiya.canvaslayout.library.LinearElement;
import com.nicaiya.canvaslayout.library.TextElement;
import com.nicaiya.canvaslayout.library.UIElement;
import com.nicaiya.canvaslayout.library.UIElementInflater;
import com.nicaiya.canvaslayout.library.UIElementView;

public class MainActivity extends ActionBarActivity {

    private UIElementView mUIElementView;
    private UIElementInflater mUIElementInflater;

    private TextElement mTestText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUIElementView = (UIElementView) findViewById(R.id.element_view);
        mUIElementInflater = UIElementInflater.from(this);
        LinearElement content = (LinearElement) mUIElementInflater.inflate(R.layout.element_content, mUIElementView, null);
        mUIElementView.setUIElement(content);

        mTestText = (TextElement) content.findElementById(R.id.test_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mTestText.setText("aaaa");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
