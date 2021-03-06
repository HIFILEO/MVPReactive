/*
Copyright 2017 LEO LLC

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.example.mvpreactive.viewcontroller;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mvpreactive.R;
import com.example.mvpreactive.adapter.NowPlayingListAdapter;
import com.example.mvpreactive.model.MovieViewInfo;
import com.example.mvpreactive.presenter.NowPlayingPresenter;
import com.example.mvpreactive.presenter.NowPlayingViewModel;
import com.example.mvpreactive.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


/**
 * This is the only activity for the application that links into the MVP architecture.
 */
public class NowPlayingActivity extends BaseActivity implements NowPlayingViewModel,
        NowPlayingListAdapter.OnLoadMoreListener {
    private static final String LAST_SCROLL_POSITION = "LAST_SCROLL_POSITION";
    private static final String LOADING_DATA = "LOADING_DATA";
    private static List<MovieViewInfo> movieViewInfoList = new ArrayList<>();
    private NowPlayingListAdapter nowPlayingListAdapter;

    @Inject
    NowPlayingPresenter nowPlayingPresenter;

    //Bind Views
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        toolbar.setTitle(getString(R.string.now_playing));
        setSupportActionBar(toolbar);

        //Start Presenter so Interactor response model is setup correctly.
        nowPlayingPresenter.onCreate(savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LAST_SCROLL_POSITION, recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putBoolean(LOADING_DATA, nowPlayingListAdapter.isLoadingMoreShowing());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        nowPlayingPresenter.onDestroy();
    }

    @Override
    public void showInProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError() {
        Toast.makeText(this, R.string.error_msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void addToAdapter(List<MovieViewInfo> movieViewInfoList) {
        if (!movieViewInfoList.isEmpty()) {
            nowPlayingListAdapter.addList(movieViewInfoList);
        } else {
            nowPlayingListAdapter.disableLoadMore();
        }
    }

    @Override
    public void restoreState(Bundle savedInstanceState) {
        Parcelable savedRecyclerLayoutState =
                savedInstanceState.getParcelable(LAST_SCROLL_POSITION);
        recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);

        if (!movieViewInfoList.isEmpty()) {
            nowPlayingPresenter.dataRestored();
        }
    }

    @Override
    public void createAdapter(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            movieViewInfoList.clear();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(android.R.color.black, null)));
        nowPlayingListAdapter = new NowPlayingListAdapter(movieViewInfoList, this, recyclerView,
                savedInstanceState != null && savedInstanceState.getBoolean(LOADING_DATA));
        recyclerView.setAdapter(nowPlayingListAdapter);
    }

    @Override
    public void onLoadMore() {
        nowPlayingPresenter.loadMoreInfo();
    }
}
