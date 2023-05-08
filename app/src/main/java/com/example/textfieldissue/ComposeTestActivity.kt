package com.example.textfieldissue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.textfieldissue.ui.theme.TextFieldIssueTheme

class ComposeTestActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			TextFieldIssueTheme {
				Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

					var value by remember { mutableStateOf("") }

					Column(
						modifier = Modifier
							.verticalScroll(rememberScrollState())
							.fillMaxSize()
					) {
						TextField(
							value = value,
							onValueChange = { value = it },
							modifier = Modifier.fillMaxWidth()
						)
					}
				}
			}
		}
	}
}
