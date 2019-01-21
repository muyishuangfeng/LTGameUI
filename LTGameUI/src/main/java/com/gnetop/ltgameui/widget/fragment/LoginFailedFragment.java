package com.gnetop.ltgameui.widget.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.gnetop.ltgamecommon.model.BundleData;
import com.gnetop.ltgameui.R;
import com.gnetop.ltgameui.base.BaseFragment;


public class LoginFailedFragment extends BaseFragment {

    LinearLayout mLytFailed;
    String mAgreementUrl;
    String mPrivacyUrl;
    String googleClientID;
    String baseURL;
    String LTAppID;
    String LTAppKey;

    public static LoginFailedFragment newInstance(BundleData data) {
        Bundle args = new Bundle();
        LoginFailedFragment fragment = new LoginFailedFragment();
        args.putSerializable(ARG_NUMBER,data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_login_failed;
    }

    @Override
    protected void initView(View view) {
        mLytFailed = view.findViewById(R.id.lyt_login_failed);
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
                Log.e("LoginFailedFragment",mPrivacyUrl+"===="+mAgreementUrl);
                initData(mPrivacyUrl,mAgreementUrl);
            }
        }
    }

    /**
     * 跳转
     * @param mPrivacyUrl
     * @param mAgreementUrl
     */
    private void initData(final String mPrivacyUrl, final String mAgreementUrl){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BundleData data=new BundleData();
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
        },2000);
    }
}
