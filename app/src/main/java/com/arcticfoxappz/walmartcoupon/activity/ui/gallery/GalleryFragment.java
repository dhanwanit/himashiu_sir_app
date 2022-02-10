package com.arcticfoxappz.walmartcoupon.activity.ui.gallery;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.arcticfoxappz.walmartcoupon.R;
import com.arcticfoxappz.walmartcoupon.utils.AppConstants;
import java.io.InputStream;
import static android.content.Context.MODE_PRIVATE;


public class GalleryFragment extends Fragment {

    private static WebView webView;
    private View view;
    private TextView dialogTV,dialog12TV;
    private String currentUrl;
    private boolean click_webview;
    private Handler handler;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery, container, false);

        webView = (WebView) view.findViewById(R.id.webView);
        dialogTV = (TextView) view.findViewById(R.id.dialogTV);
        dialogTV.setVisibility(View.INVISIBLE);
        handler=new Handler();

        if (!AppConstants.checkInternetConnection(getContext())) {
            webView.setVisibility(View.INVISIBLE);
            dialogTV.setText("You don't have internet connection.");
            dialogTV.setVisibility(View.VISIBLE);
           // dialogTV.startAnimation(animationFadeIn);

        } else {
            webView.setVisibility(View.INVISIBLE);
            webView.setWebViewClient(new MyBrowser());
            // disable copy of content on long click
            webView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });

            SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
            currentUrl = sharedPreferences.getString("web_url","");
            Log.d("web_url ====>   ",""+currentUrl);


           // currentUrl="https://www.dontpayfull.com/browse/food";
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.loadUrl(currentUrl);
        }

        webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_webview=true;
            }
        });


        
        return view;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("out")) { // Could be cleverer and use a regex
                view.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
                String temp = "https://www.retailmenot.com/showcoupon/";
                String[] separated = url.split("/");
                view.loadUrl(temp + separated[separated.length - 1] + "/");
                return true; // Leave webview and use browser
            } else {
                view.loadUrl(url);
                return true;
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            dialogTV.setVisibility(View.VISIBLE);
            webView.setVisibility(View.INVISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            webView.goBack();
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            injectCSS();
            webView.getSettings().setUserAgentString("");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialogTV.setVisibility(View.INVISIBLE);
                    webView.setVisibility(View.VISIBLE);
                }
            }, 1000);
            super.onPageFinished(view, url);
        }

    }

    public static boolean mCanGoBack() {
        return webView.canGoBack();
    }

    public static void mGoBack() {
        webView.goBack();
    }
    private void injectCSS() {
        try {
            InputStream inputStream = getContext().getAssets().open("style.css");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            webView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
