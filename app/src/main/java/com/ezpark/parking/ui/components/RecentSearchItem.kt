package com.ezpark.parking.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
fun RecentSearchItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = EzparkSpacing.lg, vertical = EzparkSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(EzparkSpacing.sm),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_recent),
            contentDescription = null,
            tint = EzparkColors.gray60,
            modifier = Modifier.size(EzparkSpacing.lg),
        )
        Text(
            text = text,
            style = EzparkTypography.caption2,
            color = EzparkColors.gray100,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EzparkTheme {
        RecentSearchItem(text = "강남역 공영주차장", onClick = {})
    }
}
