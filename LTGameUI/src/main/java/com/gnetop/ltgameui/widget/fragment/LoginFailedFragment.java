package com.gnetop.ltgameui.widget.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.gnetop.ltgamecommon.model.BundleData;
import com.gnetop.ltgamecommon.model.Event;
import com.gnetop.ltgamecommon.util.EventUtils;
import com.gnetop.ltgameui.R;
import com.gnetop.ltgameui.base.BaseFragment;
import com.gnetop.ltgameui.ui.ProgressView;

import org.greenrobot.eventbus.Subscribe;

import static com.gnetop.ltgameui.widget.fragment.LoginFragment.LOGIN_ERROR;
import static com.gnetop.ltgameui.widget.fragment.LoginFragment.LOGIN_LOADING;


public class LoginFailedFragment extends BaseFragment {

    LinearLayout mLytFailed;
    String mAgreementUrl;
    String mPrivacyUrl;
    String googleClientID;
    String baseURL;
    String LTAppID;
    String LTAppKey;
    ProgressView mPgbLoading;

    public static LoginFailedFragment newInstance(BundleData data) {
        Bundle args = new Bundle();
        LoginFailedFragment fragment = new LoginFailedFragment();
        args.putSerializable(ARG_NUMBER, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_login_failed;
    }

    @Override
    protected void initView(View view) {
        EventUtils.register(this);
        mLytFailed = view.findViewById(R.id.lyt_login_failed);
        mPgbLoading = view.findViewById(R.id.pgb_loading);
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
            }
        }
    }

    /**
     * 跳转
     *
     * @param mPrivacyUrl
     * @param mAgreementUrl
     */
    private void initData(final String mPrivacyUrl, final String mAgreementUrl) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BundleData data = new BundleData();
                data.setPrivacyUrl(mPrivacyUrl);
                data.setAgreementUrl(mAgreementUrl);
                data.setLTAppKey(LTAppKey);
                data.setLTAppID(LTAppID);
                data.setGoogleClientID(googleClientID);
                data.setBaseURL(baseURL);
                getProxyActivity().addFragment(LoginFragment.newInstance(data),
                        false,
                        true);
            }
        }, 2000);
    }

    @Subscribe
    public void receiveEvent(Event event) {
        switch (event.getCode()) {
            case LOGIN_LOADING: {//加载中
                if (mLytFailed.getVisibility()==View.VISIBLE){
                    mLytFailed.setVisibility(View.GONE);
                }
                mPgbLoading.setVisibility(View.VISIBLE);
                break;
            }
            case LOGIN_ERROR: {//失败
                if (mLytFailed.getVisibility()==View.GONE){
                    mLytFailed.setVisibility(View.VISIBLE);
                }
                mPgbLoading.setVisibility(View.GONE);
                mPgbLoading.stopAnimation();
                initData(mPrivacyUrl, mAgreementUrl);
                break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPgbLoading.stopAnimation();
        EventUtils.unregister(this);
    }
}
