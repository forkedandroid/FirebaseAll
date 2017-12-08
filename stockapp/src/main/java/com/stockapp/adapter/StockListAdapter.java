package com.stockapp.adapter;

import android.content.Intent;
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
public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.ViewHolder> {
  private static final String TAG = "StockListAdapter";

  private List<StocklistItemListResponse> mDataSet;
  Realm realm;

  // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
  /**
   * Initialize the dataset of the Adapter.
   *
   * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
   */
  public StockListAdapter(List<StocklistItemListResponse> dataSet,Realm realm) {
    mDataSet = dataSet;
    this.realm = realm;
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
    StocklistItemListResponse stocklistItemListResponse= mDataSet.get(position);
    viewHolder.setTitle(stocklistItemListResponse.Name);
    viewHolder.setDescription("Symbol - "+stocklistItemListResponse.Symbol +
            "\nExchange - "+ stocklistItemListResponse.Exchange);

    if(realm !=null)
    {
      if(App.getCheckIsFavorite(realm,stocklistItemListResponse.Symbol + "_"+stocklistItemListResponse.Exchange) !=null  && App.getCheckIsFavorite(realm,stocklistItemListResponse.Symbol + "_"+stocklistItemListResponse.Exchange).size() > 0)
      {
        viewHolder.imageView.setSelected(true);
        viewHolder.imageView.setImageResource(R.drawable.ic_star_black_24dp);
      }
      else
      {
        viewHolder.imageView.setSelected(false);
        viewHolder.imageView.setImageResource(R.drawable.ic_star_border_black_24dp);
      }
    }
    viewHolder.descriptionTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        App.showLog("========position===="+position);
        App.showLog("========position====Symbol==="+mDataSet.get(position).Symbol);

       /* Intent intent = new Intent(co,StockDetailActivity.class);
        intent.putExtra(App.tagStocklistItemListResponse , mDataSet.get(position));
        mView.startActivity(intent);*/
      }
    });



    viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(view.isSelected() == true)
        {
         App.showLog("=====fav added----Remove==");
        }
        else
        {
          App.showLog("=====fav not added----Add==");

          if(realm !=null)
          {
            App.insertStockSymbol(realm,mDataSet.get(position));
          }
        }
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
