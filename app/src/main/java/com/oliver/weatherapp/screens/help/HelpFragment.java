package com.oliver.weatherapp.screens.help;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.oliver.weatherapp.R;

public class HelpFragment extends Fragment {


    private static final String HELP_HTML_PAGE_LOCATION = "file:///android_asset/help/index.html";

    public static Fragment newInstance() {
        return new HelpFragment();
    }

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        WebView webView = view.findViewById(R.id.web_view);
        webView.loadUrl(HELP_HTML_PAGE_LOCATION);
    }

    private void initToolbar() {
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(R.string.help_screen_title);
        }
    }
}
