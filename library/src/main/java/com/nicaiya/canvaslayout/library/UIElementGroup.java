
package com.nicaiya.canvaslayout.library;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

import java.util.ArrayList;
import java.util.List;

public abstract class UIElementGroup extends AbstractUIElement {

    private static final String TAG = UIElementGroup.class.getSimpleName();
    private static final boolean DEG = false;

    private final List<UIElement> mElements;

    public UIElementGroup(UIElementHost host) {
        this(host, (AttributeSet) null);
    }

    public UIElementGroup(UIElementHost host, AttributeSet attrs) {
        super(host, attrs);
        mElements = new ArrayList<UIElement>();
    }

    public UIElementGroup(UIElementHost host, UIAttributeSet attrs) {
        super(host, attrs);
        mElements = new ArrayList<UIElement>();
    }

    @Override
    public boolean swapHost(UIElementHost host) {
        boolean changed = super.swapHost(host);

        if (mElements != null) {
            for (UIElement element : mElements) {
                element.swapHost(host);
            }
        }

        return changed;
    }

    @Override
    public void onAttachedToHost() {
        super.onAttachedToHost();

        if (mElements != null) {
            for (UIElement element : mElements) {
                if (element instanceof AbstractUIElement) {
                    ((AbstractUIElement) element).onAttachedToHost();
                }
            }
        }
    }

    @Override
    public void onDetachedFromHost() {
        super.onDetachedFromHost();

        for (UIElement element : mElements) {
            if (element instanceof AbstractUIElement) {
                ((AbstractUIElement) element).onDetachedFromHost();
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        final int saveCount = canvas.getSaveCount();
        canvas.save();

        for (UIElement element : mElements) {
            if (element.getVisibility() == View.VISIBLE) {
                element.draw(canvas);
            }
        }

        canvas.restoreToCount(saveCount);
    }

    @Override
    public void drawableStateChanged() {
        for (UIElement element : mElements) {
            element.drawableStateChanged();
        }
    }

    public void addElement(UIElement element) {
        LayoutParams lp = element.getLayoutParams();
        if (lp == null) {
            lp = generateDefaultLayoutParams();
        }

        addElement(element, lp);
    }

    public void addElement(UIElement element, LayoutParams lp) {
        if (!checkLayoutParams(lp)) {
            lp = generateLayoutParams(lp);
        }

        element.setLayoutParams(lp);
        mElements.add(element);
        requestLayout();
    }

    public void removeElement(UIElement element) {
        mElements.remove(element);
        requestLayout();
    }

    public void removeAllElement() {
        mElements.clear();
    }

    public UIElement findElementById(int id) {
        for (UIElement element : mElements) {
            if (element.getId() == id) {
                return element;
            }
        }

        return null;
    }

    public int getElementCount() {
        return mElements.size();
    }

    public UIElement getElementAt(int index) {
        return mElements.get(index);
    }

    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        final int size = mElements.size();
        for (int i = 0; i < size; ++i) {
            final UIElement element = mElements.get(i);
            if (element.getVisibility() != View.GONE) {
                measureChild(element, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    protected void measureChild(UIElement child, int parentWidthMeasureSpec,
                                int parentHeightMeasureSpec) {
        final LayoutParams lp = child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight(), lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom(), lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    protected boolean checkLayoutParams(LayoutParams lp) {
        return (lp != null && lp instanceof MarginLayoutParams);
    }

    protected LayoutParams generateLayoutParams(LayoutParams lp) {
        if (lp == null) {
            return generateDefaultLayoutParams();
        }

        return new MarginLayoutParams(lp.width, lp.height);
    }

    public LayoutParams generateLayoutParams(UIAttributeSet attrs) {
        Rect margin = new Rect();
        int width = -3;
        int height = -3;

        final int indexCount = attrs.getAttributeCount();
        for (int i = 0; i < indexCount; i++) {
            String name = attrs.getAttributeName(i);
            String value = attrs.getAttributeValue(i);

            if (name.equals("layout_width")) {
                if (value.equals("fill_parent") || value.equals("match_parent")) {
                    width = LayoutParams.MATCH_PARENT;
                } else if (value.equals("wrap_content")) {
                    width = LayoutParams.WRAP_CONTENT;
                } else {
                    width = (int) DimensionConverter.stringToDimension(value, getResources().getDisplayMetrics());
                }
            } else if (name.equals("layout_height")) {
                if (value.equals("fill_parent") || value.equals("match_parent")) {
                    height = LayoutParams.MATCH_PARENT;
                } else if (value.equals("wrap_content")) {
                    height = LayoutParams.WRAP_CONTENT;
                } else {
                    height = (int) DimensionConverter.stringToDimension(value, getResources().getDisplayMetrics());
                }
            } else if (name.equals("layout_margin")) {
                int padding = (int) DimensionConverter.stringToDimension(value, getResources().getDisplayMetrics());
                margin.left = margin.top = margin.right = margin.bottom = padding;
            } else if (name.equals("layout_marginLeft")) {
                margin.left = (int) DimensionConverter.stringToDimension(value, getResources().getDisplayMetrics());
            } else if (name.equals("layout_marginTop")) {
                margin.top = (int) DimensionConverter.stringToDimension(value, getResources().getDisplayMetrics());
            } else if (name.equals("layout_marginRight")) {
                margin.right = (int) DimensionConverter.stringToDimension(value, getResources().getDisplayMetrics());
            } else if (name.equals("layout_marginBottom")) {
                margin.bottom = (int) DimensionConverter.stringToDimension(value, getResources().getDisplayMetrics());
            }
        }

        if (width == -3 || height == -3) {
            throw new RuntimeException("UIElement: You must supply layout_width and layout_height attribute.");
        }


        MarginLayoutParams mp = new MarginLayoutParams(width, height);
        mp.setMargins(margin.left, margin.top, margin.right, margin.bottom);
        return mp;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    protected void measureElementWithMargins(UIElement element,
                                             int parentWidthMeasureSpec, int widthUsed,
                                             int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) element.getLayoutParams();

        final int childWidthMeasureSpec = getElementMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width
        );

        final int childHeightMeasureSpec = getElementMeasureSpec(parentHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin
                        + heightUsed, lp.height
        );

        element.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);
        int size = Math.max(0, specSize - padding);
        int resultSize = 0;
        int resultMode = 0;
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    resultSize = size;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;
            case MeasureSpec.AT_MOST:
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;
            case MeasureSpec.UNSPECIFIED:
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    resultSize = 0;
                    resultMode = MeasureSpec.UNSPECIFIED;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    resultSize = 0;
                    resultMode = MeasureSpec.UNSPECIFIED;
                }
                break;
        }
        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }

    protected static int getElementMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);

        int size = Math.max(0, specSize - padding);

        int resultSize = 0;
        int resultMode = 0;

        switch (specMode) {
            // Parent has imposed an exact size on us
            case MeasureSpec.EXACTLY:
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size. So be it.
                    resultSize = size;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

            // Parent has imposed a maximum size on us
            case MeasureSpec.AT_MOST:
                if (childDimension >= 0) {
                    // Child wants a specific size... so be it
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size, but our size is not fixed.
                    // Constrain child to not be bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

            // Parent asked to see how big we want to be
            case MeasureSpec.UNSPECIFIED:
                if (childDimension >= 0) {
                    // Child wants a specific size... let him have it
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size... find out how big it should
                    // be
                    resultSize = 0;
                    resultMode = MeasureSpec.UNSPECIFIED;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size.... find out how
                    // big it should be
                    resultSize = 0;
                    resultMode = View.MeasureSpec.UNSPECIFIED;
                }
                break;
        }

        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }
}
