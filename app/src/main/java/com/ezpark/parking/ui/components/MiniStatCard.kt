package com.ezpark.parking.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ezpark.parking.ui.theme.EzparkColors
import com.ezpark.parking.ui.theme.EzparkShapes
import com.ezpark.parking.ui.theme.EzparkSpacing
import com.ezpark.parking.ui.theme.EzparkTheme
import com.ezpark.parking.ui.theme.EzparkTypography

data class StatData(
    val label: String,
    val value: String,
    val unit: String? = null,
)

@Composable
fun MiniStatCard(
    data: StatData,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = EzparkShapes.card,
        color = EzparkColors.primaryAccent,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = EzparkSpacing.sm, horizontal = EzparkSpacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(EzparkSpacing.xs),
        ) {
            Text(
                text = data.label,
                style = EzparkTypography.subtitle5,
                color = EzparkColors.gray100,
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(EzparkSpacing.xs),
            ) {
                Text(
                    text = data.value,
                    style = EzparkTypography.subtitle3,
                    color = EzparkColors.gray100,
                )
                if (data.unit != null) {
                    Text(
                        text = data.unit,
                        style = EzparkTypography.caption2,
                        color = EzparkColors.gray60,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EzparkTheme {
        MiniStatCard(data = StatData(label = "남은자리", value = "3", unit = "대"))
    }
}
