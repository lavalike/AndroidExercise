package com.android.exercise.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.android.exercise.R;
import com.android.exercise.base.BaseActivity;
import com.android.exercise.base.BaseRecyclerAdapter;
import com.android.exercise.base.recycler.wrapper.HeaderAndFooterWrapper;
import com.android.exercise.base.retrofit.callback.CommonCallback;
import com.android.exercise.base.task.GithubReposTask;
import com.android.exercise.base.toolbar.ToolBarCommonHolder;
import com.android.exercise.domain.GithubBean;
import com.android.exercise.ui.adapter.ReposAdapter;
import com.android.exercise.util.T;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Retrofit 2.0
 * created by wangzhen on 2016/11/07
 */
public class RetrofitActivity extends BaseActivity {

    @BindView(R.id.recycler_repos)
    RecyclerView recyclerRepos;
    @BindView(R.id.swipe_repos)
    SwipeRefreshLayout swipeRepos;
    private ReposAdapter mReposAdapter;
    private View mHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        ButterKnife.bind(this);
        initSwipe();
        initRecycler();
        startLoading();
        loadList();
    }

    private void initSwipe() {
        swipeRepos.setColorSchemeResources(R.color.colorPrimary);
        swipeRepos.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadList();
            }
        });
    }

    private void startLoading() {
        swipeRepos.post(new Runnable() {
            @Override
            public void run() {
                swipeRepos.setRefreshing(true);
            }
        });
    }

    private void stopLoading() {
        swipeRepos.post(new Runnable() {
            @Override
            public void run() {
                swipeRepos.setRefreshing(false);
            }
        });
    }

    private void initRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerRepos.setLayoutManager(manager);
    }

    @Override
    protected void onSetupToolbar(Toolbar toolbar, ActionBar actionBar) {
        new ToolBarCommonHolder(this, toolbar, getString(R.string.item_retrofit), true);
    }

    /**
     * 加载数据
     */
    public void loadList() {
        new GithubReposTask(new CommonCallback<List<GithubBean>>() {

            @Override
            public void onError(String error) {
                T.get(mContext).toast(error);
            }

            @Override
            public void onSuccess(List<GithubBean> list) {
                mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.activity_greendao, null);
                mReposAdapter = new ReposAdapter(mContext, list);
                mReposAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnRecyclerItemClickListener<GithubBean>() {
                    @Override
                    public void onItemClick(View view, int position, GithubBean data) {
                        String reposUrl = data.getHtml_url();
                        Intent intent = new Intent(mContext, HtmlActivity.class);
                        intent.putExtra("url", reposUrl);
                        startActivity(intent);
                    }
                });
                //添加头部
                HeaderAndFooterWrapper wrapper = new HeaderAndFooterWrapper(mReposAdapter);
                wrapper.addHeaderView(mHeaderView);
                recyclerRepos.setAdapter(wrapper);
            }

            @Override
            public void onAfter() {
                stopLoading();
            }
        }).exe("lavalike");
    }
}