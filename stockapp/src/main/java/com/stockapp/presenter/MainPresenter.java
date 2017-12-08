package com.stockapp.presenter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;

import com.stockapp.App;
import com.stockapp.adapter.CustomAdapter;
import com.stockapp.model.PlaylistItem;
import com.stockapp.model.PlaylistItemListResponse;
import com.stockapp.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Presenter class for MVP triad.
 *
 * @author Josiah Campbell
 * @version December 2015
 */
public class MainPresenter {

  private static final String TAG = "MainPresenter";
  private static final String INTENT_URL = "https://www.youtube.com/watch?v=";
  private static final String DEFAULT_PLAYLIST
      = "UUAPurJWGIUtvlul3mApdQRw"; // GVSU YouTube Channel
  private MainActivity mView;

  private Adapter mAdapter;
  private List<PlaylistItem> playlistItems;
  private String playlistId;
  private String key;
  private String nextPageToken;


  public MainPresenter(MainActivity activity) {
    mView = activity;
    playlistItems = new ArrayList<>();

    playlistId = DEFAULT_PLAYLIST;
    key = "AIzaSyBUPwUuvnWxYyYN-TpX0m7fGH7dPPcq0tY";
    nextPageToken = "";
    mAdapter = new CustomAdapter(playlistItems);




  }

  /**
   * Load items from YouTube API using RxJava.
   */
  public void loadPlaylistItems() {

            App.getApiService()
            .getPlayListItemsList(playlistId, nextPageToken, key)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<PlaylistItemListResponse>() {

              @Override
              public void onSubscribe(Disposable d) {

              }

              @Override
              public void onNext(PlaylistItemListResponse playlistItemListResponse) {
                playlistItems.clear();
                playlistItems.addAll(playlistItemListResponse.getPlaylistItems());
              }

              @Override
              public void onError(Throwable e) {
                Log.d(TAG, "We got an error: " + e);
                mView.setLoading(false);
              }

              @Override
              public void onComplete() {
                mView.swapAdapter(mAdapter);
                mView.setLoading(false);
              }
            });




  }

  public void setPlaylistId(String playlistId) {
    this.playlistId = playlistId;
  }

  /**
   * Intent to launch YouTube app based on {@link PlaylistItem} video ID.
   * @param position Position of {@link PlaylistItem} in dataset
   */
  public void startYouTubeIntent(int position) {
    Intent intent = new Intent(Intent.ACTION_VIEW,
        Uri.parse(
            INTENT_URL + playlistItems.get(position).getSnippet().getResourceId().getVideoId()));
    mView.startActivity(intent);
  }

}
