
package com.nicaiya.canvaslayout.library;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class BaseUIElement implements UIElement {

    private static final String TAG = BaseUIElement.class.getSimpleName();
    private static final boolean DEG = false;

    protected UIElementHost mHost;

    private int mId;

    private int mMeasuredWidth;
    private int mMeasuredHeight;

    private Rect mBounds = new Rect();
    private Rect mPadding = new Rect();

    private LayoutParams mLayoutParams;

    private int mVisibility = View.VISIBLE;

    private float mAlpha = 1f;

    public static int resolveSize(int size, int measureSpec) {
        int result = size;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case View.MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case View.MeasureSpec.AT_MOST:
                if (specSize < size) {
                    result = specSize;
                } else {
                    result = size;
                }
                break;
            case View.MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    public BaseUIElement(UIElementHost host) {
        this(host, (AttributeSet) null);
    }

    public BaseUIElement(UIElementHost host, AttributeSet attrs) {
        swapHost(host);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UIElement, 0, 0);

        int leftPadding = -1;
        int topPadding = -1;
        int rightPadding = -1;
        int bottomPadding = -1;
        int padding = -1;

        final int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            final int attr = a.getIndex(i);

            if (attr == R.styleable.UIElement_android_padding) {
                padding = a.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.UIElement_android_paddingLeft) {
                leftPadding = a.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.UIElement_android_paddingTop) {
                topPadding = a.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.UIElement_android_paddingRight) {
                rightPadding = a.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.UIElement_android_paddingBottom) {
                bottomPadding = a.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.UIElement_android_id) {
                mId = a.getResourceId(attr, -1);
            } else if (attr == R.styleable.UIElement_android_visibility) {
                mVisibility = a.getInt(attr, View.VISIBLE);
            } else if (attr == R.styleable.UIElement_android_alpha) {
                setAlpha(a.getFloat(attr, 1f));
            }
        }

        if (padding >= 0) {
            leftPadding = padding;
            topPadding = padding;
            rightPadding = padding;
            bottomPadding = padding;
        }

        internalSetPadding(leftPadding > 0 ? leftPadding : 0, topPadding > 0 ? topPadding : 0,
                rightPadding > 0 ? rightPadding : 0, bottomPadding > 0 ? bottomPadding : 0);

        a.recycle();
    }

    protected void onAttachedToHost() {
    }

    protected void onDetachedFromHost() {
    }

    @Override
    public boolean swapHost(UIElementHost host) {
        if (mHost == host) {
            return false;
        }

        if (mHost != null) {
            onDetachedFromHost();
        }

        mHost = host;

        if (mHost != null) {
            onAttachedToHost();
        }

        return true;
    }

    @Override
    public boolean isAttachedToHost() {
        return (mHost != null);
    }

    @Override
    public int getId() {
        return mId;
    }

    protected void setMeasuredDimension(int width, int height) {
        mMeasuredWidth = width;
        mMeasuredHeight = height;
    }

    @Override
    public int getMeasuredWidth() {
        return mMeasuredWidth;
    }

    @Override
    public int getMeasuredHeight() {
        return mMeasuredHeight;
    }

    @Override
    public int getPaddingLeft() {
        return mPadding.left;
    }

    @Override
    public int getPaddingTop() {
        return mPadding.top;
    }

    @Override
    public int getPaddingRight() {
        return mPadding.right;
    }

    @Override
    public int getPaddingBottom() {
        return mPadding.bottom;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        internalSetPadding(left, top, right, bottom);

        requestLayout();
    }

    private void internalSetPadding(int left, int top, int right, int bottom) {
        mPadding.left = left;
        mPadding.top = top;
        mPadding.right = right;
        mPadding.bottom = bottom;
    }

    @Override
    public int getVisibility() {
        return mVisibility;
    }

    @Override
    public void setVisibility(int visibility) {
        if (mVisibility == visibility) {
            return;
        }

        mVisibility = visibility;

        requestLayout();
        invalidate();
    }

    public float getAlpha() {
        return mAlpha;
    }

    public void setAlpha(float alpha) {
        if (mAlpha == alpha) {
            return;
        }

        mAlpha = alpha;

        invalidate();
    }

    @Override
    public final void draw(Canvas canvas) {
        final int saveCount = canvas.getSaveCount();
        canvas.save();

        canvas.clipRect(mBounds);
        canvas.translate(mBounds.left, mBounds.top);

        canvas.saveLayerAlpha(mBounds.left, mBounds.top, mBounds.right, mBounds.bottom, (int) (mAlpha * 255), Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);

        onDraw(canvas);

        canvas.restoreToCount(saveCount);
    }

    @Override
    public final void measure(int widthMeasureSpec, int heightMeasureSpec) {
        onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public final void layout(int left, int top, int right, int bottom) {
        mBounds.left = left;
        mBounds.top = top;
        mBounds.right = right;
        mBounds.bottom = bottom;

        onLayout(left, top, right, bottom);
    }

    @Override
    public int getLeft() {
        return mBounds.left;
    }

    @Override
    public int getTop() {
        return mBounds.top;
    }

    @Override
    public int getRight() {
        return mBounds.right;
    }

    @Override
    public int getBottom() {
        return mBounds.bottom;
    }

    @Override
    public int getWidth() {
        return mBounds.right - mBounds.left;
    }

    @Override
    public int getHeight() {
        return mBounds.bottom - mBounds.top;
    }

    @Override
    public void setLayoutParams(LayoutParams lp) {
        if (mLayoutParams == lp) {
            return;
        }

        mLayoutParams = lp;
        requestLayout();
    }

    @Override
    public LayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    @Override
    public void onFinishInflate() {
    }

    @Override
    public Context getContext() {
        return mHost.getContext();
    }

    @Override
    public Resources getResources() {
        return mHost.getResources();
    }

    @Override
    public void requestLayout() {
        mHost.requestLayout();
    }

    @Override
    public void invalidate() {
        mHost.invalidate(mBounds.left, mBounds.top, mBounds.right, mBounds.bottom);
    }

    protected void onDraw(Canvas canvas) {

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    }

    protected void onLayout(int left, int top, int right, int bottom) {

    }

    public void drawableStateChanged() {

    }
}
