package com.youngonessoft.android.actiondirecte.analysismodule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youngonessoft.android.actiondirecte.R;

public class ClimbingAnalysisFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_climbing_analysis, container, false);
        return rootView;
    }

}
