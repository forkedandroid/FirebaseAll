package com.stockapp.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;

import android.view.View;
import android.widget.ProgressBar;


import com.stockapp.R;
import com.stockapp.adapter.RecyclerItemClickListener;
import com.stockapp.presenter.MainPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private RecyclerView.Adapter mAdapter;
  private MainPresenter mPresenter;


  @BindView(R.id.recyclerView)
  RecyclerView mRecyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout mSwipeRefreshLayout;
  @BindView(R.id.circleProgressBar)
  ProgressBar circleProgressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stock_list);
    ButterKnife.bind(this);

    mPresenter = new MainPresenter(this);

    mRecyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.addOnItemTouchListener(
        new RecyclerItemClickListener(this, mRecyclerView,
            (View view, int position) ->
                mPresenter.startYouTubeIntent(position)
        )
    );
    mPresenter.loadPlaylistItems();
    mSwipeRefreshLayout.setOnRefreshListener(mPresenter::loadPlaylistItems);
  }

  public void setLoading(boolean loading) {
    circleProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    mSwipeRefreshLayout.setRefreshing(loading);
  }

  /**
   * Adapter that should be passed by the presenter class.
   * @param adapter supplied from presenter
   */
  public void swapAdapter(Adapter adapter) {
    mRecyclerView.swapAdapter(adapter, false);
  }

  @Override
  protected void onStop() {
    super.onStop();

  }
}
