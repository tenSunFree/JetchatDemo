package com.example.jetchatdemo.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.jetchatdemo.LocalBackPressedDispatcher
import com.example.jetchatdemo.common.theme.JetchatTheme
import dev.chrisbanes.accompanist.insets.ExperimentalAnimatedInsets
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.ViewWindowInsetObserver

class HomeFragment : Fragment() {

    @OptIn(ExperimentalAnimatedInsets::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        val windowInsets = ViewWindowInsetObserver(this)
            .start(windowInsetsAnimationsEnabled = true)
        setContent {
            CompositionLocalProvider(
                LocalBackPressedDispatcher provides requireActivity().onBackPressedDispatcher,
                LocalWindowInsets provides windowInsets,
            ) {
                JetchatTheme { HomeContent(uiState = homeState) }
            }
        }
    }
}
