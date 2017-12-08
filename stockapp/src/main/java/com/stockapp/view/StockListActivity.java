package com.stockapp.view;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stockapp.App;
import com.stockapp.R;
import com.stockapp.adapter.RecyclerItemClickListener;
import com.stockapp.presenter.StockListPresenter;
import com.utils.HideKey;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockListActivity extends AppCompatActivity {

  private static final String TAG = "StockListActivity";
  private Adapter mAdapter;
  private StockListPresenter mPresenter;


  @BindView(R.id.recyclerView)
  RecyclerView mRecyclerView;

  @BindView(R.id.tvNodata)
  TextView tvNodata;

  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout mSwipeRefreshLayout;

  @BindView(R.id.circleProgressBar)
  ProgressBar circleProgressBar;

  @BindView(R.id.fabSearch)
  FloatingActionButton fabSearch;

  SearchView searchView;
  String strInput = "a";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stock_list);
    ButterKnife.bind(this);



    mPresenter = new StockListPresenter(this, App.getApiService());

    mRecyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.addOnItemTouchListener(
        new RecyclerItemClickListener(this, mRecyclerView,
            (View view, int position) ->
                mPresenter.startYouTubeIntent(position)
        )
    );
    mPresenter.loadPlaylistItems(strInput);
    mSwipeRefreshLayout.setOnRefreshListener(mPresenter::loadPlaylistItems);


    fabSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (searchView != null) {
          searchView.setIconifiedByDefault(true);
          searchView.requestFocus();
        }
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    HideKey.initialize(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_search, menu);
    // Retrieve the SearchView and plug it into SearchManager
    searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
    SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
    searchView.setQueryHint("Search here");
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        strInput = query;
        App.showLog("===query==strInput=" + strInput);
        tvNodata.setVisibility(View.GONE);
        mPresenter.loadPlaylistItems(strInput);
        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });

    searchView.setIconifiedByDefault(false);
    return true;
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
    if(adapter !=null && adapter.getItemCount() > 0)
    {
      tvNodata.setVisibility(View.GONE);
    }
    else
    {
      tvNodata.setVisibility(View.VISIBLE);
    }
  }

  @Override
  protected void onStop() {
    super.onStop();

  }
}
