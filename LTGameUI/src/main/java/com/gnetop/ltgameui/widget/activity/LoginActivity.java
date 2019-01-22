package com.gnetop.ltgameui.widget.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.gnetop.ltgamecommon.model.BundleData;
import com.gnetop.ltgameui.R;
import com.gnetop.ltgameui.base.BaseAppActivity;
import com.gnetop.ltgameui.widget.fragment.LoginFragment;

public class LoginActivity extends BaseAppActivity {

    @Override
    protected int getViewId() {
        return R.layout.activity_login;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        Bundle bundle = getIntent().getBundleExtra("bundleData");
        String mAgreementUrl = bundle.getString("mAgreementUrl");
        String mPrivacyUrl = bundle.getString("mPrivacyUrl");
        String googleClientID = bundle.getString("googleClientID");
        String baseURL = bundle.getString("baseURL");
        String LTAppID = bundle.getString("LTAppID");
        String LTAppKey = bundle.getString("LTAppKey");

        BundleData data = new BundleData();
        data.setAgreementUrl(mAgreementUrl);
        data.setPrivacyUrl(mPrivacyUrl);
        data.setBaseURL(baseURL);
        data.setGoogleClientID(googleClientID);
        data.setLTAppID(LTAppID);
        data.setLTAppKey(LTAppKey);
        if (!TextUtils.isEmpty(mAgreementUrl) &&
                !TextUtils.isEmpty(mPrivacyUrl)) {
            if (findFragment(LoginFragment.class) == null) {
                addFragment(LoginFragment.newInstance(data),
                        false,
                        true);
            }
        }

    }

    @Override
    protected void initData() {
    }


}
