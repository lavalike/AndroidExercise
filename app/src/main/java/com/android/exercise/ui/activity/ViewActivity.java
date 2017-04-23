package com.android.exercise.ui.activity;

import android.os.Bundle;

import com.android.exercise.R;
import com.android.exercise.base.BaseActivity;

/**
 * 自定义View
 * Created by wangzhen on 2017/4/22.
 */
public class ViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
    }

//    @Override
//    protected void onSetupToolbar(Toolbar toolbar, ActionBar actionBar) {
//        new ToolBarCommonHolder(this, toolbar, getString(R.string.item_view), true);
//    }


    @Override
    public boolean showToolbar() {
        return false;
    }
}
