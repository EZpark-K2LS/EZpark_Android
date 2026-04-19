package com.ezpark.parking.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ezpark.parking.R
import com.ezpark.parking.ui.theme.EzparkColors
import com.ezpark.parking.ui.theme.EzparkShapes
import com.ezpark.parking.ui.theme.EzparkSpacing
import com.ezpark.parking.ui.theme.EzparkTheme
import com.ezpark.parking.ui.theme.EzparkTypography

private val SearchBarElevation = 6.dp // spacing-scale exception: shadow elevation per Figma
private val ClearButtonSize = 22.dp // spacing-scale exception: small inline icon hit area

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "어디로 가볼까요?",
    onFocusChanged: (Boolean) -> Unit = {},
    onSubmit: () -> Unit = {},
    requestFocus: Boolean = false,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(requestFocus) {
        if (requestFocus) focusRequester.requestFocus()
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = EzparkShapes.card,
        color = EzparkColors.gray10,
        shadowElevation = SearchBarElevation,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = EzparkSpacing.lg, vertical = EzparkSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(EzparkSpacing.md),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = EzparkColors.gray60,
            )
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                if (query.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = EzparkTypography.caption1,
                        color = EzparkColors.gray60,
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = EzparkTypography.caption1.copy(color = EzparkColors.gray100),
                    cursorBrush = SolidColor(EzparkColors.primaryBlue),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        keyboardController?.hide()
                        onSubmit()
                    }),
                    interactionSource = remember { MutableInteractionSource() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { onFocusChanged(it.isFocused) },
                )
            }
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.size(ClearButtonSize),
                ) {
                    Box(
                        modifier = Modifier
                            .size(ClearButtonSize)
                            .background(EzparkColors.gray40, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "×",
                            style = EzparkTypography.subtitle5,
                            color = EzparkColors.gray10,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEmpty() {
    EzparkTheme {
        SearchBar(query = "", onQueryChange = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithQuery() {
    EzparkTheme {
        SearchBar(query = "신성동", onQueryChange = {})
    }
}
