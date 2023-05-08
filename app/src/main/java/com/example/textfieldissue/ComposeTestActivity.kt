package com.example.textfieldissue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.example.textfieldissue.ui.theme.TextFieldIssueTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ComposeTestActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			TextFieldIssueTheme {
				Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

					val coroutineScope = rememberCoroutineScope()
					val scrollState = rememberScrollState()

					var columnBound: Rect? by remember { mutableStateOf(null) }
					var textFieldBound: Rect? by remember { mutableStateOf(null) }
					var textFieldValue: TextFieldValue by remember { mutableStateOf(TextFieldValue()) }
					var prevTextLayoutResult: TextLayoutResult? by remember { mutableStateOf(null) }

					Column(
						modifier = Modifier
							.verticalScroll(scrollState)
							.fillMaxSize()
							.onGloballyPositioned {
								columnBound = it.boundsInParent()
							}
					) {
						BasicTextField(
							value = textFieldValue,
							onValueChange = {
								if (textFieldValue.text == it.text && (textFieldValue.selection.start != it.selection.start || textFieldValue.selection.end != it.selection.end)) {
									val targetOffset = if (isNeedToUseStartSelection(textFieldValue.selection, it.selection)) {
										it.selection.start
									} else {
										it.selection.end
									}

									val cursorRect = runCatching {
										prevTextLayoutResult?.getCursorRect(targetOffset)
									}.getOrNull()

									coroutineScope.launch {
										cursorRect ?: return@launch
										processScrollToCursorPosition(
											scrollState,
											cursorRect,
											textFieldBound,
											columnBound
										)
									}
								}
								textFieldValue = it
							},
							onTextLayout = { textLayoutResult ->
								val targetOffset = textFieldValue.selection.start

								val cursorRect = textLayoutResult.getCursorRect(targetOffset)
								coroutineScope.launch {
									processScrollToCursorPosition(
										scrollState,
										cursorRect,
										textFieldBound,
										columnBound
									)
								}

								prevTextLayoutResult = textLayoutResult
							},
							modifier = Modifier
								.fillMaxWidth()
								.onFocusEvent {
									if (it.isFocused) {
										val targetOffset = textFieldValue.selection.start

										val cursorRect = runCatching {
											prevTextLayoutResult?.getCursorRect(targetOffset)
										}.getOrNull()

										coroutineScope.launch {
											cursorRect ?: return@launch
											delay(300L)
											processScrollToCursorPosition(
												scrollState,
												cursorRect,
												textFieldBound,
												columnBound
											)
										}
									}
								}
								.onGloballyPositioned {
									textFieldBound = it.boundsInParent()
								}
						)
					}
				}
			}
		}
	}

	private fun isNeedToUseStartSelection(prevTextRange: TextRange?, currentTextRange: TextRange): Boolean {
		prevTextRange ?: return true

		return when {
			currentTextRange.start == prevTextRange.start && currentTextRange.end != prevTextRange.end -> false
			currentTextRange.end == prevTextRange.end && currentTextRange.start != prevTextRange.start -> true
			else -> true
		}
	}

	private suspend fun processScrollToCursorPosition(
		scrollState: ScrollState,
		cursorRect: Rect?,
		textFieldBound: Rect?,
		columnBound: Rect?,
	) {
		if (scrollState.isScrollInProgress) {
			return
		}

		if (cursorRect == null || textFieldBound == null || columnBound == null) {
			return
		}

		val currentScrollValue = scrollState.value
		val cursorBottomFromColumn = textFieldBound.top + cursorRect.bottom - currentScrollValue
		val cursorTopFromColumn = textFieldBound.top + cursorRect.top - currentScrollValue
		val columnHeight = columnBound.height

		val scrollAmount = when {
			(cursorBottomFromColumn > columnHeight) -> cursorBottomFromColumn - columnHeight
			(cursorTopFromColumn < 0f) -> cursorTopFromColumn
			else -> null
		} ?: return

		scrollState.scrollBy(scrollAmount)
	}
}
