package com.nicaiya.canvaslayout.library;

import android.util.AttributeSet;

public class UIAttributeSet {

    private AttributeSet mAttrSet;

    public UIAttributeSet(AttributeSet set) {
        mAttrSet = set;
    }

    public int getAttributeCount() {
        return mAttrSet.getAttributeCount();
    }

    public String getAttributeName(int i) {
        return mAttrSet.getAttributeName(i);
    }

    public String getAttributeValue(int i) {
        return mAttrSet.getAttributeValue(i);
    }
}
