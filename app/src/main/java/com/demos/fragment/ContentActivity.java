package com.demos.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.firebaseall.R;
import com.king.base.util.LogUtils;

/**
 * Created by prashant.patel on 11/24/2017.
 */

public class ContentActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        swichFragment(getIntent());
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

}
