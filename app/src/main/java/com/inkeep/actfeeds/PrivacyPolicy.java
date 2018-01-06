package com.inkeep.actfeeds;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.inkeep.actfeeds.R;

public class PrivacyPolicy extends AppCompatActivity {
    WebView browser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        Intent intent = this.getIntent();
        String pageName = intent.getStringExtra("PageName");
         browser = (WebView) findViewById(R.id.webViewPrivacyPolicy);
        //WebSettings webSettings = browser.getSettings();
        //webSettings.setJavaScriptEnabled(true);

        browser.setWebViewClient(new WebViewClient());
        String privacy= "PrivacyPolicy";
        if(privacy.equals(pageName)){
            browser.loadUrl("https://docs.google.com/document/d/1ld3w6U99k6X5IQXJ_cWtEkyOky5kZijddZAb4DxUO1s/edit?usp=sharing");
        }else{
            browser.loadUrl("https://docs.google.com/document/d/1ZhMCTMZM-lO_xN0TMHgPSQyDp2b6c5WufOUXI01JljA/edit?usp=sharing");
        }
        browser.canGoBack();
    }

    @Override
    public void onBackPressed() {
        if(browser.canGoBack()) {
            browser.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
