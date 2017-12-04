package com.demos.fragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.firebaseall.R;
import com.king.base.util.LogUtils;
import com.utils.ConnectivityReceiverListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prashant.patel on 11/24/2017.
 */

public class ContentActivity extends AppCompatActivity {

    @BindView(R.id.tvNoConnection)
    TextView tvNoConnection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        swichFragment(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        com.firebaseall.App.setConnectivityListener(new ConnectivityReceiverListener() {
            @Override
            public void onNetworkConnectionChanged(boolean isConnected) {
                com.firebaseall.App.showLog("====onNetworkConnectionChanged======"+isConnected);
                if(isConnected == true)
                {
                    tvNoConnection.setVisibility(View.GONE);
                }
                else
                {
                    tvNoConnection.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void swichFragment(Intent intent){

        int fragmentKey = intent.getIntExtra(Constants.KEY_FRAGMENT,0);

        fragmentKey = Constants.ROOM_FRAGMENT;
        fragmentKey = Constants.WEB_FRAGMENT;


        switch (fragmentKey){
            case Constants.ROOM_FRAGMENT:
                //replaceFragment(AboutFragment.newInstance(intent.getStringExtra(Constants.KEY_UID)));
                replaceFragment(AboutFragment.newInstance());
                break;
            case Constants.LIVE_FRAGMENT: {
                String title = intent.getStringExtra(Constants.KEY_TITLE);
                String slug = intent.getStringExtra(Constants.KEY_SLUG);
                boolean isTabLive = intent.getBooleanExtra(Constants.KEY_IS_TAB_LIVE, false);
                //replaceFragment(AboutFragment.newInstance(title, slug, isTabLive));
                replaceFragment(AboutFragment.newInstance(title, slug));
                break;
            }case Constants.WEB_FRAGMENT: {
                String title = intent.getStringExtra(Constants.KEY_TITLE);
                String url = intent.getStringExtra(Constants.KEY_URL);
                replaceFragment(LoginFragment.newInstance(title, url));
                break;
            }case Constants.LOGIN_FRAGMENT:
                replaceFragment(AboutFragment.newInstance());
                break;
            case Constants.ABOUT_FRAGMENT:
                replaceFragment(AboutFragment.newInstance());
                break;
            case Constants.FULL_ROOM_FRAGMENT:
                String uid = intent.getStringExtra(Constants.KEY_UID);
                String cover = intent.getStringExtra(Constants.KEY_COVER);
                replaceFragment(AboutFragment.newInstance(uid,cover));
                break;
            case Constants.SEARCH_FRAGMENT:
                replaceFragment(AboutFragment.newInstance());
                break;
            default:
                LogUtils.d("Not found fragment:" + Integer.toHexString(fragmentKey));
                break;
        }
    }


    public void replaceFragment(Fragment fragmnet){
        replaceFragment(R.id.fragmentContent,fragmnet);
    }

    public void replaceFragment(@IdRes int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(id, fragment).commit();
    }


    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm !=null && fm.getBackStackEntryCount() > 0) {
            com.firebaseall.App.showLog("ContentActivity", "popping backstack");
            fm.popBackStack();
        } else {
            com.firebaseall.App.showLog("ContentActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }
}
