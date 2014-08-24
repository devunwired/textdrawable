/**
 * Copyright (c) 2012 Wireless Designs, LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.example.textdrawable;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.textdrawable.drawable.TextDrawable;

public class MyActivity extends Activity implements Runnable {
    //Row One
    ImageView mTwoLineOne, mTwoLineTwo;
    //Row Two
    ImageView mPathOne, mPathTwo;
    //Row Three
    ImageView mCompoundOne, mCompoundTwo;
    //Row Four
    ImageView mAnimatedText;
    //Row Four
    EditText mEditText;
    //Row Five
    Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTwoLineOne = (ImageView) findViewById(R.id.image1);
        mTwoLineTwo = (ImageView) findViewById(R.id.image2);
        mPathOne = (ImageView) findViewById(R.id.image3);
        mPathTwo = (ImageView) findViewById(R.id.image4);
        mCompoundOne = (ImageView) findViewById(R.id.image5);
        mCompoundTwo = (ImageView) findViewById(R.id.image6);
        mAnimatedText = (ImageView) findViewById(R.id.image7);
        mEditText = (EditText) findViewById(R.id.edittext1);
        mButton = (Button) findViewById(R.id.button1);

        loadDrawables();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Start the animation
        mAnimator.post(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Stop the animation
        mAnimator.removeCallbacksAndMessages(null);
    }

    private Handler mAnimator = new Handler();
    private int mLevel = 0;
    @Override
    public void run() {
        mLevel = (mLevel += 25) % 10000;
        mAnimatedText.setImageLevel(mLevel);

        mAnimator.postDelayed(this, 10);
    }

    private void loadDrawables() {
        /*
         * Create a simple TextDrawable with all default settings.
         * Text display settings will be pulled from the theme.
         */
        TextDrawable d = new TextDrawable(this);
        d.setText("SAMPLE TEXT\nLINE TWO");
        d.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        //Apply to two ImageViews with different scale types
        mTwoLineOne.setImageDrawable(d);
        mTwoLineTwo.setImageDrawable(d);

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
        int origin = getResources().getDimensionPixelOffset(R.dimen.path_origin);
        int radius = getResources().getDimensionPixelOffset(R.dimen.path_radius);
        int bound = getResources().getDimensionPixelOffset(R.dimen.path_bound);
        p.addCircle(origin, origin, radius, Path.Direction.CW);
        d.setTextPath(p);
        //Must call setBounds() since we are using a Path
        d.setBounds(0, 0, bound, bound);
        //Apply to two ImageViews with different scale types
        mPathOne.setImageDrawable(d);
        mPathTwo.setImageDrawable(d);

        /*
         * Create a compound drawable with TextDrawable and a shape. Since TextDrawable
         * is a non-framework class, we can't do this purely in XML. The example also
         * includes adding insets to the text over the shape.
         */
        d = new TextDrawable(this);
        d.setText("LAYER\nTEXT");
        Drawable background = getResources().getDrawable(R.drawable.background);
        LayerDrawable container = new LayerDrawable(new Drawable[] {background, d});
        int inset = getResources().getDimensionPixelOffset(R.dimen.layer_inset);
        container.setLayerInset(1, inset, inset, inset, inset);
        mCompoundOne.setImageDrawable(container);
        mCompoundTwo.setImageDrawable(container);

        /*
         * Wrap a simple TextDrawable in a ClipDrawable to animate.  One convenient
         * advantage of ImageView is we can call setImageLevel() on the view itself to affect
         * the Drawable content.
         */
        d = new TextDrawable(this);
        d.setText("SHOW ME TEXT");
        ClipDrawable clip = new ClipDrawable(d, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        mAnimatedText.setImageDrawable(clip);

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
