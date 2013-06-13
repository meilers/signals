package com.signals_app.signals.activity;

import android.content.Context;
import android.graphics.Rect;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Text view that auto adjusts text size to fit within the view. If the text
 * size equals the minimum text size and still does not fit, append with an
 * ellipsis.
 *
 * @author Chase Colburn
 * @since Apr 4, 2011
 */
public class AutoResizeTextView extends TextView {

    // in dip
    private static final int MIN_TEXT_SIZE = 20;

    private static final boolean SHRINK_TEXT_SIZE = true;

    private static final char ELLIPSIS = '\u2026';

    private static final float LINE_SPACING_MULTIPLIER_MULTILINE = 0.8f;

    private static final float LINE_SPACING_MULTIPLIER_SINGLELINE = 0.1f;

    private static final float LINE_SPACING_EXTRA = 0.0f;

    private CharSequence mOriginalText;

    // temporary upper bounds on the starting text size
    private float mMaxTextSize;

    // lower bounds for text size
    private float mMinTextSize;

    // determines whether we're currently in the process of measuring ourselves,
    // so we do not enter onMeasure recursively
    private boolean mInMeasure = false;

    // if the text size should be shrinked or if the text size should be kept
    // constant and only characters should be removed to hit the boundaries
    private boolean mShrinkTextSize;

    public AutoResizeTextView(Context context) {
        this(context, null);
        init(context, null);
    }

