package com.stockapp.view;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stockapp.App;
import com.stockapp.R;
import com.stockapp.adapter.SlidingImageAdapter;
import com.stockapp.model.StockDetailResponse;
import com.stockapp.model.StocklistItemListResponse;
import com.stockapp.presenter.StockDetailPresenter;
import com.stockapp.presenter.StockListPresenter;
import com.utils.HideKey;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class StockDetailActivity extends AppCompatActivity {

    private static final String TAG = "StockDetailActivity";
    private StockDetailPresenter mPresenter;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvLastPrice)
    TextView tvLastPrice;
    @BindView(R.id.tvDetail)
    TextView tvDetail;

    @BindView(R.id.fabFavourite)
    FloatingActionButton fabFavourite;
    @BindView(R.id.ivGraph)
    ImageView ivGraph;

   /* @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;*/

    @BindView(R.id.circleProgressBar)
    ProgressBar circleProgressBar;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    String strSymbol = "";

    SearchView searchView;

    StocklistItemListResponse stocklistItemListResponse = new StocklistItemListResponse();

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        ButterKnife.bind(this);

        realm = Realm.getInstance(App.getRealmConfiguration());
        mPresenter = new StockDetailPresenter(this, App.getApiService());
        setSupportActionBar(toolbar);

        getIntentData();

       /* if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(stocklistItemListResponse !=null && stocklistItemListResponse.Symbol  !=null)
        {
            strSymbol = stocklistItemListResponse.Symbol;
        }

        mPresenter.loadPlaylistItems(strSymbol);
    //    mSwipeRefreshLayout.setOnRefreshListener(mPresenter::loadPlaylistItems);
        Picasso.with(StockDetailActivity.this).load("http://ak1.picdn.net/shutterstock/videos/8414821/thumb/1.jpg").placeholder(R.drawable.ic_poll_black_48dp).fit().centerCrop().into(ivGraph);

        fabFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   if (searchView != null) {
                    searchView.setIconifiedByDefault(true);
                    searchView.requestFocus();
                }*/



             if(stocklistItemListResponse !=null) {
                 if (view.isSelected() == true) {
                     App.showLog("=====fav added----Remove==");
                     view.setSelected(false);
                     fabFavourite.setImageResource(R.drawable.ic_star_border_black_24dp);
                     App.removeStockSymbol(realm, stocklistItemListResponse);
                 } else {
                     App.showLog("=====fav not added----Add==");

                     if (realm != null) {
                         view.setSelected(true);
                         fabFavourite.setImageResource(R.drawable.ic_star_black_24dp);
                         App.insertStockSymbol(realm, stocklistItemListResponse);
                     }
                 }
             }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        HideKey.initialize(this);
    }

    private void getIntentData()
    {
        try{
            if(getIntent().getExtras() !=null) {
                Bundle bundle = getIntent().getExtras();

                if(bundle !=null && bundle.getSerializable(App.tagStocklistItemListResponse) !=null)
                {
                    stocklistItemListResponse = (StocklistItemListResponse) bundle.getSerializable(App.tagStocklistItemListResponse);
                }

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        // Retrieve the SearchView and plug it into SearchManager
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search symbol");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                strSymbol = query;
                App.showLog("===query=strSymbol=" +strSymbol );
                mPresenter.loadPlaylistItems(strSymbol);
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
        //mSwipeRefreshLayout.setRefreshing(loading);
    }

    public void setDataView(StockDetailResponse stockDetailResponse) {
        if (stockDetailResponse.LastPrice != null) {
            viewPager.setVisibility(View.VISIBLE);
            strSymbol = stockDetailResponse.Symbol;
            App.showLog("======stockDetailResponse==Name==" + stockDetailResponse.Name);
            App.showLog("======stockDetailResponse==strSymbol==" + strSymbol);
            tvName.setText(stockDetailResponse.Name);

            initViewPager();
            tvLastPrice.setText(stockDetailResponse.LastPrice);
            tvDetail.setText("Symbol - " + stockDetailResponse.Symbol + "\n" +
                    "Change - " + stockDetailResponse.Change + "\n" +
                    "ChangePercent - " + stockDetailResponse.ChangePercent + "\n" +
                    "Timestamp - " + stockDetailResponse.Timestamp + "\n" +
                    "MSDate - " + stockDetailResponse.MSDate + "\n" +
                    "MarketCap - " + stockDetailResponse.MarketCap + "\n" +
                    "Volume - " + stockDetailResponse.Volume + "\n" +
                    "ChangeYTD - " + stockDetailResponse.ChangeYTD + "\n" +
                    "ChangePercentYTD - " + stockDetailResponse.ChangePercentYTD + "%\n" +
                    "High - $" + stockDetailResponse.High + "\n" +
                    "Low - $" + stockDetailResponse.Low + "\n" +
                    "Open - $" + stockDetailResponse.Open + "\n"
            );

            stocklistItemListResponse.Name = stockDetailResponse.Name;
            stocklistItemListResponse.Symbol = stockDetailResponse.Symbol;
            stocklistItemListResponse.Exchange = "";

            if(App.getCheckIsFavorite(realm,stocklistItemListResponse.Symbol,stocklistItemListResponse.Exchange) !=null  && App.getCheckIsFavorite(realm,stocklistItemListResponse.Symbol,stocklistItemListResponse.Exchange).size() > 0){
                fabFavourite.setImageResource(R.drawable.ic_star_black_24dp);
                fabFavourite.setSelected(true);
            }
            else
            {
                fabFavourite.setImageResource(R.drawable.ic_star_border_black_24dp);
                fabFavourite.setSelected(false);
            }

        } else {
            viewPager.setVisibility(View.GONE);
            tvName.setText("No data found.");
            tvLastPrice.setText("");
            tvDetail.setText("");
        }
    }

    private void initViewPager() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            integerArrayList.add(i);
        }

        viewPager.setAdapter(new SlidingImageAdapter(StockDetailActivity.this, integerArrayList, strSymbol));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
