package com.youngonessoft.android.actiondirecte.logbookmodule;

public class CopyWorkoutExpandedItem {

    private int mRowID;
    private boolean mIsExpanded;

    public CopyWorkoutExpandedItem(int rowID, boolean isExpanded) {
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
