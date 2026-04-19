package com.ezpark.parking.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ezpark.parking.ui.theme.EzparkColors
import com.ezpark.parking.ui.theme.EzparkShapes
import com.ezpark.parking.ui.theme.EzparkSpacing
import com.ezpark.parking.ui.theme.EzparkTheme

@Composable
fun RecentSearchList(
    items: List<String>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = EzparkShapes.card,
        color = EzparkColors.gray10,
        shadowElevation = EzparkSpacing.xs,
    ) {
        Column {
            items.forEach { item ->
                RecentSearchItem(text = item, onClick = { onItemClick(item) })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EzparkTheme {
        RecentSearchList(
            items = listOf("강남역 공영주차장", "역삼역 1번 출구", "선릉역 사거리"),
            onItemClick = {},
        )
    }
}