    public AutoResizeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public AutoResizeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // the current text size is used as maximum text size we can apply to
        // our widget
        mMaxTextSize = getTextSize();
        if (attrs != null) {
            mMinTextSize = 10.0f;
            mShrinkTextSize = true;
        }
    }

    @Override
    public void setTextSize(float size) {
        mMaxTextSize = size;
        super.setTextSize(size);
    }

    /**
     * Returns the original, unmodified text of this widget
     *
     * @return
     */
    public CharSequence getOriginalText() {
        // text has not been resized yet
        if (mOriginalText == null) {
            return getText();
        }
        return mOriginalText;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!mInMeasure) {
            mOriginalText = text.toString();
        }
        super.setText(text, type);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mInMeasure = true;
        try {
            int availableWidth = MeasureSpec.getSize(widthMeasureSpec) - getCompoundPaddingLeft()
                    - getCompoundPaddingRight();
            int availableHeight = MeasureSpec.getSize(heightMeasureSpec) - getCompoundPaddingTop()
                    - getCompoundPaddingBottom();

            // Do not resize if the view does not have dimensions or there is no
            // text
            if (mOriginalText == null || mOriginalText.length() == 0 || availableWidth <= 0) {
                return;
            }

            TextPaint textPaint = getPaint();

            // start with the recorded max text size
            float targetTextSize = mMaxTextSize;
            String originalText = mOriginalText.toString();
            String finalText = originalText;

            Rect textSize = getTextSize(originalText, textPaint, targetTextSize);
            boolean textExceedsBounds = textSize.height() > availableHeight || textSize.width() > availableWidth;
            if (mShrinkTextSize && textExceedsBounds) {
                // check whether all lines can be rendered in the available
                // width / height without violating the bounds of the parent and
                // without using a text size that is smaller than the minimum
                // text size
                float heightMultiplier = availableHeight / (float) textSize.height();
                float widthMultiplier = availableWidth / (float) textSize.width();
                float multiplier = Math.min(heightMultiplier, widthMultiplier);
                targetTextSize = Math.max(targetTextSize * multiplier, mMinTextSize);

                // measure again
                textSize = getTextSize(finalText, textPaint, targetTextSize);
            }


            // we cannot shrink the height further when we hit the available
            // height, but we can shrink the width by applying an ellipsis on
            // each line
            /*
            if (textSize.width() > availableWidth) {
                StringBuilder modifiedText = new StringBuilder();
                String lines[] = originalText.split(System.getProperty("line.separator"));
                for (int i = 0; i < lines.length; i++) {
                    modifiedText.append(resizeLine(textPaint, lines[i], availableWidth));
                    // add the separator back to all but the last processed line
                    if (i != lines.length - 1) {
                        modifiedText.append(System.getProperty("line.separator"));
                    }
                }
                finalText = modifiedText.toString();

                // measure again
                textSize = getTextSize(finalText, textPaint, targetTextSize);
            }
*/
            textPaint.setTextSize(targetTextSize);

            /*
            boolean isMultiline = finalText.indexOf('\n') > -1;
            // do not include extra font padding (for accents, ...) for
            // multiline texts, this will prevent proper placement with
            // Gravity.CENTER_VERTICAL
            if (isMultiline) {
                setLineSpacing(LINE_SPACING_EXTRA, LINE_SPACING_MULTIPLIER_MULTILINE);
                setIncludeFontPadding(false);
            } else {
                setLineSpacing(LINE_SPACING_EXTRA, LINE_SPACING_MULTIPLIER_SINGLELINE);
                setIncludeFontPadding(true);
            }
            */

            setLineSpacing(LINE_SPACING_EXTRA, LINE_SPACING_MULTIPLIER_SINGLELINE);
            setIncludeFontPadding(false);

            // according to
            // <http://code.google.com/p/android/issues/detail?id=22493>
            // we have to add a unicode character to trigger the text centering
            // in ICS. this particular character is known as "zero-width" and
            // does no harm.
            setText(finalText + "\u200B");

            int measuredWidth = textSize.width() + getCompoundPaddingLeft() + getCompoundPaddingRight();
            int measuredHeight = textSize.height() + getCompoundPaddingTop() + getCompoundPaddingBottom();

            // expand the view to the parent's height in case it is smaller or
            // to the minimum height that has been set
            // FIXME: honor the vertical measure mode (EXACTLY vs AT_MOST) here
            // somehow
            setMeasuredDimension(measuredWidth, measuredHeight);
        } finally {
            mInMeasure = false;
        }
    }

    private Rect getTextSize(String text, TextPaint textPaint, float textSize) {
        textPaint.setTextSize(textSize);
        // StaticLayout depends on a given width in which it should lay out the
        // text (and optionally also split into separate lines).
        // Therefor we calculate the current text width manually and start with
        // a fake (read: maxmimum) width for the height calculation.
        // We do _not_ use layout.getLineWidth() here since this returns
        // slightly smaller numbers and therefor would lead to exceeded text box
        // drawing.
        StaticLayout layout = new StaticLayout(text, textPaint, Integer.MAX_VALUE, Alignment.ALIGN_NORMAL, 0.8f, 0f, true);
        int textWidth = 0;
        String lines[] = text.split(System.getProperty("line.separator"));
        for (int i = 0; i < lines.length; ++i) {
            textWidth = Math.max(textWidth, measureTextWidth(textPaint, lines[i]));
        }
        return new Rect(0, 0, textWidth, layout.getHeight());
    }

    private String resizeLine(TextPaint textPaint, String line, int availableWidth) {
        int textWidth = measureTextWidth(textPaint, line);
        int lastDeletePos = -1;
        StringBuilder builder = new StringBuilder(line);
        while (textWidth > availableWidth && builder.length() > 0) {
            lastDeletePos = builder.length() / 2;
            builder.deleteCharAt(builder.length() / 2);
            // don't forget to measure the ellipsis character as well; it
            // doesn't matter where it is located in the line, it just has to be
            // there, since there are no (known) ligatures that use this glyph
            String textToMeasure = builder.toString() + ELLIPSIS;
            textWidth = measureTextWidth(textPaint, textToMeasure);
        }
        if (lastDeletePos > -1) {
            builder.insert(lastDeletePos, ELLIPSIS);
        }
        return builder.toString();
    }

    // there are several methods in Android to determine the text width, namely
    // getBounds() and measureText().
    // The latter works for us the best as it gives us the best / nearest
    // results without that our text canvas needs to wrap its text later on
    // again.
    private int measureTextWidth(TextPaint textPaint, String line) {
        return Math.round(textPaint.measureText(line));
    }
}