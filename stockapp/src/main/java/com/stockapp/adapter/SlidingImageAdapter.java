package com.stockapp.adapter;

/**
 * Created by prashant.patel on 12/8/2017.
 */
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stockapp.R;

import java.util.ArrayList;


public class SlidingImageAdapter extends PagerAdapter {


    String strSymbol = "a";
    private ArrayList<Integer> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImageAdapter(Context context,ArrayList<Integer> IMAGES,String strSymbol) {
        this.context = context;
        this.IMAGES=IMAGES;
        this.strSymbol = strSymbol;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.pager_adapter, view, false);

        assert imageLayout != null;

        final ImageView ivImage = (ImageView) imageLayout
                .findViewById(R.id.ivImage);
        final TextView tvDays = (TextView) imageLayout
                .findViewById(R.id.tvDays);

        Picasso.with(context).load("https://finance.google.com/finance/getchart?p="+(position + 1)+"d&q=" + strSymbol).placeholder(R.drawable.ic_poll_black_48dp).fit().centerInside().into(ivImage);
        //ivImage.setImageResource(IMAGES.get(position));
        tvDays.setText((position + 1)+" Days");
        view.addView(imageLayout, 0);


        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}