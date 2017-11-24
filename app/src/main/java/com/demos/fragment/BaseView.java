package com.demos.fragment;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by prashant.patel on 11/24/2017.
 */

public interface BaseView extends MvpView {


    void showProgress();

    void onCompleted();

    void onError(Throwable e);
}
