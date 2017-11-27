package com.demos.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.firebaseall.R;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends SimpleFragment {

    @BindView(R.id.fabNext)
    FloatingActionButton fabNext;
    
    @BindView(R.id.tvLogin)
    TextView tvLogin;

    @BindView(R.id.tvForgot)
    TextView tvForgot;


    @BindView(R.id.ivBgImage)
    ImageView ivBgImage;

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
        /*Glide.with(getActivity())
                .load("asdasd")
                .into(ivBgImage);*/

        Glide.with(this)
                .load("err")
               // .apply(new RequestOptions().override(400, 400).placeholder(R.drawable.bg_image).error(R.drawable.bg_image))
                .apply(new RequestOptions().placeholder(R.drawable.bg_image).error(R.drawable.bg_image))
                .into(ivBgImage);


     /*   Glide.with(getActivity()).asBitmap()
                .load("https://wallpaperscraft.com/image/line_pattern_background_texture_light_50374_1080x1920.jpg")
                .into(new SimpleTarget<Bitmap>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        Bitmap blurred = blurRenderScript(resource, 10);
                        ivBgImage.setImageBitmap(blurred);
                    }
                });*/


      //  Picasso.with(getActivity()).load(R.drawable.bg_image).into(ivBgImage);

        /*GlideApp
                .with(getActivity())
                .load("qawe24")
                .placeholder(R.drawable.bg_image) // can also be a drawable
                .into(ivBgImage);
*/

    }

    @Override
    public void initData() {

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {
        RenderScript rs = RenderScript.create(getActivity());
        Bitmap blurredBitmap = smallBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
        Allocation output = Allocation.createTyped(rs, input.getType());

        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setInput(input);

        script.setRadius(radius);
        script.forEach(output);
        output.copyTo(blurredBitmap);

        return blurredBitmap;
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
