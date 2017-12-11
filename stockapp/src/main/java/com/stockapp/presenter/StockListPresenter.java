package com.stockapp.presenter;

import android.content.Intent;
import android.util.Log;

import com.stockapp.App;
import com.stockapp.adapter.StockListAdapter;
import com.stockapp.model.PlaylistItem;
import com.stockapp.model.StocklistItemListResponse;
import com.stockapp.service.ApiService;
import com.stockapp.view.StockDetailActivity;
import com.stockapp.view.StockListActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class StockListPresenter {

  private static final String TAG = "StockListPresenter";
  private StockListActivity mView;
  private ApiService mYouTubeService;
  String input = "";
  private StockListAdapter mAdapter;
  private List<StocklistItemListResponse> listStocklistItemListResponse;



  public StockListPresenter(StockListActivity activity, ApiService youTubeService, Realm realm) {
    mView = activity;
    mYouTubeService = youTubeService;
    listStocklistItemListResponse = new ArrayList<>();

    mAdapter = new StockListAdapter(activity,listStocklistItemListResponse,realm);




  }

  /**
   * Load items from YouTube API using RxJava.
   */
  public void loadPlaylistItems(String input) {
      App.showLog(TAG , "======loadPlaylistItems===input="+input);
      mView.setLoading(true);
        this.input = input;
        mYouTubeService
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
                  Log.d(TAG, "We got an error: " + e);
                  mView.setLoading(false);
              }

              @Override
              public void onComplete() {
                  App.showLog(TAG , "======onComplete===");
                  mView.swapAdapter(mAdapter);
                  mView.setLoading(false);
              }
            });

  }

  /**
   * Intent to launch YouTube app based on {@link PlaylistItem} video ID.
   * @param position Position of {@link PlaylistItem} in dataset
   */
  public void startYouTubeIntent(int position) {

    App.showLog("========position===="+position);
    App.showLog("========position====Symbol==="+listStocklistItemListResponse.get(position).Symbol);

    Intent intent = new Intent(mView,StockDetailActivity.class);
    intent.putExtra(App.tagStocklistItemListResponse , listStocklistItemListResponse.get(position));
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
