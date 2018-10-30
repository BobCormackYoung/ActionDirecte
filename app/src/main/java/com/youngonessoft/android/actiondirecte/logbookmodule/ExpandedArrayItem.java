package com.youngonessoft.android.actiondirecte.logbookmodule;

public class ExpandedArrayItem {

    private int mRowID;
    private boolean mIsExpanded;

    public ExpandedArrayItem(int rowID, boolean isExpanded) {
        mRowID = rowID;
        mIsExpanded = isExpanded;
    }

    public int getRowID() {
        return mRowID;
    }

    public boolean getIsExpanded() {
        return mIsExpanded;
    }

}
