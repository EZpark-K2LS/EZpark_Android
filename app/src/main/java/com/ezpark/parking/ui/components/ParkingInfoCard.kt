package com.ezpark.parking.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ezpark.parking.R
import com.ezpark.parking.ui.theme.EzparkColors
import com.ezpark.parking.ui.theme.EzparkShapes
import com.ezpark.parking.ui.theme.EzparkSpacing
import com.ezpark.parking.ui.theme.EzparkTheme
import com.ezpark.parking.ui.theme.EzparkTypography

@Composable
fun ParkingInfoCard(
    title: String,
    badgeText: String,
    distance: String,
    leftStat: StatData,
    rightStat: StatData,
    ctaText: String,
    onCta: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = EzparkShapes.card,
        color = EzparkColors.gray10,
        shadowElevation = EzparkSpacing.sm,
    ) {
        Column(
            modifier = Modifier.padding(EzparkSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(EzparkSpacing.md),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(EzparkSpacing.sm),
            ) {
                Text(
                    text = title,
                    style = EzparkTypography.heading3,
                    color = EzparkColors.gray100,
                    modifier = Modifier.weight(1f),
                )
                MapPinBadge(text = badgeText)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(EzparkSpacing.xs),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_marker_plain),
                    contentDescription = null,
                    tint = EzparkColors.gray60,
                    modifier = Modifier.size(EzparkSpacing.lg),
                )
                Text(
                    text = distance,
                    style = EzparkTypography.body3,
                    color = EzparkColors.gray60,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(EzparkSpacing.xs),
            ) {
                MiniStatCard(data = leftStat, modifier = Modifier.weight(1f))
                MiniStatCard(data = rightStat, modifier = Modifier.weight(1f))
            }
            PrimaryCtaButton(text = ctaText, onClick = onCta)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EzparkTheme {
        ParkingInfoCard(
            title = "강남역 공영주차장",
            badgeText = "3자리 남음",
            distance = "30m 남음",
            leftStat = StatData(label = "남은자리", value = "3", unit = "대"),
            rightStat = StatData(label = "요금", value = "2,000", unit = "원"),
            ctaText = "주차장 이동",
            onCta = {},
        )
    }
}
