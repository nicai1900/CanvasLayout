package com.nicaiya.canvaslayout.library;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;

import com.nicaiya.canvaslayout.library.utils.DimensionConverter;

public class AbsoluteElement extends UIElementGroup {

    private static final String TAG = AbsoluteElement.class.getSimpleName();
    private static final boolean DEG = false;

    public AbsoluteElement(UIElementHost host) {
        super(host);
    }

    public AbsoluteElement(UIElementHost host, AttributeSet attrs) {
        super(host, attrs);
    }

    public AbsoluteElement(UIElementHost host, UIAttributeSet attrs) {
        super(host, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getElementCount();

        int maxHeight = 0;
        int maxWidth = 0;

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < count; i++) {
            UIElement child = getElementAt(i);
            if (child.getVisibility() != View.GONE) {
                int childRight;
                int childBottom;
                AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) child.getLayoutParams();

                childRight = lp.x + child.getMeasuredWidth();
                childBottom = lp.y + child.getMeasuredHeight();

                maxWidth = Math.max(maxWidth, childRight);
                maxHeight = Math.max(maxHeight, childBottom);
            }
        }
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec), resolveSize(maxHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(int left, int top, int right, int bottom) {
        int count = getElementCount();

        for (int i = 0; i < count; i++) {
            UIElement child = getElementAt(i);
            if (child.getVisibility() != View.GONE) {

                AbsoluteLayout.LayoutParams lp =
                        (AbsoluteLayout.LayoutParams) child.getLayoutParams();

                int childLeft = getPaddingLeft() + lp.x;
                int childTop = getPaddingTop() + lp.y;
                child.layout(childLeft, childTop,
                        childLeft + child.getMeasuredWidth(),
                        childTop + child.getMeasuredHeight());
            }
        }
    }

    @Override
    public AbsoluteLayout.LayoutParams generateLayoutParams(UIAttributeSet attrs) {
        ViewGroup.LayoutParams lParams = super.generateLayoutParams(attrs);
        AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(lParams);

        final int indexCount = attrs.getAttributeCount();
        for (int i = 0; i < indexCount; i++) {
            String name = attrs.getAttributeName(i);
            String value = attrs.getAttributeValue(i);
            if (name.equals("layout_x")) {
                lp.x = DimensionConverter.stringToDimensionPixelSize(value, getResources().getDisplayMetrics());
            } else if (name.equals("layout_y")) {
                lp.y = DimensionConverter.stringToDimensionPixelSize(value, getResources().getDisplayMetrics());
            }
        }
        return lp;
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams lp) {
        return lp instanceof AbsoluteLayout.LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new AbsoluteLayout.LayoutParams(lp);
    }
}
