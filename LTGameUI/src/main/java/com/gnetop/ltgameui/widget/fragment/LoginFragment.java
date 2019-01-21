package com.gnetop.ltgameui.widget.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.gnetop.ltgamecommon.impl.OnLoginSuccessListener;
import com.gnetop.ltgamecommon.model.BaseEntry;
import com.gnetop.ltgamecommon.model.BundleData;
import com.gnetop.ltgamecommon.model.ResultData;
import com.gnetop.ltgamefacebook.FaceBookLoginManager;
import com.gnetop.ltgamegoogle.GoogleLoginManager;
import com.gnetop.ltgameui.R;
import com.gnetop.ltgameui.base.BaseFragment;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    LinearLayout mLytGoogle, mLytFaceBook;
    String mAgreementUrl;
    String mPrivacyUrl;
    String googleClientID;
    String baseURL;
    String LTAppID;
    String LTAppKey;

    public static LoginFragment newInstance(BundleData data) {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        args.putSerializable(ARG_NUMBER, data);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getFragmentId() {
        return R.layout.fragment_loign;
    }

    @Override
    protected void initView(View view) {
        mLytGoogle = view.findViewById(R.id.lyt_login_google);
        mLytGoogle.setOnClickListener(this);

        mLytFaceBook = view.findViewById(R.id.lyt_login_facebook);
        mLytFaceBook.setOnClickListener(this);
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        Bundle args = getArguments();
        if (args != null) {
            BundleData mData = (BundleData) args.getSerializable(ARG_NUMBER);
            if (mData != null) {
                mAgreementUrl = mData.getAgreementUrl();
                mPrivacyUrl = mData.getPrivacyUrl();
                googleClientID = mData.getGoogleClientID();
                baseURL = mData.getBaseURL();
                LTAppID = mData.getLTAppID();
                LTAppKey = mData.getLTAppKey();
                Log.e("LoginFragment", mPrivacyUrl + "====" + mAgreementUrl
                        + "===" + googleClientID + "===" + baseURL + "===" + LTAppKey + "===" + LTAppID);
            }
        }
        initFaceBook();
    }

    @Override
    public void onClick(View view) {
        int resID = view.getId();
        if (resID == R.id.lyt_login_facebook) {//facebook
            FaceBookLoginManager.getInstance().faceBookLogin(mActivity);
        } else if (resID == R.id.lyt_login_google) {//google
            if (!TextUtils.isEmpty(googleClientID)) {
                GoogleLoginManager.googleLogin(mActivity,
                        googleClientID);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FaceBookLoginManager.getInstance().setOnActivityResult(requestCode, resultCode, data);
        if (!TextUtils.isEmpty(baseURL) &&
                !TextUtils.isEmpty(LTAppID) &&
                !TextUtils.isEmpty(LTAppKey)) {
            GoogleLoginManager.onGoogleResult(requestCode, data, mActivity,
                    baseURL, LTAppID, LTAppKey,
                    new OnLoginSuccessListener() {
                        @Override
                        public void onSuccess(BaseEntry<ResultData> result) {
                            Log.e("google==", "onSuccess" + result.getResult());
                            toAgreementUrl();
                        }

                        @Override
                        public void onFailed(Throwable ex) {
                            Log.e("google==", "onFailed" + ex.getMessage());
                            loginFailed();
                            GoogleLoginManager.stopConnection(mActivity);
                        }

                        @Override
                        public void onComplete() {
                            Log.e("google==", "onComplete");
                        }

                        @Override
                        public void onParameterError(String result) {
                            Log.e("google==", "nParameterError" + result);
                            loginFailed();
                            GoogleLoginManager.stopConnection(mActivity);
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("google==", "onError" + error);
                            loginFailed();
                            GoogleLoginManager.stopConnection(mActivity);
                        }
                    });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GoogleLoginManager.stopConnection(mActivity);
    }

    /**
     * 登录失败
     */
    private void loginFailed() {
        if (findChildFragment(LoginFailedFragment.class) == null) {
            BundleData data = new BundleData();
            data.setAgreementUrl(mAgreementUrl);
            data.setPrivacyUrl(mPrivacyUrl);
            data.setLTAppKey(LTAppKey);
            data.setLTAppID(LTAppID);
            data.setGoogleClientID(googleClientID);
            data.setBaseURL(baseURL);
            getProxyActivity().addFragment(LoginFailedFragment.newInstance(data),
                    false,
                    true);
        }
    }

    /**
     * 登录失败
     */
    private void toAgreementUrl() {
        if (findChildFragment(AgreementFragment.class) == null) {
            BundleData data = new BundleData();
            data.setAgreementUrl(mAgreementUrl);
            data.setPrivacyUrl(mPrivacyUrl);
            data.setLTAppKey(LTAppKey);
            data.setLTAppID(LTAppID);
            data.setGoogleClientID(googleClientID);
            data.setBaseURL(baseURL);
            getProxyActivity().addFragment(AgreementFragment.newInstance(data),
                    false,
                    true);
        }
    }

    /**
     * 初始化facebook
     */
    private void initFaceBook() {
        if (!TextUtils.isEmpty(baseURL) &&
                !TextUtils.isEmpty(LTAppID) &&
                !TextUtils.isEmpty(LTAppKey)) {

            FaceBookLoginManager.getInstance().initFaceBook(mActivity, baseURL,
                    LTAppID, LTAppKey,
                    new OnLoginSuccessListener() {
                        @Override
                        public void onSuccess(BaseEntry<ResultData> result) {
                            Log.e("facebook", result.getResult());
                            toAgreementUrl();
                        }

                        @Override
                        public void onFailed(Throwable ex) {
                            Log.e("facebook", ex.getMessage());
                            loginFailed();
                        }

                        @Override
                        public void onComplete() {
                            Log.e("facebook", "===onComplete");
                        }

                        @Override
                        public void onParameterError(String result) {
                            Log.e("facebook", result);
                            loginFailed();
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("facebook", error);
                            loginFailed();
                        }
                    });
        }
    }

}
