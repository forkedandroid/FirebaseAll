package com.stockapp.view;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.stockapp.App;
import com.stockapp.R;
import com.stockapp.adapter.RecyclerItemClickListener;
import com.stockapp.presenter.FavoriteStockListPresenter;
import com.utils.HideKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class FavoriteStockListActivity extends AppCompatActivity {

  private static final String TAG = "FavoriteStockListActivity";
  private Adapter mAdapter;
  private FavoriteStockListPresenter mPresenter;


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


  @BindView(R.id.toolbar)
  Toolbar toolbar;

  SearchView searchView;
  String strInput = "a";

  Realm realm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stock_list);
    ButterKnife.bind(this);

    toolbar.setNavigationIcon(R.mipmap.ic_launcher);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
       // finish();
      }
    });



      /* // Clear the realm from last time
            Realm.deleteRealm(realmConfiguration);
*/
            /*RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                    .encryptionKey(App.getEncryptRawKey())
                    .build();
*/
    //realm = Realm.getInstance(realmConfiguration);
    realm = Realm.getInstance(App.getRealmConfiguration());
    mPresenter = new FavoriteStockListPresenter(this, App.getApiService(),realm);

    mRecyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    mRecyclerView.setAdapter(mAdapter);
  /*  mRecyclerView.addOnItemTouchListener(
        new RecyclerItemClickListener(this, mRecyclerView,
            (View view, int position) ->
                mPresenter.startYouTubeIntent(position)
        )
    );*/
    mPresenter.loadPlaylistItems(strInput);
    mSwipeRefreshLayout.setOnRefreshListener(mPresenter::loadPlaylistItems);

    fabSearch.setVisibility(View.VISIBLE);
    fabSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(FavoriteStockListActivity.this,StockListActivity.class);
        startActivity(intent);

      }
    });

    fabSearch.setImageResource(R.drawable.ic_add_white_24dp);
  }

  @Override
  protected void onResume() {
    super.onResume();
    try {
      HideKey.initialize(this);
      if (mPresenter !=null && mAdapter != null && mAdapter.getItemCount() > 0) {
        mPresenter.loadPlaylistItems(strInput);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

 /* @Override
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
  }*/
  public void setLoading(boolean loading) {
    circleProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    mSwipeRefreshLayout.setRefreshing(loading);
  }

  /**
   * Adapter that should be passed by the presenter class.
   * @param adapter supplied from presenter
   */
  public void swapAdapter(Adapter adapter) {
    mAdapter = adapter;
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


  boolean doubleBackToExitPressedOnce = false;

  @Override
  public void onBackPressed() {
    if (doubleBackToExitPressedOnce) {
      super.onBackPressed();
      return;
    }

    this.doubleBackToExitPressedOnce = true;
    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

    new Handler().postDelayed(new Runnable() {

      @Override
      public void run() {
        doubleBackToExitPressedOnce=false;
      }
    }, 2000);
  }
}
