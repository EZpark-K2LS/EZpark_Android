package com.ezpark.parking.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ezpark.parking.R

private val Pretendard = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
)

object EzparkTypography {
    val heading3 = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
    val subtitle2 = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
    val subtitle3 = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
    val subtitle4 = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    val subtitle5 = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
    val body3 = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 16.sp)
    val button2 = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 16.sp)
    val caption1 = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Normal, fontSize = 16.sp)
    val caption2 = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Normal, fontSize = 14.sp)
}
