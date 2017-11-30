package com.demos.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demos.fragment.bean.LiveInfo;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import com.king.base.util.LogUtils;
import com.utils.HideKey;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by prashant.patel on 11/24/2017.
 */


public abstract class BaseFragment<V extends BaseView, P extends BasePresenter<V>>  extends MvpFragment<V,P> {

    protected Context context;

    private View rootView;

    private Unbinder mUnbinder;

    public <T extends View> T findView(@IdRes int id){
        return (T)rootView.findViewById(id);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getActivity();
        rootView = inflater.inflate(getRootViewId(),container,false);
        mUnbinder = ButterKnife.bind(this,rootView);
        LogUtils.d("onCreateView");
        initUI();
        HideKey.initialize(getActivity(), rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mUnbinder!=null)
            mUnbinder.unbind();
    }

    public View getRootView(){
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void replaceFragment(@IdRes int id, Fragment fragment) {
        getFragmentManager().beginTransaction().replace(id, fragment).commit();
    }


    public void replaceFragmentNullBack(@IdRes int id, Fragment fragment) {

        //getFragmentManager().beginTransaction().replace(id, fragment).commit();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(id, fragment ); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }


    public void closeFragment(Fragment fragment) {
        fragment.getActivity().getFragmentManager().popBackStack();
    }

    public void replaceChildFragment(@IdRes int id, Fragment fragment) {
        getChildFragmentManager().beginTransaction().replace(id, fragment).commit();
    }

    public App getApp(){
        return (App)getActivity().getApplication();
    }

    public <T> void  toSetList(List<T> list, List<T> newList, boolean isMore){

        if(list!=null && newList!=null){
            synchronized (BaseFragment.class){
                if(!isMore){
                    list.clear();
                }
                list.addAll(newList);
            }
        }
    }


    public static void registerEvent(Object obj){
        EventBus.getDefault().register(obj);
    }

    public static void unregisterEvent(Object obj){
        EventBus.getDefault().unregister(obj);
    }

    public static void sendEvent(Object obj){
        EventBus.getDefault().post(obj);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Object obj){

    }

    //--------------------------------

    protected Intent getIntent(){
        return getActivity().getIntent();
    }


    protected Intent getFragmentIntent(int fragmentKey){
        Intent intent = getContentActivityIntent();
        intent.putExtra(Constants.KEY_FRAGMENT,fragmentKey);
        return intent;
    }

    protected Intent getContentActivityIntent(){
       // return new Intent(context, ContentActivity.class);
        return new Intent(context, ContentActivity.class);
    }

    protected void startActivity(Class<?> cls){
        startActivity(new Intent(context,cls));
    }


    protected void finish(){
        getActivity().finish();
    }


    protected void startWeb(String title,String url){
        Intent intent = getFragmentIntent(Constants.WEB_FRAGMENT);
        intent.putExtra(Constants.KEY_TITLE,title);
        intent.putExtra(Constants.KEY_URL,url);
        startActivity(intent);
    }

    protected void startRoom(LiveInfo liveInfo){

        int fragmentKey = Constants.ROOM_FRAGMENT;
        if(Constants.SHOWING.equalsIgnoreCase(liveInfo.getCategory_slug())){
            fragmentKey = Constants.FULL_ROOM_FRAGMENT;
        }
        Intent intent = getFragmentIntent(fragmentKey);
        intent.putExtra(Constants.KEY_UID,liveInfo.getUid());
        intent.putExtra(Constants.KEY_COVER,liveInfo.getThumb());
        startActivity(intent);
    }

    protected void startLogin(){
        Intent intent = getFragmentIntent(Constants.LOGIN_FRAGMENT);
        startActivity(intent);
    }


    protected void startAbout(){
        Intent intent = getFragmentIntent(Constants.ABOUT_FRAGMENT);
        startActivity(intent);
    }

    //--------------------------------


    public abstract int getRootViewId();

    public abstract void initUI();

    public abstract void initData();


}
