package me.jaspr.rssclient;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

import static android.content.ContentValues.TAG;

/**
 * Created by Jaspr on 19/11/2017.
 */

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private boolean mIsAnimating = false;

    // 因为需要在布局xml中引用，所以必须实现该构造方法
    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target,
                                       @ViewCompat.ScrollAxis int axes, @ViewCompat.NestedScrollType int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (dyConsumed > 0 && !mIsAnimating && child.getVisibility() == View.VISIBLE) { // 向下滑动
            Log.i(TAG, "onNestedScroll: 向下滚动");
            animateOut(child);
        } else if (dyConsumed < 0 && !mIsAnimating && child.getVisibility() == View.INVISIBLE) { // 向上滑动
            Log.i(TAG, "onNestedScroll: 向上滚动");
            animateIn(child);
        }
    }

    // FAB移出屏幕动画（隐藏动画）
    private void animateOut(FloatingActionButton fab) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        int distanceToScroll = fab.getHeight() + fabBottomMargin;
        ViewCompat.animate(fab).translationY(distanceToScroll)
                .setInterpolator(INTERPOLATOR).withLayer()
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        ScrollAwareFABBehavior.this.mIsAnimating=true;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        ScrollAwareFABBehavior.this.mIsAnimating=false;
                        view.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        ScrollAwareFABBehavior.this.mIsAnimating=false;

                    }
                }).start();
        if (fab.getVisibility() == View.INVISIBLE) {
            Log.i(TAG, "animateOut: FAB is INVISIBLE");
        }
    }

    // FAB移入屏幕动画（显示动画）
    private void animateIn(FloatingActionButton fab) {
        fab.setVisibility(View.VISIBLE);
        ViewCompat.animate(fab).translationY(0)
                .setInterpolator(INTERPOLATOR).withLayer()
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        ScrollAwareFABBehavior.this.mIsAnimating=true;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        ScrollAwareFABBehavior.this.mIsAnimating=false;
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        ScrollAwareFABBehavior.this.mIsAnimating=false;
                    }
                }).start();
    }
}
