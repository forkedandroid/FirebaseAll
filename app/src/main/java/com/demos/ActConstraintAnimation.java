package com.demos;

import android.annotation.SuppressLint;
import android.app.Activity;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.constraint.ConstraintSet;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;

import com.firebaseall.R;

/**
 * Created by prashant.patel on 11/23/2017.
 */

public class ActConstraintAnimation extends Activity
{
    ConstraintSet setOne = new ConstraintSet();
    ConstraintSet setTwo = new ConstraintSet();
    ActConstraintAnimation binding;

  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActConstraintAnimation binding = DataBindingUtil.setContentView(this, R.layout.act_constraint_animation);
    }*/

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

    }

}
