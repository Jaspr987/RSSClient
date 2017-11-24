package me.jaspr.rssclient;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;

/**
 * Created by Jaspr on 20/11/2017.
 */

public class CustomQuoteSpan implements LeadingMarginSpan, LineBackgroundSpan {
    private static final int STRIPE_WIDTH = 5;
    private static final int GAP_WIDTH = 8;

    private final int mBackgroundColor;
    private final int mColor;

    public CustomQuoteSpan() {
        super();
        mBackgroundColor = 0xffddf1fd;
        mColor = 0xff098fdf;
    }

    public int getLeadingMargin(boolean first) {
        return STRIPE_WIDTH + GAP_WIDTH;
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout layout) {
        Paint.Style style = p.getStyle();
        int color = p.getColor();

        p.setStyle(Paint.Style.FILL);
        p.setColor(mColor);

        c.drawRect(x, top, x + dir * STRIPE_WIDTH, bottom, p);

        p.setStyle(style);
        p.setColor(color);
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        int paintColor = p.getColor();
        p.setColor(mBackgroundColor);
        c.drawRect(left, top, right, bottom, p);
        p.setColor(paintColor);
    }
}
