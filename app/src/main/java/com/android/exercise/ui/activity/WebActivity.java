package com.android.exercise.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.exercise.R;
import com.android.exercise.base.BaseActivity;
import com.android.exercise.base.toolbar.ToolBarRightTextHolder;
import com.android.exercise.listener.IJSCallback;
import com.android.exercise.ui.JSInterface;
import com.android.exercise.ui.widget.dialog.ADWindowDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends BaseActivity implements IJSCallback {

    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        config();
        String path = "file:///android_asset/html/js.html";
        webview.loadUrl(path);
    }

    private void config() {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                ADWindowDialog adWindowDialog = new ADWindowDialog();
                adWindowDialog.showDialog(getFragmentManager());
                result.confirm();
                return true;
            }
        });
        webview.addJavascriptInterface(new JSInterface(this), "android");
    }

    @Override
    protected void onSetupToolbar(Toolbar toolbar, ActionBar actionBar) {
        ToolBarRightTextHolder holder = new ToolBarRightTextHolder(this, toolbar, getString(R.string.item_webview), "点我");
        holder.getRightMenu().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview.evaluateJavascript("javascript:test()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Toast.makeText(mContext, value, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void exeJS(final String data) {
        webview.post(new Runnable() {
            @Override
            public void run() {
                webview.loadUrl("javascript:getAppInfoAsyncCallback('" + data + "')");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webview.destroy();
    }
}
