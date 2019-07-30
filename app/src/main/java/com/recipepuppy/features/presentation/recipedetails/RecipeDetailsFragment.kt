package com.recipepuppy.features.presentation.recipedetails

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.recipepuppy.R
import com.recipepuppy.core.extension.gone
import com.recipepuppy.core.platform.BaseFragment
import com.recipepuppy.features.presentation.recipedetails.RecipeDetailsFragmentArgs.fromBundle
import kotlinx.android.synthetic.main.fragment_recipe_details.*
import timber.log.Timber

/**
 * Created by danieh on 30/07/2019.
 */
class RecipeDetailsFragment : BaseFragment() {

    override fun layoutId() = R.layout.fragment_recipe_details

    companion object {
        private val TAG = RecipeDetailsFragment::class.java.simpleName
        private const val DELAY = 300L
    }

    private val href by lazy {
        arguments?.let { fromBundle(it).href }
    }

    private val name by lazy {
        arguments?.let { fromBundle(it).name }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipe_details_webview.settings.apply {
            javaScriptEnabled = true
            setSupportZoom(true)
        }

        recipe_details_webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                if (recipe_details_loader != null) {
                    recipe_details_loader.gone()
                }
            }
        }

        Timber.tag(TAG).d("href: $href")
        recipe_details_webview.postDelayed({
            recipe_details_webview.loadUrl(href)
        }, DELAY)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (recipe_details_webview.canGoBack()) {
                    // If web view have back history, then go to the web view back history
                    recipe_details_webview.goBack()
                } else {
                    fragmentManager?.popBackStack()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
