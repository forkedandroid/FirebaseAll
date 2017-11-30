package com.demos.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebaseall.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgotPasswordFragment extends SimpleFragment {

    @BindView(R.id.fabNext)
    FloatingActionButton fabNext;
    
    @BindView(R.id.tvLogin)
    TextView tvLogin;

    @BindView(R.id.tvRegister)
    TextView tvRegister;

    @BindView(R.id.tvForgot)
    TextView tvForgot;


    @BindView(R.id.ivBgImage)
    ImageView ivBgImage;

    @BindView(R.id.status)
    TextView mStatusTextView;

    @BindView(R.id.detail)
    TextView mDetailTextView;



    @BindView(R.id.verify_email_button)
    Button verify_email_button;

    @BindView(R.id.signed_in_buttons)
    LinearLayout signed_in_buttons;

    @BindView(R.id.etFullName)
    MaterialEditText etFullName;

    @BindView(R.id.etEmail)
    MaterialEditText mEmailField;

    @BindView(R.id.etPassword)
    MaterialEditText mPasswordField;

    String TAG = ForgotPasswordFragment.class.getName();


    // [START declare_auth]
    FirebaseAuth mAuth;

    // [END declare_auth]

    public static ForgotPasswordFragment newInstance() {

        Bundle args = new Bundle();

        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }



    String uid;
    String coverUrl;

    public static ForgotPasswordFragment newInstance(String uid, String coverUrl) {

        Bundle args = new Bundle();

        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
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
        com.firebaseall.App.showLog(TAG,"===initData====");

        mPasswordField.setVisibility(View.GONE);
        tvForgot.setVisibility(View.GONE);
        tvLogin.setText("Forgot Password");
        tvRegister.setText("Login");

        tvLogin.setSelected(true);

        Glide.with(this)
                .load("err")
               // .apply(new RequestOptions().override(400, 400).placeholder(R.drawable.bg_image).error(R.drawable.bg_image))
                .apply(new RequestOptions().placeholder(R.drawable.bg_image).error(R.drawable.bg_image))
                .into(ivBgImage);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

    }

    @Override
    public void initData() {
        com.firebaseall.App.showLog(TAG,"===initData====");
        if(mAuth !=null) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }
    }


    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }



        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }


        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void forgotPassword(String email) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }


        // [START sign_in_with_email]

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(getActivity(), "Email sent successfully.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Log.d(TAG, "Email sent fail.");
                            Toast.makeText(getActivity(), "Email id not registered.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        // Disable button
        verify_email_button.setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        verify_email_button.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getActivity(),
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
       /* if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }*/

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));


            signed_in_buttons.setVisibility(View.VISIBLE);

            verify_email_button.setEnabled(!user.isEmailVerified());
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);


            signed_in_buttons.setVisibility(View.GONE);
        }
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

    @OnClick({R.id.fabNext,R.id.tvForgot,R.id.sign_out_button,R.id.verify_email_button,R.id.tvLogin,R.id.tvRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabNext:
                forgotPassword(mEmailField.getText().toString());
                break;
                case R.id.tvRegister:
                    //closeFragment(ForgotPasswordFragment.this);
                    getActivity().onBackPressed();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.verify_email_button:
                sendEmailVerification();
                break;

           /* case R.id.tvLogin:
                if(tvLogin.isSelected() == false) {
                    tvLogin.setSelected(true);
                    tvRegister.setSelected(false);
                    com.firebaseall.App.collapse(etFullName);
                }
                break;

            case R.id.tvForgot:
                if(tvLogin.isSelected() == true) {
                    tvLogin.setSelected(false);
                    tvRegister.setSelected(true);
                    com.firebaseall.App.expandWRAP_CONTENT(etFullName);
                }
                break;*/

        }
    }



}
