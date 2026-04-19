package com.ezpark.parking.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ezpark.parking.R
import com.ezpark.parking.ui.theme.EzparkColors
import com.ezpark.parking.ui.theme.EzparkSpacing
import com.ezpark.parking.ui.theme.EzparkTheme
import com.ezpark.parking.ui.theme.EzparkTypography

@Composable
fun DetailHeader(
    title: String,
    onBack: () -> Unit,
    onOptions: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = EzparkSpacing.xxl, vertical = EzparkSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(onClick = onBack) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = "뒤로",
                tint = EzparkColors.gray100,
            )
        }
        Text(
            text = title,
            style = EzparkTypography.subtitle2,
            color = EzparkColors.gray100,
        )
        IconButton(onClick = onOptions) {
            Icon(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = "옵션",
                tint = EzparkColors.gray100,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EzparkTheme {
        DetailHeader(title = "강남역 공영주차장", onBack = {}, onOptions = {})
    }
}
