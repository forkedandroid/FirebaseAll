package com.stockapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stockapp.App;
import com.stockapp.R;
import com.stockapp.model.StocklistItemListResponse;
import com.stockapp.view.StockDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {
  private static final String TAG = "FavoriteListAdapter";

  private List<StocklistItemListResponse> mDataSet;
  Realm realm;
  Activity activity;

  // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
  /**
   * Initialize the dataset of the Adapter.
   *
   * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
   */
  public FavoriteListAdapter(Activity activity, List<StocklistItemListResponse> dataSet, Realm realm) {
    mDataSet = dataSet;
    this.realm = realm;
    this.activity = activity;
  }
  // END_INCLUDE(recyclerViewSampleViewHolder)

  // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
  // Create new views (invoked by the layout manager)
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    // Create a new view.
    View v = LayoutInflater.from(
        viewGroup.getContext()).inflate(R.layout.item_two_line, viewGroup, false);

    return new ViewHolder(v);
  }

  // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(ViewHolder viewHolder, final int position) {
    // Get element from your dataset at this position and replace the contents of the view
    // with that element
    StocklistItemListResponse stocklistItemListResponse = mDataSet.get(position);
    viewHolder.setTitle(stocklistItemListResponse.Name);
    viewHolder.setDescription("Symbol - "+stocklistItemListResponse.Symbol +
            "\nExchange - "+ stocklistItemListResponse.Exchange);

    if(realm !=null)
    {
      if(App.getCheckIsFavorite(realm,stocklistItemListResponse.Symbol,stocklistItemListResponse.Exchange) !=null  && App.getCheckIsFavorite(realm,stocklistItemListResponse.Symbol,stocklistItemListResponse.Exchange).size() > 0)
      {
        viewHolder.imageView.setSelected(true);
        viewHolder.imageView.setImageResource(R.drawable.ic_star_black_24dp);
       // mDataSet.get(position).isFavorite = "1";
      }
      else
      {
        viewHolder.imageView.setSelected(false);
        viewHolder.imageView.setImageResource(R.drawable.ic_star_border_black_24dp);
        //mDataSet.get(position).isFavorite = "0";
      }
    }
    viewHolder.cvMain.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        App.showLog("========position===="+position);
        App.showLog("========position====Symbol==="+mDataSet.get(position).Symbol);

        Intent intent = new Intent(activity,StockDetailActivity.class);

        StocklistItemListResponse stocklistItemListResponse = new StocklistItemListResponse();
        stocklistItemListResponse.Exchange = mDataSet.get(position).Exchange;
        stocklistItemListResponse.Symbol= mDataSet.get(position).Symbol;
        stocklistItemListResponse.Name= mDataSet.get(position).Name;
        stocklistItemListResponse.isFavorite= mDataSet.get(position).isFavorite;

        Bundle bundle = new Bundle();
        bundle.putSerializable(App.tagStocklistItemListResponse, stocklistItemListResponse);

        intent.putExtras(bundle);
        activity.startActivity(intent);
      }
    });



    viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(view.isSelected() == true)
        {
         App.showLog("=====fav added----Remove==");
          view.setSelected(false);
          App.removeStockSymbol(realm,mDataSet.get(position));
          mDataSet.remove(position);
        }
        else
        {
          App.showLog("=====fav not added----Add==");

          if(realm !=null)
          {
            view.setSelected(true);
            App.insertStockSymbol(realm,mDataSet.get(position));
          }
        }

        notifyDataSetChanged();
      }
    });
  //  viewHolder.setImageView(stocklistItemListResponse.getThumbnails().getHigh().getUrl());
  }
  // END_INCLUDE(recyclerViewOnCreateViewHolder)

  // Return the size of your dataset (invoked by the layout manager)
  @Override
  public int getItemCount() {
    return mDataSet.size();
  }
  // END_INCLUDE(recyclerViewOnBindViewHolder)

  /**
   * Provide a reference to the type of views that you are using (custom ViewHolder).
   */
  public static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    TextView titleTextView;
    @BindView(R.id.description)
    TextView descriptionTextView;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.cvMain)
    CardView cvMain;


    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    public void setTitle(String title) {
      titleTextView.setText(title);
    }

    public void setDescription(String description) {
      descriptionTextView.setText(description);
    }

    /**
     * Populate item ImageView with video thumbnail using Picasso.
     *
     * @param url Url for video thumbnail
     */
    public void setImageView(String url) {
      Picasso.with(App.getAppContext())
          .load(url)
          .into(imageView);
    }
  }
}
