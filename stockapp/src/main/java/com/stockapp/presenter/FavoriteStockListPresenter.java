package com.stockapp.presenter;

import android.content.Intent;

import com.stockapp.App;
import com.stockapp.adapter.FavoriteListAdapter;
import com.stockapp.adapter.StockListAdapter;
import com.stockapp.model.PlaylistItem;
import com.stockapp.model.StocklistItemListResponse;
import com.stockapp.service.ApiService;
import com.stockapp.view.FavoriteStockListActivity;
import com.stockapp.view.StockDetailActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class FavoriteStockListPresenter {

    private static final String TAG = "FavoriteStockListPresenter";
    private FavoriteStockListActivity mView;
    private ApiService mYouTubeService;
    String input = "";
    private FavoriteListAdapter mAdapter;
    private List<StocklistItemListResponse> listStocklistItemListResponse;
    Realm realm;


    public FavoriteStockListPresenter(FavoriteStockListActivity activity, ApiService youTubeService, Realm realm) {
        mView = activity;
        this.realm = realm;
        mYouTubeService = youTubeService;
        listStocklistItemListResponse = new ArrayList<>();
        mAdapter = new FavoriteListAdapter(activity, listStocklistItemListResponse, realm);
    }

    /**
     * Load items from YouTube API using RxJava.
     */
    public void loadPlaylistItems(String input) {
        App.showLog(TAG, "======loadPlaylistItems===input=" + input);

        mView.setLoading(true);
        this.input = input;
        listStocklistItemListResponse.clear();
        listStocklistItemListResponse.addAll(App.getAllStockSymbolList(realm));
        mView.swapAdapter(mAdapter);
        mView.setLoading(false);



     /*   mYouTubeService
            .getSearchStockCompanyList(input)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ArrayList<StocklistItemListResponse>>() {

              @Override
              public void onSubscribe(Disposable d) {
                  App.showLog(TAG , "======onSubscribe===");

              }

              @Override
              public void onNext(ArrayList<StocklistItemListResponse> value) {
                  App.showLog(TAG , "======onNext===");
                  listStocklistItemListResponse.clear();
                  listStocklistItemListResponse.addAll(value);
              }

              @Override
              public void onError(Throwable e) {
                  App.showLog(TAG , "======onError===");
                  App.showLog(TAG, "We got an error: " + e);
                  mView.setLoading(false);
              }

              @Override
              public void onComplete() {
                  App.showLog(TAG , "======onComplete===");
                  mView.swapAdapter(mAdapter);
                  mView.setLoading(false);
              }
            });*/

    }

    /**
     * Intent to launch YouTube app based on {@link PlaylistItem} video ID.
     *
     * @param position Position of {@link PlaylistItem} in dataset
     */
    public void startYouTubeIntent(int position) {

        App.showLog("========position====" + position);
        App.showLog("========position====Symbol===" + listStocklistItemListResponse.get(position).Symbol);

        Intent intent = new Intent(mView, StockDetailActivity.class);
        intent.putExtra(App.tagStocklistItemListResponse, listStocklistItemListResponse.get(position));
        mView.startActivity(intent);


    /* Intent intent = new Intent(Intent.ACTION_VIEW,
        Uri.parse(
            INTENT_URL + listStocklistItemListResponse.get(position).getSnippet().getResourceId().getVideoId()));
    mView.startActivity(intent);*/
    }

    public void loadPlaylistItems() {
        loadPlaylistItems(input);
    }
}
