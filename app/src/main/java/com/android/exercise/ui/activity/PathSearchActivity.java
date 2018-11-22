package com.android.exercise.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.android.exercise.R;
import com.android.exercise.base.BaseActivity;
import com.android.exercise.ui.widget.PathSearchView;

/**
 * PathSearchActivity
 * Created by wangzhen on 2018/11/21.
 */
public class PathSearchActivity extends BaseActivity implements View.OnClickListener {

    private PathSearchView pathSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_search);
        fitDarkStatus(true);
        pathSearchView = (PathSearchView) findViewById(R.id.path_search_view);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                pathSearchView.setState(PathSearchView.State.SEARCHING);
                break;
            case R.id.btn_stop:
                pathSearchView.setState(PathSearchView.State.DONE);
                break;
        }
    }
}
