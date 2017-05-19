package com.android.exercise.base.toolbar;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.exercise.R;

/**
 * 常用布局 ：左返回-标题中-右无
 * Created by wangzhen on 16/10/19.
 */

public class ToolBarCommonHolder extends BaseToolBarHolder {

    private TextView tv_title;

    /**
     * @param activity
     * @param toolbar
     * @param title
     */
    public ToolBarCommonHolder(Activity activity, Toolbar toolbar, String title) {
        this(activity, toolbar, title, true);
    }

    /**
     * @param activity
     * @param toolbar
     * @param title
     * @param canBack
     */
    public ToolBarCommonHolder(Activity activity, Toolbar toolbar, String title, boolean canBack) {
        super(activity, toolbar);
        tv_title.setText(title);
        if (!canBack) {
            //去除返回箭头
            mToolbar.setNavigationIcon(null);
            //去除TextView预留的右边距
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_title.getLayoutParams();
            params.rightMargin = 0;
            tv_title.setLayoutParams(params);
        }
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
    }

    @Override
    protected void initView() {
        tv_title = findById(R.id.tv_toolbar_common_title);
    }

    @Override
    protected int getToolBarLayoutResId() {
        return R.layout.layout_toolbar_common;
    }

    @Override
    protected <T extends View> T getRightMenu() {
        return null;
    }

}
