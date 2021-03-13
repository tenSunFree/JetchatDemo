package com.example.jetchatdemo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.jetchatdemo.common.component.JetchatScaffold
import com.example.jetchatdemo.databinding.ContentMainBinding
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideWindowInsets(consumeWindowInsets = false) {
                CompositionLocalProvider(
                    LocalBackPressedDispatcher provides this.onBackPressedDispatcher
                ) {
                    val scaffoldState = rememberScaffoldState()
                    JetchatScaffold(scaffoldState) {
                        AndroidViewBinding(ContentMainBinding::inflate)
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController().navigateUp() || super.onSupportNavigateUp()
    }

    private fun findNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }
}
