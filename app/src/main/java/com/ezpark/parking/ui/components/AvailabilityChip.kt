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
fun AvailabilityChip(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = EzparkShapes.chip,
        color = EzparkColors.gray100,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = EzparkSpacing.lg,
                vertical = EzparkSpacing.sm,
            ),
            style = EzparkTypography.subtitle4,
            color = EzparkColors.gray10,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EzparkTheme {
        AvailabilityChip(text = "전체 보기")
    }
}
