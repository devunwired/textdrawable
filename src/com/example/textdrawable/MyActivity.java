package com.example.textdrawable;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.text.Layout;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.textdrawable.drawable.TextDrawable;

public class MyActivity extends Activity {
    //Row One
    ImageView mImageOne, mImageTwo;
    //Row Two
    ImageView mImageThree, mImageFour;
    //Row Three
    EditText mEditText;
    //Row Four
    Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mImageOne = (ImageView) findViewById(R.id.image1);
        mImageTwo = (ImageView) findViewById(R.id.image2);
        mImageThree = (ImageView) findViewById(R.id.image3);
        mImageFour = (ImageView) findViewById(R.id.image4);
        mEditText = (EditText) findViewById(R.id.edittext1);
        mButton = (Button) findViewById(R.id.button1);

        loadDrawables();
    }

    private void loadDrawables() {
        /*
         * Create a simple TextDrawable with all default settings.
         * Text display settings will be pulled from the theme.
         */
        TextDrawable d = new TextDrawable(this);
        d.setText("SAMPLE TEXT\nLINE TWO");
        //Apply to two ImageViews with different scale types
        mImageOne.setImageDrawable(d);
        mImageTwo.setImageDrawable(d);

        /*
         * Create a second TextDrawable with a path applied.
         * Applying a path negates implicit bounds measurement, so you
         * must explicitly call setBounds() with a size that covers your
         * path constraint.
         */
        d = new TextDrawable(this);
        d.setText("TEXT DRAWN IN A CIRCLE");
        //Customize text size and color
        d.setTextColor(Color.BLUE);
        d.setTextSize(12);

        /*
         * Create a circular path on which to draw the text.  It's best to
         * keep all path values positive, so the circle is centered such that
         * it's radius keeps the circle from going below (0, 0).
         *
         * We are using complex units so the values scale appropriately to different displays
         */
        Path p = new Path();
        int origin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                40, getResources().getDisplayMetrics());
        int radius = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                30, getResources().getDisplayMetrics());
        int bound = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                80, getResources().getDisplayMetrics());
        p.addCircle(origin, origin, radius, Path.Direction.CW);
        d.setTextPath(p);
        //Must call setBounds() since we are using a Path
        d.setBounds(0, 0, bound, bound);
        //Apply to two ImageViews with different scale types
        mImageThree.setImageDrawable(d);
        mImageFour.setImageDrawable(d);

        /*
         * Construct a simple TextDrawable to act as a static prefix
         * inside of an EditText element
         */
        d = new TextDrawable(this);
        d.setText("SKU#");
        mEditText.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);

        /*
         * Create another simple TextDrawable that will be applied to a TextView
         * as the left and right drawable instance.  The instance is also clickable,
         * so the drawable text colors will change with state.
         */
        d = new TextDrawable(this);
        d.setText("TEXT\nDRAW");
        //Enlarge the text size
        d.setTextSize(20);
        //Set the text colors from a resource list
        d.setTextColor(getResources().getColorStateList(R.color.text_colors));
        //Center align the text
        d.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        //Apply to a TextView, using the intrinsic bounds method
        // TextDrawable internally calculates the required size.
        mButton.setCompoundDrawablesWithIntrinsicBounds(d, null, d, null);
    }
}
