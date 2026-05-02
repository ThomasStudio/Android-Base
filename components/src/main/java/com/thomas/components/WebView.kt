package com.thomas.components

import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Simple composable that shows a web page for the given [url].
 *
 * Usage:
 * WebPage("https://www.example.com")
 */
@Composable
fun WebPage(
	url: String,
	modifier: Modifier = Modifier,
	enableJavaScript: Boolean = true,
	onPageFinished: ((String) -> Unit)? = null
) {
	val context = LocalContext.current

	AndroidView(
		factory = { ctx ->
			WebView(ctx).apply {
				layoutParams = ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT
				)
				settings.javaScriptEnabled = enableJavaScript
				webViewClient = object : WebViewClient() {
					override fun shouldOverrideUrlLoading(
						view: WebView?,
						request: WebResourceRequest?
					): Boolean {
						// Let the WebView handle the URL
						return false
					}

					override fun onPageFinished(view: WebView?, finishedUrl: String?) {
						super.onPageFinished(view, finishedUrl)
						if (finishedUrl != null) onPageFinished?.invoke(finishedUrl)
					}
				}
			}
		},
		update = { webView ->
			// Only load when the URL is different to avoid reloading on recomposition
			if (webView.url != url) webView.loadUrl(url)
		},
		modifier = modifier
	)
}

