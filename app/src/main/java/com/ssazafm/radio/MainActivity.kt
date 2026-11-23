package com.ssazafm.radio

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : Activity() {
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(12, 18, 62)
        window.navigationBarColor = Color.rgb(8, 12, 38)

        webView = WebView(this).apply {
            setBackgroundColor(Color.rgb(12, 18, 62))
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mediaPlaybackRequiresUserGesture = true
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    val uri = request.url
                    val host = uri.host.orEmpty()
                    val allowed = host == "ssazafm998.github.io" ||
                        host == "ssazafm998.ismyradio.com" ||
                        host.endsWith(".caster.fm")
                    if (allowed) return false
                    return try {
                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                        true
                    } catch (_: Exception) { true }
                }
            }
        }
        setContentView(webView)

        if (savedInstanceState == null) {
            webView.loadUrl("https://ssazafm998.github.io/ssaza-radio-app/")
        } else {
            webView.restoreState(savedInstanceState)
        }
    }

    @Deprecated("Deprecated in Android API; retained for broad device compatibility")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::webView.isInitialized) webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        if (::webView.isInitialized) webView.destroy()
        super.onDestroy()
    }
}