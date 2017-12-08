package com.stockapp.service;



import com.stockapp.App;
import com.stockapp.model.PlaylistItemListResponse;
import com.stockapp.model.StockDetailResponse;
import com.stockapp.model.StocklistItemListResponse;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import io.reactivex.Observable;

/**
 * Java implementation of YouTube Data API.
 * <p>While there is a Java library available, I've used the JSON web implementation
 * in order to illustrate the use of Retrofit and RxJava</p>
 *
 * @author Josiah Campbell
 * @version December 2015
 */
  public interface ApiService {
    /* Retrofit will return any object, but Gson will parse JSON responses for us.
       The result is an observable object that can be subscribed to in our
       Presenter class.
     */
    @GET("playlistItems?part=snippet&maxResults=10")
    Observable<PlaylistItemListResponse> getPlayListItemsList(
            @Query("playlistId") String playlistId,
            @Query("nextPageToken") String pageToken,
            @Query("key") String key);


  //    https://www.webname.org/api/all_list/?m=testapi

  @Multipart
  @POST("all_list/")
  Call<ArrayList<PlaylistItemListResponse>> getMarkersList(
          @Query("m") String testapi,
          @PartMap() Map<String, RequestBody> partMap

  );


  //http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input=goog
  @GET("v2/Lookup/json?")
  Observable<ArrayList<StocklistItemListResponse>> getSearchStockCompanyList(
          @Query("input") String input);

  //http://dev.markitondemand.com/MODApis/Api/v2/Quote/json/?symbol=goog
  @GET("v2/Quote/json?")
  Observable<StockDetailResponse> getStockDetail(
          @Query("symbol") String input);

}


