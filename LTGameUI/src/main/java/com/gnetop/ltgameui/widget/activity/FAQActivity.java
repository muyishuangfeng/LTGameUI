package com.gnetop.ltgameui.widget.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gnetop.ltgameui.R;


public class FAQActivity extends AppCompatActivity {

    WebView mWebFaq;
    WebSettings settings;
    String url = "";
    String mContent;
    ProgressBar mPgb;
    TextView mTxtTitle;
    AppCompatImageView mImgBack;
    public static final String URL_CONFIG = "URL_CONFIG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_faq);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    protected void initView() {
        mWebFaq = findViewById(R.id.web_faq);
        mPgb = findViewById(R.id.web_progress);
        mImgBack = findViewById(R.id.img_web_back);
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebFaq.canGoBack()) {
                    mWebFaq.goBack();
                } else {
                    finish();
                }
            }
        });

        mTxtTitle = findViewById(R.id.txt_web_title);
        mContent = getIntent().getStringExtra("title");
    }

    /**
     * 初始化数据
     */
    protected void initData() {
        url += getIntent().getStringExtra(URL_CONFIG);
        if (url.contains("privacy")) {
            mTxtTitle.setText(getResources().getString(R.string.text_user_privacys));
        } else if (url.contains("terms")) {
            mTxtTitle.setText(getResources().getString(R.string.text_user_agreement));
        }
        initWebView(url);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebFaq.canGoBack()) {
            mWebFaq.goBack();// 返回上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * webview设置
     *
     * @param url
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(String url) {
        settings = mWebFaq.getSettings();
        // 如果访问的页面中有Javascript，则WebView必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportZoom(false); // 支持缩放
        settings.setBuiltInZoomControls(false); // 支持手势缩放
        settings.setDisplayZoomControls(false); // 是否显示缩放按钮

        // >= 19(SDK4.4)启动硬件加速，否则启动软件加速
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebFaq.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            settings.setLoadsImagesAutomatically(true); // 支持自动加载图片
        } else {
            mWebFaq.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            settings.setLoadsImagesAutomatically(false);
        }

        settings.setUseWideViewPort(true); // 将图片调整到适合WebView的大小
        settings.setLoadWithOverviewMode(true); // 自适应屏幕
        settings.setDomStorageEnabled(true);
        settings.setSaveFormData(true);
        settings.setSupportMultipleWindows(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 优先使用缓存

        mWebFaq.setHorizontalScrollbarOverlay(true);
        mWebFaq.setHorizontalScrollBarEnabled(false);
        mWebFaq.setOverScrollMode(View.OVER_SCROLL_NEVER); // 取消WebView中滚动或拖动到顶部、底部时的阴影
        mWebFaq.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); // 取消滚动条白边效果
        mWebFaq.requestFocus();
        mWebFaq.loadUrl(url);

        mWebFaq.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mPgb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWebFaq.setLayerType(View.LAYER_TYPE_NONE, null);
                mPgb.setVisibility(View.GONE);
                if (!settings.getLoadsImagesAutomatically()) {
                    settings.setLoadsImagesAutomatically(true);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mPgb.setVisibility(View.GONE);
            }
        });

        mWebFaq.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 100) {
                    mPgb.setVisibility(View.GONE);
                } else {
                    if (mPgb.getVisibility() == View.GONE) {
                        mPgb.setVisibility(View.VISIBLE);
                    }
                    mPgb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);

            }
        });
    }


}
