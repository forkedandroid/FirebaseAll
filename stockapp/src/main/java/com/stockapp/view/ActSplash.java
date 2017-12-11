package com.stockapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stockapp.App;
import com.stockapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prashant.patel on 12/11/2017.
 */

public class ActSplash extends Activity
{
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar2;

    boolean checkExpire = true;
    String strExpDate = "2017-12-29";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.actsplash);
        ButterKnife.bind(this);

        checkCondition();
        /*
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/


    }


    public static boolean isDateBefore(String startDate,String endDate)
    {
        try
        {
            String myFormatString = "yyyy-MM-dd"; // for example
            SimpleDateFormat df = new SimpleDateFormat(myFormatString);
            Date date1 = df.parse(endDate);
            Date startingDate = df.parse(startDate);

            if (date1.before(startingDate))
                return true;
            else
                return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }


    private void checkCondition()
    {
        try{
            {

                String date = new SimpleDateFormat("yyyy-MM-dd").format( new Date());

             //   App.showLog("===date=="+date);

                if(isDateBefore(date,strExpDate) == true){
                    checkExpire = true;
                }
                else {
                    checkExpire = false;
                }


                if(checkExpire == true)
                {
                    tvName.setText("Opps, App Expired contact developer.");
                    progressBar2.setVisibility(View.GONE);
                }
                else {
                    tvName.setText(getResources().getString(R.string.app_name));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            finish();
                            startActivity(new Intent(ActSplash.this, FavoriteStockListActivity.class));
                        }
                    }, 2500);
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
