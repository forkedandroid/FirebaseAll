package com.stockapp.adapter;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.ViewHolder> {
  private static final String TAG = "StockListAdapter";

  private List<StocklistItemListResponse> mDataSet;

  // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
  /**
   * Initialize the dataset of the Adapter.
   *
   * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
   */
  public StockListAdapter(List<StocklistItemListResponse> dataSet) {
    mDataSet = dataSet;
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
