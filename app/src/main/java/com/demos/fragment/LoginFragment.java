package com.demos.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebaseall.R;
import com.king.base.util.SystemUtils;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginFragment extends SimpleFragment {

    @BindView(R.id.fabNext)
    FloatingActionButton fabNext;
    
    @BindView(R.id.tvLogin)
    TextView tvLogin;

    @BindView(R.id.tvForgot)
    TextView tvForgot;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }



    private String uid;
    private String coverUrl;

    public static LoginFragment newInstance(String uid, String coverUrl) {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.uid = uid;
        fragment.coverUrl = coverUrl;
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getRootViewId() {
        return R.layout.fragment_login;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void initUI() {
        tvLogin.setText("Login");

    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.fabNext,R.id.tvForgot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabNext:
                finish();
                break;
            case R.id.tvForgot:
                finish();
                break;

        }
    }
}
