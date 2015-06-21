package com.nicaiya.canvaslayout.library;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 线性布局
 * <p/>
 * 支持横向和纵向布局
 * android:orientation="horizontal"
 * <p/>
 * 支持布局内容排布方向
 * 例如横向布局，支持整体居左，居中，居右
 * 例如纵向布局，支持整体居上，居中，居下
 * android:gravity="left"
 * <p/>
 * 支持子布局在该线性布局中的排布方向
 * 与安卓线性布局行为一致
 * android:layout_gravity="top"
 * <p/>
 * Created by zhengjie on 15-5-20.
 */
public class LinearElement extends UIElementGroup {

    private static final String TAG = LinearElement.class.getSimpleName();
    private static final boolean DEG = false;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private int mOrientation = HORIZONTAL;

    private int mGravity = Gravity.TOP;

    private int mTotalLength;

    public LinearElement(UIElementHost host) {
        super(host);
    }

    public LinearElement(UIElementHost host, AttributeSet attrs) {
        super(host, attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LinearElement, 0, 0);

        int index = a.getInt(R.styleable.LinearElement_android_orientation, -1);
        if (index >= 0) {
            setOrientation(index);
        }

        index = a.getInt(R.styleable.LinearElement_android_gravity, -1);
        if (index >= 0) {
            setGravity(index);
        }

        a.recycle();
    }

    public void setOrientation(int orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
            requestLayout();
        }
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            mGravity = gravity;
            requestLayout();
        }
    }

    public int getGravity() {
        return mGravity;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOrientation == VERTICAL) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        mTotalLength = 0;
        int maxHeight = 0;

        int count = getElementCount();

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < count; i++) {
            UIElement child = getElementAt(i);
            if (child.getVisibility() != BaseUIElement.GONE) {
                int childRight;
                int childBottom;
                childRight = child.getMeasuredWidth();
                childBottom = child.getMeasuredHeight();

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();

                mTotalLength += childRight + lp.leftMargin + lp.rightMargin;
                maxHeight = Math.max(maxHeight, childBottom);
            }

            if (DEG) {
                Log.d(TAG, "measureHorizontal totalLength " + mTotalLength);
            }
        }
        mTotalLength += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(resolveSize(mTotalLength, widthMeasureSpec), resolveSize(maxHeight, heightMeasureSpec));
    }

    void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        mTotalLength = 0;
        int maxWidth = 0;

        int count = getElementCount();

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < count; i++) {
            UIElement child = getElementAt(i);
            if (child.getVisibility() != BaseUIElement.GONE) {
                int childRight;
                int childBottom;

                childRight = child.getMeasuredWidth();
                childBottom = child.getMeasuredHeight();

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();

                mTotalLength += childBottom + lp.topMargin + lp.bottomMargin;
                maxWidth = Math.max(maxWidth, childRight);
            }

            if (DEG) {
                Log.d(TAG, "measureVertical totalLength " + mTotalLength);
            }
        }
        mTotalLength += getPaddingTop() + getPaddingBottom();
        maxWidth += getPaddingLeft() + getPaddingRight();
        setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec), resolveSize(mTotalLength, heightMeasureSpec));
    }

    @Override
    protected void onLayout(int left, int top, int right, int bottom) {
        if (mOrientation == VERTICAL) {
            layoutVertical();
        } else {
            layoutHorizontal();
        }
    }

    void layoutVertical() {
        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        int childTop;
        int childLeft;

        // Where right end of child should go
        final int width = getWidth();
        int childRight = width - paddingRight;

        // Space available for child
        int childSpace = width - paddingLeft - paddingRight;

        final int majorGravity = mGravity & Gravity.VERTICAL_GRAVITY_MASK;

        switch (majorGravity) {
            case Gravity.BOTTOM:
                // mTotalLength contains the padding already
                childTop = paddingTop + getHeight() - mTotalLength;
                break;

            // mTotalLength contains the padding already
            case Gravity.CENTER_VERTICAL:
                childTop = paddingTop + (getHeight() - mTotalLength) / 2;
                break;

            case Gravity.TOP:
            default:
                childTop = paddingTop;
                break;
        }

        int count = getElementCount();
        for (int i = 0; i < count; i++) {
            UIElement child = getElementAt(i);
            if (child.getVisibility() != BaseUIElement.GONE) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();

                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();

                int gravity = lp.gravity;

                switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.CENTER_HORIZONTAL:
                        childLeft = paddingLeft + ((childSpace - childWidth) / 2)
                                + lp.leftMargin - lp.rightMargin;
                        break;

                    case Gravity.RIGHT:
                        childLeft = childRight - childWidth - lp.rightMargin;
                        break;

                    case Gravity.LEFT:
                    default:
                        childLeft = paddingLeft + lp.leftMargin;
                        break;
                }

                childTop += lp.topMargin;
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

                if (DEG) {
                    Log.d(TAG, "layoutVertical " + childLeft + "," + childTop + "," + (childLeft + childWidth) + "," + (childTop + childHeight));
                }
                childTop += childHeight + lp.bottomMargin;
            }
        }

    }

    void layoutHorizontal() {
        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        int paddingBottom = getPaddingRight();

        int childTop;
        int childLeft;

        // Where bottom of child should go
        final int height = getHeight();
        int childBottom = height - paddingBottom;

        // Space available for child
        int childSpace = height - paddingTop - paddingBottom;

        final int majorGravity = mGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        switch (majorGravity) {
            case Gravity.RIGHT:
                // mTotalLength contains the padding already
                childLeft = paddingLeft + getWidth() - mTotalLength;
                break;

            case Gravity.CENTER_HORIZONTAL:
                // mTotalLength contains the padding already
                childLeft = paddingLeft + (getWidth() - mTotalLength) / 2;
                break;

            case Gravity.LEFT:
            default:
                childLeft = paddingLeft;
                break;
        }

        int count = getElementCount();
        for (int i = 0; i < count; i++) {
            UIElement child = getElementAt(i);
            if (child.getVisibility() != BaseUIElement.GONE) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();

                int gravity = lp.gravity;

                switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                    case Gravity.TOP:
                        childTop = paddingTop + lp.topMargin;
                        break;
                    case Gravity.CENTER_VERTICAL:
                        childTop = paddingTop + ((childSpace - childHeight) / 2)
                                + lp.topMargin - lp.bottomMargin;
                        break;
                    case Gravity.BOTTOM:
                        childTop = childBottom - childHeight - lp.bottomMargin;
                        break;
                    default:
                        childTop = paddingTop;
                        break;
                }

                childLeft += lp.leftMargin;
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

                if (DEG) {
                    Log.d(TAG, "layoutHorizontal " + childLeft + "," + childTop + "," + (childLeft + childWidth) + "," + (childTop + childHeight));
                }

                childLeft += childWidth + lp.rightMargin;
            }
        }
    }

    // Override to allow type-checking of LayoutParams.
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams lp) {
        return lp instanceof LinearLayout.LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new LinearLayout.LayoutParams(lp);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LinearLayout.LayoutParams(getContext(), attrs);
    }

}
