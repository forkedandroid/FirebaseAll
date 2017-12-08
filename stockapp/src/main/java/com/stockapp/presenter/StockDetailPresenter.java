package com.stockapp.presenter;

import android.util.Log;

import com.stockapp.App;
import com.stockapp.model.StockDetailResponse;
import com.stockapp.service.ApiService;
import com.stockapp.view.StockDetailActivity;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StockDetailPresenter {

  private static final String TAG = "StockDetailPresenter";

  private StockDetailActivity mView;
  private ApiService mYouTubeService;
  private StockDetailResponse stockDetailResponse;
  String input = "";

  public StockDetailPresenter(StockDetailActivity activity, ApiService youTubeService) {
    mView = activity;
    mYouTubeService = youTubeService;
    stockDetailResponse = new StockDetailResponse();
  }

  /**
   * Load items from YouTube API using RxJava.
   */
  public void loadPlaylistItems(String input) {
      App.showLog(TAG , "======loadPlaylistItems===input="+input);
      mView.setLoading(true);
      this.input = input;
        mYouTubeService
            .getStockDetail(input)
            .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<StockDetailResponse>() {

              @Override
              public void onSubscribe(Disposable d) {

              }

              @Override
              public void onNext(StockDetailResponse value) {
                  stockDetailResponse = value;
              }

              @Override
              public void onError(Throwable e) {
                  Log.d(TAG, "We got an error: " + e);
                  mView.setLoading(false);
              }

              @Override
              public void onComplete() {
                  mView.setDataView(stockDetailResponse);
                  mView.setLoading(false);
              }
            });

  }
    public void loadPlaylistItems() {
        loadPlaylistItems(input);
    }
}
