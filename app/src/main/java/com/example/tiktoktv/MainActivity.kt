package com.example.tiktoktv

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)

        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true          // necesario para mantener sesión / login
        settings.mediaPlaybackRequiresUserGesture = false
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        // User-Agent de escritorio: la versión móvil no se adapta bien a pantalla horizontal
        settings.userAgentString =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                injectTvStyles()
            }
        }

        webView.loadUrl("https://www.tiktok.com/")
    }

    /**
     * Inyecta CSS/JS para:
     * 1) Centrar el feed vertical de TikTok en una pantalla horizontal.
     * 2) Ocultar elementos pensados para dedo (barra lateral táctil, etc.) si estorban.
     */
    private fun injectTvStyles() {
        val css = """
            javascript:(function() {
                var style = document.createElement('style');
                style.innerHTML = `
                    body { background: #000 !important; }
                    [class*="DivVideoWrapper"], video {
                        margin: 0 auto !important;
                        max-height: 100vh !important;
                    }
                `;
                document.head.appendChild(style);
            })()
        """.trimIndent()
        webView.evaluateJavascript(css, null)
    }

    // Traduce las teclas del control remoto a scroll de feed (siguiente/anterior video)
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                scrollFeed("down")
                return true
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                scrollFeed("up")
                return true
            }
            KeyEvent.KEYCODE_DPAD_CENTER,
            KeyEvent.KEYCODE_ENTER -> {
                togglePlayPause()
                return true
            }
            KeyEvent.KEYCODE_BACK -> {
                if (webView.canGoBack()) {
                    webView.goBack()
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun scrollFeed(direction: String) {
        val delta = if (direction == "down") "window.innerHeight" else "-window.innerHeight"
        webView.evaluateJavascript(
            "javascript:window.scrollBy({top: $delta, behavior: 'smooth'});", null
        )
    }

    private fun togglePlayPause() {
        val js = """
            javascript:(function() {
                var v = document.querySelector('video');
                if (v) { v.paused ? v.play() : v.pause(); }
            })()
        """.trimIndent()
        webView.evaluateJavascript(js, null)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
