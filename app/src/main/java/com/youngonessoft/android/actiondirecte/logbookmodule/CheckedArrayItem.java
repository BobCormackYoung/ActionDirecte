package com.youngonessoft.android.actiondirecte.logbookmodule;

public class CheckedArrayItem {

    private int mRowID;
    private boolean mIsChecked;
    private boolean mIsExpanded;

    public CheckedArrayItem(int rowID, boolean isChecked) {
        mRowID = rowID;
        mIsChecked = isChecked;
    }

    public int getRowID() {
        return mRowID;
    }

    public boolean getIsChecked() {
        return mIsChecked;
    }

}
