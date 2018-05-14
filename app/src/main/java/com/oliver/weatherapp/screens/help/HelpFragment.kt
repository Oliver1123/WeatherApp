package com.oliver.weatherapp.screens.help


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.oliver.weatherapp.R

class HelpFragment : Fragment() {
    private val HELP_HTML_PAGE_LOCATION = "file:///android_asset/help/index.html"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        val webView = view.findViewById<WebView>(R.id.web_view)
        webView.loadUrl(HELP_HTML_PAGE_LOCATION)
    }

    private fun initToolbar() {
        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar?.setTitle(R.string.help_screen_title)
    }

    companion object {

        fun newInstance(): Fragment {
            return HelpFragment()
        }
    }
}
