package com.ezpark.parking.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
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

@Composable
fun MapPinBadge(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = EzparkShapes.pill,
        color = EzparkColors.alertRed,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = EzparkSpacing.sm,
                vertical = EzparkSpacing.xs,
            ),
            style = EzparkTypography.subtitle5,
            color = EzparkColors.gray10,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EzparkTheme {
        MapPinBadge(text = "3자리 남음")
    }
}
