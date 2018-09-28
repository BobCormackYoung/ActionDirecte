package com.youngonessoft.android.actiondirecte.logbookmodule;

/**
 * Created by Bobek on 12/11/2017.
 */

public class LogBookItem {

    private String mLogBookTaskName;
    private String mGrade;
    private String mInfoLine1;
    private String mInfoLine2;
    private String mInfoLine3;
    private Boolean mTrainingTrigger;


    /**
     * Create a new Word object.
     *
     * @param logBookTaskName name of log-book task
     * @param grade climbing grade
     * @param infoLine1 information assigned with task
     * @param infoLine2 information assigned with task
     * @param infoLine3 information assigned with task
     * @param trainingTrigger boolean true if training item, false if logged climb
     */
    public LogBookItem(String logBookTaskName, String grade, String infoLine1, String infoLine2, String infoLine3, Boolean trainingTrigger) {
        mLogBookTaskName = logBookTaskName;
        mGrade=grade;
        mInfoLine1=infoLine1;
        mInfoLine2=infoLine2;
        mInfoLine3=infoLine3;
        mTrainingTrigger=trainingTrigger;
    }

    public String getLogBookTaskName() {
        return mLogBookTaskName;
    }

    public String getGrade() {return mGrade; }

    public String getInfoLine1() {return mInfoLine1; }

    public String getInfoLine2() {return mInfoLine2; }

    public String getInfoLine3() {return mInfoLine3; }

    public Boolean getTrainingTrigger() {return mTrainingTrigger; }
}
