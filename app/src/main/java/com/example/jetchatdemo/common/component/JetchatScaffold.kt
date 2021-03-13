package com.example.jetchatdemo.common.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import com.example.jetchatdemo.common.theme.JetchatTheme

@Composable
fun JetchatScaffold(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    content: @Composable (PaddingValues) -> Unit
) {
    JetchatTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            content = content
        )
    }
}
