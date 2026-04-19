package com.ezpark.parking.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ezpark.parking.ui.theme.EzparkColors
import com.ezpark.parking.ui.theme.EzparkShapes
import com.ezpark.parking.ui.theme.EzparkSpacing
import com.ezpark.parking.ui.theme.EzparkTheme
import com.ezpark.parking.ui.theme.EzparkTypography

// spacing-scale exception: 44dp button height per Figma spec
private val ButtonHeight = 44.dp

@Composable
fun PrimaryCtaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(ButtonHeight),
        enabled = enabled,
        shape = EzparkShapes.card,
        colors = ButtonDefaults.buttonColors(
            containerColor = EzparkColors.primaryBlue,
            contentColor = EzparkColors.gray10,
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = EzparkSpacing.lg,
            vertical = EzparkSpacing.sm,
        ),
    ) {
        Text(text = text, style = EzparkTypography.button2)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EzparkTheme {
        PrimaryCtaButton(text = "주차장 이동", onClick = {})
    }
}
